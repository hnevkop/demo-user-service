FROM maven:3.8.2-openjdk-17-slim as BUILD_STAGE
LABEL maintainer="premysl.hnevkovsky@gmail.com"

WORKDIR /app
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

COPY . .

RUN mvn package
FROM openjdk:17-jdk-alpine
WORKDIR /app

COPY --from=BUILD_STAGE /app/target/demo-user-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]