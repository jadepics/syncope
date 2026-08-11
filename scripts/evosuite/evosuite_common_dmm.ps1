Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

# ============================================================================
# EvoSuite / Apache Syncope - configurazione comune per Windows 11 + PowerShell
#
# Posizione prevista:
#   <syncope>\scripts\evosuite\evosuite_common.ps1
#
# Questo file NON va eseguito direttamente: viene importato dagli altri script.
# ============================================================================

# Individua automaticamente la root del repository Syncope risalendo
# dalla cartella dello script. Evita dipendenze dal numero esatto di livelli
# della directory scripts\evosuite.
function Find-SyncopeRepoRoot {
    param([Parameter(Mandatory=$true)][string]$StartDirectory)

    $current = (Resolve-Path $StartDirectory).Path

    while ($true) {
        $rootPom = Join-Path $current "pom.xml"
        $provisioningPom = Join-Path $current "core\provisioning-java\pom.xml"

        if ((Test-Path $rootPom) -and (Test-Path $provisioningPom)) {
            return $current
        }

        $parent = Split-Path $current -Parent
        if ([string]::IsNullOrWhiteSpace($parent) -or $parent -eq $current) {
            break
        }

        $current = $parent
    }

    throw @"
Impossibile individuare automaticamente la root del repository Syncope
partendo da:
    $StartDirectory

La root attesa deve contenere contemporaneamente:
    pom.xml
    core\provisioning-java\pom.xml
"@
}

$script:RepoRoot = Find-SyncopeRepoRoot -StartDirectory $PSScriptRoot
$script:ModuleRelative = "core\provisioning-java"
$script:ModuleDir = Join-Path $script:RepoRoot $script:ModuleRelative

$script:TargetClass = "org.apache.syncope.core.provisioning.java.DefaultMappingManager"
$script:TargetClassRelative = "org\apache\syncope\core\provisioning\java\DefaultMappingManager"
$script:TargetSource = Join-Path $script:ModuleDir "src\main\java\$($script:TargetClassRelative).java"
$script:RealClassFile = Join-Path $script:ModuleDir "target\classes\$($script:TargetClassRelative).class"

$script:GeneratedTestRoot = Join-Path $script:ModuleDir "src\test\evosuite"
$script:GeneratedPackageDir = Join-Path $script:GeneratedTestRoot "org\apache\syncope\core\provisioning\java"
$script:GeneratedTestFile = Join-Path $script:GeneratedPackageDir "DefaultMappingManager_ESTest.java"
$script:GeneratedScaffoldingFile = Join-Path $script:GeneratedPackageDir "DefaultMappingManager_ESTest_scaffolding.java"
$script:GeneratedTestFqn = "org.apache.syncope.core.provisioning.java.DefaultMappingManager_ESTest"

$script:CompatRoot = Join-Path $script:RepoRoot "target\evosuite-compat\dmm"
$script:CompatClasses = Join-Path $script:CompatRoot "classes"
$script:CompatSource = Join-Path $script:CompatRoot "src\DefaultMappingManager.java"
$script:CompatRawLib = Join-Path $script:CompatRoot "lib-raw"
$script:CompatLib = Join-Path $script:CompatRoot "lib"
$script:CompatPom = Join-Path $script:CompatRoot "pom.xml"

$script:EvoGeneratedRoot = Join-Path $script:ModuleDir "target\evosuite-generated\dmm"
$script:EvoGeneratedTests = Join-Path $script:EvoGeneratedRoot "evosuite-tests"
$script:EvoReportDir = Join-Path $script:EvoGeneratedRoot "evosuite-report"

$script:TemporaryRunPom = Join-Path $script:ModuleDir "pom-evosuite-dmm-temp.xml"

# DMM: generation against the REAL Java 21 bytecode.
# EvoSuite 1.2.0 embeds ASM 9.2, therefore an ASM overlay is built locally
# and placed before evosuite-1.2.0.jar in the generator runtime classpath.
$script:DmmGenerationRoot = Join-Path $script:RepoRoot "target\evosuite-dmm-generation"
$script:DmmDependencyCpFile = Join-Path $script:DmmGenerationRoot "maven-classpath.txt"

