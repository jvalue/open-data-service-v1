#!/bin/bash
#
# Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
#
# SPDX-License-Identifier: AGPL-3.0-only
#

# Set config if not set
/bin/sh set-config.sh

# Start ODS service
# JVM option "-Xtune:virtualized" is for OpenJ9 VMs
# See: https://www.eclipse.org/openj9/docs/xtunevirtualized/
java -jar -Xtune:virtualize /ods.jar server ods-configuration.yml
