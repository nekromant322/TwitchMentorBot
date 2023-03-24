FROM openjdk:11-jre
COPY target/twitch-0.0.1-SNAPSHOT.jar twitch-bot.jar
ENTRYPOINT ["java", "-jar", "twitch-bot.jar"]