$script:AsmOverlayRoot = Join-Path $script:RepoRoot "target\evosuite-asm-java21-overlay"
$script:AsmOverlayPom = Join-Path $script:AsmOverlayRoot "pom.xml"
$script:AsmOverlayTarget = Join-Path $script:AsmOverlayRoot "target"


function Write-Section {
    param([Parameter(Mandatory=$true)][string]$Text)
    Write-Host ""
    Write-Host ("=" * 78)
    Write-Host $Text
    Write-Host ("=" * 78)
}

function Get-JavaMajorFromHome {
    param([Parameter(Mandatory=$true)][string]$JdkHome)

    $javaExe = Join-Path $JdkHome "bin\java.exe"
    if (-not (Test-Path $javaExe)) {
        return $null
    }

    # java -version scrive normalmente su STDERR.
    # Con $ErrorActionPreference = "Stop", Windows PowerShell può trasformare
    # questo output legittimo in NativeCommandError. Usiamo ProcessStartInfo
    # per catturare stdout/stderr senza coinvolgere la pipeline degli errori.
    $psi = New-Object System.Diagnostics.ProcessStartInfo
    $psi.FileName = $javaExe
    $psi.Arguments = "-version"
    $psi.UseShellExecute = $false
    $psi.RedirectStandardOutput = $true
    $psi.RedirectStandardError = $true
    $psi.CreateNoWindow = $true

    $process = New-Object System.Diagnostics.Process
    $process.StartInfo = $psi

    [void]$process.Start()
    $stdout = $process.StandardOutput.ReadToEnd()
    $stderr = $process.StandardError.ReadToEnd()
    $process.WaitForExit()

    $out = "$stdout`n$stderr"

    if ($process.ExitCode -ne 0) {
        return $null
    }

    if ($out -match 'version\s+"(?:1\.)?(?<major>\d+)') {
        return [int]$Matches["major"]
    }

    return $null
}

function Find-JdkHome {
    param(
        [Parameter(Mandatory=$true)][int]$Major,
        [Parameter(Mandatory=$true)][string]$EnvironmentVariable
    )

    $explicit = [Environment]::GetEnvironmentVariable($EnvironmentVariable)
    if (-not [string]::IsNullOrWhiteSpace($explicit)) {
        $explicit = $explicit.Trim('"')
        $foundMajor = Get-JavaMajorFromHome -JdkHome $explicit
        if ($foundMajor -eq $Major) {
            return (Resolve-Path $explicit).Path
        }
        throw "$EnvironmentVariable='$explicit' non punta a una JDK $Major valida."
    }

    $roots = @(
        (Join-Path $env:USERPROFILE ".jdks"),
        "C:\Program Files\Eclipse Adoptium",
        "C:\Program Files\Microsoft",
        "C:\Program Files\Java"
    ) | Where-Object { Test-Path $_ }

    $candidates = New-Object System.Collections.Generic.List[string]

    foreach ($root in $roots) {
        if (Test-Path (Join-Path $root "bin\java.exe")) {
            $candidates.Add($root)
        }

        Get-ChildItem -Path $root -Directory -ErrorAction SilentlyContinue | ForEach-Object {
            if (Test-Path (Join-Path $_.FullName "bin\java.exe")) {
                $candidates.Add($_.FullName)
            }
        }
    }

    foreach ($candidate in ($candidates | Select-Object -Unique)) {
        if ((Get-JavaMajorFromHome -JdkHome $candidate) -eq $Major) {
            return (Resolve-Path $candidate).Path
        }
    }

    throw @"
JDK $Major non trovata automaticamente.
Installa una JDK $Major oppure, nel terminale PowerShell di IntelliJ, imposta:

    `$env:$EnvironmentVariable = "C:\percorso\alla\jdk-$Major"

e rilancia lo script.
"@
}

function Find-EvoSuiteJar {
    $explicit = $env:EVOSUITE_JAR
    if (-not [string]::IsNullOrWhiteSpace($explicit)) {
        $explicit = $explicit.Trim('"')
        if (-not (Test-Path $explicit)) {
            throw "EVOSUITE_JAR punta a un file inesistente: $explicit"
        }
        return (Resolve-Path $explicit).Path
    }

    $tools = Join-Path $env:USERPROFILE "Tools"
    if (-not (Test-Path $tools)) {
        throw "Directory Tools non trovata: $tools"
    }

    $preferred = Get-ChildItem -Path $tools -Recurse -File -Filter "evosuite-1.2.0.jar" -ErrorAction SilentlyContinue |
            Select-Object -First 1
    if ($null -ne $preferred) {
        return $preferred.FullName
    }

    $fallback = Get-ChildItem -Path $tools -Recurse -File -Filter "*evosuite*.jar" -ErrorAction SilentlyContinue |
            Where-Object { $_.Name -notmatch "runtime" } |
            Select-Object -First 1

    if ($null -eq $fallback) {
        throw @"
Nessun JAR EvoSuite trovato sotto:
    $tools

Estrai evosuite.zip e assicurati che esista un file come:
    C:\Users\micci\Tools\evosuite\evosuite-1.2.0.jar

In alternativa:
    `$env:EVOSUITE_JAR = "C:\percorso\evosuite-1.2.0.jar"
"@
    }

    return $fallback.FullName
}

