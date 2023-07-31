#!/usr/bin/env bash

REPOSITORY=/home/ubuntu/BidderOwn_BE/build/libs

echo "> Check running pid"

CURRENT_PID=$(pgrep -fla java | grep hayan | awk '{print $1}')

echo "running pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "skip"
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> Deploy new application"

JAR_NAME=$(ls -tr $REPOSITORY/*SNAPSHOT.jar | tail -n 1)

echo "> JAR NAME: $JAR_NAME"

echo "> Add permission on $JAR_NAME"

chmod +x $JAR_NAME

echo "> Execute $JAR_NAME"

nohup java -Dspring.profiles.active=leuiprod -jar $JAR_NAME &