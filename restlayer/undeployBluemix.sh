#!/bin/sh

APP_NAME=restlayer

cf ic stop $APP_NAME
sleep 6
cf ic rm -f $APP_NAME