function Find-MavenExecutable {
    foreach ($name in @("mvn.cmd", "mvn.exe", "mvn")) {
        $cmd = Get-Command $name -ErrorAction SilentlyContinue
        if ($null -ne $cmd) {
            return $cmd.Source
        }
    }
    throw "Maven non trovato nel PATH. Verifica che 'mvn -version' funzioni nel terminale PowerShell di IntelliJ."
}

$script:Jdk21Home = Find-JdkHome -Major 21 -EnvironmentVariable "JDK21_HOME"
$script:EvoSuiteJar = Find-EvoSuiteJar
$script:MavenExe = Find-MavenExecutable

function Invoke-Maven {
    param(
        [Parameter(Mandatory=$true)][string]$JdkHome,
        [Parameter(Mandatory=$true)][string[]]$Arguments,
        [string]$WorkingDirectory = $script:RepoRoot
    )

    $oldJavaHome = $env:JAVA_HOME
    $oldPath = $env:PATH

    Push-Location $WorkingDirectory
    try {
        $env:JAVA_HOME = $JdkHome
        $env:PATH = "$(Join-Path $JdkHome 'bin');$oldPath"

        & $script:MavenExe @Arguments
        if ($LASTEXITCODE -ne 0) {
            throw "Maven terminato con exit code $LASTEXITCODE."
        }
    }
    finally {
        $env:JAVA_HOME = $oldJavaHome
        $env:PATH = $oldPath
        Pop-Location
    }
}

function Invoke-MavenCapture {
    param(
        [Parameter(Mandatory=$true)][string]$JdkHome,
        [Parameter(Mandatory=$true)][string[]]$Arguments,
        [string]$WorkingDirectory = $script:RepoRoot
    )

    $oldJavaHome = $env:JAVA_HOME
    $oldPath = $env:PATH
    $oldErrorActionPreference = $ErrorActionPreference

    Push-Location $WorkingDirectory
    try {
        $env:JAVA_HOME = $JdkHome
        $env:PATH = "$(Join-Path $JdkHome 'bin');$oldPath"

        # CRITICO SU WINDOWS POWERSHELL:
        # con $ErrorActionPreference = "Stop", l'output STDERR di un native
        # command rediretto con 2>&1 può essere trasformato in NativeCommandError.
        #
        # Maven / java scrivono legittimamente su STDERR messaggi come:
        #   NOTE: Picked up JDK_JAVA_OPTIONS: ...
        #
        # Non è un fallimento. Il criterio corretto per un native process è
        # l'exit code. Rendiamo quindi non-terminante SOLO questa invocazione,
        # catturiamo stdout+stderr, salviamo subito LASTEXITCODE e poi
        # ripristiniamo il comportamento globale "Stop".
        $ErrorActionPreference = "Continue"

        $rawOutput = & $script:MavenExe @Arguments 2>&1
        $exitCode = $LASTEXITCODE

        $ErrorActionPreference = $oldErrorActionPreference

        $outputLines = @(
        foreach ($entry in $rawOutput) {
            if ($entry -is [System.Management.Automation.ErrorRecord]) {
                if ($null -ne $entry.Exception -and
                        -not [string]::IsNullOrWhiteSpace($entry.Exception.Message)) {
                    $entry.Exception.Message
                }
                else {
                    $entry.ToString()
                }
            }
            else {
                $entry.ToString()
            }
        }
        )

        $outputText = ($outputLines -join [Environment]::NewLine).Trim()

        if ($exitCode -ne 0) {
            throw "Maven terminato con exit code $exitCode.`n$outputText"
        }

        return $outputText
    }
    finally {
        $ErrorActionPreference = $oldErrorActionPreference
        $env:JAVA_HOME = $oldJavaHome
        $env:PATH = $oldPath
        Pop-Location
    }
}

