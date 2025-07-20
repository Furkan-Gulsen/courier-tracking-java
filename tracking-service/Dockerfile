FROM openjdk:17-jdk-slim AS build

WORKDIR /app

COPY pom.xml .
COPY src src

RUN apt-get update && \
    apt-get install -y maven && \
    mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/tracking-service-1.0.0.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"] 