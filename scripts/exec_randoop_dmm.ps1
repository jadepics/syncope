param(
    [Parameter(Position=0, Mandatory=$true)]
    [ValidateSet("generate", "run")]
    [string]$Action,

    [Parameter(Position=1, Mandatory=$true)]
    [int]$Seed,

    [switch]$NoPit,
    [switch]$NoJacoco
)

$ErrorActionPreference = "Stop"
Set-StrictMode -Version Latest

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$Root = if ($env:ROOT) {
    (Resolve-Path $env:ROOT).Path
} else {
    (Resolve-Path (Join-Path $ScriptDir "..")).Path
}

$JavaHome = if ($env:JDK25) {
    $env:JDK25
} elseif ($env:JAVA_HOME) {
    $env:JAVA_HOME
} else {
    "C:\Program Files\Eclipse Adoptium\jdk-25.0.3.9-hotspot"
}

$Java = Join-Path $JavaHome "bin\java.exe"
$Javac = Join-Path $JavaHome "bin\javac.exe"

$RandoopHome = if ($env:RANDOOP_HOME) {
    $env:RANDOOP_HOME
} else {
    "C:\Users\micci\Tools\randoop-4.3.4"
}

$RandoopJar = Join-Path $RandoopHome "randoop-all-4.3.4.jar"

if (-not (Test-Path $Java)) {
    throw "JDK 25 non trovato: $Java"
}
if (-not (Test-Path $Javac)) {
    throw "javac JDK 25 non trovato: $Javac"
}
if (-not (Test-Path $RandoopJar)) {
    throw "Randoop non trovato: $RandoopJar"
}

# ============================================================================
# Target
# ============================================================================

$Module = "core\provisioning-java"
$ModuleRoot = Join-Path $Root $Module
$Pom = Join-Path $ModuleRoot "pom.xml"

$TargetClass = "org.apache.syncope.core.provisioning.java.DefaultMappingManager"
$RequiredClassRegex = "^org\.apache\.syncope\.core\.provisioning\.java\.DefaultMappingManager$"

$TestPackage = "org.apache.syncope.core.provisioning.java.randoop.s$Seed"
$RegressionBase = "DefaultMappingManagerRandoopRegressionS$Seed"
$ErrorBase = "DefaultMappingManagerRandoopErrorS$Seed"
$PitTargetClasses = $TargetClass

if (-not (Test-Path $Pom)) {
    throw "POM non trovato: $Pom"
}

# ============================================================================
# Configurazione Randoop DMM persistente
# ============================================================================

$RandoopConfigRoot = Join-Path $ModuleRoot "randoop\dmm"

$ClassListFile = Join-Path $RandoopConfigRoot "classes.txt"
$LiteralsFile = Join-Path $RandoopConfigRoot "literals.txt"
$OmitMethodsFile = Join-Path $RandoopConfigRoot "omit-methods.txt"
$ScopeFile = Join-Path $RandoopConfigRoot "scope.txt"
$SupportSource = Join-Path $ModuleRoot "src\test\java\org\apache\syncope\core\provisioning\java\randoopsupport\DefaultMappingManagerRandoopFactory.java"

foreach ($RequiredFile in @(
    $ClassListFile,
    $LiteralsFile,
    $OmitMethodsFile,
    $ScopeFile,
    $SupportSource
)) {
    if (-not (Test-Path $RequiredFile)) {
        throw "File Randoop DMM mancante: $RequiredFile"
    }
}

# ============================================================================
# Output test / build temporaneo
# ============================================================================

