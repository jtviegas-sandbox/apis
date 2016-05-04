#!/bin/sh

. ./VARS.sh

docker stop $CONTAINER
sleep 6
docker rm -f $CONTAINER