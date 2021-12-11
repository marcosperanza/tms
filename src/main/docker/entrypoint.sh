#!/bin/bash

java -jar /data/@project.build.finalName@.jar db migrate /data/config.yml
java -jar /data/@project.build.finalName@.jar server /data/config.yml
