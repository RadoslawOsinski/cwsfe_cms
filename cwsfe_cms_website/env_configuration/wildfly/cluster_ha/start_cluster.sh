#!/usr/bin/env bash

JBOSS_HOME=/home/rmo/BinaryPrograms/wildfly-8.2.0.Final
JBOSS_CLI=$JBOSS_HOME/bin/jboss-cli.sh
JBOSS_MODE=${1:-"domain"}
JBOSS_CONFIG=${2:-"$JBOSS_MODE.xml"}

echo "=> Starting WildFly server";
$JBOSS_HOME/bin/$JBOSS_MODE.sh -b 0.0.0.0 -bmanagement 0.0.0.0 -c $JBOSS_CONFIG &
