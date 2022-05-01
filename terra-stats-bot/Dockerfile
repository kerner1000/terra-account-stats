FROM openjdk:17-oracle

EXPOSE 8080
EXPOSE 5005

ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,address=*:5005,server=y,suspend=n

ADD ./target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]