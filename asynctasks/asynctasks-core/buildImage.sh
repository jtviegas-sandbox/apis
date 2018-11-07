#!/bin/sh

this_folder=$(dirname $(readlink -f $0))
parent_folder=$(dirname $this_folder)

# include file
. $this_folder/include

echo "going to build image $IMG"

_pwd=`pwd`
cd $this_folder
docker rmi $IMG:$IMG_VERSION
#mvn clean install
docker build -t $IMG .
cd $_pwd

echo "... done."


