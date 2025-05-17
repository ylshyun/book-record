FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/SpringBoot-0.0.1-SNAPSHOT.jar bookrecord.jar

ENTRYPOINT ["java", "-jar", "bookrecord.jar"]
