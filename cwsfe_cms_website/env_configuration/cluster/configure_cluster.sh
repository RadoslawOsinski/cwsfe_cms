#!/usr/bin/env bash

JBOSS_HOME=/home/rmo/BinaryPrograms/wildfly-8.2.0.Final
JBOSS_CLI=$JBOSS_HOME/bin/jboss-cli.sh
JBOSS_MODE=${1:-"domain"}
JBOSS_CONFIG=${2:-"$JBOSS_MODE.xml"}

echo "Add admin account";
$JBOSS_HOME/bin/add-user.sh admin admin --silent

curl -o /tmp/psql-jdbc.jar https://jdbc.postgresql.org/download/postgresql-9.4-1201.jdbc4.jar

$JBOSS_CLI --file=cwsfe.cli
