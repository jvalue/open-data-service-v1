#!/bin/bash

/bin/sh set-config.sh

 while ! nc -z couchdb 5984;
     do
        echo "waiting for CouchDB...";
        sleep 1;
    done;
        echo CouchDB is ready!;
        /usr/bin/java -jar /ods.jar server ods-configuration.yml
