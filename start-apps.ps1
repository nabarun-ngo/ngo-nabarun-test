# Define base folder and script paths
$baseDir = $env:LOCALAPPDATA
$script1 = Join-Path $baseDir "Nabarun-APPS\ngo-nabarun-fe\start-client.ps1"
$script2 = Join-Path $baseDir "Nabarun-APPS\ngo-nabarun-be\start-server.ps1"

# Start each script in its own PowerShell process with the correct working directory
Start-Process powershell -ArgumentList "-NoExit", "-ExecutionPolicy Bypass", "-File `"$script1`"" -WorkingDirectory (Split-Path $script1)
Start-Process powershell -ArgumentList "-NoExit", "-ExecutionPolicy Bypass", "-File `"$script2`"" -WorkingDirectory (Split-Path $script2)

Write-Host "Both server scripts started in separate PowerShell windows."
