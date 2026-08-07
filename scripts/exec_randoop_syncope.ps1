param(
    [Parameter(Position=0, Mandatory=$true)]
    [ValidateSet("verifier", "dmm")]
    [string]$Target,

    [Parameter(Position=1, Mandatory=$true)]
    [ValidateSet("generate", "run")]
    [string]$Action,

    [Parameter(Position=2, Mandatory=$true)]
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

$RandoopHome = if ($env:RANDOOP_HOME) {
    $env:RANDOOP_HOME
} else {
    "C:\Users\micci\Tools\randoop-4.3.4"
}

$RandoopJar = Join-Path $RandoopHome "randoop-all-4.3.4.jar"
$CoveredClassJar = Join-Path $RandoopHome "covered-class-4.3.4.jar"

if (-not (Test-Path $Java)) {
    throw "JDK 25 non trovato: $Java"
}

if (-not (Test-Path $RandoopJar)) {
    throw "Randoop non trovato: $RandoopJar"
}

if (-not (Test-Path $CoveredClassJar)) {
    throw "covered-class agent non trovato: $CoveredClassJar"
}

# ---------------------------------------------------------------------------
# Scrittura UTF-8 senza BOM
# Evita che Randoop legga il BOM come parte del primo nome di classe.
# ---------------------------------------------------------------------------

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

function Write-LinesNoBom {
    param(
        [Parameter(Mandatory=$true)]
        [string]$Path,

        [Parameter(Mandatory=$true)]
        [string[]]$Lines
    )

    $Content = [string]::Join([Environment]::NewLine, $Lines)
    Write-TextNoBom -Path $Path -Content $Content
}

switch ($Target) {
    "verifier" {
        $Module = "core\spring"
        $TargetClass = "org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier"
        $TestPackage = "org.apache.syncope.core.spring.security.jws.randoop.s$Seed"
        $RegressionBase = "AccessTokenVerifierRandoopRegressionS$Seed"
        $ErrorBase = "AccessTokenVerifierRandoopErrorS$Seed"
        $PitTargetClasses = "org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier"

        $SupportClasses = @(
            "org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier",
            "com.nimbusds.jose.JWSAlgorithm",
            "com.nimbusds.jose.JWSHeader",
            "com.nimbusds.jose.util.Base64URL"
        )
    }

    "dmm" {
        $Module = "core\provisioning-java"
        $TargetClass = "org.apache.syncope.core.provisioning.java.DefaultMappingManager"
        $TestPackage = "org.apache.syncope.core.provisioning.java.randoop.s$Seed"
        $RegressionBase = "DefaultMappingManagerRandoopRegressionS$Seed"
        $ErrorBase = "DefaultMappingManagerRandoopErrorS$Seed"
        $PitTargetClasses = "org.apache.syncope.core.provisioning.java.DefaultMappingManager"

        $SupportClasses = @(
            "org.apache.syncope.core.provisioning.java.DefaultMappingManager"
        )
    }
}

$ModuleRoot = Join-Path $Root $Module
$Pom = Join-Path $ModuleRoot "pom.xml"

if (-not (Test-Path $Pom)) {
    throw "POM non trovato: $Pom"
}

$PackagePath = $TestPackage.Replace(".", "\")
$TestSourceRoot = Join-Path $ModuleRoot "src\test\java"
$CaseRoot = Join-Path $TestSourceRoot $PackagePath

$TmpRoot = Join-Path $Root ".tmp\randoop\$Target\seed-$Seed"
$RawOutput = Join-Path $TmpRoot "raw"
$ConfigRoot = Join-Path $TmpRoot "config"

$ClassListFile = Join-Path $ConfigRoot "classes.txt"
$RequiredCoveredFile = Join-Path $ConfigRoot "required-covered.txt"
$OmitMethodsFile = Join-Path $ConfigRoot "omit-methods.txt"
$LiteralsFile = Join-Path $ConfigRoot "literals.txt"
$ClasspathFile = Join-Path $ConfigRoot "classpath.txt"

New-Item -ItemType Directory -Force -Path $ConfigRoot | Out-Null

function Write-RandoopSupportFiles {

    # IMPORTANTE:
    # tutti i file letti direttamente da Randoop vengono scritti
    # esplicitamente in UTF-8 SENZA BOM.
    Write-LinesNoBom -Path $ClassListFile -Lines $SupportClasses
    Write-TextNoBom -Path $RequiredCoveredFile -Content $TargetClass

    $OmitMethods = @'
.*\.removeLast\(\)
.*\.removeFirst\(\)
.*\.remove\(.*\)
.*\.removeAll\(.*\)
.*\.retainAll\(.*\)
.*\.clear\(\)
'@

    Write-TextNoBom -Path $OmitMethodsFile -Content $OmitMethods

    if ($Target -eq "verifier") {

        $Literals = @'
START CLASSLITERALS
CLASSNAME
org.apache.syncope.core.spring.security.jws.AccessTokenJWSVerifier
LITERALS
String:"0123456789abcdef0123456789abcdef"
String:"fedcba9876543210fedcba9876543210"
String:"0123456789abcdef0123456789abcdef0123456789abcdef"
String:"0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef"
String:"header.payload"
String:""
END CLASSLITERALS
'@

        Write-TextNoBom -Path $LiteralsFile -Content $Literals
    }
    else {

        $Literals = @'
START CLASSLITERALS
CLASSNAME
org.apache.syncope.core.provisioning.java.DefaultMappingManager
LITERALS
String:""
String:"name"
String:"username"
String:"key"
String:"value"
String:"schema"
END CLASSLITERALS
'@

        Write-TextNoBom -Path $LiteralsFile -Content $Literals
    }
}

function Build-ProjectClasspath {

    Write-Host "=== Maven test-compile + dependency classpath ==="

    if (Test-Path $ClasspathFile) {
        Remove-Item $ClasspathFile -Force
    }

    & mvn.cmd `
        -f $Pom `
        "-DskipTests" `
        "-Djacoco.skip=true" `
        test-compile `
        dependency:build-classpath `
        "-Dmdep.outputFile=$ClasspathFile"

    if ($LASTEXITCODE -ne 0) {
        throw "Maven classpath generation failed"
    }

    if (-not (Test-Path $ClasspathFile)) {
        throw "Classpath file non generato: $ClasspathFile"
    }

    $DependencyCp = (Get-Content $ClasspathFile -Raw).Trim()
    $ClassesDir = Join-Path $ModuleRoot "target\classes"

    return "$ClassesDir;$DependencyCp"
}

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
        }
        else {

            $Text = "package $TestPackage;`r`n`r`n" + $Text
        }

        $Destination = Join-Path $CaseRoot $File.Name

        # Anche i .java normalizzati vengono scritti senza BOM,
        # evitando problemi di compilazione con javac.
        Write-TextNoBom -Path $Destination -Content $Text

        Write-Host "GENERATED $Destination"
    }
}

