FROM java:openjdk-8-jre

ENV APP_NAME knapsack-store
ENV APP_VERSION 0.0.1-SNAPSHOT

RUN mkdir -p /opt/app
ADD target/knapsack-store-0.0.1-SNAPSHOT.jar /opt/app/app.jar
ADD start.sh /opt/app/
WORKDIR /opt/app
CMD [ "./start.sh" ]