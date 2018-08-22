#REPO: mojo-docker.cs.fau.de/osrg_ods_public/ods

FROM adoptopenjdk/openjdk10-openj9:x86_64-alpine-jdk-10.0.2.13_openj9-0.9.0-slim

ENV ODS_LOG_DIR /var/log/ods

COPY assets/server*-all.jar /ods.jar
COPY assets/ods-configuration.yml.template /ods-configuration.yml
COPY assets/start.sh /start.sh
COPY assets/set-config.sh set-config.sh

CMD /bin/sh start.sh
