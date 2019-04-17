#!/bin/sh

this_folder="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
parent_folder=$(dirname $this_folder)
grand_parent_folder=$(dirname $parent_folder)
base_folder=$(dirname $grand_parent_folder)

# include file
. $parent_folder/include.sh

echo "going to run container from image $IMG"
secret_key=`cat $base_folder/devops/SECRET`
docker rm $CONTAINER
docker run -d --name $CONTAINER --env APPLICATION_INSIGHTS_IKEY=$secret_key  -p $PORT:$PORT $IMG:$IMG_VERSION

echo "... done."