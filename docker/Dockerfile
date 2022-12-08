FROM eclipse-temurin:11-centos7
ARG JAR_FILE=/build/libs/octopus-test-*.jar
WORKDIR app
COPY $JAR_FILE app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]
CMD "pwd"
EXPOSE 8080