function Get-MavenProperty {
    param([Parameter(Mandatory=$true)][string]$Name)

    $rootPom = Join-Path $script:RepoRoot "pom.xml"

    try {
        [xml]$xml = Get-Content -Raw $rootPom
        $node = $xml.SelectSingleNode("/*[local-name()='project']/*[local-name()='properties']/*[local-name()='$Name']")
        if ($null -ne $node -and -not [string]::IsNullOrWhiteSpace($node.InnerText)) {
            return $node.InnerText.Trim()
        }
    }
    catch {
        # Fallback Maven sotto.
    }

    $value = Invoke-MavenCapture -JdkHome $script:Jdk21Home -Arguments @(
        "-q",
        "help:evaluate",
        "-Dexpression=$Name",
        "-DforceStdout"
    )

    if ([string]::IsNullOrWhiteSpace($value) -or
            $value -match "null object" -or
            $value -match "invalid expression" -or
            $value -match "^\$\{") {
        throw "Impossibile determinare la proprietà Maven '$Name'."
    }

    return (($value -split "`r?`n") | Where-Object {
        $_ -and $_ -notmatch "^\[" -and $_ -notmatch "WARNING"
    } | Select-Object -Last 1).Trim()
}

function Get-ClassMajorVersion {
    param([Parameter(Mandatory=$true)][string]$ClassFile)

    if (-not (Test-Path $ClassFile)) {
        throw "Class file non trovato: $ClassFile"
    }

    $stream = [System.IO.File]::OpenRead($ClassFile)
    try {
        $bytes = New-Object byte[] 8
        $read = $stream.Read($bytes, 0, 8)
        if ($read -lt 8 -or
                $bytes[0] -ne 0xCA -or
                $bytes[1] -ne 0xFE -or
                $bytes[2] -ne 0xBA -or
                $bytes[3] -ne 0xBE) {
            throw "File non valido come Java class: $ClassFile"
        }
        return (($bytes[6] -shl 8) -bor $bytes[7])
    }
    finally {
        $stream.Dispose()
    }
}

