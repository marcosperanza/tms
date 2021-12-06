FROM openjdk:8u232-stretch

LABEL MAINTAINER Marco Speranza <marco.speranza@anritsu.com>
LABEL DESCRIPTION="REST server for a simple task management system"

ADD target/@project.build.finalName@.jar /data/@project.build.finalName@.jar
ADD ./config.yml /data/config.yml

RUN java -jar /data/@project.build.finalName@.jar db migrate /data/config.yml

CMD java -jar /data/@project.build.finalName@.jar server /data/config.yml

EXPOSE 8080
