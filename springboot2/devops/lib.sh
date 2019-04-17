LOG_TRACE=TRUE

debug(){
    local __msg="$1"
    echo "\n [DEBUG] `date` ... $__msg\n"
}

info(){
    local __msg="$1"
    echo "\n [INFO]  `date` ->>> $__msg\n"
}

warn(){
    local __msg="$1"
    echo "\n [WARN]  `date` *** $__msg\n"
}

err(){
    local __msg="$1"
    echo "\n [ERR]   `date` !!! $__msg\n"
}

goin(){
    if [ ! -z $LOG_TRACE ]; then
        local __msg="$1"
        local __params="$2"
        echo "\n [IN]    `date` ___ $__msg [$__params]\n"
    fi
}

goout(){
    if [ ! -z $LOG_TRACE ]; then
        local __msg="$1"
        local __outcome="$2"
        echo "\n [OUT]   `date` ___ $__msg [$__outcome]\n"
    fi
}


findResourceGroup(){
    goin "findResourceGroup" "$1"
    local __resourceGroup=$1
    local __r=0
    az group list -o tsv | grep $__resourceGroup
    __r=$?
    if [ ! "$__r" -eq "0" ]
    then
        debug "no resource group found: $__resourceGroup"
    else
        debug "resource group $__resourceGroup already there."
    fi
    goout "findResourceGroup" $__r
    return $__r
}

deleteResourceGroup(){
    goin "deleteResourceGroup" "$1"
    local __resourceGroup=$1
    local __r=0
    az group delete --yes --name $__resourceGroup
    __r=$?
    if [ ! "$__r" -eq "0" ] ; then
        warn "could not delete resource group $__resourceGroup"
    else
        info "deleted resource group $__resourceGroup"
    fi
    goout "deleteResourceGroup" $__r
    return $__r
}

createResourceGroup()
{
    goin "createResourceGroup" "$1 $2 $3"
    local __resourceGroup=$1
    local __location=$2
    local __tags=$3
    local __r=0

     az group create --name $__resourceGroup --location $__location --tags $__tags
     __r=$?
     if [ ! "$__r" -eq "0" ] ; then warn "! could not create resource group $__resourceGroup !"; else info "created resource group $__resourceGroup" ; fi

    goout "createResourceGroup" $__r
    return $__r
}

findAppServicePlan(){
    goin "findAppServicePlan" "$1"
    local __plan=$1
    local __r=0
    az appservice plan list -o tsv | grep $__plan
    __r=$?
    if [ ! "$__r" -eq "0" ]
    then
        debug "no app service plan found: $__plan"
    else
        debug "app service plan $__plan already there."
    fi
    goout "findAppServicePlan" $__r
    return $__r
}

searchAppServicePlan()
{
    local __r=0
    local __plan=$1
    local __result=`az appservice plan list -o tsv | grep $__plan | awk '{print $12}'`

    echo "$__result"
}

createAppServicePlan()
{
    goin "createAppServicePlan" "$1 $2 $3"
    local __plan=$1
    local __location=$2
    local __resourceGroup=$3
    local __r=0

    az appservice plan create --name $__plan --resource-group $__resourceGroup --location $__location
    __r=$?
    if [ ! "$__r" -eq "0" ] ; then warn "! could not create app service plan $__plan !"; else info "created app service plan $__plan" ; fi

    goout "createAppServicePlan" $__r
    return $__r
}

deleteAppServicePlan(){
    goin "deleteAppServicePlan" "$1 $2"
    local __plan=$1
    local __resourceGroup=$2
    local __r=0
    az appservice plan delete --yes --name $__plan --resource-group $__resourceGroup
    __r=$?
    if [ ! "$__r" -eq "0" ] ; then
        warn "could not delete app service plan $__plan"
    else
        info "deleted app service plan $__plan"
    fi
    goout "deleteAppServicePlan" $__r
    return $__r
}


