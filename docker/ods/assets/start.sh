#!/bin/bash

# Set config if not set
/bin/sh set-config.sh

# Start ODS service
/usr/bin/java -jar /ods.jar server ods-configuration.yml
