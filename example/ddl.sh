#!/bin/sh

set -e

echo code generate example
java -jar path-to-cg.jar -type ddl_mysql -lang sql -in ddl.xlsx -out ./out --type mysql
