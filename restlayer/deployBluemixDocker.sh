#!/bin/sh
clear
. ./VARS.sh
echo ""
echo "starting to deploy on Bluemix docker..."
echo ""
echo "...building the application war: "
echo "$ mvn clean package"
echo ""
mvn clean package

if [ ! -e target/$WAR ]
then
	echo " no war !!! was build ok ????...leaving..."
	exit 1	
fi
echo ""
echo "...building the docker image with the war just built (check Dockerfile):"
echo "$ docker build -t $BX_IMG ."
echo ""
docker build -t $BX_IMG .
echo ""
echo "...going to push image just built ( $BX_IMG ), by its name, "
echo " docker understands that the image is not local and pushes it "
echo " to a namespace in a remote registry( $BX_REGISTRY ):"
echo "$ docker push $BX_IMG"
echo ""
docker push $BX_IMG
echo ""
echo "...going to run a container in bluemix with name $CONTAINER "
echo " based on the image just pushed there ( $BX_IMG ) and exposing port $PORT:"
echo "$ cf ic run --name $CONTAINER -p $PORT -m $BX_CONTAINER_MEMORY $BX_IMG"
echo ""
cf ic run --name $CONTAINER -p $PORT -m $BX_CONTAINER_MEMORY $BX_IMG
sleep 6
echo ""
echo "...going to bind the available public ip ( $BX_IP ) to the running container ( $CONTAINER ) :"
echo "$ cf ic ip bind $BX_IP $CONTAINER"
echo ""
cf ic ip bind $BX_IP $CONTAINER
sleep 12
echo ""
echo "...checking containers currently loaded in bluemix:"
echo "$ cf ic ps"
echo ""
cf ic ps
sleep 24
echo ""
echo "...checking bluemix api with:"
echo "curl -H 'Content-Type: application/json' -d '{\"msg\":\"ola carino\"}' http://$BX_IP:$BX_PORT/$APP/api/echo"
echo "...outcome:"
curl -H 'Content-Type: application/json' -d '{"msg":"ola carino"}' http://$BX_IP:$BX_PORT/$APP/api/echo
echo ""
echo "...done!"

