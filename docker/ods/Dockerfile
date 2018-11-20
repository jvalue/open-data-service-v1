#REPO: mojo-docker.cs.fau.de/osrg_ods_public/ods

FROM adoptopenjdk/openjdk8-openj9:alpine

ENV ODS_LOG_DIR /var/log/ods

COPY assets/server*-all.jar /ods.jar
COPY assets/ods-configuration.yml.template /ods-configuration.yml
COPY assets/start.sh /start.sh
COPY assets/set-config.sh set-config.sh

CMD /bin/sh start.sh
