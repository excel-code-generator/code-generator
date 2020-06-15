@echo off

REM For more information about the cg.jar, please vist the following page:
REM https://github.com/excel-code-generator/code-generator
set CG_PATH=%APPDATA%\cg\bin\cg.jar

REM update
if "update" == "%1" (
  echo update code generator
  powershell "iex ((New-Object System.Net.WebClient).DownloadString('https://raw.githubusercontent.com/excel-code-generator/code-generator/master/install.ps1'))"

  exit /B %ERRORLEVEL%
)

if not exist %CG_PATH% (
  echo ERROR: The file %CG_PATH% does not exist.
  echo run `cg update` command to reinstall.
  echo=
  echo For more information about the cg.jar, please vist the following page:
  echo https://github.com/excel-code-generator/code-generator
  exit /B 1
)

java -jar %CG_PATH% %*
