#!/usr/bin/env bash

JBOSS_HOME=/home/rmo/BinaryPrograms/wildfly-8.2.0.Final
JBOSS_CLI=$JBOSS_HOME/bin/jboss-cli.sh
JBOSS_MODE=${1:-"domain"}
JBOSS_CONFIG=${2:-"$JBOSS_MODE.xml"}

echo "=> Shutting down WildFly"
if [ "$JBOSS_MODE" = "standalone" ]; then
  $JBOSS_CLI -c ":shutdown"
else
  $JBOSS_CLI -c "/host=*:shutdown"
fi
