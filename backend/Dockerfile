FROM gradle:8.12-jdk23-alpine AS build

COPY ./src /app/src
COPY ./gradle /app/gradle
COPY ./build.gradle /app/build.gradle
COPY ./settings.gradle /app/settings.gradle
COPY ./gradlew.bat /app/gradlew.bat
COPY ./gradlew /app/gradlew

WORKDIR /app

RUN gradle build

FROM openjdk:23-bookworm AS final

COPY --from=build /app/build/libs/backend-0.0.1-SNAPSHOT.jar /app/backend.jar

CMD java -jar /app/backend.jar
