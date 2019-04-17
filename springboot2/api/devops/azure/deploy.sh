#!/bin/sh

this_folder="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
parent_folder=$(dirname $this_folder)
grand_parent_folder=$(dirname $parent_folder)
base_folder=$(dirname $grand_parent_folder)

# include file
. $parent_folder/include.sh
. $base_folder/devops/lib.sh

echo "going to deploy app $NAME"

findResourceGroup $AZURE_RESOURCE_GROUP
__r=$?
if [ ! "$__r" -eq "0" ] ; then
     createResourceGroup $AZURE_RESOURCE_GROUP $AZURE_RESOURCE_GROUP_LOCATION $AZURE_RESOURCE_GROUP_TAGS
     __r=$?
     if [ ! "$__r" -eq "0" ] ; then exit 1; fi
fi

findContainerRegistry $AZURE_CONTAINER_REGISTRY
__r=$?
if [ ! "$__r" -eq "0" ] ; then
     createContainerRegistry $AZURE_CONTAINER_REGISTRY $AZURE_CONTAINER_REGISTRY_LOCATION $AZURE_RESOURCE_GROUP
     __r=$?
     if [ ! "$__r" -eq "0" ] ; then exit 1; fi
fi

findAppInsightsResourceForWebApp $AZURE_APP_INSIGHTS_RESOURCE $AZURE_RESOURCE_GROUP
__r=$?
if [ ! "$__r" -eq "0" ] ; then
     createAppInsightsResourceForWebApp $AZURE_APP_INSIGHTS_RESOURCE $AZURE_RESOURCE_GROUP_LOCATION $AZURE_RESOURCE_GROUP
     __r=$?
     if [ ! "$__r" -eq "0" ] ; then
        exit 1;
     else
        appInsightsInstrumentationKey="$(az resource show -g $AZURE_RESOURCE_GROUP -n $AZURE_APP_INSIGHTS_RESOURCE --resource-type 'Microsoft.Insights/components' --query properties.InstrumentationKey)"
        appInsightsInstrumentationKey="${appInsightsInstrumentationKey%\"}"
        appInsightsInstrumentationKey="${appInsightsInstrumentationKey#\"}"
        echo $appInsightsInstrumentationKey > $base_folder/devops/SECRET
     fi
fi

cd $grand_parent_folder

secret_key=`cat $base_folder/devops/SECRET`
sed "/APPLICATION_INSIGHTS_IKEY/s/XPTOXPTO/$secret_key/g" $grand_parent_folder/pom.xml-template > $grand_parent_folder/pom.xml
sed "/instrumentationKey/s/XPTOXPTO/$secret_key/g" $this_folder/logback-spring.xml > $grand_parent_folder/src/main/resources/logback-spring.xml
mvn -DAPPLICATION_INSIGHTS_IKEY=$secret_key clean package docker:build -Pazure -DpushImage && mvn -DAPPLICATION_INSIGHTS_IKEY=$secret_key -X azure-webapp:deploy
# reset files so that git doesn't take the instrumentation key
cp $grand_parent_folder/pom.xml-template $grand_parent_folder/pom.xml
#cp $this_folder/logback-spring.xml-ORIGINAL $grand_parent_folder/src/main/resources/logback-spring.xml


az webapp log config --name $AZURE_APPNAME --resource-group $AZURE_RESOURCE_GROUP --application-logging true --detailed-error-messages true --failed-request-tracing true --web-server-logging filesystem


cd $this_folder

echo "... app $NAME deployment done."