function Generate-RandoopTests {

    Write-Host "=== GENERATE $Target seed $Seed ==="
    Write-Host "Target class: $TargetClass"

    Write-RandoopSupportFiles

    $ProjectCp = Build-ProjectClasspath

    if (Test-Path $RawOutput) {
        Remove-Item $RawOutput -Recurse -Force
    }

    New-Item -ItemType Directory -Force -Path $RawOutput | Out-Null

    $RandoopCp = "$RandoopJar;$ProjectCp"

    $Arguments = @(
        "-Xmx3g",
        "-javaagent:$CoveredClassJar",
        "-classpath", $RandoopCp,
        "randoop.main.Main",
        "gentests",
        "--classlist=$ClassListFile",
        "--require-covered-classes=$RequiredCoveredFile",
        "--omit-methods-file=$OmitMethodsFile",
        "--only-test-public-members=true",
        "--literals-file=$LiteralsFile",
        "--literals-level=ALL",
        "--junit-output-dir=$RawOutput",
        "--regression-test-basename=$RegressionBase",
        "--error-test-basename=$ErrorBase",
        "--generated-limit=10000",
        "--output-limit=200",
        "--maxsize=20",
        "--randomseed=$Seed",
        "--flaky-test-behavior=DISCARD"
    )

    & $Java @Arguments

    if ($LASTEXITCODE -ne 0) {
        throw "Randoop generation failed"
    }

    Normalize-GeneratedTests

    Write-Host "=== Generated files ==="

    Get-ChildItem $CaseRoot -Filter "*.java" -File |
        Sort-Object Name |
        ForEach-Object {
            Write-Host $_.FullName
        }
}

function Run-RandoopMetrics {

    Write-Host "=== RUN $Target seed $Seed ==="

    if (-not (Test-Path $CaseRoot)) {
        throw "Esegui prima generate per questo seed"
    }

    $RegressionFiles = Get-ChildItem `
        $CaseRoot `
        -Filter "$RegressionBase*.java" `
        -File

    if (-not $RegressionFiles) {
        throw "Nessun regression test trovato in $CaseRoot"
    }

    $TestPattern = "$RegressionBase*"
    $PitTestPattern = "$TestPackage.$RegressionBase*"

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
}

switch ($Action) {
    "generate" {
        Generate-RandoopTests
    }

    "run" {
        Run-RandoopMetrics
    }
}