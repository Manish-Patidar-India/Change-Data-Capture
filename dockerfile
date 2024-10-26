
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar cdc.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "cdc.jar"]