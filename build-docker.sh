#!/usr/bin/env bash

# Build Script for cg project.
# Author: me@yanglb.com
# Date: 2026/10/26

set -euo pipefail

function printUsage {
    echo "Build cg docker image."
    echo "Usage: ./build-docker.sh [TAG] [options]"
    echo ""
    echo "TAG         image version default is latest"
    echo ""
    echo "Options"
    echo "  -h --help     Print usage."
}

TAG=${1:-"latest"}
if [ $TAG = '-h' ] || [ $TAG = '--help' ];then
    printUsage
    exit 0
fi

echo "Build docker image for yanglibing/code-generator:${TAG}"

# build docker
docker buildx build . \
    --tag yanglibing/code-generator:${TAG} \
    --provenance=false \
    --output type=docker \
    --platform linux/amd64,linux/arm64

# publish
docker push yanglibing/code-generator:${TAG}
