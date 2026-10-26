#!/usr/bin/env bash

set -euo pipefail

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
POM="$PROJECT_DIR/pom.xml"
VERSION="$(mvn -q -f "$POM" -DforceStdout help:evaluate -Dexpression=project.version)"
JAR="$PROJECT_DIR/target/code-generator-$VERSION-jar-with-dependencies.jar"

if [ -z "$VERSION" ]; then
    echo "Could not determine project version from pom.xml." >&2
    exit 1
fi

echo "Building project..."
mvn -f "$POM" clean package

if [ ! -f "$JAR" ]; then
    echo "Expected build artifact not found: $JAR" >&2
    exit 1
fi

cp "$JAR" "$PROJECT_DIR/cg.jar"
echo "Built ${JAR##*/} and copied it to cg.jar."
