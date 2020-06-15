#!/usr/bin/env sh
set -e

# Always download the latest version
# For more information about the cg.jar, please vist the following page:
# https://github.com/excel-code-generator/code-generator

CG_PATH="excel-code-generator/code-generator"
BASE_PATH="https://github.com/$CG_PATH"
RAW_PATH="https://raw.githubusercontent.com/$CG_PATH"

# latest version
echo "Get the latest version"
VER=`curl --silent "$BASE_PATH/releases/latest" | sed 's#.*tag/\(.*\)".*#\1#'`
echo "Latest version: $VER"

# download
SH_FILE="$RAW_PATH/$VER/cg"
JAR_FILE="$BASE_PATH/releases/download/$VER/cg.jar"

download_file() {
    echo "Download file `tput setaf 4`$2`tput sgr0`"
    curl -Lfo /tmp/$1 $2
}
echo "Downloading..."
download_file cg $SH_FILE
download_file cg.jar $JAR_FILE

echo "Download success."

# move file
CG_DIR=$HOME/.cg/bin
mkdir -p $CG_DIR

chmod +x /tmp/cg
mv /tmp/cg /usr/local/bin/
mv /tmp/cg.jar $CG_DIR/

echo "Install success!"
cg -v
