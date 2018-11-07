FROM openjdk:8-jdk

ENV APP_NAME knapsack-core
ENV APP_VERSION 0.0.1-SNAPSHOT

RUN apt-get update && apt-get install -q -y wget libstdc++6 

RUN mkdir -p /opt/app/lib
ADD target/knapsack-core-0.0.1-SNAPSHOT.jar /opt/app/app.jar
ADD start.sh /opt/app/
ADD lib /opt/app/lib
WORKDIR /opt/app
CMD [ "./start.sh" ]