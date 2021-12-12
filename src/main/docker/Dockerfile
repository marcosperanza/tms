FROM openjdk:8u232-stretch

LABEL MAINTAINER Marco Speranza <marco.speranza79@gmail.com>
LABEL DESCRIPTION="REST server for a simple task management system"

ADD target/@project.build.finalName@.jar /data/@project.build.finalName@.jar
ADD ./config.yml /data/config.yml
ADD ./target/docker/entrypoint.sh /entrypoint.sh
RUN chmod 755 /entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/entrypoint.sh"]