function Test-AndPrepareJarForJava11 {
    param(
        [Parameter(Mandatory=$true)][string]$InputJar,
        [Parameter(Mandatory=$true)][string]$OutputDirectory
    )

    Add-Type -AssemblyName System.IO.Compression.FileSystem

    $jarName = [System.IO.Path]::GetFileName($InputJar)
    $needsStrip = $false
    $badEntries = New-Object System.Collections.Generic.List[string]

    $zip = [System.IO.Compression.ZipFile]::OpenRead($InputJar)
    try {
        foreach ($entry in $zip.Entries) {
            if (-not $entry.FullName.EndsWith(".class", [System.StringComparison]::OrdinalIgnoreCase)) {
                continue
            }

            $mrVersion = $null
            if ($entry.FullName -match '^META-INF/versions/(?<v>\d+)/') {
                $mrVersion = [int]$Matches["v"]
                if ($mrVersion -gt 11) {
                    $needsStrip = $true
                    continue
                }
            }

            $s = $entry.Open()
            try {
                $bytes = New-Object byte[] 8
                $read = $s.Read($bytes, 0, 8)
                if ($read -lt 8) { continue }

                if ($bytes[0] -eq 0xCA -and $bytes[1] -eq 0xFE -and
                        $bytes[2] -eq 0xBA -and $bytes[3] -eq 0xBE) {
                    $major = (($bytes[6] -shl 8) -bor $bytes[7])
                    if ($major -gt 55) {
                        $badEntries.Add("$($entry.FullName) [major=$major]")
                    }
                }
            }
            finally {
                $s.Dispose()
            }
        }
    }
    finally {
        $zip.Dispose()
    }

    if ($badEntries.Count -gt 0) {
        $details = ($badEntries | Select-Object -First 15) -join "`n    "
        throw @"
Dipendenza non compatibile con Java 11:
    $InputJar

Sono presenti classi necessarie con major version > 55:
    $details

La dipendenza non viene eliminata automaticamente: la generazione viene fermata
per evitare un classpath EvoSuite semanticamente incompleto.
"@
    }

    New-Item -ItemType Directory -Force -Path $OutputDirectory | Out-Null
    $outputJar = Join-Path $OutputDirectory $jarName

    if (-not $needsStrip) {
        Copy-Item -Force $InputJar $outputJar
        return $outputJar
    }

    Write-Host "Sanitizzazione multi-release > Java 11: $jarName"

    $work = Join-Path $script:CompatRoot ("jarwork-" + [Guid]::NewGuid().ToString("N"))
    New-Item -ItemType Directory -Force -Path $work | Out-Null

    try {
        [System.IO.Compression.ZipFile]::ExtractToDirectory($InputJar, $work)

        $versionsDir = Join-Path $work "META-INF\versions"
        if (Test-Path $versionsDir) {
            Get-ChildItem $versionsDir -Directory -ErrorAction SilentlyContinue | ForEach-Object {
                $n = 0
                if ([int]::TryParse($_.Name, [ref]$n) -and $n -gt 11) {
                    Remove-Item -Recurse -Force $_.FullName
                }
            }
        }

        # Se modifichiamo un JAR firmato, le firme non sono più valide.
        $metaInf = Join-Path $work "META-INF"
        if (Test-Path $metaInf) {
            Get-ChildItem $metaInf -File -ErrorAction SilentlyContinue |
                    Where-Object { $_.Extension -in @(".SF", ".RSA", ".DSA", ".EC") } |
                    Remove-Item -Force
        }

        if (Test-Path $outputJar) {
            Remove-Item -Force $outputJar
        }

        [System.IO.Compression.ZipFile]::CreateFromDirectory(
                $work,
                $outputJar,
                [System.IO.Compression.CompressionLevel]::Optimal,
                $false
        )
    }
    finally {
        if (Test-Path $work) {
            Remove-Item -Recurse -Force $work
        }
    }

    return $outputJar
}

