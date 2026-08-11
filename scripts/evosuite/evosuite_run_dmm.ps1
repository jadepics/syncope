param(
    [Parameter(Position=0)]
    [ValidateSet("test", "jacoco", "pit", "all", "clean-generated", "info")]
    [string]$Action = "test",

    [switch]$SkipRebuild
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

. (Join-Path $PSScriptRoot "evosuite_common_dmm.ps1")

# Report archive stabile: non viene cancellato da "mvn clean".
$script:ReportTimestamp = Get-Date -Format "yyyyMMdd-HHmmss"
$script:StableResultsRoot = Join-Path $script:RepoRoot "evosuite-results\dmm\$($script:ReportTimestamp)"


# Durante test/JaCoCo/PIT il modulo deve restare dentro il REACTOR Maven.
# Se lo eseguiamo come POM standalone, Maven risolve i 5.0.0-SNAPSHOT da
# ~/.m2: nel repository locale dell'utente tali JAR possono essere stati
# compilati con JDK 25 (major 69), mentre questa pipeline usa JDK 21
# (major 65). Per evitare QUALSIASI dipendenza da quei JAR stale, durante
# l'esecuzione installiamo temporaneamente:
#   1) il POM modulo con il profilo EvoSuite;
#   2) il POM root con un profilo "reactor guard" che salta i test nei
#      moduli upstream, ma continua a compilarne il production bytecode.
# Entrambi i POM originali vengono ripristinati byte-per-byte nel finally.
$script:RootPomPath = Join-Path $script:RepoRoot "pom.xml"
$script:ModulePomPath = Join-Path $script:ModuleDir "pom.xml"
$script:TemporaryRootGuardPom = Join-Path `
    $script:RepoRoot `
    "target\evosuite-dmm-run\pom-root-reactor-guard.xml"

$script:OriginalRootPomBytes = $null
$script:OriginalModulePomBytes = $null
$script:ReactorPomSwapApplied = $false

function Ensure-Directory {
    param([Parameter(Mandatory=$true)][string]$Path)
    [void][System.IO.Directory]::CreateDirectory($Path)
}

function Configure-TemporaryPomReportOutputs {
    # I report vengono scritti DIRETTAMENTE fuori da target.
    # In questo modo:
    #  - mvn clean non li cancella;
    #  - non dipendiamo dai path ereditati dal parent Syncope;
    #  - evitiamo problemi dovuti alla Maven Build Cache.
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

    function New-XmlElement([string]$Name, [string]$Value = $null) {
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
# Compiler TEST: compila SOLO i due sorgenti EvoSuite.
#
# Questo evita che "run_dmm test" ricompili anche tutti i test manuali
# presenti in src/test/java. I test manuali restano completamente
# intatti ma non fanno parte di questa esecuzione automatica EvoSuite.
#
# Inoltre <skip>false</skip> sovrascrive il reactor guard ereditato dal
# parent root, che invece salta testCompile nei moduli upstream.
# ---------------------------------------------------------------------
$compiler = Get-OrCreatePlugin `
        -GroupId "org.apache.maven.plugins" `
        -ArtifactId "maven-compiler-plugin" `
        -Version "3.15.0"

[void]$compiler.Configuration.AppendChild(
        (New-XmlElement "skip" "false")
)

$testIncludes = New-XmlElement "testIncludes"
[void]$testIncludes.AppendChild(
        (New-XmlElement "testInclude" "**/DefaultMappingManager_ESTest.java")
)
[void]$testIncludes.AppendChild(
        (New-XmlElement "testInclude" "**/DefaultMappingManager_ESTest_scaffolding.java")
)
[void]$compiler.Configuration.AppendChild($testIncludes)

# ---------------------------------------------------------------------
# Surefire: report XML/TXT direttamente nella directory stabile.
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

# Sovrascrive <skipTests>true</skip> del reactor guard per il SOLO modulo CUT.
[void]$surefire.Configuration.AppendChild(
        (New-XmlElement "skipTests" "false")
)

# Non usiamo più -Dtest globale: così i moduli upstream non devono cercare
# una classe di test che appartiene soltanto a provisioning-java.
$surefireIncludes = New-XmlElement "includes"
[void]$surefireIncludes.AppendChild(
        (New-XmlElement "include" "**/DefaultMappingManager_ESTest.java")
)
[void]$surefire.Configuration.AppendChild($surefireIncludes)

# EvoSuite 1.2.0 genera questa suite come JUnit 4 con EvoRunner.
# Syncope porta nel classpath anche JUnit Platform, quindi Surefire 3.5.x
# auto-seleziona JUnitPlatformProvider. Per evitare discovery/compatibility
# attraverso Vintage, forziamo il provider JUnit 4.7 nativo di Surefire.
#
# Documentazione Maven Surefire 3.5.x:
# aggiungere surefire-junit47 come dependency del plugin forza il provider.
$surefireDependencies = $surefire.Plugin.SelectSingleNode(
        "*[local-name()='dependencies']"
)
if ($null -eq $surefireDependencies) {
    $surefireDependencies = New-XmlElement "dependencies"
    [void]$surefire.Plugin.AppendChild($surefireDependencies)
}

# Evita duplicati.
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

# Il Vintage Engine non serve più per questa pipeline: la suite viene
# eseguita direttamente dal provider JUnit 4 di Surefire.
$profileDependencies = $profile.SelectSingleNode("*[local-name()='dependencies']")
if ($null -ne $profileDependencies) {
    $vintageNode = $profileDependencies.SelectSingleNode(
            "*[local-name()='dependency'][*[local-name()='groupId' and text()='org.junit.vintage'] and *[local-name()='artifactId' and text()='junit-vintage-engine']]"
    )
    if ($null -ne $vintageNode) {
        [void]$profileDependencies.RemoveChild($vintageNode)
    }
}

# Diagnostica più leggibile in caso di errore del fork.
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
# JaCoCo: .exec + HTML/XML/CSV direttamente nella directory stabile.
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
# PIT: HTML + XML + CSV direttamente nella directory stabile.
# ---------------------------------------------------------------------
$pit = Get-OrCreatePlugin `
        -GroupId "org.pitest" `
        -ArtifactId "pitest-maven" `
        -Version "1.25.9"

# CRITICO NEL REACTOR MULTI-MODULO:
# il parent guard configura PIT con <skip>true</skip> per gli upstream.
# Maven normalmente effettua il merge delle configurazioni plugin.
# combine.self="override" rende esplicito che, nel SOLO modulo CUT,
# questa configurazione PIT sostituisce integralmente quella ereditata.
$combineSelf = $doc.CreateAttribute("combine.self")
$combineSelf.Value = "override"
[void]$pit.Configuration.Attributes.Append($combineSelf)

[void]$pit.Configuration.AppendChild(
        (New-XmlElement "reportsDirectory" $pitDir)
)
[void]$pit.Configuration.AppendChild(
        (New-XmlElement "timestampedReports" "false")
)
[void]$pit.Configuration.AppendChild(
        (New-XmlElement "threads" "1")
)
[void]$pit.Configuration.AppendChild(
        (New-XmlElement "skip" "false")
)

# Non trasformiamo l'assenza di mutanti in un falso problema di reporting.
# Il report deve comunque essere prodotto e ispezionabile.
[void]$pit.Configuration.AppendChild(
        (New-XmlElement "failWhenNoMutations" "false")
)

# Diagnostica PIT più utile in caso di problemi residui.
[void]$pit.Configuration.AppendChild(
        (New-XmlElement "verbose" "true")
)
[void]$pit.Configuration.AppendChild(
        (New-XmlElement "exportLineCoverage" "true")
)

$targetClasses = New-XmlElement "targetClasses"
[void]$targetClasses.AppendChild((New-XmlElement "param" $script:TargetClass))
[void]$pit.Configuration.AppendChild($targetClasses)

$targetTests = New-XmlElement "targetTests"
[void]$targetTests.AppendChild((New-XmlElement "param" $script:GeneratedTestFqn))
[void]$pit.Configuration.AppendChild($targetTests)

$formats = New-XmlElement "outputFormats"
foreach ($format in @("HTML", "XML", "CSV")) {
    [void]$formats.AppendChild((New-XmlElement "param" $format))
}
[void]$pit.Configuration.AppendChild($formats)

# PIT avvia JVM figlie (minion). Passiamo esplicitamente le aperture Java 21
# già risultate necessarie nella pipeline EvoSuite, anziché affidarci
# esclusivamente all'ereditarietà di JDK_JAVA_OPTIONS.
$jvmArgs = New-XmlElement "jvmArgs"
foreach ($jvmArg in @(
    "-Djava.security.manager=allow",
    "--add-opens=java.desktop/java.awt=ALL-UNNAMED",
    "--add-opens=java.base/java.util=ALL-UNNAMED",
    "--add-opens=java.base/java.net=ALL-UNNAMED"
)) {
    [void]$jvmArgs.AppendChild((New-XmlElement "jvmArg" $jvmArg))
}
[void]$pit.Configuration.AppendChild($jvmArgs)

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
Write-Section "Risultati EvoSuite"

$foundAny = $false

foreach ($name in @("surefire", "jacoco", "pit")) {
$dir = Join-Path $script:StableResultsRoot $name

if (Test-Path -LiteralPath $dir -PathType Container) {
$files = @(Get-ChildItem -LiteralPath $dir -File -Recurse -ErrorAction SilentlyContinue)

if ($files.Count -gt 0) {
$foundAny = $true
Write-Host ""
Write-Host "$name : $($files.Count) file"
$files |
Select-Object -First 12 |
ForEach-Object { Write-Host "  $($_.FullName)" }
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

In questo caso conserva l'output Maven completo: significa che Surefire/
JaCoCo/PIT sono stati saltati o intercettati prima della produzione dei report.
"@
}

Write-Host ""
Write-Host "Report stabili:"
Write-Host "  $script:StableResultsRoot"
}



function New-TemporaryRootReactorGuardPom {
Write-Section "Preparazione POM root temporaneo - reactor guard"

if (-not (Test-Path -LiteralPath $script:RootPomPath -PathType Leaf)) {
throw "POM root non trovato: $script:RootPomPath"
}

$guardDir = Split-Path $script:TemporaryRootGuardPom -Parent
Ensure-Directory $guardDir

$doc = New-Object System.Xml.XmlDocument
$doc.PreserveWhitespace = $true
$doc.Load($script:RootPomPath)

$project = $doc.DocumentElement
$ns = $project.NamespaceURI

function New-GuardElement([string]$Name, [string]$Value = $null) {
$node = $doc.CreateElement($Name, $ns)
if ($null -ne $Value) {
$node.InnerText = $Value
}
return $node
}

$profiles = $project.SelectSingleNode("*[local-name()='profiles']")
if ($null -eq $profiles) {
$profiles = New-GuardElement "profiles"
[void]$project.AppendChild($profiles)
}

$existing = $profiles.SelectSingleNode(
"*[local-name()='profile'][*[local-name()='id' and text()='evosuite-dmm-reactor-guard']]"
)
if ($null -ne $existing) {
throw "Il POM root contiene già il profilo evosuite-dmm-reactor-guard."
}

$profile = New-GuardElement "profile"
[void]$profiles.AppendChild($profile)
[void]$profile.AppendChild(
(New-GuardElement "id" "evosuite-dmm-reactor-guard")
)

$build = New-GuardElement "build"
[void]$profile.AppendChild($build)

$plugins = New-GuardElement "plugins"
[void]$build.AppendChild($plugins)

function Add-GuardPlugin(
[string]$GroupId,
[string]$ArtifactId,
[string]$Version,
[string]$ConfigName,
[string]$ConfigValue
) {
$p = New-GuardElement "plugin"
[void]$plugins.AppendChild($p)
[void]$p.AppendChild((New-GuardElement "groupId" $GroupId))
[void]$p.AppendChild((New-GuardElement "artifactId" $ArtifactId))
[void]$p.AppendChild((New-GuardElement "version" $Version))

$cfg = New-GuardElement "configuration"
[void]$p.AppendChild($cfg)
[void]$cfg.AppendChild((New-GuardElement $ConfigName $ConfigValue))
}

# Nei moduli upstream ci serve SOLO il production bytecode JDK21.
# Il modulo provisioning-java sovrascrive queste tre opzioni con false
# tramite il profilo evosuite-dmm-temp.
Add-GuardPlugin `
    "org.apache.maven.plugins" `
    "maven-compiler-plugin" `
    "3.15.0" `
    "skip" `
    "true"

Add-GuardPlugin `
    "org.apache.maven.plugins" `
    "maven-surefire-plugin" `
    "3.5.6" `
    "skipTests" `
    "true"

Add-GuardPlugin `
    "org.pitest" `
    "pitest-maven" `
    "1.25.9" `
    "skip" `
    "true"

$doc.Save($script:TemporaryRootGuardPom)

Write-Host "POM root reactor guard creato:"
Write-Host "  $script:TemporaryRootGuardPom"

return $script:TemporaryRootGuardPom
}

function Install-TemporaryReactorPoms {
Write-Section "Attivazione temporanea POM reactor-aware"

if (-not (Test-Path -LiteralPath $script:TemporaryRunPom -PathType Leaf)) {
throw "POM modulo temporaneo non trovato: $script:TemporaryRunPom"
}

if (-not (Test-Path -LiteralPath $script:TemporaryRootGuardPom -PathType Leaf)) {
throw "POM root guard non trovato: $script:TemporaryRootGuardPom"
}

if ($script:ReactorPomSwapApplied) {
throw "Lo swap temporaneo dei POM risulta già attivo."
}

$script:OriginalRootPomBytes = [System.IO.File]::ReadAllBytes(
$script:RootPomPath
)
$script:OriginalModulePomBytes = [System.IO.File]::ReadAllBytes(
$script:ModulePomPath
)

$traceDir = Join-Path $script:StableResultsRoot "pom-trace"
Ensure-Directory $traceDir

[System.IO.File]::WriteAllBytes(
(Join-Path $traceDir "root-pom.original.xml"),
$script:OriginalRootPomBytes
)
[System.IO.File]::WriteAllBytes(
(Join-Path $traceDir "module-pom.original.xml"),
$script:OriginalModulePomBytes
)

# Segniamo lo swap come attivo PRIMA delle scritture: se la seconda copia
# fallisce, il finally potrà comunque ripristinare il primo POM già cambiato.
$script:ReactorPomSwapApplied = $true

try {
Copy-Item `
    -LiteralPath $script:TemporaryRootGuardPom `
    -Destination $script:RootPomPath `
    -Force

Copy-Item `
    -LiteralPath $script:TemporaryRunPom `
    -Destination $script:ModulePomPath `
    -Force
}
catch {
Restore-OriginalReactorPoms
throw
}

Write-Host "POM root attivo:"
Write-Host "  $script:RootPomPath"
Write-Host "  + profilo evosuite-dmm-reactor-guard"
Write-Host "POM modulo attivo:"
Write-Host "  $script:ModulePomPath"
Write-Host "  + profilo evosuite-dmm-temp"
Write-Host ""
Write-Host "Maven verrà eseguito dal ROOT reactor con:"
Write-Host "  -pl core/provisioning-java -am"
Write-Host ""
Write-Host "Questo impedisce l'uso dei JAR Syncope SNAPSHOT JDK25 presenti in ~/.m2."
}

function Restore-OriginalReactorPoms {
if (-not $script:ReactorPomSwapApplied) {
return
}

if ($null -ne $script:OriginalModulePomBytes) {
[System.IO.File]::WriteAllBytes(
$script:ModulePomPath,
$script:OriginalModulePomBytes
)
}

if ($null -ne $script:OriginalRootPomBytes) {
[System.IO.File]::WriteAllBytes(
$script:RootPomPath,
$script:OriginalRootPomBytes
)
}

$script:ReactorPomSwapApplied = $false

Write-Host "POM originali ripristinati byte-per-byte:"
Write-Host "  $script:RootPomPath"
Write-Host "  $script:ModulePomPath"
}

function Assert-GeneratedTestSurefireReport {
$surefireDir = Join-Path $script:StableResultsRoot "surefire"

if (-not (Test-Path -LiteralPath $surefireDir -PathType Container)) {
throw "Surefire non ha creato la directory report attesa: $surefireDir"
}

$reports = @(
Get-ChildItem `
    -LiteralPath $surefireDir `
    -File `
    -Recurse `
    -ErrorAction SilentlyContinue
)

if ($reports.Count -eq 0) {
throw "Surefire non ha prodotto alcun report in: $surefireDir"
}

$matched = $false

foreach ($report in $reports) {
if ($report.Extension -ieq ".xml" -or $report.Extension -ieq ".txt") {
$content = [System.IO.File]::ReadAllText($report.FullName)

if ($content -match [regex]::Escape("DefaultMappingManager_ESTest")) {
$matched = $true
break
}
}
}

if (-not $matched) {
throw @"
Surefire ha prodotto report, ma nessuno contiene DefaultMappingManager_ESTest.

Directory:
    $surefireDir

Questo controllo impedisce di considerare riuscita una build reactor in cui
il test EvoSuite target fosse stato accidentalmente saltato.
"@
}

Write-Host "Surefire ha eseguito DefaultMappingManager_ESTest: OK"
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

$traceDir = Join-Path $script:StableResultsRoot "generated-test-trace"
Ensure-Directory $traceDir

$originalCopy = Join-Path $traceDir "DefaultMappingManager_ESTest.original.java"
[System.IO.File]::WriteAllText($originalCopy, $original)

$adapted = $original
$removedCount = 0

# 1) Import dell'harness EvoSuite / JUnit RunWith.
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

# 2) @RunWith(EvoRunner.class), anche se sulla stessa riga
#    di @EvoRunnerParameters(...).
$before = $adapted
$adapted = [regex]::Replace(
$adapted,
'@(?:org\.junit\.runner\.)?RunWith\s*\(\s*(?:org\.evosuite\.runtime\.)?EvoRunner\.class\s*\)\s*',
''
)
if ($adapted -ne $before) {
$removedCount++
}

# 3) @EvoRunnerParameters(...), anche sulla stessa riga.
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

# 4) Verifica finale sul CODICE, ignorando commenti.
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
Le righe residue sono state stampate sopra.
"@
}

# 5) Solo ora scriviamo la variante temporanea.
[System.IO.File]::WriteAllText($script:GeneratedTestFile, $adapted)
$script:PlainJUnitAdaptationApplied = $true

$adaptedCopy = Join-Path $traceDir "DefaultMappingManager_ESTest.java21-plain-junit.java"
[System.IO.File]::WriteAllText($adaptedCopy, $adapted)

Write-Host "Harness EvoRunner rimosso TEMPORANEAMENTE."
Write-Host "Blocchi harness rimossi: $removedCount"
Write-Host "Corpi dei test e assertion NON modificati."
Write-Host "Originale salvato in:"
Write-Host "  $originalCopy"
Write-Host "Variante eseguita salvata in:"
Write-Host "  $adaptedCopy"
}

function Restore-OriginalEvoSuiteTest {
if ($script:PlainJUnitAdaptationApplied -and $null -ne $script:OriginalGeneratedTestContent) {
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
Write-Host "Build reale Syncope saltata per richiesta (-SkipRebuild)."
return
}

Write-Section "Build REALE Syncope con JDK 21"

Invoke-Maven -JdkHome $script:Jdk21Home -WorkingDirectory $script:RepoRoot -Arguments @(
"-pl", "core/provisioning-java",
"-am",
"-DtargetJdk=21",

# IMPORTANTE:
# -DskipTests salta solo l'esecuzione ma continua a compilare src/test/java.
# Nel tuo progetto sono presenti test Randoop JUnit 4 che il POM normale
# di core/provisioning-java non compila senza JUnit 4.
#
# In questa fase ci serve soltanto ricostruire il production bytecode reale
# di Syncope con JDK 21. I test verranno compilati nella fase successiva
# usando il POM temporaneo EvoSuite, che aggiunge JUnit 4 / Vintage.
"-Dmaven.test.skip=true",

# Apache RAT controlla gli header di licenza anche dei file locali
# temporanei/generati (.tmp/randoop, script PowerShell, ecc.).
# Questa è una build tecnica locale per il testing EvoSuite:
# saltiamo RAT senza modificare il POM reale del progetto.
"-Drat.skip=true",

"-Dspotless.check.skip=true",
"-Dcheckstyle.skip=true",

# In questa fase ci serve solo il bytecode production reale della CUT.
# "install" attraversa package/verify e attiva controlli di packaging
# (Geronimo verify-legal-files, Javadoc, ecc.) non pertinenti a EvoSuite.
# "clean compile" ricostruisce invece target/classes con JDK 21 e si ferma
# prima di package/verify.
"clean",
"compile"
)

if (-not (Test-Path $script:RealClassFile)) {
throw "Dopo la build JDK21 la CUT reale non è stata trovata: $script:RealClassFile"
}

$major = Get-ClassMajorVersion -ClassFile $script:RealClassFile
Write-Host "CUT reale classfile major version: $major"

# Java 21 = classfile major 65.
# Controllo esplicito per evitare di eseguire accidentalmente i test
# contro la CUT compatibility Java 11 o contro una build Java 25.
if ($major -ne 65) {
throw @"
La CUT reale non risulta compilata per Java 21.
Major trovata: $major
Major attesa per Java 21: 65

Controlla che:
  - JDK21_HOME punti davvero a JDK 21
  - Maven riceva -DtargetJdk=21
"@
}
}

function Prepare-RunEnvironment {
Assert-GeneratedTestsExist
Install-EvoSuiteRuntimeIntoLocalMaven
Build-RealSyncope

# EvoSuite 1.2.0 non sa reinstrumentare classfile Java 21 (major 65).
# Per questa CUT eseguiamo i test generati come JUnit 4 standard,
# mantenendo invariati corpi e assertion.
Convert-EvoSuiteTestToPlainJUnitForJava21

if (Test-Path $script:TemporaryRunPom) {
Remove-Item -Force $script:TemporaryRunPom
}

if (Test-Path $script:TemporaryRootGuardPom) {
Remove-Item -Force $script:TemporaryRootGuardPom
}

$null = New-TemporaryEvoSuiteRunPom
Configure-TemporaryPomReportOutputs
$null = New-TemporaryRootReactorGuardPom

Write-Host "POM modulo temporaneo EvoSuite:"
Write-Host "  $script:TemporaryRunPom"
Write-Host "POM root temporaneo reactor guard:"
Write-Host "  $script:TemporaryRootGuardPom"

Install-TemporaryReactorPoms
}

function Cleanup-TemporaryRunPom {
# Ordine intenzionale:
# 1) ripristina subito i POM reali;
# 2) ripristina il test EvoSuite originale;
# 3) elimina soltanto i file temporanei.
Restore-OriginalReactorPoms
Restore-OriginalEvoSuiteTest

if (Test-Path $script:TemporaryRunPom) {
Remove-Item -Force $script:TemporaryRunPom
}

if (Test-Path $script:TemporaryRootGuardPom) {
Remove-Item -Force $script:TemporaryRootGuardPom
}
}

function Invoke-WithJava21SecurityManagerAllow {
param(
[Parameter(Mandatory=$true)]
[scriptblock]$ScriptBlockToRun
)

# IMPORTANTE:
# questo parametro NON deve chiamarsi $Action, perché $Action è anche
# l'argomento principale dello script ("test", "jacoco", "pit", "all").
# In PowerShell lo scriptblock eseguito qui può vedere lo scope dinamico
# della funzione; usare lo stesso nome nascondeva il valore "all" e
# impediva l'esecuzione di qualunque ramo dello switch.
#
# JDK 21 disabilita per default l'installazione dinamica del SecurityManager.
# EvoSuite 1.2.0 usa System.setSecurityManager() nel proprio runtime.
# JDK_JAVA_OPTIONS viene ereditata anche dalla JVM forkata da Surefire.
$oldJdkJavaOptions = $env:JDK_JAVA_OPTIONS

try {
# Compatibilità EvoSuite 1.2.0 su JDK 21:
# 1) consente l'installazione dinamica del SecurityManager;
# 2) consente a org.evosuite.runtime.GuiSupport di accedere via
#    reflection a java.awt.GraphicsEnvironment.headless.
$requiredOptions = @(
"-Djava.security.manager=allow",
"--add-opens=java.desktop/java.awt=ALL-UNNAMED",
"--add-opens=java.base/java.util=ALL-UNNAMED",
"--add-opens=java.base/java.net=ALL-UNNAMED"
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
Write-Section "Esecuzione test EvoSuite / ROOT REACTOR / CUT REALE Java 21"

Invoke-Maven -JdkHome $script:Jdk21Home -WorkingDirectory $script:RepoRoot -Arguments @(
"-pl", $script:ModuleRelative,
"-am",
"-Pevosuite-dmm-reactor-guard,evosuite-dmm-temp",
"-DtargetJdk=21",
"-Dmaven.build.cache.skipCache=true",
"-Dspotless.check.skip=true",
"-Dcheckstyle.skip=true",
"-Drat.skip=true",
"test"
)

Assert-GeneratedTestSurefireReport
}

function Invoke-EvoSuiteJaCoCo {
Write-Section "JaCoCo - ROOT REACTOR / test EvoSuite plain JUnit / DMM"

$jacocoExec = Join-Path $script:StableResultsRoot "jacoco\jacoco.exec"

Invoke-Maven -JdkHome $script:Jdk21Home -WorkingDirectory $script:RepoRoot -Arguments @(
"-pl", $script:ModuleRelative,
"-am",
"-Pevosuite-dmm-reactor-guard,evosuite-dmm-temp",
"-DtargetJdk=21",
"-Dmaven.build.cache.skipCache=true",
"-Dspotless.check.skip=true",
"-Dcheckstyle.skip=true",
"-Drat.skip=true",
"-Djacoco.skip=false",
"-Djacoco.destFile=$jacocoExec",
"-Djacoco.dataFile=$jacocoExec",
"org.jacoco:jacoco-maven-plugin:0.8.15:prepare-agent",
"test",
"org.jacoco:jacoco-maven-plugin:0.8.15:report"
)

Assert-GeneratedTestSurefireReport

$jacocoDir = Join-Path $script:StableResultsRoot "jacoco"
$jacocoFiles = @(
Get-ChildItem `
    -LiteralPath $jacocoDir `
    -File `
    -Recurse `
    -ErrorAction SilentlyContinue
)

if ($jacocoFiles.Count -eq 0) {
throw "JaCoCo non ha prodotto file nella directory stabile: $jacocoDir"
}

Write-Host ""
Write-Host "Report JaCoCo stabile:"
Write-Host "  $jacocoDir"
}


function Assert-EffectivePitConfiguration {
Write-Section "Preflight PIT - effective POM del modulo CUT"

$pitDir = Join-Path $script:StableResultsRoot "pit"
Ensure-Directory $pitDir

$effectivePom = Join-Path $pitDir "effective-pom-pit.xml"

if (Test-Path -LiteralPath $effectivePom) {
Remove-Item -LiteralPath $effectivePom -Force
}

# Eseguiamo help:effective-pom direttamente sul modulo, mentre i POM
# reactor-aware temporanei sono ancora installati. Non compila nulla:
# ci serve solo vedere la configurazione Maven effettivamente risultante.
Invoke-Maven -JdkHome $script:Jdk21Home `
        -WorkingDirectory $script:ModuleDir `
        -Arguments @(
"-Pevosuite-dmm-reactor-guard,evosuite-dmm-temp",
"-DtargetJdk=21",
"help:effective-pom",
"-Doutput=$effectivePom"
)

if (-not (Test-Path -LiteralPath $effectivePom -PathType Leaf)) {
throw "Maven non ha prodotto l'effective POM PIT: $effectivePom"
}

$doc = New-Object System.Xml.XmlDocument
$doc.Load($effectivePom)

$pitPlugin = $doc.SelectSingleNode(
"/*[local-name()='project']/*[local-name()='build']/*[local-name()='plugins']/" +
"*[local-name()='plugin'][" +
"*[local-name()='groupId' and text()='org.pitest'] and " +
"*[local-name()='artifactId' and text()='pitest-maven']]"
)

if ($null -eq $pitPlugin) {
throw "pitest-maven assente dall'effective POM del modulo CUT."
}

$cfg = $pitPlugin.SelectSingleNode("*[local-name()='configuration']")
if ($null -eq $cfg) {
throw "Configurazione PIT assente dall'effective POM."
}

function Get-PitCfgValue([string]$Name) {
$node = $cfg.SelectSingleNode("*[local-name()='$Name']")
if ($null -eq $node) {
return $null
}
return $node.InnerText.Trim()
}

$skip = Get-PitCfgValue "skip"
$reports = Get-PitCfgValue "reportsDirectory"
$timestamped = Get-PitCfgValue "timestampedReports"

$tcNode = $cfg.SelectSingleNode(
"*[local-name()='targetClasses']/*[local-name()='param']"
)
$ttNode = $cfg.SelectSingleNode(
"*[local-name()='targetTests']/*[local-name()='param']"
)

$tc = if ($null -eq $tcNode) { $null } else { $tcNode.InnerText.Trim() }
$tt = if ($null -eq $ttNode) { $null } else { $ttNode.InnerText.Trim() }

Write-Host "Effective PIT skip              : $skip"
Write-Host "Effective PIT reportsDirectory  : $reports"
Write-Host "Effective PIT timestampedReports: $timestamped"
Write-Host "Effective PIT targetClasses     : $tc"
Write-Host "Effective PIT targetTests       : $tt"

if ($skip -ne "false") {
throw "PIT risulta ancora skip=$skip nell'effective POM del modulo CUT."
}

if ($tc -ne $script:TargetClass) {
throw "PIT targetClasses effettivo inatteso: '$tc'"
}

if ($tt -ne $script:GeneratedTestFqn) {
throw "PIT targetTests effettivo inatteso: '$tt'"
}

$expectedReports = [System.IO.Path]::GetFullPath($pitDir)
$actualReports = [System.IO.Path]::GetFullPath($reports)

if ($actualReports -ine $expectedReports) {
throw @"
PIT reportsDirectory effettivo diverso da quello stabile.

Atteso:
    $expectedReports

Effettivo:
    $actualReports
"@
}

Write-Host "Preflight effective POM PIT: OK"
}

function Find-AndStabilizePitReports {
param(
[Parameter(Mandatory=$true)][string]$PitLog
)

$stablePitDir = Join-Path $script:StableResultsRoot "pit"
$defaultPitDir = Join-Path $script:ModuleDir "target\pit-reports"

Ensure-Directory $stablePitDir

# Il log non conta come report.
$reportPatterns = @(
"mutations.xml",
"mutations.csv",
"index.html",
"linecoverage.xml"
)

function Get-ReportFiles([string]$Root) {
if (-not (Test-Path -LiteralPath $Root -PathType Container)) {
return @()
}

return @(
Get-ChildItem `
                -LiteralPath $Root `
                -File `
                -Recurse `
                -ErrorAction SilentlyContinue |
Where-Object {
$_.Name -in $reportPatterns -or
$_.Extension -ieq ".html"
}
)
}

$stableReports = @(Get-ReportFiles $stablePitDir)

if ($stableReports.Count -eq 0) {
$fallbackReports = @(Get-ReportFiles $defaultPitDir)

if ($fallbackReports.Count -gt 0) {
Write-Warning "PIT ha scritto nel path Maven standard; copio il report nella directory stabile."

# -LiteralPath NON espande wildcard. Qui serve -Path perché
# vogliamo copiare il contenuto di target\pit-reports.
Copy-Item `
                -Path (Join-Path $defaultPitDir "*") `
                -Destination $stablePitDir `
                -Recurse `
                -Force

$stableReports = @(Get-ReportFiles $stablePitDir)
}
}

if ($stableReports.Count -eq 0) {
$diagnostic = ""

if ($PitLog -match "(?im)^\[INFO\].*Skipping project") {
$diagnostic += "`nIl log contiene 'Skipping project': PIT è stato saltato da Maven.`n"
}

if ($PitLog -match "(?im)No mutations found") {
$diagnostic += "`nIl log contiene 'No mutations found'.`n"
}

if ($PitLog -match "(?im)DefaultMappingManager") {
$diagnostic += "`nIl target DefaultMappingManager compare nel log PIT.`n"
}

throw @"
PIT è terminato ma non è stato trovato alcun report reale.

Directory stabile:
    $stablePitDir

Fallback controllato:
    $defaultPitDir
$diagnostic
Log Maven PIT:
    $(Join-Path $stablePitDir "pit-maven.log")
"@
}

# Con outputFormats XML/CSV almeno uno dei due deve contenere davvero la CUT.
$evidenceFiles = @(
Get-ChildItem `
            -LiteralPath $stablePitDir `
            -File `
            -Recurse `
            -ErrorAction SilentlyContinue |
Where-Object {
$_.Name -ieq "mutations.xml" -or
$_.Name -ieq "mutations.csv"
}
)

$targetFound = $false

foreach ($file in $evidenceFiles) {
$content = [System.IO.File]::ReadAllText($file.FullName)

if ($content -match [regex]::Escape($script:TargetClass)) {
$targetFound = $true
break
}
}

if (-not $targetFound) {
throw @"
PIT ha prodotto dei file, ma XML/CSV non contengono la CUT:

    $script:TargetClass

Questo controllo evita di accettare per errore un report proveniente
da un altro modulo del reactor.

Directory:
    $stablePitDir
"@
}

Write-Host "Report PIT verificato sulla CUT DefaultMappingManager: OK"
Write-Host "Report PIT stabile:"
Write-Host "  $stablePitDir"
}

function Invoke-EvoSuitePit {
Write-Section "PIT - ROOT REACTOR / DefaultMappingManager / EvoSuite plain JUnit"

$pitDir = Join-Path $script:StableResultsRoot "pit"
Ensure-Directory $pitDir

# Prima controlliamo cosa Maven VEDE davvero dopo il merge parent/child.
Assert-EffectivePitConfiguration

# Il testCompile reactor-aware rimane indispensabile: garantisce che PIT
# lavori con tutte le classi Syncope major 65 costruite nel reactor.
$pitArgs = @(
"-pl", $script:ModuleRelative,
"-am",
"-Pevosuite-dmm-reactor-guard,evosuite-dmm-temp",
"-DtargetJdk=21",
"-Dmaven.build.cache.skipCache=true",
"-Dspotless.check.skip=true",
"-Dcheckstyle.skip=true",
"-Drat.skip=true",
"-Djacoco.skip=true",
"-DreportsDirectory=$pitDir",
"test-compile",
"org.pitest:pitest-maven:1.25.9:mutationCoverage"
)

$pitLogFile = Join-Path $pitDir "pit-maven.log"
$pitOutput = $null

Write-Host ""
Write-Host "Avvio Maven PIT reale..."
Write-Host "Working directory:"
Write-Host "  $script:RepoRoot"
Write-Host "Log persistente:"
Write-Host "  $pitLogFile"
Write-Host "NOTA: eventuali righe STDERR di java/Maven non vengono trattate come"
Write-Host "      fallimento; viene controllato esclusivamente l'exit code."

try {
$pitOutput = Invoke-MavenCapture `
            -JdkHome $script:Jdk21Home `
            -WorkingDirectory $script:RepoRoot `
            -Arguments $pitArgs

[System.IO.File]::WriteAllText(
$pitLogFile,
[string]$pitOutput
)

# Manteniamo visibile nel terminale anche l'output catturato.
Write-Host $pitOutput
}
catch {
[System.IO.File]::WriteAllText(
$pitLogFile,
($_ | Out-String)
)
throw
}

Find-AndStabilizePitReports -PitLog ([string]$pitOutput)
}

function Clean-Generated {
Write-Section "Pulizia output EvoSuite DefaultMappingManager"

foreach ($path in @(
$script:GeneratedTestRoot,
$script:EvoGeneratedRoot,
$script:CompatRoot,
$script:DmmGenerationRoot,
$script:AsmOverlayRoot
)) {
if (Test-Path $path) {
Write-Host "Rimuovo: $path"
Remove-Item -Recurse -Force $path
}
}

Cleanup-TemporaryRunPom
Write-Host "Pulizia completata. src\test\java non è stato toccato."
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

# Copia esplicita dell'azione richiesta: evita qualsiasi ambiguità di scope
# dentro lo scriptblock eseguito dalla funzione SecurityManager.
$requestedAction = $Action

try {
# IMPORTANTE: anche Prepare-RunEnvironment è dentro il try.
# Se un errore avviene DOPO lo swap dei POM, il finally li ripristina sempre.
Prepare-RunEnvironment

Invoke-WithJava21SecurityManagerAllow {
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
throw "Azione non riconosciuta nello switch runtime: '$requestedAction'"
}
}
}

# I plugin scrivono già direttamente in evosuite-results.
Show-EvoSuiteReports
}
finally {
Cleanup-TemporaryRunPom
}

Write-Section "Operazione completata"
Write-Host "Azione: $Action"
Write-Host "CUT:    $script:TargetClass"
Write-Host "JDK di esecuzione reale: 21"
Write-Host "Harness esecuzione: plain JUnit 4 (corpi/assertion EvoSuite invariati)"
Write-Host "Maven execution: ROOT reactor (-pl core/provisioning-java -am)"
Write-Host "SNAPSHOT ~/.m2 JDK25: non usati per i moduli Syncope del reactor"
Write-Host "Report stabili: $script:StableResultsRoot"