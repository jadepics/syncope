Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

# ============================================================
# CONFIGURAZIONE
# ============================================================

$PmdExe = "C:\pmd-dist-7.26.0-bin\pmd-bin-7.26.0\bin\pmd.bat"

# Ruleset identico a quello usato dal SyncopePmdCsvGenerator
$Ruleset = Join-Path $PSScriptRoot `
    "pmd-codesmells-ruleset.xml"

$AccessTokenSource = Join-Path $PSScriptRoot `
    "core/spring/src/main/java/org/apache/syncope/core/spring/security/jws/AccessTokenJWSVerifier.java"

$DefaultMappingSource = Join-Path $PSScriptRoot `
    "core/provisioning-java/src/main/java/org/apache/syncope/core/provisioning/java/DefaultMappingManager.java"

$ReportDir = Join-Path $PSScriptRoot "pmd-reports"

$AccessTokenReport = Join-Path `
    $ReportDir `
    "pmd_AccessTokenJWSVerifier.csv"

$DefaultMappingReport = Join-Path `
    $ReportDir `
    "pmd_DefaultMappingManager.csv"


# ============================================================
# CONTROLLI
# ============================================================

Write-Host ""
Write-Host "============================================="
Write-Host " PMD - Apache Syncope smell analysis"
Write-Host "============================================="
Write-Host ""

Write-Host "PMD:"
Write-Host "  $PmdExe"
Write-Host ""

Write-Host "Ruleset:"
Write-Host "  $Ruleset"
Write-Host ""

if (-not (Test-Path $PmdExe)) {
    throw "PMD non trovato: $PmdExe"
}

if (-not (Test-Path $Ruleset)) {
    throw "Ruleset PMD non trovato: $Ruleset"
}

if (-not (Test-Path $AccessTokenSource)) {
    throw "AccessTokenJWSVerifier.java non trovato: $AccessTokenSource"
}

if (-not (Test-Path $DefaultMappingSource)) {
    throw "DefaultMappingManager.java non trovato: $DefaultMappingSource"
}

New-Item `
    -ItemType Directory `
    -Path $ReportDir `
    -Force |
        Out-Null


# ============================================================
# FUNZIONE ANALISI
# ============================================================

function Invoke-PmdAnalysis {

    param (
        [Parameter(Mandatory = $true)]
        [string]$ClassName,

        [Parameter(Mandatory = $true)]
        [string]$SourceFile,

        [Parameter(Mandatory = $true)]
        [string]$ReportFile
    )

    Write-Host ""
    Write-Host "---------------------------------------------"
    Write-Host "Analisi: $ClassName"
    Write-Host "---------------------------------------------"

    Write-Host "Source:"
    Write-Host "  $SourceFile"

    Write-Host ""

    if (Test-Path $ReportFile) {
        Remove-Item $ReportFile -Force
    }

    & $PmdExe check `
        --dir $SourceFile `
        --rulesets $Ruleset `
        --format csv `
        --report-file $ReportFile `
        --use-version java-25 `
        --encoding UTF-8 `
        --no-cache `
        --no-progress `
        --no-fail-on-violation `
        --no-fail-on-error

    $exitCode = $LASTEXITCODE

    if ($exitCode -ne 0) {
        throw "PMD è terminato con exit code $exitCode durante l'analisi di $ClassName"
    }

    if (-not (Test-Path $ReportFile)) {
        throw "Report PMD non generato: $ReportFile"
    }

    $violations = @(Import-Csv $ReportFile)

    Write-Host ""
    Write-Host "$ClassName"
    Write-Host "Violazioni PMD trovate: $($violations.Count)"
    Write-Host "Report: $ReportFile"

    if ($violations.Count -gt 0) {

        Write-Host ""

        $violations |
                Select-Object `
                Line,
        Priority,
        Rule,
        'Rule set',
        Description |
                Format-Table -AutoSize
    }
    else {
        Write-Host "Nessuna violazione PMD rilevata."
    }
}


# ============================================================
# ACCESS TOKEN JWS VERIFIER
# ============================================================

Invoke-PmdAnalysis `
    -ClassName "AccessTokenJWSVerifier" `
    -SourceFile $AccessTokenSource `
    -ReportFile $AccessTokenReport


# ============================================================
# DEFAULT MAPPING MANAGER
# ============================================================

Invoke-PmdAnalysis `
    -ClassName "DefaultMappingManager" `
    -SourceFile $DefaultMappingSource `
    -ReportFile $DefaultMappingReport


# ============================================================
# RISULTATO
# ============================================================

Write-Host ""
Write-Host "============================================="
Write-Host " ANALISI PMD COMPLETATA"
Write-Host "============================================="
Write-Host ""

Write-Host "Configurazione utilizzata:"
Write-Host "  PMD 7.26.0"
Write-Host "  Java language level: 25"
Write-Host ""
Write-Host "Categorie:"
Write-Host "  - Best Practices"
Write-Host "  - Design"
Write-Host "  - Error Prone"
Write-Host "  - Multithreading"
Write-Host "  - Performance"

Write-Host ""
Write-Host "Report generati:"
Write-Host ""
Write-Host "  $AccessTokenReport"
Write-Host "  $DefaultMappingReport"
Write-Host ""