function New-TemporaryEvoSuiteRunPom {
    $originalPom = Join-Path $script:ModuleDir "pom.xml"
    if (-not (Test-Path $originalPom)) {
        throw "POM del modulo non trovato: $originalPom"
    }

    $doc = New-Object System.Xml.XmlDocument
    $doc.PreserveWhitespace = $true
    $doc.Load($originalPom)

    $project = $doc.DocumentElement
    $ns = $project.NamespaceURI

    function New-Element([string]$Name, [string]$Text = $null) {
        $node = $doc.CreateElement($Name, $ns)
        if ($null -ne $Text) {
            $node.InnerText = $Text
        }
        return $node
    }

    $profiles = $project.SelectSingleNode("*[local-name()='profiles']")
    if ($null -eq $profiles) {
        $profiles = New-Element "profiles"
        [void]$project.AppendChild($profiles)
    }

    $profile = New-Element "profile"
    [void]$profiles.AppendChild($profile)

    [void]$profile.AppendChild((New-Element "id" "evosuite-dmm-temp"))

    $dependencies = New-Element "dependencies"
    [void]$profile.AppendChild($dependencies)

    $dependency = New-Element "dependency"
    [void]$dependencies.AppendChild($dependency)
    [void]$dependency.AppendChild((New-Element "groupId" "org.evosuite"))
    [void]$dependency.AppendChild((New-Element "artifactId" "evosuite-local-runtime"))
    [void]$dependency.AppendChild((New-Element "version" "1.2.0"))
    [void]$dependency.AppendChild((New-Element "scope" "test"))

    # EvoSuite 1.2.0 genera normalmente test JUnit 4.
    # Aggiungiamo JUnit 4 e il Vintage Engine affinché la suite possa essere
    # eseguita anche nei moduli Syncope configurati con JUnit Platform/JUnit 5.
    $junit4 = New-Element "dependency"
    [void]$dependencies.AppendChild($junit4)
    [void]$junit4.AppendChild((New-Element "groupId" "junit"))
    [void]$junit4.AppendChild((New-Element "artifactId" "junit"))
    [void]$junit4.AppendChild((New-Element "version" "4.13.2"))
    [void]$junit4.AppendChild((New-Element "scope" "test"))

    $vintageVersion = $null
    foreach ($propertyName in @("junit.version", "junit5.version", "junit-jupiter.version")) {
        try {
            $candidate = Get-MavenProperty -Name $propertyName
            if (-not [string]::IsNullOrWhiteSpace($candidate)) {
                $vintageVersion = $candidate
                break
            }
        }
        catch {
            # Prova la proprietà successiva.
        }
    }
    if ([string]::IsNullOrWhiteSpace($vintageVersion)) {
        $vintageVersion = "5.11.4"
    }

    $vintage = New-Element "dependency"
    [void]$dependencies.AppendChild($vintage)
    [void]$vintage.AppendChild((New-Element "groupId" "org.junit.vintage"))
    [void]$vintage.AppendChild((New-Element "artifactId" "junit-vintage-engine"))
    [void]$vintage.AppendChild((New-Element "version" $vintageVersion))
    [void]$vintage.AppendChild((New-Element "scope" "test"))

    $build = New-Element "build"
    [void]$profile.AppendChild($build)
    $plugins = New-Element "plugins"
    [void]$build.AppendChild($plugins)

    $plugin = New-Element "plugin"
    [void]$plugins.AppendChild($plugin)
    [void]$plugin.AppendChild((New-Element "groupId" "org.codehaus.mojo"))
    [void]$plugin.AppendChild((New-Element "artifactId" "build-helper-maven-plugin"))
    [void]$plugin.AppendChild((New-Element "version" "3.6.1"))

    $executions = New-Element "executions"
    [void]$plugin.AppendChild($executions)
    $execution = New-Element "execution"
    [void]$executions.AppendChild($execution)
    [void]$execution.AppendChild((New-Element "id" "add-evosuite-test-source"))
    [void]$execution.AppendChild((New-Element "phase" "generate-test-sources"))

    $goals = New-Element "goals"
    [void]$execution.AppendChild($goals)
    [void]$goals.AppendChild((New-Element "goal" "add-test-source"))

    $configuration = New-Element "configuration"
    [void]$execution.AppendChild($configuration)
    $sources = New-Element "sources"
    [void]$configuration.AppendChild($sources)
    [void]$sources.AppendChild((New-Element "source" '${project.basedir}/src/test/evosuite'))

    $doc.Save($script:TemporaryRunPom)
    return $script:TemporaryRunPom
}

function Install-EvoSuiteRuntimeIntoLocalMaven {
    Write-Section "Installazione del JAR EvoSuite nel repository Maven locale (test runtime)"

    Invoke-Maven -JdkHome $script:Jdk21Home -Arguments @(
        "org.apache.maven.plugins:maven-install-plugin:3.1.3:install-file",
        "-Dfile=$($script:EvoSuiteJar)",
        "-DgroupId=org.evosuite",
        "-DartifactId=evosuite-local-runtime",
        "-Dversion=1.2.0",
        "-Dpackaging=jar",
        "-DgeneratePom=true"
    )
}

function Show-Configuration {
    Write-Host "Repository Syncope : $script:RepoRoot"
    Write-Host "Modulo             : $script:ModuleRelative"
    Write-Host "CUT                : $script:TargetClass"
    Write-Host "Target relative    : $script:TargetClassRelative"
    Write-Host "Sorgente CUT       : $script:TargetSource"
    Write-Host "Class reale        : $script:RealClassFile"
    Write-Host "Test package dir   : $script:GeneratedPackageDir"
    Write-Host "JDK 21             : $script:Jdk21Home"
    Write-Host "EvoSuite           : $script:EvoSuiteJar"
    Write-Host "Maven              : $script:MavenExe"
}