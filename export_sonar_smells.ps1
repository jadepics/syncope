$projectKey = "jadepics_syncope"
$organization = "ispw"

$headers = @{
    Authorization = "Bearer $env:SONAR_TOKEN"
}

$allIssues = @()
$page = 1
$pageSize = 500

do {
    $uri = "https://sonarcloud.io/api/issues/search" +
            "?componentKeys=$projectKey" +
            "&organization=$organization" +
            "&types=CODE_SMELL" +
            "&resolved=false" +
            "&ps=$pageSize" +
            "&p=$page"

    Write-Host "Scarico pagina $page..."

    $response = Invoke-RestMethod `
        -Uri $uri `
        -Headers $headers `
        -Method Get

    $allIssues += $response.issues

    $total = $response.total
    $page++
}
while ($allIssues.Count -lt $total)

Write-Host "Code smell totali trovati: $($allIssues.Count)"

function Export-ClassReport {
    param (
        [string]$ClassName,
        [string]$OutputFile
    )

    $issues = $allIssues | Where-Object {
        $_.component -like "*$ClassName.java"
    }

    $report = $issues | Select-Object `
        @{Name="Class"; Expression={$ClassName}},
    @{Name="Line"; Expression={$_.line}},
    @{Name="Rule"; Expression={$_.rule}},
    @{Name="Severity"; Expression={$_.severity}},
    @{Name="Status"; Expression={$_.status}},
    @{Name="Message"; Expression={$_.message}},
    @{Name="Effort"; Expression={$_.effort}},
    @{Name="Component"; Expression={$_.component}},
    @{Name="IssueKey"; Expression={$_.key}}

    if ($report) {
        $report | Export-Csv `
            -Path $OutputFile `
            -NoTypeInformation `
            -Encoding UTF8

        Write-Host "$ClassName -> $($issues.Count) code smell -> $OutputFile"
    }
    else {
        # Crea comunque il CSV con intestazione
        [PSCustomObject]@{
        Class     = $ClassName
        Line      = ""
        Rule      = ""
        Severity  = ""
        Status    = ""
        Message   = "Nessun code smell rilevato"
        Effort    = ""
        Component = ""
        IssueKey  = ""
        } | Export-Csv `
            -Path $OutputFile `
            -NoTypeInformation `
            -Encoding UTF8

        Write-Host "$ClassName -> 0 code smell -> $OutputFile"
    }
}

Export-ClassReport `
    -ClassName "AccessTokenJWSVerifier" `
    -OutputFile "sonar_AccessTokenJWSVerifier.csv"

Export-ClassReport `
    -ClassName "DefaultMappingManager" `
    -OutputFile "sonar_DefaultMappingManager.csv"

Write-Host ""
Write-Host "Report completati."