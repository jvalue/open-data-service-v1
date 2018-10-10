#!/bin/bash

# Set config if not set
/bin/sh set-config.sh

# Start ODS service
# JVM option "-Xtune:virtualized" is for OpenJ9 VMs
# See: https://www.eclipse.org/openj9/docs/xtunevirtualized/
java -jar -Xtune:virtualize /ods.jar server ods-configuration.yml
