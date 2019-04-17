#!/bin/sh

this_folder="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
parent_folder=$(dirname $this_folder)
grand_parent_folder=$(dirname $parent_folder)


echo "going to undeploy all api services..."

$grand_parent_folder/api/devops/azure/undeploy.sh
$grand_parent_folder/solver-api/devops/azure/undeploy.sh
$grand_parent_folder/store-api/devops/azure/undeploy.sh

echo "...undeployment done."