@echo off
setlocal enabledelayedexpansion

echo ============================================================
echo [START] Initializing Dependent Services for NGO Nabarun
echo ============================================================

:: Define paths relative to this script's directory
set "BASE_DIR=%~dp0.."
set "BE_DIR=%BASE_DIR%\ngo-nabarun-be-nestjs"
set "FE_DIR=%BASE_DIR%\ngo-nabarun-fe"
set "PUBLIC_DIR=%BASE_DIR%\ngo-nabarun-public"

echo [1/3] Starting NestJS Backend Service...
if exist "!BE_DIR!" (
    start "Nabarun Backend (NestJS)" cmd /c "cd /d !BE_DIR! && echo Starting Backend... && npm run start"
) else (
    echo [ERROR] Backend directory not found: !BE_DIR!
)

echo [2/3] Starting Angular Frontend Service...
if exist "!FE_DIR!" (
    start "Nabarun Frontend (Angular)" cmd /c "cd /d !FE_DIR! && echo Starting Frontend... && npm run start:dev"
) else (
    echo [ERROR] Frontend directory not found: !FE_DIR!
)

echo [3/3] Starting NextJS Public App...
if exist "!PUBLIC_DIR!" (
    start "Nabarun Public (NextJS)" cmd /c "cd /d !PUBLIC_DIR! && echo Starting Public App... && npm run start:dev"
) else (
    echo [ERROR] Public App directory not found: !PUBLIC_DIR!
)

echo.
echo ============================================================
echo [SUCCESS] All dependent services are launching!
echo [INFO] Please wait a few moments for them to initialize.
echo [INFO] You can now run your tests using: run.cmd tag @smoke
echo ============================================================
echo.

:: Check if user wants to run tests immediately with tags
if not "%1" == "" (
    echo [INFO] Detected arguments, passing to run.cmd...
    call run.cmd %*
) else (
    echo Press any key to exit this setup window...
    pause > nul
)
