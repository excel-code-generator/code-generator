Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"
$PSDefaultParameterValues['*:ErrorAction']='Stop'

# Always download the latest version
# For more information about the cg.jar, please vist the following page:
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
function download_file($name, $path) {
    Write-Output "Download file $path "
    if ( Test-Path "$env:TEMP\$name" ) {
        del "$env:TEMP\$name"
    }

    Invoke-WebRequest $path -Out "$env:TEMP\$name"
}
download_file cg "$rawPath/$tag/cg"
download_file cg.bat "$rawPath/$tag/cg.bat"
download_file cg.jar "$repoPath/releases/download/$tag/cg.jar"
Write-Output "Download success."

# move file
$cgPath = "$env:APPDATA\cg\bin"
$existDir = Test-Path $cgPath
if ($existDir -eq $False) {
    mkdir -p $cgPath
}

mv "$env:TEMP\cg" $cgPath
mv "$env:TEMP\cg.bat" $cgPath
mv "$env:TEMP\cg.jar" $cgPath

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
cg -v
