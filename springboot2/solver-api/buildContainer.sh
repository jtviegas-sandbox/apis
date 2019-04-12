#!/bin/sh

this_folder="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
parent_folder=$(dirname $this_folder)

# include file
. $this_folder/include.sh

echo "going to build image $IMG"

_pwd=`pwd`
cd $this_folder
mvn clean install
docker rmi $IMG:$IMG_VERSION
#mvn clean install
docker build -t $IMG .
docker tag $IMG $DOCKER_HUB_IMG
docker push $DOCKER_HUB_IMG
cd $_pwd

echo "... done."