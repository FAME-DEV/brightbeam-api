@echo off
SETLOCAL

mvn package

REM Default (hardcoded) file paths
SET "DEFAULT_FILE1_PATH=C:\Users\user\default_data1.json"
SET "DEFAULT_FILE2_PATH=C:\Users\user\default_data2.json"

REM Check if arguments are provided; otherwise, use default paths
IF "%~1"=="" (
    SET "FILE1_PATH=%DEFAULT_FILE1_PATH%"
    SET "FILE2_PATH=%DEFAULT_FILE2_PATH%"
    echo Using default file paths:
) ELSE (
    SET "FILE1_PATH=%~1"
    SET "FILE2_PATH=%~2"
    echo Using custom file paths:
)

REM Display the selected paths
echo FILE1_PATH: %FILE1_PATH%
echo FILE2_PATH: %FILE2_PATH%

REM Export environment variables
SET FILE1_PATH=%FILE1_PATH%
SET FILE2_PATH=%FILE2_PATH%

REM Run the Java application
java -jar target/brightbeam-api-0.0.1-SNAPSHOT.jar

ENDLOCAL
