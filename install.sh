#!/usr/bin/env sh
set -e

# Always download the latest version
# For more information about the cg.jar, please vist the following page:
# https://github.com/excel-code-generator/code-generator

repo="excel-code-generator/code-generator"
repoPath="https://github.com/$repo"
rawPath="https://raw.githubusercontent.com/$repo"

# latest version
echo "Get the latest version"
tag=`curl --silent "$repoPath/releases/latest" | sed 's#.*tag/\(.*\)".*#\1#'`
echo "Latest version: $tag"

# download
download_file() {
    echo "Download file `tput setaf 4`$2`tput sgr0`"
    curl -Lfo /tmp/$1 $2
}
echo "Downloading..."
download_file cg "$rawPath/$tag/cg"
download_file cg.jar "$repoPath/releases/download/$tag/cg.jar"

echo "Download success."

# move file
cgDir=$HOME/.cg/bin
mkdir -p $cgDir
mv /tmp/cg.jar $cgDir/

chmod +x /tmp/cg
sudo mv /tmp/cg /usr/bin/

echo "Install success!"
