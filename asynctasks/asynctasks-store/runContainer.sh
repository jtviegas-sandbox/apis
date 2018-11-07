#!/bin/sh

this_folder=$(dirname $(readlink -f $0))
parent_folder=$(dirname $this_folder)

# include file
. $this_folder/include

echo "going to run container from image $IMG"

docker run -d --name $CONTAINER -p $PORT:$PORT $IMG:$IMG_VERSION

echo "... done."

