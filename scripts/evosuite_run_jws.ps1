param(
    [Parameter(Position=0)]
    [ValidateSet("test", "jacoco", "pit", "all", "clean-generated", "info")]
    [string]$Action = "test",

    [switch]$SkipRebuild
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

. (Join-Path $PSScriptRoot "evosuite_common.ps1")

function Assert-GeneratedTestsExist {
    if (-not (Test-Path $script:GeneratedTestFile)) {
        throw @"
Test EvoSuite non trovato:
    $script:GeneratedTestFile

Prima esegui:
    .\scripts\evosuite\evosuite_generate_jws.ps1
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

    Write-Section "Build REALE Syncope con JDK 25"

    Invoke-Maven -JdkHome $script:Jdk25Home -WorkingDirectory $script:RepoRoot -Arguments @(
        "-pl", "core/spring",
        "-am",
        "-DskipTests",
        "-Dspotless.check.skip=true",
        "-Dcheckstyle.skip=true",
        "clean",
        "install"
    )

    if (-not (Test-Path $script:RealClassFile)) {
        throw "Dopo la build JDK25 la CUT reale non è stata trovata: $script:RealClassFile"
    }

    $major = Get-ClassMajorVersion -ClassFile $script:RealClassFile
    Write-Host "CUT reale classfile major version: $major"

    if ($major -le 55) {
        throw @"
La CUT reale risulta ancora Java 11 o precedente (major=$major).
La fase RUN deve invece usare la build normale Syncope/JDK25.
Controlla il POM e la JDK usata da Maven.
"@
    }
}

function Prepare-RunEnvironment {
    Assert-GeneratedTestsExist
    Install-EvoSuiteRuntimeIntoLocalMaven
    Build-RealSyncope

    if (Test-Path $script:TemporaryRunPom) {
        Remove-Item -Force $script:TemporaryRunPom
    }

    $null = New-TemporaryEvoSuiteRunPom
    Write-Host "POM temporaneo EvoSuite:"
    Write-Host "  $script:TemporaryRunPom"
}

function Cleanup-TemporaryRunPom {
    if (Test-Path $script:TemporaryRunPom) {
        Remove-Item -Force $script:TemporaryRunPom
    }
}

function Invoke-EvoSuiteMavenTest {
    Write-Section "Esecuzione dei test EvoSuite sulla CUT REALE / JDK 25"

    Invoke-Maven -JdkHome $script:Jdk25Home -WorkingDirectory $script:ModuleDir -Arguments @(
        "-f", $script:TemporaryRunPom,
        "-Pevosuite-jws-temp",
        "-Dspotless.check.skip=true",
        "-Dcheckstyle.skip=true",
        "-Dtest=$($script:GeneratedTestFqn)",
        "-Dsurefire.failIfNoSpecifiedTests=true",
        "test"
    )
}

function Invoke-EvoSuiteJaCoCo {
    Write-Section "JaCoCo - sola suite EvoSuite JWS"

    Invoke-Maven -JdkHome $script:Jdk25Home -WorkingDirectory $script:ModuleDir -Arguments @(
        "-f", $script:TemporaryRunPom,
        "-Pevosuite-jws-temp",
        "-Dspotless.check.skip=true",
        "-Dcheckstyle.skip=true",
        "-Dtest=$($script:GeneratedTestFqn)",
        "-Dsurefire.failIfNoSpecifiedTests=true",
        "jacoco:prepare-agent",
        "test",
        "jacoco:report"
    )

    Write-Host ""
    Write-Host "Report JaCoCo atteso:"
    Write-Host "  $($script:ModuleDir)\target\site\jacoco\index.html"
}

function Invoke-EvoSuitePit {
    Write-Section "PIT - AccessTokenJWSVerifier / sola suite EvoSuite"

    Invoke-Maven -JdkHome $script:Jdk25Home -WorkingDirectory $script:ModuleDir -Arguments @(
        "-f", $script:TemporaryRunPom,
        "-Pevosuite-jws-temp",
        "-Dspotless.check.skip=true",
        "-Dcheckstyle.skip=true",
        "-Djacoco.skip=true",
        "-DtargetClasses=$($script:TargetClass)",
        "-DtargetTests=$($script:GeneratedTestFqn)",
        "-Dthreads=1",
        "test-compile",
        "org.pitest:pitest-maven:1.25.9:mutationCoverage"
    )

    Write-Host ""
    Write-Host "Report PIT sotto:"
    Write-Host "  $($script:ModuleDir)\target\pit-reports"
}

function Clean-Generated {
    Write-Section "Pulizia output EvoSuite JWS"

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
    Write-Host "Pulizia completata. src\test\java non è stato toccato."
}

if ($Action -eq "info") {
    Write-Section "Configurazione EvoSuite JWS"
    Show-Configuration
    Write-Host "Generated test root: $script:GeneratedTestRoot"
    Write-Host "Compatibility root : $script:CompatRoot"
    exit 0
}

if ($Action -eq "clean-generated") {
    Clean-Generated
    exit 0
}

Write-Section "EvoSuite RUN - AccessTokenJWSVerifier - Windows"
Show-Configuration

Prepare-RunEnvironment

try {
    switch ($Action) {
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
    }
}
finally {
    Cleanup-TemporaryRunPom
}

Write-Section "Operazione completata"
Write-Host "Azione: $Action"
Write-Host "CUT:    $script:TargetClass"
Write-Host "JDK di esecuzione reale: 25"
