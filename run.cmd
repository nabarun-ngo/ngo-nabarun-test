@echo off
setlocal enabledelayedexpansion

:: Default environment
set "ENVIRONMENT=dev"

:: Check if the first argument is "tag" or "scenario"
if /i "%1" == "tag" (
    if "%2" == "" (
        echo [ERROR] No tag provided.
        echo Usage: run tag @tagname [-env environment] [additional maven args]
        exit /b 1
    )
    set "FILTER_TYPE=tags"
    set "FILTER_VALUE=%2"
    shift
    shift
    goto :exec
)

if /i "%1" == "scenario" (
    if "%2" == "" (
        echo [ERROR] No scenario name provided.
        echo Usage: run scenario "Scenario Name" [-env environment] [additional maven args]
        exit /b 1
    )
    set "FILTER_TYPE=name"
    set "FILTER_VALUE=%2"
    shift
    shift
    goto :exec
)

:: If it's not a valid command, show usage
echo [ERROR] Invalid command or missing arguments.
echo Usage:
echo   run tag ^<@tagname^> [-env environment] [additional maven args]
echo   run scenario ^<"Scenario Name"^> [-env environment] [additional maven args]
echo.
echo Examples:
echo   run tag @smoke
echo   run tag "@smoke" -env stage
echo   run scenario "Create Account" -env uat
echo.
echo [TIP] If using PowerShell, remember to quote tags: run tag "@smoke"
exit /b 1

:exec
:: Validate that the filter value wasn't skipped (PowerShell @ issue)
if "!FILTER_VALUE:~0,1!" == "-" (
    echo [ERROR] Invalid !FILTER_TYPE! value: "!FILTER_VALUE!". 
    echo Did you forget the !FILTER_TYPE! name or forget to quote the tag in PowerShell?
    echo Example: run !FILTER_TYPE! "@smoke"
    exit /b 1
)

:: Collect remaining arguments and look for -env
set "EXTRA_ARGS="
:argLoop
if not "%1" == "" (
    if /i "%1" == "-env" (
        set "ENVIRONMENT=%2"
        if not exist "src\test\resources\test_config\test-config-!ENVIRONMENT!.json" (
            echo [WARNING] Environment config file not found: test-config-!ENVIRONMENT!.json
            echo Tests might fail if this environment is not in Doppler.
        )
        shift
        shift
        goto argLoop
    )
    set "EXTRA_ARGS=!EXTRA_ARGS! %1"
    shift
    goto argLoop
)

echo [INFO] Running Maven tests for %FILTER_TYPE%: "!FILTER_VALUE!"
echo [INFO] Environment: !ENVIRONMENT!
if not "!EXTRA_ARGS!" == "" echo [INFO] Additional Args:!EXTRA_ARGS!

mvn test "-Dcucumber.filter.!FILTER_TYPE!=!FILTER_VALUE!" -DENVIRONMENT=!ENVIRONMENT! !EXTRA_ARGS!
goto :eof
