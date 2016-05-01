#!/bin/sh

APP_NAME=restlayer

docker stop $APP_NAME
sleep 6
docker rm -f $APP_NAME