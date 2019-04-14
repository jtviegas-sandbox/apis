#!/bin/sh

this_folder="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
parent_folder=$(dirname $this_folder)

# include file
. $this_folder/include.sh

echo "going to run container from image $IMG"
secret_key=`cat SECRET`
docker rm $CONTAINER
docker run -d --name $CONTAINER --env APPLICATION_INSIGHTS_IKEY=$secret_key  -p $PORT:$PORT $IMG:$IMG_VERSION

echo "... done."