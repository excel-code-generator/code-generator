# code-generator docker file.
# VERSION 1.0.0
# image: yanglibing/code-generator
# by yanglb.com
#
# 升级记录
#  v1.0 | 2021/4/27  | 原始版本
#  v2.0 | 2024/11/30 | 升级jdk版本及支持Arm64架构
#  v2.1 | 2025/9/17  | 升级jdk版本

FROM eclipse-temurin:24-jre-alpine
LABEL org.opencontainers.image.authors="yanglb <dev@yanglb.com>"

COPY ./target/code-generator-*-jar-with-dependencies.jar /usr/local/lib/cg/bin/cg.jar
COPY ./cg /usr/local/bin/cg

CMD [ "cg" ]
