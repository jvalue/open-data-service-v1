#!/bin/bash

/bin/sh set-config.sh

if [ -z "$COUCHDB_HOST" ]; then
    echo "ERROR: ENV COUCHDB_HOST not set!"
    exit 1
fi

while ! nc -z $COUCHDB_HOST 5984;
    do
        echo "waiting for CouchDB [$COUCHDB_HOST:5984]  ";
        sleep 1;
    done;
        echo CouchDB is ready!;
        /usr/bin/java -jar /ods.jar server ods-configuration.yml
