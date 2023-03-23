#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -i ~/.ssh/id_rsa \
    target/twitch-0.0.1-SNAPSHOT.jar \
    masha@v1570779.hosted-by-vdsina.ru:/home/masha/

echo 'Restart server...'

ssh -i ~/.ssh/id_rsa masha@v1570779.hosted-by-vdsina.ru << EOF
pgrep java | xargs kill -9
nohup java -jar twitch-0.0.1-SNAPSHOT.jar > log.txt &
EOF

echo 'Bye'