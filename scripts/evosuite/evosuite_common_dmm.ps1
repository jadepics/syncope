param(
    [int]$BudgetSeconds = 180,
    [switch]$KeepCompatibilityFiles
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

. (Join-Path $PSScriptRoot "evosuite_common_dmm.ps1")

Write-Section "EvoSuite GENERATE - DefaultMappingManager - JDK11 generator / JDK21 build"
Show-Configuration

if (-not (Test-Path $script:TargetSource)) {
    throw "Sorgente della CUT non trovato: $script:TargetSource"
}

if (-not (Test-Path $script:EvoSuiteJar)) {
    throw "JAR EvoSuite non trovato: $script:EvoSuiteJar"
}

Write-Section "1. Build reale del modulo e delle dipendenze con JDK 21"

# Usato per riconoscere solo le target\classes ricostruite da QUESTA run.
$buildStart = (Get-Date).AddSeconds(-2)

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

$realMajor = Get-ClassMajorVersion -ClassFile $script:RealClassFile
Write-Host "CUT reale classfile major version: $realMajor"

if ($realMajor -ne 65) {
    throw @"
La CUT reale non risulta compilata per Java 21.
Major trovata: $realMajor
Major attesa: 65
"@
}

Write-Section "2. Creazione mirror compatibility per EvoSuite 1.2.0"

if (Test-Path $script:CompatRoot) {
    Remove-Item -Recurse -Force $script:CompatRoot
}

[void][System.IO.Directory]::CreateDirectory($script:CompatClasses)
[void][System.IO.Directory]::CreateDirectory($script:CompatLib)

# DefaultMappingManager usa API/costrutti moderni; non riscriviamo il sorgente.
# Partiamo invece dal bytecode REALE Java 21 e creiamo una copia separata in cui
# viene cambiato soltanto il campo classfile major version, così l'ASM interno
# di EvoSuite 1.2.0 può leggerla. Le classi reali sotto target\classes restano
# completamente intatte.
$reactorClassDirs = @(Get-ReactorClassDirectories -ModifiedSince $buildStart)

if ($reactorClassDirs.Count -eq 0) {
    throw "Nessuna directory target\classes trovata dopo la build reactor."
}

$totalPatchedProjectClasses = 0

foreach ($classDir in $reactorClassDirs) {
    Write-Host "Mirror reactor: $classDir"
    $patched = Copy-ClassDirectoryForEvoSuite `
        -SourceDirectory $classDir `
        -DestinationDirectory $script:CompatClasses

    $totalPatchedProjectClasses += $patched
}

$compatTargetClass = Join-Path `
    $script:CompatClasses `
    "$($script:TargetClassRelative).class"

if (-not (Test-Path $compatTargetClass)) {
    throw "CUT compatibility non trovata: $compatTargetClass"
}

$compatMajor = Get-ClassMajorVersion -ClassFile $compatTargetClass
Write-Host "CUT compatibility classfile major version: $compatMajor"

if ($compatMajor -ne 55) {
    throw "La CUT compatibility deve dichiarare major 55; trovata major $compatMajor."
}

Assert-OnlyClassMajorChanged `
    -OriginalClass $script:RealClassFile `
    -CompatibilityClass $compatTargetClass

Write-Host "Classi reactor con major adattata: $totalPatchedProjectClasses"

Write-Section "3. Classpath Maven e copie compatibility delle dipendenze"

$dependencyEntries = @(Resolve-ModuleDependencyClasspath)

$patchedJars = New-Object System.Collections.Generic.List[string]
$totalPatchedJarClasses = 0
$jarIndex = 0

foreach ($entry in $dependencyEntries) {
    if (-not (Test-Path -LiteralPath $entry -PathType Leaf)) {
        continue
    }

    if ([System.IO.Path]::GetExtension($entry) -ine ".jar") {
        continue
    }

    $jarIndex++
    $safeName = "{0:D4}-{1}" -f $jarIndex, [System.IO.Path]::GetFileName($entry)
    $outputJar = Join-Path $script:CompatLib $safeName

    Write-Host "Compat JAR: $([System.IO.Path]::GetFileName($entry))"

    $patchedCount = Patch-JarForEvoSuite `
        -InputJar $entry `
        -OutputJar $outputJar

    $totalPatchedJarClasses += $patchedCount
    [void]$patchedJars.Add($outputJar)
}

Write-Host "JAR compatibility creati: $($patchedJars.Count)"
Write-Host "Classi nei JAR con major adattata: $totalPatchedJarClasses"

Write-Section "4. Costruzione classpath compatibility piatto"

if (Test-Path $script:CompatFlatCp) {
    Remove-Item -Recurse -Force $script:CompatFlatCp
}
[void][System.IO.Directory]::CreateDirectory($script:CompatFlatCp)

# Prima espandiamo tutte le dipendenze compatibility.
$totalFlatEntries = 0
foreach ($jar in $patchedJars) {
    $written = Expand-CompatibilityJarToDirectory `
        -JarFile $jar `
        -DestinationDirectory $script:CompatFlatCp

    $totalFlatEntries += $written
}

# Poi sovrapponiamo per ultime le classi appena compilate dal reactor.
# In caso di duplicati, la build corrente di Syncope ha quindi precedenza
# rispetto a eventuali SNAPSHOT presenti nel repository Maven locale.
Overlay-Directory `
    -SourceDirectory $script:CompatClasses `
    -DestinationDirectory $script:CompatFlatCp

$flatTargetClass = Join-Path `
    $script:CompatFlatCp `
    "$($script:TargetClassRelative).class"

$membershipClass = Join-Path `
    $script:CompatFlatCp `
    "org\apache\syncope\common\lib\to\MembershipTO.class"

if (-not (Test-Path -LiteralPath $flatTargetClass)) {
    throw "CUT assente dal flat classpath: $flatTargetClass"
}

if (-not (Test-Path -LiteralPath $membershipClass)) {
    throw @"
MembershipTO è ancora assente dal classpath compatibility piatto:
    $membershipClass

La generazione viene fermata PRIMA di EvoSuite perché il classpath non è
completo.
"@
}

$flatTargetMajor = Get-ClassMajorVersion -ClassFile $flatTargetClass
$membershipMajor = Get-ClassMajorVersion -ClassFile $membershipClass

Write-Host "Flat classpath:"
Write-Host "  $script:CompatFlatCp"
Write-Host "CUT presente:          SI (major $flatTargetMajor)"
Write-Host "MembershipTO presente: SI (major $membershipMajor)"
Write-Host "Entry estratte dalle JAR compatibility: $totalFlatEntries"

if ($flatTargetMajor -ne 55) {
    throw "CUT nel flat classpath non è major 55: $flatTargetMajor"
}

if ($membershipMajor -gt 55) {
    throw "MembershipTO nel flat classpath è ancora troppo recente: major $membershipMajor"
}

# Passiamo a EvoSuite UNA SOLA root di classpath. Questo evita problemi
# di risoluzione con decine di JAR e rende visibili nello stesso namespace
# tutte le classi necessarie all'instrumentazione ASM.
$projectCp = $script:CompatFlatCp

Write-Section "5. Generazione EvoSuite JDK 11 con flatCP anche nel context classloader"

if (Test-Path $script:EvoGeneratedRoot) {
    Remove-Item -Recurse -Force $script:EvoGeneratedRoot
}

[void][System.IO.Directory]::CreateDirectory($script:EvoGeneratedTests)
[void][System.IO.Directory]::CreateDirectory($script:EvoReportDir)

$java11 = Join-Path $script:Jdk11Home "bin\java.exe"

if (-not (Test-Path -LiteralPath $java11)) {
    throw "java.exe JDK 11 non trovato: $java11"
}

$detectedGenerateMajor = Get-JavaMajorFromHome -JdkHome $script:Jdk11Home
if ($detectedGenerateMajor -ne 11) {
    throw @"
ERRORE DI SICUREZZA:
la fase GENERATE deve usare JDK 11.

JDK configurata:
    $script:Jdk11Home

Major rilevata:
    $detectedGenerateMajor
"@
}

Write-Host "JAVA GENERATE ATTESA: JDK 11"
Write-Host "Path:"
Write-Host "  $java11"
Write-Host "Versione effettiva:"
& $java11 -version

if ($LASTEXITCODE -ne 0) {
    throw "Impossibile eseguire la JDK 11: $java11"
}

Write-Host ""
Write-Host "Budget EvoSuite: $BudgetSeconds secondi"
Write-Host "CUT compatibility originale: $compatTargetClass"
Write-Host "CUT compatibility major: $(Get-ClassMajorVersion -ClassFile $compatTargetClass)"
Write-Host "ProjectCP effettivo EvoSuite:"
Write-Host "  $projectCp"
Write-Host ""
Write-Host "NOTA: nessuna JDK 21 viene usata dal processo EvoSuite di generazione."

# Ultimo preflight: queste classi DEVONO essere risolvibili dal flatcp che
# verrà aggiunto anche al runtime classpath del processo EvoSuite.
$preflightMembership = Join-Path `
    $projectCp `
    "org\apache\syncope\common\lib\to\MembershipTO.class"

$preflightTarget = Join-Path `
    $projectCp `
    "$($script:TargetClassRelative).class"

foreach ($requiredClass in @($preflightTarget, $preflightMembership)) {
    if (-not (Test-Path -LiteralPath $requiredClass -PathType Leaf)) {
        throw "Preflight runtime classpath fallito. Classe assente: $requiredClass"
    }
}

Write-Host "Preflight runtime CP:"
Write-Host "  DefaultMappingManager.class -> OK"
Write-Host "  MembershipTO.class          -> OK"

# IMPORTANTISSIMO:
# ComputeClassWriter di EvoSuite risolve i tipi tramite il context classloader
# del thread. Con "java -jar evosuite.jar" il projectCP NON fa parte del
# classloader di EvoSuite; per questo MembershipTO non veniva trovato anche
# se era presente fisicamente nel flatcp.
#
# Avviamo quindi EvoSuite tramite -cp e inseriamo nello stesso runtime CP:
#   1) evosuite-1.2.0.jar
#   2) flatcp compatibility
#
# -projectCP viene comunque mantenuto perché EvoSuite distingue il proprio
# classpath dal target project classpath.
$evoRuntimeCp = "$($script:EvoSuiteJar);$projectCp"

Write-Host "Runtime classpath EvoSuite:"
Write-Host "  EvoSuite JAR + flatcp"
Write-Host "Context CP contiene flatcp: SI"

$evoArgs = @(
    "-Xmx3500m",
    "-cp", $evoRuntimeCp,
    "org.evosuite.EvoSuite",
    "-generateMOSuite",
    "-class", $script:TargetClass,
    "-projectCP", $projectCp,
    "-Dtest_dir=$($script:EvoGeneratedTests)",
    "-Dreport_dir=$($script:EvoReportDir)",
    "-Dcriterion=LINE:BRANCH",
    "-Dsearch_budget=$BudgetSeconds",
    "-Dstopping_condition=MaxTime",
    "-Dassertions=true",
    "-Dminimize=true",
    "-Dsandbox=false",
    "-Dclient_on_thread=true",
    "-Dinstrument_context=false",
    "-Dmock_if_no_generator=true"
)

# La fase RUN Java 21 usa JDK_JAVA_OPTIONS per SecurityManager/add-opens.
# Durante GENERATE vogliamo invece una JVM 11 pulita e riproducibile.
$oldJdkJavaOptions = $env:JDK_JAVA_OPTIONS

try {
    Remove-Item Env:JDK_JAVA_OPTIONS -ErrorAction SilentlyContinue

    & $java11 @evoArgs

    if ($LASTEXITCODE -ne 0) {
        throw "EvoSuite terminato con exit code $LASTEXITCODE."
    }
}
finally {
    if ($null -eq $oldJdkJavaOptions) {
        Remove-Item Env:JDK_JAVA_OPTIONS -ErrorAction SilentlyContinue
    }
    else {
        $env:JDK_JAVA_OPTIONS = $oldJdkJavaOptions
    }
}

$generatedPackageDir = Join-Path `
    $script:EvoGeneratedTests `
    "org\apache\syncope\core\provisioning\java"

$rawTest = Join-Path `
    $generatedPackageDir `
    "DefaultMappingManager_ESTest.java"

$rawScaffolding = Join-Path `
    $generatedPackageDir `
    "DefaultMappingManager_ESTest_scaffolding.java"

if (-not (Test-Path $rawTest)) {
    throw "EvoSuite non ha prodotto il test atteso: $rawTest"
}

if (-not (Test-Path $rawScaffolding)) {
    throw "EvoSuite non ha prodotto lo scaffolding atteso: $rawScaffolding"
}

Write-Section "6. Copia dei test generati in src\test\evosuite"

[void][System.IO.Directory]::CreateDirectory($script:GeneratedPackageDir)

Copy-Item `
    -LiteralPath $rawTest `
    -Destination $script:GeneratedPackageDir `
    -Force

Copy-Item `
    -LiteralPath $rawScaffolding `
    -Destination $script:GeneratedPackageDir `
    -Force

if (-not (Test-Path -LiteralPath $script:GeneratedTestFile)) {
    throw "Test generato non presente dopo la copia: $script:GeneratedTestFile"
}

if (-not (Test-Path -LiteralPath $script:GeneratedScaffoldingFile)) {
    throw "Scaffolding non presente dopo la copia: $script:GeneratedScaffoldingFile"
}

Write-Host "Test:"
Write-Host "  $script:GeneratedTestFile"
Write-Host "Scaffolding:"
Write-Host "  $script:GeneratedScaffoldingFile"
Write-Host ""
Write-Host "CUT reale NON modificata."
Write-Host "Test manuali NON modificati."
Write-Host ""
Write-Host "Prossimo comando:"
Write-Host "  .\scripts\evosuite\evosuite_run_dmm.ps1 test"

if (-not $KeepCompatibilityFiles) {
    Write-Host ""
    Write-Host "L'area compatibility viene mantenuta per audit/debug."
    Write-Host "Per rimuoverla successivamente:"
    Write-Host "  .\scripts\evosuite\evosuite_run_dmm.ps1 clean-generated"
}