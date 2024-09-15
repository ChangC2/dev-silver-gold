@echo off

REM Stop and delete the service
sc stop FanucBridgeService
sc delete FanucBridgeService

REM Run setup.exe
cd /d "%~dp0"
start "" /wait "setup.exe"

REM Optional: Check if installation was successful
if %errorlevel% equ 0 (
    echo Installation completed successfully.
) else (
    echo Installation failed.
)

REM Pause console window so it doesn't close immediately
exit