param (
    [string]$Service = "all",
    [switch]$Doc
)

Write-Host "--- MediLabo Launcher V4 ---" -ForegroundColor Magenta

# 1. Generation de la Javadoc
if ($Doc) {
    Write-Host "[STEP 1] Generation de la Documentation..." -ForegroundColor Cyan
    $folders = Get-ChildItem -Directory
    foreach ($f in $folders) {
        if (Test-Path "$($f.FullName)\mvnw.cmd") {
            Write-Host "-> Traitement de : $($f.Name)" -ForegroundColor Gray
            Push-Location $f.FullName
            # On lance javadoc:javadoc de maniere isolee
            .\mvnw.cmd javadoc:javadoc -DfailOnError=false
            Pop-Location
        }
    }
}

# 2. Compilation selective
Write-Host "`n[STEP 2] Compilation des JARs..." -ForegroundColor Cyan
if ($Service -eq "all") {
    $folders = Get-ChildItem -Directory
    foreach ($f in $folders) {
        if (Test-Path "$($f.FullName)\mvnw.cmd") {
            Write-Host "-> Build : $($f.Name)" -ForegroundColor Gray
            Push-Location $f.FullName
            .\mvnw.cmd clean package -DskipTests
            Pop-Location
        }
    }
} else {
    if (Test-Path ".\$Service") {
        Push-Location ".\$Service"
        .\mvnw.cmd clean package -DskipTests
        Pop-Location
    }
}

# 3. Lancement Docker
Write-Host "`n[STEP 3] Lancement Docker..." -ForegroundColor Yellow
docker-compose up --build