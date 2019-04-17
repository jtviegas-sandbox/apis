#!/bin/sh

this_folder="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
parent_folder=$(dirname $this_folder)
grand_parent_folder=$(dirname $parent_folder)
base_folder=$(dirname $grand_parent_folder)

# include file
. $parent_folder/include.sh
. $base_folder/devops/lib.sh

echo "going to undeploy app $NAME"

appServicePlan=$(searchAppServicePlan "$AZURE_APPNAME")
deleteWebApp "$AZURE_APPNAME" $AZURE_RESOURCE_GROUP
deleteAppServicePlan $appServicePlan $AZURE_RESOURCE_GROUP

echo "... app $NAME undeployment done."