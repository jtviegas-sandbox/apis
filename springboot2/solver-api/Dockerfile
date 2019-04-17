FROM java:openjdk-8-jre

RUN mkdir -p /opt/app
RUN mkdir -p /opt/app/agent
WORKDIR /tmp/
RUN wget https://github.com/Microsoft/ApplicationInsights-Java/releases/download/2.4.0-BETA/applicationinsights-agent-2.4.0-BETA.jar
RUN mv applicationinsights-agent-2.4.0-BETA.jar /opt/app/agent/applicationinsights-agent.jar
ADD AI-Agent.xml /opt/app/agent/AI-Agent.xml
ADD target/solver-api-0.0.1-SNAPSHOT.jar /opt/app/app.jar
ADD start.sh /opt/app/
WORKDIR /opt/app
CMD [ "./start.sh" ]