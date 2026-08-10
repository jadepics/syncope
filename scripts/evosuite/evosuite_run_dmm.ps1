param(
    [Parameter(Position=0)]
    [ValidateSet("test", "jacoco", "pit", "all", "clean-generated", "info")]
    [string]$Action = "test",

    [switch]$SkipRebuild
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

. (Join-Path $PSScriptRoot "evosuite_common_dmm.ps1")

# Report persistenti: non vengono cancellati da "mvn clean".
$script:ReportTimestamp = Get-Date -Format "yyyyMMdd-HHmmss"
$script:StableResultsRoot = Join-Path `
    $script:RepoRoot `
    "evosuite-results\dmm\$($script:ReportTimestamp)"

function Ensure-Directory {
    param([Parameter(Mandatory=$true)][string]$Path)

    [void][System.IO.Directory]::CreateDirectory($Path)
}

function Configure-TemporaryPomReportOutputs {
    $surefireDir = Join-Path $script:StableResultsRoot "surefire"
    $jacocoDir = Join-Path $script:StableResultsRoot "jacoco"
    $pitDir = Join-Path $script:StableResultsRoot "pit"

    Ensure-Directory $script:StableResultsRoot
    Ensure-Directory $surefireDir
    Ensure-Directory $jacocoDir
    Ensure-Directory $pitDir

    $doc = New-Object System.Xml.XmlDocument
    $doc.PreserveWhitespace = $true
    $doc.Load($script:TemporaryRunPom)

    $project = $doc.DocumentElement
    $ns = $project.NamespaceURI

    function New-XmlElement(
            [string]$Name,
            [string]$Value = $null
    ) {
        $node = $doc.CreateElement($Name, $ns)

        if ($null -ne $Value) {
            $node.InnerText = $Value
        }

        return $node
    }

    $profile = $project.SelectSingleNode(
            "*[local-name()='profiles']/*[local-name()='profile'][*[local-name()='id' and text()='evosuite-dmm-temp']]"
    )

    if ($null -eq $profile) {
        throw "Profilo temporaneo evosuite-dmm-temp non trovato nel POM temporaneo."
    }

    $build = $profile.SelectSingleNode("*[local-name()='build']")

    if ($null -eq $build) {
        $build = New-XmlElement "build"
        [void]$profile.AppendChild($build)
    }

    $plugins = $build.SelectSingleNode("*[local-name()='plugins']")

    if ($null -eq $plugins) {
        $plugins = New-XmlElement "plugins"
        [void]$build.AppendChild($plugins)
    }

    function Get-OrCreatePlugin(
            [string]$GroupId,
            [string]$ArtifactId,
            [string]$Version = $null
    ) {
        $p = $plugins.SelectSingleNode(
                "*[local-name()='plugin'][*[local-name()='groupId' and text()='$GroupId'] and *[local-name()='artifactId' and text()='$ArtifactId']]"
        )

        if ($null -eq $p) {
            $p = New-XmlElement "plugin"
            [void]$plugins.AppendChild($p)
            [void]$p.AppendChild((New-XmlElement "groupId" $GroupId))
            [void]$p.AppendChild((New-XmlElement "artifactId" $ArtifactId))

            if (-not [string]::IsNullOrWhiteSpace($Version)) {
                [void]$p.AppendChild((New-XmlElement "version" $Version))
            }
        }

        $cfg = $p.SelectSingleNode("*[local-name()='configuration']")

        if ($null -eq $cfg) {
            $cfg = New-XmlElement "configuration"
            [void]$p.AppendChild($cfg)
        }

        return @{
        Plugin = $p
        Configuration = $cfg
    }
}

# ---------------------------------------------------------------------
# Surefire: JUnit 4 diretto, esattamente come nella pipeline JWS.
# ---------------------------------------------------------------------
$surefire = Get-OrCreatePlugin `
        -GroupId "org.apache.maven.plugins" `
        -ArtifactId "maven-surefire-plugin" `
        -Version "3.5.6"

[void]$surefire.Configuration.AppendChild(
        (New-XmlElement "reportsDirectory" $surefireDir)
)
[void]$surefire.Configuration.AppendChild(
        (New-XmlElement "failIfNoTests" "true")
)
[void]$surefire.Configuration.AppendChild(
        (New-XmlElement "failIfNoSpecifiedTests" "true")
)

$surefireDependencies = $surefire.Plugin.SelectSingleNode(
        "*[local-name()='dependencies']"
)

if ($null -eq $surefireDependencies) {
    $surefireDependencies = New-XmlElement "dependencies"
    [void]$surefire.Plugin.AppendChild($surefireDependencies)
}

$existingJUnit47 = $surefireDependencies.SelectSingleNode(
        "*[local-name()='dependency'][*[local-name()='groupId' and text()='org.apache.maven.surefire'] and *[local-name()='artifactId' and text()='surefire-junit47']]"
)

if ($null -eq $existingJUnit47) {
    $providerDependency = New-XmlElement "dependency"
    [void]$providerDependency.AppendChild(
            (New-XmlElement "groupId" "org.apache.maven.surefire")
    )
    [void]$providerDependency.AppendChild(
            (New-XmlElement "artifactId" "surefire-junit47")
    )
    [void]$providerDependency.AppendChild(
            (New-XmlElement "version" "3.5.6")
    )
    [void]$surefireDependencies.AppendChild($providerDependency)
}

$profileDependencies = $profile.SelectSingleNode(
        "*[local-name()='dependencies']"
)

if ($null -ne $profileDependencies) {
    $vintageNode = $profileDependencies.SelectSingleNode(
            "*[local-name()='dependency'][*[local-name()='groupId' and text()='org.junit.vintage'] and *[local-name()='artifactId' and text()='junit-vintage-engine']]"
    )

    if ($null -ne $vintageNode) {
        [void]$profileDependencies.RemoveChild($vintageNode)
    }
}

[void]$surefire.Configuration.AppendChild(
        (New-XmlElement "useFile" "false")
)
[void]$surefire.Configuration.AppendChild(
        (New-XmlElement "trimStackTrace" "false")
)
[void]$surefire.Configuration.AppendChild(
        (New-XmlElement "forkCount" "1")
)
[void]$surefire.Configuration.AppendChild(
        (New-XmlElement "reuseForks" "false")
)

# ---------------------------------------------------------------------
# JaCoCo: output diretto nella directory stabile.
# ---------------------------------------------------------------------
$jacoco = Get-OrCreatePlugin `
        -GroupId "org.jacoco" `
        -ArtifactId "jacoco-maven-plugin" `
        -Version "0.8.15"

$jacocoExec = Join-Path $jacocoDir "jacoco.exec"

[void]$jacoco.Configuration.AppendChild(
        (New-XmlElement "destFile" $jacocoExec)
)
[void]$jacoco.Configuration.AppendChild(
        (New-XmlElement "dataFile" $jacocoExec)
)
[void]$jacoco.Configuration.AppendChild(
        (New-XmlElement "outputDirectory" $jacocoDir)
)
[void]$jacoco.Configuration.AppendChild(
        (New-XmlElement "skip" "false")
)

# ---------------------------------------------------------------------
# PIT: target preciso DefaultMappingManager, output stabile.
# ---------------------------------------------------------------------
$pit = Get-OrCreatePlugin `
        -GroupId "org.pitest" `
        -ArtifactId "pitest-maven" `
        -Version "1.25.9"

[void]$pit.Configuration.AppendChild(
        (New-XmlElement "reportsDirectory" $pitDir)
)
[void]$pit.Configuration.AppendChild(
        (New-XmlElement "timestampedReports" "false")
)
[void]$pit.Configuration.AppendChild(
        (New-XmlElement "threads" "1")
)

$targetClasses = New-XmlElement "targetClasses"
[void]$targetClasses.AppendChild(
        (New-XmlElement "param" $script:TargetClass)
)
[void]$pit.Configuration.AppendChild($targetClasses)

$targetTests = New-XmlElement "targetTests"
[void]$targetTests.AppendChild(
        (New-XmlElement "param" $script:GeneratedTestFqn)
)
[void]$pit.Configuration.AppendChild($targetTests)

$formats = New-XmlElement "outputFormats"

foreach ($format in @(
    "HTML",
    "XML",
    "CSV"
)) {
    [void]$formats.AppendChild(
            (New-XmlElement "param" $format)
    )
}

[void]$pit.Configuration.AppendChild($formats)

$doc.Save($script:TemporaryRunPom)

Write-Host "Output report forzati:"
Write-Host "  Surefire provider -> surefire-junit47 3.5.6 (JUnit 4 diretto)"
Write-Host "  EvoRunner          -> disabilitato nell'esecuzione Java 21 (solo harness)"
Write-Host "  Test/assertion     -> invariati"
Write-Host "  Surefire -> $surefireDir"
Write-Host "  JaCoCo   -> $jacocoDir"
Write-Host "  PIT      -> $pitDir"
}

function Show-EvoSuiteReports {
Write-Section "Risultati EvoSuite DefaultMappingManager"

$foundAny = $false

foreach ($name in @(
"surefire",
"jacoco",
"pit"
)) {
$dir = Join-Path $script:StableResultsRoot $name

if (Test-Path -LiteralPath $dir -PathType Container) {
$files = @(
Get-ChildItem `
                    -LiteralPath $dir `
                    -File `
                    -Recurse `
                    -ErrorAction SilentlyContinue
)

if ($files.Count -gt 0) {
$foundAny = $true
Write-Host ""
Write-Host "$name : $($files.Count) file"

$files |
Select-Object -First 12 |
ForEach-Object {
Write-Host "  $($_.FullName)"
}
}
else {
Write-Warning "$($name): directory presente ma senza file."
}
}
else {
Write-Warning "$($name): directory non presente."
}
}

if (-not $foundAny) {
throw @"
I goal Maven sono terminati ma nessun file di report è stato prodotto.

Directory attesa:
    $script:StableResultsRoot
"@
}

Write-Host ""
Write-Host "Report stabili:"
Write-Host "  $script:StableResultsRoot"
}

$script:OriginalGeneratedTestContent = $null
$script:PlainJUnitAdaptationApplied = $false

function Convert-EvoSuiteTestToPlainJUnitForJava21 {
Write-Section "Adattamento harness EvoSuite -> JUnit 4 per Java 21"

if (-not (Test-Path -LiteralPath $script:GeneratedTestFile)) {
throw "Test EvoSuite non trovato: $script:GeneratedTestFile"
}

$original = [System.IO.File]::ReadAllText($script:GeneratedTestFile)
$script:OriginalGeneratedTestContent = $original

$traceDir = Join-Path `
        $script:StableResultsRoot `
        "generated-test-trace"

Ensure-Directory $traceDir

$originalCopy = Join-Path `
        $traceDir `
        "DefaultMappingManager_ESTest.original.java"

[System.IO.File]::WriteAllText($originalCopy, $original)

$adapted = $original
$removedCount = 0

$patterns = @(
'(?m)^\s*import\s+org\.evosuite\.runtime\.EvoRunner\s*;\s*\r?\n?',
'(?m)^\s*import\s+org\.evosuite\.runtime\.EvoRunnerParameters\s*;\s*\r?\n?',
'(?m)^\s*import\s+org\.junit\.runner\.RunWith\s*;\s*\r?\n?'
)

foreach ($pattern in $patterns) {
$before = $adapted
$adapted = [regex]::Replace($adapted, $pattern, '')

if ($adapted -ne $before) {
$removedCount++
}
}

# Gestisce anche il formato generato da EvoSuite con entrambe
# le annotazioni sulla stessa riga.
$before = $adapted
$adapted = [regex]::Replace(
$adapted,
'@(?:org\.junit\.runner\.)?RunWith\s*\(\s*(?:org\.evosuite\.runtime\.)?EvoRunner\.class\s*\)\s*',
''
)

if ($adapted -ne $before) {
$removedCount++
}

$before = $adapted
$adapted = [regex]::Replace(
$adapted,
'@(?:org\.evosuite\.runtime\.)?EvoRunnerParameters\s*\([^)]*\)\s*',
''
)

if ($adapted -ne $before) {
$removedCount++
}

if ($adapted -eq $original) {
throw @"
Non è stato trovato alcun harness EvoRunner nel test:
    $script:GeneratedTestFile

Il test originale NON è stato modificato.
"@
}

$withoutBlockComments = [regex]::Replace(
$adapted,
'(?s)/\*.*?\*/',
''
)

$remaining = New-Object System.Collections.Generic.List[string]

foreach ($codeLine in ($withoutBlockComments -split '\r?\n')) {
$codeOnly = $codeLine -replace '//.*$', ''

if ($codeOnly -match '\bEvoRunner(?:Parameters)?\b' -or
$codeOnly -match '@(?:org\.junit\.runner\.)?RunWith\b') {
[void]$remaining.Add($codeLine)
}
}

if ($remaining.Count -gt 0) {
Write-Host ""
Write-Host "Riferimenti EvoRunner ancora presenti nel CODICE:"

foreach ($remainingLine in $remaining) {
Write-Host "  $remainingLine"
}

throw @"
L'adattamento non ha rimosso completamente l'harness EvoRunner.
Il test originale NON è stato modificato.
"@
}

[System.IO.File]::WriteAllText(
$script:GeneratedTestFile,
$adapted
)

$script:PlainJUnitAdaptationApplied = $true

$adaptedCopy = Join-Path `
        $traceDir `
        "DefaultMappingManager_ESTest.java21-plain-junit.java"

[System.IO.File]::WriteAllText(
$adaptedCopy,
$adapted
)

Write-Host "Harness EvoRunner rimosso TEMPORANEAMENTE."
Write-Host "Blocchi harness rimossi: $removedCount"
Write-Host "Corpi dei test e assertion NON modificati."
Write-Host "Originale:"
Write-Host "  $originalCopy"
Write-Host "Variante eseguita:"
Write-Host "  $adaptedCopy"
}

function Restore-OriginalEvoSuiteTest {
if ($script:PlainJUnitAdaptationApplied -and
$null -ne $script:OriginalGeneratedTestContent) {

[System.IO.File]::WriteAllText(
$script:GeneratedTestFile,
$script:OriginalGeneratedTestContent
)

$script:PlainJUnitAdaptationApplied = $false

Write-Host "Test EvoSuite originale ripristinato:"
Write-Host "  $script:GeneratedTestFile"
}
}

function Assert-GeneratedTestsExist {
if (-not (Test-Path $script:GeneratedTestFile)) {
throw @"
Test EvoSuite non trovato:
    $script:GeneratedTestFile

Prima esegui:
    .\scripts\evosuite\evosuite_generate_dmm.ps1
"@
}

if (-not (Test-Path $script:GeneratedScaffoldingFile)) {
throw "Scaffolding EvoSuite non trovato: $script:GeneratedScaffoldingFile"
}
}

function Build-RealSyncope {
if ($SkipRebuild) {
Write-Host "Build reale Syncope saltata (-SkipRebuild)."
return
}

Write-Section "Build REALE DefaultMappingManager con JDK 21"

Invoke-Maven `
        -JdkHome $script:Jdk21Home `
        -WorkingDirectory $script:RepoRoot `
        -Arguments @(
"-pl", $script:ModuleRelative,
"-am",
"-DtargetJdk=21",
"-Dmaven.build.cache.skipCache=true",
"-Dmaven.test.skip=true",
"-Drat.skip=true",
"-Dspotless.check.skip=true",
"-Dcheckstyle.skip=true",
"clean",
"compile"
)

if (-not (Test-Path $script:RealClassFile)) {
throw "CUT reale non trovata dopo la build: $script:RealClassFile"
}

$major = Get-ClassMajorVersion -ClassFile $script:RealClassFile
Write-Host "CUT reale classfile major version: $major"

if ($major -ne 65) {
throw @"
La CUT reale non risulta compilata per Java 21.
Major trovata: $major
Major attesa: 65

Controlla JDK21_HOME e -DtargetJdk=21.
"@
}
}

function Prepare-RunEnvironment {
Assert-GeneratedTestsExist
Install-EvoSuiteRuntimeIntoLocalMaven
Build-RealSyncope

# Stesso adattamento già validato sulla classe JWS:
# rimozione TEMPORANEA del solo harness EvoRunner.
Convert-EvoSuiteTestToPlainJUnitForJava21

if (Test-Path $script:TemporaryRunPom) {
Remove-Item -Force $script:TemporaryRunPom
}

$null = New-TemporaryEvoSuiteRunPom
Configure-TemporaryPomReportOutputs

Write-Host "POM temporaneo EvoSuite:"
Write-Host "  $script:TemporaryRunPom"
}

function Cleanup-TemporaryRunPom {
Restore-OriginalEvoSuiteTest

if (Test-Path $script:TemporaryRunPom) {
Remove-Item -Force $script:TemporaryRunPom
}
}

function Invoke-WithJava21EvoSuiteCompatibility {
param(
[Parameter(Mandatory=$true)]
[scriptblock]$ScriptBlockToRun
)

$oldJdkJavaOptions = $env:JDK_JAVA_OPTIONS

try {
$requiredOptions = @(
"-Djava.security.manager=allow",
"--add-opens=java.desktop/java.awt=ALL-UNNAMED"
)

$effectiveOptions = $oldJdkJavaOptions

foreach ($required in $requiredOptions) {
if ([string]::IsNullOrWhiteSpace($effectiveOptions)) {
$effectiveOptions = $required
}
elseif ($effectiveOptions -notmatch [regex]::Escape($required)) {
$effectiveOptions = "$effectiveOptions $required"
}
}

$env:JDK_JAVA_OPTIONS = $effectiveOptions

Write-Host "JDK_JAVA_OPTIONS EvoSuite/JDK21:"
Write-Host "  $env:JDK_JAVA_OPTIONS"

& $ScriptBlockToRun
}
finally {
$env:JDK_JAVA_OPTIONS = $oldJdkJavaOptions
}
}

function Invoke-EvoSuiteMavenTest {
Write-Section "Esecuzione test EvoSuite DMM come JUnit 4 / CUT reale Java 21"

Invoke-Maven `
        -JdkHome $script:Jdk21Home `
        -WorkingDirectory $script:ModuleDir `
        -Arguments @(
"-f", $script:TemporaryRunPom,
"-Pevosuite-dmm-temp",
"-DtargetJdk=21",
"-Dmaven.build.cache.skipCache=true",
"-DskipTests=false",
"-Dmaven.test.skip=false",
"-Dspotless.check.skip=true",
"-Dcheckstyle.skip=true",
"-Drat.skip=true",
"-Dtest=$($script:GeneratedTestFqn)",
"-Dsurefire.failIfNoSpecifiedTests=true",
"test"
)
}

function Invoke-EvoSuiteJaCoCo {
Write-Section "JaCoCo - test EvoSuite DefaultMappingManager"

Invoke-Maven `
        -JdkHome $script:Jdk21Home `
        -WorkingDirectory $script:ModuleDir `
        -Arguments @(
"-f", $script:TemporaryRunPom,
"-Pevosuite-dmm-temp",
"-DtargetJdk=21",
"-Dmaven.build.cache.skipCache=true",
"-DskipTests=false",
"-Dmaven.test.skip=false",
"-Dspotless.check.skip=true",
"-Dcheckstyle.skip=true",
"-Drat.skip=true",
"-Djacoco.skip=false",
"-Djacoco.destFile=$($script:StableResultsRoot)\jacoco\jacoco.exec",
"-Djacoco.dataFile=$($script:StableResultsRoot)\jacoco\jacoco.exec",
"-Dtest=$($script:GeneratedTestFqn)",
"-Dsurefire.failIfNoSpecifiedTests=true",
"jacoco:prepare-agent",
"test",
"jacoco:report"
)
}

function Invoke-EvoSuitePit {
Write-Section "PIT - DefaultMappingManager / test EvoSuite plain JUnit"

Invoke-Maven `
        -JdkHome $script:Jdk21Home `
        -WorkingDirectory $script:ModuleDir `
        -Arguments @(
"-f", $script:TemporaryRunPom,
"-Pevosuite-dmm-temp",
"-DtargetJdk=21",
"-Dmaven.build.cache.skipCache=true",
"-DskipTests=false",
"-Dmaven.test.skip=false",
"-Dspotless.check.skip=true",
"-Dcheckstyle.skip=true",
"-Drat.skip=true",
"-Djacoco.skip=true",
"-DtargetClasses=$($script:TargetClass)",
"-DtargetTests=$($script:GeneratedTestFqn)",
"-Dthreads=1",
"-DreportsDirectory=$($script:StableResultsRoot)\pit",
"-DtimestampedReports=false",
"test-compile",
"org.pitest:pitest-maven:1.25.9:mutationCoverage"
)
}

function Clean-Generated {
Write-Section "Pulizia output EvoSuite DefaultMappingManager"

foreach ($path in @(
$script:GeneratedTestRoot,
$script:EvoGeneratedRoot,
$script:CompatRoot
)) {
if (Test-Path $path) {
Write-Host "Rimuovo: $path"
Remove-Item -Recurse -Force $path
}
}

Cleanup-TemporaryRunPom

Write-Host "Pulizia completata."
Write-Host "src\test\java NON è stato toccato."
}

if ($Action -eq "info") {
Write-Section "Configurazione EvoSuite DefaultMappingManager"
Show-Configuration
Write-Host "Generated test root: $script:GeneratedTestRoot"
Write-Host "Compatibility root : $script:CompatRoot"
exit 0
}

if ($Action -eq "clean-generated") {
Clean-Generated
exit 0
}

Write-Section "EvoSuite RUN - DefaultMappingManager - Windows"
Show-Configuration

Prepare-RunEnvironment

# Evita la collisione di scope PowerShell già risolta nella pipeline JWS.
$requestedAction = $Action

try {
Invoke-WithJava21EvoSuiteCompatibility {
Write-Host "Azione effettivamente eseguita: $requestedAction"

switch ($requestedAction) {
"test" {
Invoke-EvoSuiteMavenTest
}

"jacoco" {
Invoke-EvoSuiteJaCoCo
}

"pit" {
Invoke-EvoSuitePit
}

"all" {
Invoke-EvoSuiteMavenTest
Invoke-EvoSuiteJaCoCo
Invoke-EvoSuitePit
}

default {
throw "Azione non riconosciuta: '$requestedAction'"
}
}
}

Show-EvoSuiteReports
}
finally {
Cleanup-TemporaryRunPom
}

Write-Section "Operazione completata"
Write-Host "Azione: $Action"
Write-Host "CUT:    $script:TargetClass"
Write-Host "JDK di esecuzione reale: 21"
Write-Host "Harness: plain JUnit 4 (corpi/assertion EvoSuite invariati)"
Write-Host "Report stabili: $script:StableResultsRoot"