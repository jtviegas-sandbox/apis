#!/bin/sh

this_folder=$(dirname $(readlink -f $0))
parent_folder=$(dirname $this_folder)

whereis java
ls

debug_switch=""
if [ ! -z "$DEBUG_PORT" ]; then
	debug_switch="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=$DEBUG_PORT"
fi

java $debug_switch -Djava.library.path=".:./lib" -jar /opt/app/app.jar


