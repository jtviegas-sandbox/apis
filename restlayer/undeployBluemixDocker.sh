#!/bin/sh
clear
. ./VARS.sh
echo ""
echo "starting to undeploy $CONTAINER on Bluemix docker..."
echo ""
echo "...stopping the container: "
echo "$ cf ic stop $CONTAINER"
cf ic stop $CONTAINER
sleep 6
echo ""
echo "...deleting the container: "
echo "$ cf ic rm -f $CONTAINER"
echo ""
cf ic rm -f $CONTAINER
echo ""
echo "...done!"