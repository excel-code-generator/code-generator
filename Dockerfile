# code-generator docker file.
# VERSION 1.0.0
# image: yanglibing/code-generator
# by yanglb.com
#
FROM openjdk:8-jre-alpine
LABEL maintainer "yanglb <dev@yanglb.com>"

COPY ./target/code-generator-*-jar-with-dependencies.jar /usr/local/lib/cg/bin/cg.jar
COPY ./cg /usr/local/bin/cg

CMD [ "cg" ]
