#REPO: mojo-docker.cs.fau.de/osrg_ods/ods-SNAPSHOT

# Base Alpine Linux based image with OpenJDK JRE only
FROM openjdk:8-jre-alpine

ENV ODS_LOG_DIR /var/log/ods

COPY assets/server*-all.jar /ods.jar
COPY assets/ods-configuration.yml.template /ods-configuration.yml
COPY assets/start.sh /start.sh
COPY assets/set-config.sh set-config.sh

## Add the wait script to the image
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.0.0/wait /wait
RUN chmod +x /wait

CMD /wait && /bin/sh start.sh
