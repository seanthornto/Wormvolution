@echo off
set PROJECT_NAME=Wormvolution
set MAIN_CLASS=objects/GUI.java
set CLASSPATH=dependencies/swingx-all-1.6.5-1.jar
set BUILD_DIR=class

echo.
echo --- Running the application ---
java -cp %BUILD_DIR%;%CLASSPATH% %MAIN_CLASS%

echo.
echo --- Finished ---
pause