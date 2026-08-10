param(
    [int]$BudgetSeconds = 90,
    [switch]$KeepCompatibilityFiles
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

. (Join-Path $PSScriptRoot "evosuite_common.ps1")

Write-Section "EvoSuite GENERATE - AccessTokenJWSVerifier - Windows"
Show-Configuration

if (-not (Test-Path $script:TargetSource)) {
    throw "Sorgente della CUT non trovato: $script:TargetSource"
}

if (-not (Test-Path $script:EvoSuiteJar)) {
    throw "JAR EvoSuite non trovato: $script:EvoSuiteJar"
}

Write-Section "1. Preparazione area compatibility separata"

if (Test-Path $script:CompatRoot) {
    Remove-Item -Recurse -Force $script:CompatRoot
}
New-Item -ItemType Directory -Force -Path $script:CompatClasses | Out-Null
New-Item -ItemType Directory -Force -Path (Split-Path $script:CompatSource -Parent) | Out-Null
New-Item -ItemType Directory -Force -Path $script:CompatRawLib | Out-Null
New-Item -ItemType Directory -Force -Path $script:CompatLib | Out-Null

Copy-Item -Force $script:TargetSource $script:CompatSource

$srcHash = (Get-FileHash -Algorithm SHA256 $script:TargetSource).Hash
$copyHash = (Get-FileHash -Algorithm SHA256 $script:CompatSource).Hash

if ($srcHash -ne $copyHash) {
    throw "La copia compatibility della CUT non coincide byte-per-byte con il sorgente reale."
}

Write-Host "CUT copiata senza modifiche."
Write-Host "SHA-256: $srcHash"

Write-Section "2. Lettura delle versioni dipendenze dal POM Syncope"

$nimbusVersion = Get-MavenProperty -Name "nimbus-jose-jwt.version"
$commonsLangVersion = Get-MavenProperty -Name "commons-lang3.version"

Write-Host "Nimbus JOSE+JWT : $nimbusVersion"
Write-Host "Commons Lang3   : $commonsLangVersion"

Write-Section "3. Risoluzione dipendenze minime e transitive"

$compatPomContent = @"
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>local.evosuite</groupId>
  <artifactId>syncope-jws-compat</artifactId>
  <version>1.0</version>

  <dependencies>
    <dependency>
      <groupId>com.nimbusds</groupId>
      <artifactId>nimbus-jose-jwt</artifactId>
      <version>$nimbusVersion</version>
    </dependency>

    <dependency>
      <groupId>org.apache.commons</groupId>
      <artifactId>commons-lang3</artifactId>
      <version>$commonsLangVersion</version>
    </dependency>
  </dependencies>
</project>
"@

Set-Content -Path $script:CompatPom -Value $compatPomContent -Encoding UTF8

Invoke-Maven -JdkHome $script:Jdk21Home -WorkingDirectory $script:CompatRoot -Arguments @(
    "-f", $script:CompatPom,
    "org.apache.maven.plugins:maven-dependency-plugin:3.8.1:copy-dependencies",
    "-DincludeScope=runtime",
    "-DoutputDirectory=$($script:CompatRawLib)"
)

$rawJars = @(Get-ChildItem -Path $script:CompatRawLib -File -Filter "*.jar")
if ($rawJars.Count -eq 0) {
    throw "Maven non ha prodotto alcun JAR nel classpath compatibility."
}

Write-Section "4. Controllo bytecode delle dipendenze per Java 11"

$compatibleJars = New-Object System.Collections.Generic.List[string]

foreach ($jar in $rawJars) {
    Write-Host "Controllo: $($jar.Name)"
    $prepared = Test-AndPrepareJarForJava11 -InputJar $jar.FullName -OutputDirectory $script:CompatLib
    $compatibleJars.Add($prepared)
}

$projectCpParts = New-Object System.Collections.Generic.List[string]
$projectCpParts.Add($script:CompatClasses)
foreach ($jar in $compatibleJars) {
    $projectCpParts.Add($jar)
}
$projectCp = $projectCpParts -join ";"

Write-Section "5. Compilazione della SOLA CUT con JDK 11 --release 11"

$javac = Join-Path $script:Jdk11Home "bin\javac.exe"

& $javac `
    --release 11 `
    -encoding UTF-8 `
    -cp ($compatibleJars -join ";") `
    -d $script:CompatClasses `
    $script:CompatSource

if ($LASTEXITCODE -ne 0) {
    throw "javac JDK11 non è riuscito a compilare AccessTokenJWSVerifier."
}

$compatClassFile = Join-Path $script:CompatClasses "$($script:TargetClassRelative).class"
$major = Get-ClassMajorVersion -ClassFile $compatClassFile

Write-Host "Classfile compatibility major version: $major"

if ($major -gt 55) {
    throw "La CUT compatibility ha major $major; EvoSuite/JDK11 richiede <= 55."
}

Write-Section "6. Generazione EvoSuite con JDK 11"

if (Test-Path $script:EvoGeneratedRoot) {
    Remove-Item -Recurse -Force $script:EvoGeneratedRoot
}
New-Item -ItemType Directory -Force -Path $script:EvoGeneratedTests | Out-Null
New-Item -ItemType Directory -Force -Path $script:EvoReportDir | Out-Null

$java11 = Join-Path $script:Jdk11Home "bin\java.exe"

$evoArgs = @(
    "-Xmx2500m",
    "-jar", $script:EvoSuiteJar,
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
    "-Dclient_on_thread=true"
)

Write-Host "Budget EvoSuite: $BudgetSeconds secondi"
& $java11 @evoArgs

if ($LASTEXITCODE -ne 0) {
    throw "EvoSuite terminato con exit code $LASTEXITCODE."
}

$generatedPackageDir = Join-Path $script:EvoGeneratedTests "org\apache\syncope\core\spring\security\jws"
$rawTest = Join-Path $generatedPackageDir "AccessTokenJWSVerifier_ESTest.java"
$rawScaffolding = Join-Path $generatedPackageDir "AccessTokenJWSVerifier_ESTest_scaffolding.java"

if (-not (Test-Path $rawTest)) {
    throw "EvoSuite non ha prodotto il test atteso: $rawTest"
}
if (-not (Test-Path $rawScaffolding)) {
    throw "EvoSuite non ha prodotto lo scaffolding atteso: $rawScaffolding"
}

Write-Section "7. Copia dei test generati nella directory EvoSuite separata"

# Su Windows creiamo esplicitamente tutta la gerarchia con System.IO.
# Questo evita problemi del provider PowerShell quando la directory src\test\evosuite
# e l'intero package non esistono ancora.
[void][System.IO.Directory]::CreateDirectory($script:GeneratedPackageDir)

if (-not (Test-Path -LiteralPath $script:GeneratedPackageDir -PathType Container)) {
    throw "Impossibile creare la directory di destinazione: $script:GeneratedPackageDir"
}

Write-Host "Directory destinazione:"
Write-Host "  $script:GeneratedPackageDir"

# Copiamo verso la directory, non verso un path file completo:
# PowerShell mantiene automaticamente i nomi originali dei due file.
Copy-Item -LiteralPath $rawTest -Destination $script:GeneratedPackageDir -Force
Copy-Item -LiteralPath $rawScaffolding -Destination $script:GeneratedPackageDir -Force

if (-not (Test-Path -LiteralPath $script:GeneratedTestFile)) {
    throw "Il test generato non risulta presente dopo la copia: $script:GeneratedTestFile"
}
if (-not (Test-Path -LiteralPath $script:GeneratedScaffoldingFile)) {
    throw "Lo scaffolding non risulta presente dopo la copia: $script:GeneratedScaffoldingFile"
}

Write-Host "Test:"
Write-Host "  $script:GeneratedTestFile"
Write-Host "Scaffolding:"
Write-Host "  $script:GeneratedScaffoldingFile"
Write-Host ""
Write-Host "IMPORTANTE: nessun test manuale è stato modificato."
Write-Host "La CUT originale non è stata modificata."
Write-Host ""
Write-Host "Prossimo comando:"
Write-Host "  .\scripts\evosuite\evosuite_run_jws.ps1 test"

if (-not $KeepCompatibilityFiles) {
    Write-Host ""
    Write-Host "Nota: l'area compatibility viene mantenuta perché è utile per audit/debug."
    Write-Host "Puoi rimuoverla con:"
    Write-Host "  .\scripts\evosuite\evosuite_run_jws.ps1 clean-generated"
}
