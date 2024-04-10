#!/usr/bin/env bash
set -m
./opt/mssql/bin/sqlservr & /container-setup/setup_database.sh
fg