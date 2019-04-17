#!/bin/sh

this_folder="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
parent_folder=$(dirname $this_folder)
grand_parent_folder=$(dirname $parent_folder)

# include file
. $parent_folder/include.sh

echo "going to stop container $CONTAINER"
docker stop $CONTAINER
echo "... done."