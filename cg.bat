@echo off
REM Copyright 2015-2020 yanglb.com
REM 
REM Licensed under the Apache License, Version 2.0 (the "License");
REM you may not use this file except in compliance with the License.
REM You may obtain a copy of the License at
REM 
REM     http://www.apache.org/licenses/LICENSE-2.0
REM 
REM Unless required by applicable law or agreed to in writing, software
REM distributed under the License is distributed on an "AS IS" BASIS,
REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
REM See the License for the specific language governing permissions and
REM limitations under the License.

REM cg.jar path
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
  echo For more information about the cg.jar, please visit the following page:
  echo https://github.com/excel-code-generator/code-generator
  exit /B 1
)

java -jar %CG_PATH% %*
