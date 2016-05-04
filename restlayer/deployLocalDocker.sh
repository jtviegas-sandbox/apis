#!/bin/sh

clear
. ./VARS.sh

echo ""
echo "starting to deploy on local docker..."
echo ""
echo "...building the application war: "
echo "$ mvn clean package"
echo ""
mvn clean package
echo ""

if [ ! -e target/$WAR ]
then
	echo " no war !!! was build ok ????...leaving... !!!"
	exit 1	
fi

echo "...building the docker image with the war just built (check Dockerfile):"
echo "$ docker build -t $IMG ."
echo ""
docker build -t $IMG .
echo ""
echo "...going to run a container with name $CONTAINER based on the image just built ( $IMG ) and exposing port $PORT:"
echo "$ docker run -d --name $CONTAINER -p $PORT:$PORT $IMG"
echo ""
docker run -d --name $CONTAINER -p $PORT:$PORT $IMG
sleep 6
echo ""
echo "...checking containers currently loaded locally:"
echo "$ docker ps"
echo ""
docker ps
sleep 12
echo ""
echo "...checking api with:"
echo "curl -H 'Content-Type: application/json' -d '{\"msg\":\"ola carino\"}' http://$IP:$PORT/$APP/api/echo"
echo "...outcome:"
curl -H 'Content-Type: application/json' -d '{"msg":"ola carino"}' http://$IP:$PORT/$APP/api/echo
echo ""
echo "...done!"
