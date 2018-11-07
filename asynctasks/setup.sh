#!/bin/sh

this_folder=$(dirname $(readlink -f $0))
parent_folder=$(dirname $this_folder)

_pwd=`pwd`
cd $this_folder
mvn clean install
cd $this_folder/knapsack-store 
./buildImage.sh
cd $this_folder/knapsack-core 
./buildImage.sh
cd $this_folder/knapsack-api 
./buildImage.sh
cd $_pwd
