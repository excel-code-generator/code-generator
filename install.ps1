# Copyright 2015-2020 yanglb.com
# 
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#     http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"
$PSDefaultParameterValues['*:ErrorAction']='Stop'

# Always download the latest version
# For more information about the cg.jar, please visit the following page:
# https://github.com/excel-code-generator/code-generator

$repo="excel-code-generator/code-generator"
$repoPath="https://github.com/$repo"
$rawPath="https://raw.githubusercontent.com/$repo"

# latest version
Write-Output "Get the latest version"
$releasesUri = "https://api.github.com/repos/$repo/releases/latest"
$tag = (Invoke-WebRequest $releasesUri | ConvertFrom-Json)[0].tag_name
Write-Output "Latest version: $tag"

# download
Write-Output "Downloading..."
$tempPath = Get-Random -Maximum 500
$tempPath = "$env:TEMP\cg-$tempPath"
if ( Test-Path $tempPath ) {
    rm $tempPath\*
} else {
    mkdir -p $tempPath
}
function download_file($name, $path) {
    Write-Output "Download file $path "
    Invoke-WebRequest $path -Out "$tempPath\$name"
}
download_file cg "$rawPath/$tag/cg"
download_file cg.bat "$rawPath/$tag/cg.bat"
download_file cg.jar "$repoPath/releases/download/$tag/cg.jar"
Write-Output "Download success."

# move file
$cgPath = "$env:APPDATA\cg\bin"
if (Test-Path $cgPath) {
    rm $cgPath\*
} else {
    mkdir -p $cgPath
}

mv "$tempPath\*" $cgPath
rm $tempPath

# set environment
Write-Output "setting environment."
$alreadyAdded = $False
$path = [environment]::GetEnvironmentvariable("PATH", "User")
$path.Split(";") | ForEach-Object -Process {if ($_ -eq $cgPath) { $alreadyAdded = $True }}

if (! $alreadyAdded) {
    $path = $path + ";" + $cgPath
    $path = [environment]::SetEnvironmentvariable("PATH", $path, "User")
}

Write-Output "Install success!"