$PackagePath = $TestPackage.Replace(".", "\")
$TestSourceRoot = Join-Path $ModuleRoot "src\test\java"
$CaseRoot = Join-Path $TestSourceRoot $PackagePath

$TmpRoot = Join-Path $Root ".tmp\randoop\dmm\seed-$Seed"
$RawOutput = Join-Path $TmpRoot "raw"
$BuildRoot = Join-Path $TmpRoot "build"
$ClasspathFile = Join-Path $BuildRoot "classpath.txt"
$SupportClassesDir = Join-Path $BuildRoot "support-classes"

New-Item -ItemType Directory -Force -Path $BuildRoot | Out-Null

$Utf8NoBom = New-Object System.Text.UTF8Encoding($false)

function Write-TextNoBom {
    param(
        [Parameter(Mandatory=$true)]
        [string]$Path,
        [Parameter(Mandatory=$true)]
        [AllowEmptyString()]
        [string]$Content
    )

    [System.IO.File]::WriteAllText($Path, $Content, $Utf8NoBom)
}

# ============================================================================
# Classpath
# ============================================================================

function Build-ProjectClasspath {

    Write-Host "=== Maven compile + dependency classpath (test scope) ==="

    if (Test-Path $ClasspathFile) {
        Remove-Item $ClasspathFile -Force
    }

    # compile (non test-compile): i test Randoop di seed precedenti non
    # devono interferire con una nuova generazione.
    #
    # includeScope=test serve per Mockito, usato dalla factory.
    #
    # L'output Maven viene catturato localmente per evitare che PowerShell
    # lo incorpori nel valore restituito dalla funzione.
    $MavenOutput = & mvn.cmd `
        -f $Pom `
        "-DskipTests" `
        "-Djacoco.skip=true" `
        compile `
        dependency:build-classpath `
        "-Dmdep.includeScope=test" `
        "-Dmdep.outputFile=$ClasspathFile" 2>&1

    $MavenExitCode = $LASTEXITCODE

    foreach ($Line in $MavenOutput) {
        Write-Host $Line
    }

    if ($MavenExitCode -ne 0) {
        throw "Maven classpath generation failed"
    }

    if (-not (Test-Path $ClasspathFile)) {
        throw "Classpath file non generato: $ClasspathFile"
    }

    $DependencyCp = (Get-Content $ClasspathFile -Raw).Trim()
    $ClassesDir = Join-Path $ModuleRoot "target\classes"

    if (-not (Test-Path $ClassesDir)) {
        throw "Directory target\classes non trovata: $ClassesDir"
    }

    $TargetClassFile = Join-Path `
        $ClassesDir `
        (($TargetClass.Replace(".", "\")) + ".class")

    if (-not (Test-Path $TargetClassFile)) {
        throw "La classe target compilata non esiste: $TargetClassFile"
    }

    Write-Host "Target class compilata: $TargetClassFile"

    return [PSCustomObject]@{
        ClassesDir   = $ClassesDir
        DependencyCp = $DependencyCp
        ProjectCp    = "$ClassesDir;$DependencyCp"
    }
}

# ============================================================================
# Factory di supporto
# ============================================================================

function Compile-RandoopSupport {
    param(
        [Parameter(Mandatory=$true)]
        $BuildInfo
    )

    Write-Host "=== Compile DefaultMappingManagerRandoopFactory ==="

    if (Test-Path $SupportClassesDir) {
        Remove-Item $SupportClassesDir -Recurse -Force
    }

    New-Item -ItemType Directory -Force -Path $SupportClassesDir | Out-Null

    & $Javac `
        "-cp" $BuildInfo.ProjectCp `
        "-d" $SupportClassesDir `
        $SupportSource

    if ($LASTEXITCODE -ne 0) {
        throw "Compilation of DefaultMappingManagerRandoopFactory failed"
    }

    $FactoryClassFile = Join-Path `
        $SupportClassesDir `
        "org\apache\syncope\core\provisioning\java\randoopsupport\DefaultMappingManagerRandoopFactory.class"

    if (-not (Test-Path $FactoryClassFile)) {
        throw "Factory compilata non trovata: $FactoryClassFile"
    }

    Write-Host "Factory compilata: $FactoryClassFile"
}

# ============================================================================
# Normalizzazione package test generati
# ============================================================================

function Normalize-GeneratedTests {

    if (Test-Path $CaseRoot) {
        Remove-Item $CaseRoot -Recurse -Force
    }

    New-Item -ItemType Directory -Force -Path $CaseRoot | Out-Null

    $GeneratedFiles = Get-ChildItem `
        $RawOutput `
        -Filter "*.java" `
        -File `
        -ErrorAction SilentlyContinue

    foreach ($File in $GeneratedFiles) {

        $Text = Get-Content $File.FullName -Raw

        if ($Text -match "(?m)^\s*package\s+[^;]+;") {
            $Text = [regex]::Replace(
                $Text,
                "(?m)^\s*package\s+[^;]+;",
                "package $TestPackage;",
                1
            )
        } else {
            $Text = "package $TestPackage;`r`n`r`n" + $Text
        }

        $Destination = Join-Path $CaseRoot $File.Name
        Write-TextNoBom -Path $Destination -Content $Text
        Write-Host "GENERATED $Destination"
    }
}

# ============================================================================
# Generate
# ============================================================================

function Generate-RandoopTests {

    Write-Host "============================================================"
    Write-Host "RANDOOP - DefaultMappingManager"
    Write-Host "ACTION : GENERATE"
    Write-Host "SEED   : $Seed"
    Write-Host "TARGET : $TargetClass"
    Write-Host "CONFIG : $RandoopConfigRoot"
    Write-Host "============================================================"

    $BuildInfo = Build-ProjectClasspath
    Compile-RandoopSupport -BuildInfo $BuildInfo

    if (Test-Path $RawOutput) {
        Remove-Item $RawOutput -Recurse -Force
    }

    New-Item -ItemType Directory -Force -Path $RawOutput | Out-Null

    # Randoop vede:
    # - proprio jar
    # - factory compilata
    # - classi produzione del modulo
    # - dipendenze Maven, incluso Mockito
    $RandoopCp = "$RandoopJar;$SupportClassesDir;$($BuildInfo.ProjectCp)"

    if ($RandoopCp -match "\[INFO\]" -or $RandoopCp -match "\r|\n") {
        throw "Classpath contaminato da output Maven. Interruzione preventiva."
    }

    Write-Host "=== Randoop classpath sanity check ==="
    Write-Host "Project classes: $($BuildInfo.ClassesDir)"
    Write-Host "Support classes: $SupportClassesDir"
    Write-Host "Classpath length: $($RandoopCp.Length) characters"

    $Arguments = @(
        "-Xmx3g",
        "-classpath", $RandoopCp,

        "randoop.main.Main",
        "gentests",

        "--classlist=$ClassListFile",

        # Per DMM NON usiamo covered-class javaagent: causava ClassFormatError
        # su MappingManager$PreparedAttrs. Questo filtro richiede invece che
        # ogni test emesso usi direttamente DefaultMappingManager.
        "--require-classname-in-test=$RequiredClassRegex",

        "--omit-methods-file=$OmitMethodsFile",
        "--only-test-public-members=true",

        "--literals-file=$LiteralsFile",
        "--literals-level=ALL",

        "--junit-output-dir=$RawOutput",
        "--regression-test-basename=$RegressionBase",
        "--error-test-basename=$ErrorBase",

        "--generated-limit=10000",
        "--output-limit=200",
        "--maxsize=25",
        "--randomseed=$Seed",
        "--flaky-test-behavior=DISCARD"
    )

    & $Java @Arguments

    if ($LASTEXITCODE -ne 0) {
        throw "Randoop generation failed"
    }

    Normalize-GeneratedTests

    Write-Host "=== Generated files ==="

    $Generated = Get-ChildItem $CaseRoot -Filter "*.java" -File | Sort-Object Name

    if (-not $Generated) {
        Write-Warning "Randoop ha terminato senza produrre file Java DMM."
    } else {
        $Generated | ForEach-Object { Write-Host $_.FullName }
    }
}

# ============================================================================
# Run JaCoCo / PIT
# ============================================================================

function Run-RandoopMetrics {

    Write-Host "============================================================"
    Write-Host "RANDOOP - DefaultMappingManager"
    Write-Host "ACTION : RUN"
    Write-Host "SEED   : $Seed"
    Write-Host "============================================================"

    if (-not (Test-Path $CaseRoot)) {
        throw "Esegui prima generate per questo seed"
    }

    # Eseguiamo soltanto le classi leaf, non anche la suite aggregatrice,
    # per evitare la doppia esecuzione già osservata nel verifier.
    $EscapedRegressionBase = [regex]::Escape($RegressionBase)

    $RegressionFiles = Get-ChildItem `
        $CaseRoot `
        -Filter "$RegressionBase*.java" `
        -File |
        Where-Object {
            $_.BaseName -match "^${EscapedRegressionBase}[0-9]+$"
        }

    if (-not $RegressionFiles) {
        throw "Nessuna classe regression leaf trovata in $CaseRoot"
    }

    $TestPattern = (
        $RegressionFiles |
        Sort-Object Name |
        ForEach-Object { $_.BaseName }
    ) -join ","

    $PitTestPattern = (
        $RegressionFiles |
        Sort-Object Name |
        ForEach-Object { "$TestPackage.$($_.BaseName)" }
    ) -join ","

    Write-Host "Randoop regression leaf classes:"
    $RegressionFiles |
        Sort-Object Name |
        ForEach-Object { Write-Host "  $($_.BaseName)" }

    Write-Host "Surefire test pattern: $TestPattern"
    Write-Host "PIT test pattern:      $PitTestPattern"

    & mvn.cmd `
        -f $Pom `
        -Prandoop-only `
        "-Djacoco.skip=true" `
        clean `
        test-compile

    if ($LASTEXITCODE -ne 0) {
        throw "Compilation of Randoop tests failed"
    }

    if (-not $NoJacoco) {

        & mvn.cmd `
            -f $Pom `
            -Prandoop-only `
            "-Dtest=$TestPattern" `
            "-Dsurefire.failIfNoSpecifiedTests=true" `
            test `
            jacoco:report

        if ($LASTEXITCODE -ne 0) {
            throw "JaCoCo/Surefire failed"
        }

        Write-Host "JaCoCo report: $(Join-Path $ModuleRoot 'target\site\jacoco\index.html')"
    }

    if (-not $NoPit) {

        & mvn.cmd `
            -f $Pom `
            -Prandoop-only `
            "-Djacoco.skip=true" `
            "-DtargetClasses=$PitTargetClasses" `
            "-DtargetTests=$PitTestPattern" `
            "-Dthreads=1" `
            test-compile `
            org.pitest:pitest-maven:1.25.9:mutationCoverage

        if ($LASTEXITCODE -ne 0) {
            throw "PIT failed"
        }

        Write-Host "PIT reports: $(Join-Path $ModuleRoot 'target\pit-reports')"
    }

    Write-Host "Surefire reports: $(Join-Path $ModuleRoot 'target\surefire-reports')"
}

switch ($Action) {
    "generate" { Generate-RandoopTests }
    "run"      { Run-RandoopMetrics }
}
