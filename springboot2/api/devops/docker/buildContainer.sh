#!/bin/sh

this_folder="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
parent_folder=$(dirname $this_folder)
grand_parent_folder=$(dirname $parent_folder)
base_folder=$(dirname $grand_parent_folder)

# include file
. $parent_folder/include.sh

echo "going to build image $IMG"

_pwd=`pwd`
cd $grand_parent_folder
mvn clean install
cd $this_folder
docker rmi $IMG:$IMG_VERSION
docker build -t $IMG $grand_parent_folder/
docker tag $IMG $DOCKER_HUB_IMG
docker push $DOCKER_HUB_IMG
cd $_pwd

echo "... done."