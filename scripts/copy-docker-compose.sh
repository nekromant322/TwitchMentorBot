#!/usr/bin/env bash

echo 'Copy files...'

scp -i ~/.ssh/id_rsa \
    ./docker-compose.yml \
    masha@v1570779.hosted-by-vdsina.ru:/home/masha/

echo 'Bye'