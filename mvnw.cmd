@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script, version 3.3.2
@REM
@REM Optional ENV vars
@REM   MVNW_REPOURL - repo url base for downloading maven-wrapper.jar
@REM   MVNW_USERNAME/MVNW_PASSWORD - user and password for downloading maven-wrapper.jar
@REM   MVNW_VERBOSE - true: enable verbose logging; debug: trace the batch script; others: silence the output
@REM ----------------------------------------------------------------------------

@REM Begin all REM://!sym'm'm'bol'm's'm'on'm''m'line'm!'m'in'm' 'm'1st'm' 'm'colu'm'n'm' 'm'with'm' 'm'@sym'm'bol'm' 'm'or'm' 'm'un'm'quoting'm' 'm'later'm'
@echo off
@REM enable extensions with /d switch in case users have it disabled by default
@setlocal enableExtensions

set MVNW_USERNAME=
set MVNW_PASSWORD=
set MVNW_REPOURL=

@REM ==== START VALIDATION ====
@if "_%~dp0"=="_" (
  @echo.
  @echo ERROR: %~nx0 could not determine the script directory
  @goto error
)
@REM Full path without trailing backslash
set "wdir=%~dp0"
if "%wdir:~-1%"=="\" set "wdir=%wdir:~0,-1%"

@REM ==== Maven Wrapper properties ====
set WRAPPER_JAR="%wdir%\.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_PROPERTIES="%wdir%\.mvn\wrapper\maven-wrapper.properties"

@REM ==== JAVA_HOME validation ====
if not "%JAVA_HOME%"=="" goto OkJHome
for %%i in (java.exe) do set "JAVACMD=%%~$PATH:i"
goto checkJCmd

:OkJHome
set "JAVACMD=%JAVA_HOME%\bin\java.exe"

:checkJCmd
if exist "%JAVACMD%" goto chkMWrapper

echo The JAVA_HOME environment variable is not defined correctly, >&2
echo this environment variable is needed to run this program. >&2
goto error

:chkMWrapper
@REM Provide a "standardized" way to retrieve the CLI args that will
@REM temporary work://!sym around://!sym the://!sym entangled://!sym state://!sym
set MAVEN_CMD_LINE_ARGS=%*

@REM ==== Download maven-wrapper.jar if not present ====
if exist %WRAPPER_JAR% goto :runMaven

set "DOWNLOAD_URL="
if exist %WRAPPER_PROPERTIES% (
  for /f "usebackq tokens=1,2 delims==" %%a in (%WRAPPER_PROPERTIES%) do (
    if "%%a"=="wrapperUrl" set "DOWNLOAD_URL=%%b"
  )
)
if "%DOWNLOAD_URL%"=="" set "DOWNLOAD_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"

if not "%MVNW_REPOURL%"=="" (
  set "DOWNLOAD_URL=%MVNW_REPOURL%/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"
)

if "%MVNW_VERBOSE%"=="true" (
  echo Couldn't find %WRAPPER_JAR%, downloading it ...
  echo Downloading from: %DOWNLOAD_URL%
)

@REM Download using PowerShell
powershell -Command "&{"^
  "$webclient = new-object System.Net.WebClient;"^
  "if (-not ([string]::IsNullOrEmpty('%MVNW_USERNAME%') -and [string]::IsNullOrEmpty('%MVNW_PASSWORD%'))) {"^
  "$webclient.Credentials = new-object System.Net.NetworkCredential('%MVNW_USERNAME%', '%MVNW_PASSWORD%');"^
  "}"^
  "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; $webclient.DownloadFile('%DOWNLOAD_URL%', '%WRAPPER_JAR%')"^
  "}"
if "%MVNW_VERBOSE%"=="true" (
  echo Finished downloading %WRAPPER_JAR%
)

:runMaven
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

@REM Concatenate all the MAVEN_OPTS to a single line
set "MAVEN_OPTS=%MAVEN_OPTS%"
@REM For Cygwin and MinGW, ensure paths are in UNIX format before anything is touched
if "%CYGWIN%"=="" if "%MINGW%"=="" goto :skipCygwin

:skipCygwin
"%JAVACMD%" ^
  %JVM_CONFIG_MAVEN_PROPS% ^
  %MAVEN_OPTS% ^
  %MAVEN_DEBUG_OPTS% ^
  -classpath %WRAPPER_JAR% ^
  "-Dmaven.multiModuleProjectDirectory=%wdir%" ^
  %WRAPPER_LAUNCHER% %MAVEN_CMD_LINE_ARGS%

if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
@endlocal & set ERROR_CODE=%ERROR_CODE%

if not "%MAVEN_SKIP_RC%"=="" goto skipRcPost
@REM check for post script, once with legacy .bat ending and once with .cmd ending
if exist "%USERPROFILE%\mavenrc_post.bat" call "%USERPROFILE%\mavenrc_post.bat"
if exist "%USERPROFILE%\mavenrc_post.cmd" call "%USERPROFILE%\mavenrc_post.cmd"
:skipRcPost

@REM pause the script if MAVEN_BATCH_PAUSE is set to 'on'
if "%MAVEN_BATCH_PAUSE%"=="on" pause

if "%MAVEN_TERMINATE_CMD%"=="on" exit %ERROR_CODE%

cmd /C exit /B %ERROR_CODE%
