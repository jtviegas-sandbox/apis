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
docker push $APP_IMG

cf ic run --name $APP_NAME -p $PORT -m $CONTAINER_MEMORY $APP_IMG

cf ic ip bind $IP $APP_NAME

curl -H "Content-Type: application/json" -d '{"msg":"ola carino"}' http://$IP:$PORT/$APP_NAME/api/echo


