# give the application a name, 
# should match the war in this case 
APP=restlayer
WAR=$APP.war

# --- local docker deployment ---
# local ip/host
IP=localhost
# container name
CONTAINER=$APP
# api port
PORT=9080
# docker image name
IMG=$APP

# --- bluemix docker deployment ---
# bluemix image registry and user namespace
BX_REGISTRY=registry.ng.bluemix.net/mynodeappbue
# bluemix image name - must include namespace registry
BX_IMG=$BX_REGISTRY/$IMG
# ip to bind to - check commands:
# 	cf ic ip list		-> list available ip's
# 	cf ic ip request 	-> request new ip
BX_IP=169.44.6.116
# adjust container memory
BX_CONTAINER_MEMORY=128
# container port to expose to outside world
BX_PORT=$PORT 