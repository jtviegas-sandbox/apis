#!/bin/sh

this_folder="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
parent_folder=$(dirname $this_folder)

# include file
. $this_folder/include.sh

echo "going to stop container $CONTAINER"
docker stop $CONTAINER
echo "... done."