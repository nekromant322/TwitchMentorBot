##Maven Build
#FROM maven:apline AS builder
#COPY pom.xml /app/
#COPY src /app/src
#FROM maven:apline
#RUN --mount=type=cache,target=/root/.m2 mvn -f /app/pom.xml clean package -DskipTests
#
#FROM openjdk:11-jre
#COPY /target/twitch-0.0.1-SNAPSHOT.jar twitch-bot.jar
#ENTRYPOINT ["java", "-jar", "twitch-bot.jar"]

FROM maven:3.6.3-jdk-11-slim AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src
RUN mvn -B -f pom.xml clean package -DskipTests

FROM openjdk:11-jdk-slim
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]