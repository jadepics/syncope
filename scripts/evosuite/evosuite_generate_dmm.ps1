Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

# ============================================================================
# EvoSuite / Apache Syncope / DefaultMappingManager
# Configurazione comune - Windows 11 + PowerShell
#
# Posizione prevista:
#   <syncope>\scripts\evosuite\evosuite_common_dmm.ps1
#
# Non eseguire direttamente questo file: viene importato dagli altri script.
# ============================================================================

function Find-SyncopeRepoRoot {
    param([Parameter(Mandatory=$true)][string]$StartDirectory)

    $current = (Resolve-Path $StartDirectory).Path

    while ($true) {
        $rootPom = Join-Path $current "pom.xml"
        $modulePom = Join-Path $current "core\provisioning-java\pom.xml"

        if ((Test-Path $rootPom) -and (Test-Path $modulePom)) {
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

La root attesa deve contenere:
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

# Area compatibility usata SOLO per la generazione EvoSuite.
# Le classi reali del progetto non vengono modificate.
$script:CompatRoot = Join-Path $script:RepoRoot "target\evosuite-compat\dmm"
$script:CompatClasses = Join-Path $script:CompatRoot "classes"
$script:CompatLib = Join-Path $script:CompatRoot "lib"
$script:CompatFlatCp = Join-Path $script:CompatRoot "flatcp"
$script:CompatClasspathFile = Join-Path $script:CompatRoot "maven-classpath.txt"

$script:EvoGeneratedRoot = Join-Path $script:ModuleDir "target\evosuite-generated\dmm"
$script:EvoGeneratedTests = Join-Path $script:EvoGeneratedRoot "evosuite-tests"
$script:EvoReportDir = Join-Path $script:EvoGeneratedRoot "evosuite-report"

$script:TemporaryRunPom = Join-Path $script:ModuleDir "pom-evosuite-dmm-temp.xml"

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
            [void]$candidates.Add($root)
        }

        Get-ChildItem -Path $root -Directory -ErrorAction SilentlyContinue | ForEach-Object {
            if (Test-Path (Join-Path $_.FullName "bin\java.exe")) {
                [void]$candidates.Add($_.FullName)
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

Installa una JDK $Major oppure imposta:
    `$env:$EnvironmentVariable = "C:\percorso\alla\jdk-$Major"
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

    $preferred = Get-ChildItem `
        -Path $tools `
        -Recurse `
        -File `
        -Filter "evosuite-1.2.0.jar" `
        -ErrorAction SilentlyContinue |
            Select-Object -First 1

    if ($null -ne $preferred) {
        return $preferred.FullName
    }

    $fallback = Get-ChildItem `
        -Path $tools `
        -Recurse `
        -File `
        -Filter "*evosuite*.jar" `
        -ErrorAction SilentlyContinue |
            Where-Object { $_.Name -notmatch "runtime" } |
            Select-Object -First 1

    if ($null -eq $fallback) {
        throw @"
Nessun JAR EvoSuite trovato sotto:
    $tools

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

    throw "Maven non trovato nel PATH. Verifica che 'mvn -version' funzioni."
}

$script:Jdk11Home = Find-JdkHome -Major 11 -EnvironmentVariable "JDK11_HOME"
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

    Push-Location $WorkingDirectory

    try {
        $env:JAVA_HOME = $JdkHome
        $env:PATH = "$(Join-Path $JdkHome 'bin');$oldPath"

        $output = & $script:MavenExe @Arguments 2>&1

        if ($LASTEXITCODE -ne 0) {
            throw "Maven terminato con exit code $LASTEXITCODE.`n$($output | Out-String)"
        }

        return ($output | Out-String).Trim()
    }
    finally {
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
        $node = $xml.SelectSingleNode(
                "/*[local-name()='project']/*[local-name()='properties']/*[local-name()='$Name']"
        )

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

    return (($value -split "`r?`n") |
            Where-Object {
                $_ -and $_ -notmatch "^\[" -and $_ -notmatch "WARNING"
            } |
            Select-Object -Last 1).Trim()
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

function Patch-ClassBytesForEvoSuite {
    param(
        [Parameter(Mandatory=$true)][byte[]]$Bytes,
        [int]$TargetMajor = 55
    )

    if ($Bytes.Length -lt 8) {
        return $false
    }

    if ($Bytes[0] -ne 0xCA -or
            $Bytes[1] -ne 0xFE -or
            $Bytes[2] -ne 0xBA -or
            $Bytes[3] -ne 0xBE) {
        return $false
    }

    $major = (($Bytes[6] -shl 8) -bor $Bytes[7])

    if ($major -gt $TargetMajor) {
        $Bytes[6] = [byte](($TargetMajor -shr 8) -band 0xFF)
        $Bytes[7] = [byte]($TargetMajor -band 0xFF)
        return $true
    }

    return $false
}

function Copy-ClassDirectoryForEvoSuite {
    param(
        [Parameter(Mandatory=$true)][string]$SourceDirectory,
        [Parameter(Mandatory=$true)][string]$DestinationDirectory
    )

    if (-not (Test-Path -LiteralPath $SourceDirectory -PathType Container)) {
        return 0
    }

    $sourceFull = (Resolve-Path $SourceDirectory).Path
    [void][System.IO.Directory]::CreateDirectory($DestinationDirectory)

    $patchedCount = 0

    Get-ChildItem -LiteralPath $sourceFull -File -Recurse | ForEach-Object {
        $relative = $_.FullName.Substring($sourceFull.Length).TrimStart('\')
        $dest = Join-Path $DestinationDirectory $relative
        [void][System.IO.Directory]::CreateDirectory((Split-Path $dest -Parent))

        if ($_.Extension -ieq ".class") {
            $bytes = [System.IO.File]::ReadAllBytes($_.FullName)

            if (Patch-ClassBytesForEvoSuite -Bytes $bytes -TargetMajor 55) {
                $patchedCount++
            }

            [System.IO.File]::WriteAllBytes($dest, $bytes)
        }
        else {
            Copy-Item -LiteralPath $_.FullName -Destination $dest -Force
        }
    }

    return $patchedCount
}

function Patch-JarForEvoSuite {
    param(
        [Parameter(Mandatory=$true)][string]$InputJar,
        [Parameter(Mandatory=$true)][string]$OutputJar
    )

    Add-Type -AssemblyName System.IO.Compression
    Add-Type -AssemblyName System.IO.Compression.FileSystem

    if (Test-Path $OutputJar) {
        Remove-Item -Force $OutputJar
    }

    [void][System.IO.Directory]::CreateDirectory((Split-Path $OutputJar -Parent))

    $inputStream = [System.IO.File]::OpenRead($InputJar)
    $inputZip = [System.IO.Compression.ZipArchive]::new(
            $inputStream,
            [System.IO.Compression.ZipArchiveMode]::Read,
            $false
    )

    $outputStream = [System.IO.File]::Create($OutputJar)
    $outputZip = [System.IO.Compression.ZipArchive]::new(
            $outputStream,
            [System.IO.Compression.ZipArchiveMode]::Create,
            $false
    )

    $patchedCount = 0

    try {
        foreach ($entry in $inputZip.Entries) {
            # Le firme non sono più valide se modifichiamo classfile nella copia.
            if ($entry.FullName -match '(?i)^META-INF/.*\.(SF|RSA|DSA|EC)$') {
                continue
            }

            $newEntry = $outputZip.CreateEntry(
                    $entry.FullName,
                    [System.IO.Compression.CompressionLevel]::Optimal
            )

            if ($entry.FullName.EndsWith("/")) {
                continue
            }

            $source = $entry.Open()
            $memory = New-Object System.IO.MemoryStream

            try {
                $source.CopyTo($memory)
                $bytes = $memory.ToArray()
            }
            finally {
                $source.Dispose()
                $memory.Dispose()
            }

            if ($entry.FullName.EndsWith(
                    ".class",
                    [System.StringComparison]::OrdinalIgnoreCase
            )) {
                if (Patch-ClassBytesForEvoSuite -Bytes $bytes -TargetMajor 55) {
                    $patchedCount++
                }
            }

            $dest = $newEntry.Open()

            try {
                $dest.Write($bytes, 0, $bytes.Length)
            }
            finally {
                $dest.Dispose()
            }
        }
    }
    finally {
        $inputZip.Dispose()
        $inputStream.Dispose()
        $outputZip.Dispose()
        $outputStream.Dispose()
    }

    return $patchedCount
}


function Expand-CompatibilityJarToDirectory {
    param(
        [Parameter(Mandatory=$true)][string]$JarFile,
        [Parameter(Mandatory=$true)][string]$DestinationDirectory
    )

    Add-Type -AssemblyName System.IO.Compression
    Add-Type -AssemblyName System.IO.Compression.FileSystem

    [void][System.IO.Directory]::CreateDirectory($DestinationDirectory)

    $stream = [System.IO.File]::OpenRead($JarFile)
    $zip = [System.IO.Compression.ZipArchive]::new(
            $stream,
            [System.IO.Compression.ZipArchiveMode]::Read,
            $false
    )

    $written = 0

    try {
        foreach ($entry in $zip.Entries) {
            if ([string]::IsNullOrWhiteSpace($entry.Name)) {
                continue
            }

            # Per il flat classpath non servono firme / metadata Maven / versioni
            # multi-release. Usiamo le classi base delle dipendenze.
            if ($entry.FullName -match '(?i)^META-INF/(?:versions/|.*\.(SF|RSA|DSA|EC)$)') {
                continue
            }

            $relative = $entry.FullName.Replace('/', '\')
            $dest = Join-Path $DestinationDirectory $relative
            [void][System.IO.Directory]::CreateDirectory((Split-Path $dest -Parent))

            $source = $entry.Open()
            try {
                $target = [System.IO.File]::Create($dest)
                try {
                    $source.CopyTo($target)
                }
                finally {
                    $target.Dispose()
                }
            }
            finally {
                $source.Dispose()
            }

            $written++
        }
    }
    finally {
        $zip.Dispose()
        $stream.Dispose()
    }

    return $written
}

function Overlay-Directory {
    param(
        [Parameter(Mandatory=$true)][string]$SourceDirectory,
        [Parameter(Mandatory=$true)][string]$DestinationDirectory
    )

    if (-not (Test-Path -LiteralPath $SourceDirectory -PathType Container)) {
        throw "Directory sorgente overlay non trovata: $SourceDirectory"
    }

    $sourceFull = (Resolve-Path $SourceDirectory).Path
    [void][System.IO.Directory]::CreateDirectory($DestinationDirectory)

    Get-ChildItem -LiteralPath $sourceFull -File -Recurse | ForEach-Object {
        $relative = $_.FullName.Substring($sourceFull.Length).TrimStart('\')
        $dest = Join-Path $DestinationDirectory $relative
        [void][System.IO.Directory]::CreateDirectory((Split-Path $dest -Parent))
        Copy-Item -LiteralPath $_.FullName -Destination $dest -Force
    }
}

function Assert-OnlyClassMajorChanged {
    param(
        [Parameter(Mandatory=$true)][string]$OriginalClass,
        [Parameter(Mandatory=$true)][string]$CompatibilityClass
    )

    $a = [System.IO.File]::ReadAllBytes($OriginalClass)
    $b = [System.IO.File]::ReadAllBytes($CompatibilityClass)

    if ($a.Length -ne $b.Length) {
        throw "La copia compatibility della CUT ha dimensione diversa dall'originale."
    }

    $different = New-Object System.Collections.Generic.List[int]

    for ($i = 0; $i -lt $a.Length; $i++) {
        if ($a[$i] -ne $b[$i]) {
            [void]$different.Add($i)
        }
    }

    $allowed = @(
        6,
        7
    )

    $unexpected = @($different | Where-Object { $_ -notin $allowed })

    if ($unexpected.Count -gt 0) {
        throw @"
La copia compatibility della CUT differisce dall'originale oltre al campo
classfile major version.

Offset inattesi:
    $($unexpected -join ", ")
"@
    }

    Write-Host "Verifica bytecode CUT: differenze limitate ai byte 6-7 (major version)."
}

function Get-ReactorClassDirectories {
    param(
        [datetime]$ModifiedSince = [datetime]::MinValue
    )

    $compatPrefix = $script:CompatRoot.ToLowerInvariant()
    $result = New-Object System.Collections.Generic.List[string]

    $candidates = @(
    Get-ChildItem `
            -Path $script:RepoRoot `
            -Directory `
            -Recurse `
            -Filter "classes" `
            -ErrorAction SilentlyContinue |
            Where-Object {
                $_.Parent.Name -eq "target" -and
                        -not $_.FullName.ToLowerInvariant().StartsWith($compatPrefix)
            }
    )

    foreach ($candidate in $candidates) {
        if ($ModifiedSince -eq [datetime]::MinValue) {
            [void]$result.Add($candidate.FullName)
            continue
        }

        $freshClass = Get-ChildItem `
            -LiteralPath $candidate.FullName `
            -File `
            -Recurse `
            -Filter "*.class" `
            -ErrorAction SilentlyContinue |
                Where-Object {
                    $_.LastWriteTime -ge $ModifiedSince
                } |
                Select-Object -First 1

        if ($null -ne $freshClass) {
            [void]$result.Add($candidate.FullName)
        }
    }

    return @($result | Select-Object -Unique)
}

function Resolve-ModuleDependencyClasspath {
    if (Test-Path $script:CompatClasspathFile) {
        Remove-Item -Force $script:CompatClasspathFile
    }

    Invoke-Maven `
        -JdkHome $script:Jdk21Home `
        -WorkingDirectory $script:ModuleDir `
        -Arguments @(
        "-DtargetJdk=21",
        "-Dmaven.test.skip=true",
        "-Drat.skip=true",
        "-Dspotless.check.skip=true",
        "-Dcheckstyle.skip=true",
        "org.apache.maven.plugins:maven-dependency-plugin:3.8.1:build-classpath",
        "-DincludeScope=runtime",
        "-Dmdep.outputFile=$($script:CompatClasspathFile)"
    )

    if (-not (Test-Path $script:CompatClasspathFile)) {
        throw "Maven non ha prodotto il classpath: $script:CompatClasspathFile"
    }

    $raw = (Get-Content -Raw $script:CompatClasspathFile).Trim()

    if ([string]::IsNullOrWhiteSpace($raw)) {
        return @()
    }

    return @(
    $raw -split ";" |
            Where-Object {
                -not [string]::IsNullOrWhiteSpace($_)
            }
    )
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

    $runtime = New-Element "dependency"
    [void]$dependencies.AppendChild($runtime)
    [void]$runtime.AppendChild((New-Element "groupId" "org.evosuite"))
    [void]$runtime.AppendChild((New-Element "artifactId" "evosuite-local-runtime"))
    [void]$runtime.AppendChild((New-Element "version" "1.2.0"))
    [void]$runtime.AppendChild((New-Element "scope" "test"))

    $junit4 = New-Element "dependency"
    [void]$dependencies.AppendChild($junit4)
    [void]$junit4.AppendChild((New-Element "groupId" "junit"))
    [void]$junit4.AppendChild((New-Element "artifactId" "junit"))
    [void]$junit4.AppendChild((New-Element "version" "4.13.2"))
    [void]$junit4.AppendChild((New-Element "scope" "test"))

    # Viene aggiunto inizialmente per compatibilità generale;
    # lo script RUN lo rimuove e forza surefire-junit47, come nella pipeline JWS.
    $vintageVersion = $null

    foreach ($propertyName in @(
        "junit.version",
        "junit5.version",
        "junit-jupiter.version"
    )) {
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
    Write-Section "Installazione del JAR EvoSuite nel repository Maven locale"

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
    Write-Host "JDK 11 (generate)  : $script:Jdk11Home"
    Write-Host "JDK 21 (real/run)  : $script:Jdk21Home"
    Write-Host "EvoSuite           : $script:EvoSuiteJar"
    Write-Host "Maven              : $script:MavenExe"
}