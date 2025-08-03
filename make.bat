@echo off
set PROJECT_NAME=Wormvolution
set MAIN_CLASS=java/GUI.java
set CLASSPATH=dependencies/swingx-all-1.6.5-1.jar
set BUILD_DIR=class

echo --- Cleaning up old build directory ---
if exist %BUILD_DIR% (
    rmdir /s /q %BUILD_DIR%
)
mkdir %BUILD_DIR%

echo --- Compiling Java files ---
javac -d %BUILD_DIR% -cp %CLASSPATH% java/*.java

if %errorlevel% neq 0 (
    echo.
    echo --- Compilation failed! ---
    goto :end
)

echo.
echo --- Compilation successful! ---
:end
echo.
echo --- Finished ---
pause