findContainerRegistry(){
    goin "findContainerRegistry" "$1"
    local __registry=$1
    local __r=0
    az acr list -o tsv | grep $__registry
    __r=$?
    if [ ! "$__r" -eq "0" ]
    then
        debug "no container registry found: $__registry"
    else
        debug "container registry $__registry already there."
    fi
    goout "findContainerRegistry" $__r
    return $__r
}

createContainerRegistry()
{
    goin "createContainerRegistry" "$1 $2 $3"
    local __name=$1
    local __location=$2
    local __resourceGroup=$3
    local __sku=Basic
    if [ ! -z $4 ]; then
        __sku=$4
    fi
    local __r=0

    az acr create --admin-enabled --resource-group $__resourceGroup --location $__location --name $__name --sku $__sku
    __r=$?
    if [ ! "$__r" -eq "0" ] ; then warn "! could not create container registry $__registry !"; else info "created container registry $__registry " ; fi

    goout "createContainerRegistry" $__r
    return $__r
}

deleteContainerRegistry(){
    goin "deleteContainerRegistry" "$1 $2"
    local __registry=$1
    local __resourceGroup=$2
    local __r=0
    az acr delete --name $__registry --resource-group $__resourceGroup
    __r=$?
    if [ ! "$__r" -eq "0" ] ; then
        warn "could not delete container registry $__registry"
    else
        info "deleted container registry $__registry"
    fi
    goout "deleteContainerRegistry" $__r
    return $__r
}

printContainerRegistryPassword(){
    goin "printContainerRegistryPassword" "$1"
    local __registry=$1
    local __r=0
    az acr credential show -o tsv --name $__registry --query passwords[0] | awk '{print $2}'
    goout "printContainerRegistryPassword" $__r
    return $__r
}

deleteWebApp(){
    goin "deleteWebApp" "$1 $2"
    local __name=$1
    local __resourceGroup=$2
    local __r=0
    az webapp delete -g $__resourceGroup --name $__name
    __r=$?
    if [ ! "$__r" -eq "0" ] ; then
        warn "could not delete web app $__name"
    else
        info "deleted web app $__name"
    fi
    goout "deleteWebApp" $__r
    return $__r
}

createAppInsightsResourceForWebApp(){

    goin "createAppInsightsResourceForWebApp" "$1 $2 $3"
    local __name=$1
    local __location=$2
    local __resourceGroup=$3

    local __r=0
    az resource create --resource-group $__resourceGroup --resource-type "Microsoft.Insights/components" --name $__name --location $__location --properties '{"ApplicationType":"web"}'
    __r=$?
    if [ ! "$__r" -eq "0" ] ; then warn "! could not create app insights resource for web app $__name !"; else info "created app insights resource for web app $__name" ; fi

    goout "createAppInsightsResourceForWebApp" $__r
    return $__r

}


findAppInsightsResourceForWebApp(){
    goin "findAppInsightsResourceForWebApp" "$1 $2"
    local __name=$1
    local __resourceGroup=$2
    az resource list --resource-type "Microsoft.Insights/components" --resource-group $__resourceGroup -o tsv | grep $__name
    __r=$?
    if [ ! "$__r" -eq "0" ]
    then
        info "app insights resource for web app $__name not found"
    else
        info "app insights resource for web app $__name is already there."
    fi
    goout "findAppInsightsResourceForWebApp" $__r
    return $__r
}

deleteAppInsightsResourceForWebApp(){
    goin "deleteAppInsightsResourceForWebApp" "$1 $2"
    local __name=$1
    local __resourceGroup=$2
    local __r=0
    az resource delete --resource-type "Microsoft.Insights/components" --resource-group $__resourceGroup --name $__name
    __r=$?
    if [ ! "$__r" -eq "0" ] ; then
        warn "could not delete app insights resource for web app $__name"
    else
        info "deleted app insights resource for web app $__name"
    fi
    goout "deleteAppInsightsResourceForWebApp" $__r
    return $__r
}