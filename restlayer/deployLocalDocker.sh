#!/bin/sh

. ./VARS.sh

mvn clean package

if [ ! -e target/$WAR ]
then
	echo " no war !!! was build ok ????...leaving..."
	exit 1	
fi

docker build -t $IMG .
docker run -d --name $CONTAINER -p $PORT:$PORT $IMG
sleep 6
echo "checking containers loaded..."
docker ps
sleep 6
echo "...checking api with:"
echo "curl -H 'Content-Type: application/json' -d '{\"msg\":\"ola carino\"}' http://$IP:$PORT/$APP/api/echo"
echo "...outcome:"
curl -H 'Content-Type: application/json' -d '{"msg":"ola carino"}' http://$IP:$PORT/$APP/api/echo
echo ""
echo "...done!"
