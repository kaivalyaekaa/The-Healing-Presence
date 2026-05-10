@echo off
setlocal
set "JAVA_HOME=C:\Program Files\Java\jdk-21"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo JAVA_HOME=[%JAVA_HOME%]
echo Test java.exe exists:
if exist "%JAVA_HOME%\bin\java.exe" (echo   YES) else (echo   NO)
echo Running mvnw...
call .\mvnw.cmd %*
endlocal
