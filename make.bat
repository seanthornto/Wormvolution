@echo off
set PROJECT_NAME=Wormvolution
set MAIN_CLASS=objects/GUI.java
set CLASSPATH=dependencies/swingx-all-1.6.5-1.jar
set BUILD_DIR=class
set JAR_NAME=%PROJECT_NAME%.jar
set MANIFEST_FILE=manifest.txt

echo --- Cleaning up old build directory ---
if exist %BUILD_DIR% (
    rmdir /s /q %BUILD_DIR%
)
mkdir %BUILD_DIR%

echo --- Compiling Java files ---
javac -d %BUILD_DIR% -cp %CLASSPATH% objects/*.java objects/ui_components/*.java

if %errorlevel% neq 0 (
    echo.
    echo --- Compilation failed! ---
    goto :end
)

echo.
echo --- Compilation successful. Creating manifest for JAR. ---
echo Main-Class: GUI > %MANIFEST_FILE%
echo Class-Path: %CLASSPATH% >> %MANIFEST_FILE%

echo --- Creating executable JAR ---
jar cfm %JAR_NAME% %MANIFEST_FILE% -C %BUILD_DIR% .

echo --- Deleting Manifest ---
del %MANIFEST_FILE%

:end
echo.
echo --- Finished ---
pause
