#REPO: mojo-docker.cs.fau.de/osrg_ods_public/ods

# Base Alpine Linux based image with OpenJDK JRE only
FROM openjdk:8-jre-alpine

ENV ODS_LOG_DIR /var/log/ods

COPY assets/server*-all.jar /ods.jar
COPY assets/ods-configuration.yml.template /ods-configuration.yml
COPY assets/start.sh /start.sh
COPY assets/set-config.sh set-config.sh

CMD /bin/sh start.sh
