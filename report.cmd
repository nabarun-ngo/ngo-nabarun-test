@echo off
setlocal

set "ALLURE_RESULTS=target\allure-results"
set "ALLURE_REPORT=target\allure-report"
set "BACKUP_HISTORY=target\.allure-history"

echo ==================================================
echo      Preparing Allure Report with History
echo ==================================================

:: 1. Restore history from backup to results directory before generating the report
if exist "%BACKUP_HISTORY%" (
    echo Restoring history from backup...
    if not exist "%ALLURE_RESULTS%\history" mkdir "%ALLURE_RESULTS%\history"
    xcopy /E /I /Y "%BACKUP_HISTORY%\*" "%ALLURE_RESULTS%\history\" >nul
) else (
    echo No previous history backup found. First time generating report?
)

:: 2. Generate the report
echo.
echo Generating Allure Report using Maven...
call mvn allure:report -q

:: 3. Backup the newly generated history for next time (survives mvn clean)
if exist "%ALLURE_REPORT%\history" (
    echo.
    echo Backing up new history for next execution...
    if not exist "%BACKUP_HISTORY%" mkdir "%BACKUP_HISTORY%"
    xcopy /E /I /Y "%ALLURE_REPORT%\history\*" "%BACKUP_HISTORY%\" >nul
    echo History backup completed successfully.
) else (
    echo.
    echo Warning: Could not find history in the generated report to backup.
)

echo.
echo ==================================================
echo Report generation complete! Opening report...
echo ==================================================
call mvn allure:serve -q
endlocal
