FROM maven:3.6.3-jdk-11-slim AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src
RUN mvn -B -f pom.xml clean package -DskipTests

FROM openjdk:11-jdk-slim as extracter
WORKDIR application
COPY --from=build /workspace/target/*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:11-jdk-slim
WORKDIR application
COPY --from=extracter application/dependencies/ ./
COPY --from=extracter application/spring-boot-loader/ ./
COPY --from=extracter application/snapshot-dependencies/ ./
RUN true
COPY --from=extracter application/application/ ./
EXPOSE 9000
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]