#!/bin/sh

this_folder="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
parent_folder=$(dirname $this_folder)


usage()
{
        cat <<EOM
        usage:
        $(basename $0) start|stop
EOM
        exit 1
}

[ -z $1 ] && { usage; }
[ "$1" != "start" -a "$1" != "stop" ] && { usage; }

echo "going to $1 the store api container..."

if [ "$1" == "start" ]
then
    $parent_folder/store-api/buildContainer.sh
    $this_folder/buildContainer.sh
    secret_key=`cat SECRET`
    export APPLICATION_INSIGHTS_IKEY=$secret_key
    docker-compose -f $this_folder/docker-compose.yml up -d
    echo "...waiting for the api to load..."
    sleep 16
else
    docker-compose -f $this_folder/docker-compose.yml down
fi

echo "....done."


