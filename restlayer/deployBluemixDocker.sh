#!/bin/sh

. ./VARS.sh

mvn clean package

if [ ! -e target/$WAR ]
then
	echo " no war !!! was build ok ????...leaving..."
	exit 1	
fi

docker build -t $BX_IMG .
docker push $BX_IMG

cf ic run --name $CONTAINER -p $PORT -m $BX_CONTAINER_MEMORY $BX_IMG
sleep 6
cf ic ip bind $BX_IP $CONTAINER
echo "checking containers loaded in bluemix..."
cf ic ps
sleep 24
echo "...checking bluemix api with:"
echo "curl -H 'Content-Type: application/json' -d '{\"msg\":\"ola carino\"}' http://$BX_IP:$BX_PORT/$APP/api/echo"
echo "...outcome:"
curl -H 'Content-Type: application/json' -d '{"msg":"ola carino"}' http://$BX_IP:$BX_PORT/$APP/api/echo
echo ""
echo "...done!"

