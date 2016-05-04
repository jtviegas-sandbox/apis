#!/bin/sh
clear
. ./VARS.sh
echo ""
echo "starting to undeploy $CONTAINER on local docker..."
echo ""
echo "...stopping the container: "
echo "$ docker stop $CONTAINER"
docker stop $CONTAINER
sleep 6
echo "...deleting the container: "
echo "$ docker rm -f $CONTAINER"
docker rm -f $CONTAINER
echo ""
echo "...done!"