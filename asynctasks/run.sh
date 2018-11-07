#!/bin/sh

this_folder=$(dirname $(readlink -f $0))
parent_folder=$(dirname $this_folder)

COMPOSE_FILE=$this_folder/docker-compose.yml

docker-compose -f $COMPOSE_FILE up
