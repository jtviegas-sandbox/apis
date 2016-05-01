#!/bin/sh

. ./VARS.sh

APP_NAME=restlayer
WAR=$APP_NAME.war
APP_IMG=$REGISTRY/$APP_NAME

mvn clean package

if [ ! -e target/$WAR ]
then
	echo " no war !!! was build ok ????...leaving..."
	exit 1	
fi

docker build -t $APP_IMG .
docker run -d --name $APP_NAME -p $PORT:$PORT $APP_IMG
sleep 6
echo "checking containers loaded..."
docker ps
sleep 6
echo "...checking api..."
curl -H "Content-Type: application/json" -d '{"msg":"ola carino"}' http://localhost:$PORT/$APP_NAME/api/echo
echo ""
echo "...done!"
