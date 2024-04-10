#!/usr/bin/env bash
sleep 20 #TODO: add healthcheck instead of waiting 20s
./opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P $MSSQL_SA_PASSWORD -i /container-setup/databases-setup.sql
if [ "$ADD_SAMPLE_DATA" == "true" ]
then
  ./opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P $MSSQL_SA_PASSWORD -i /container-setup/sample-data.sql
fi