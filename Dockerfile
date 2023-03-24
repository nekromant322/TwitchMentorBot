##Maven Build
#FROM maven:apline AS builder
#COPY pom.xml /app/
#COPY src /app/src
FROM maven:apline
RUN --mount=type=cache,target=/root/.m2 mvn -f /app/pom.xml clean package -DskipTests

FROM openjdk:11-jre
COPY /target/twitch-0.0.1-SNAPSHOT.jar twitch-bot.jar
ENTRYPOINT ["java", "-jar", "twitch-bot.jar"]
