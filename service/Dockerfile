FROM gradle:8.12-jdk23-alpine AS build

COPY . /app

WORKDIR /app

RUN ./gradlew build

FROM openjdk:23-jdk-slim AS final

COPY --from=build /app/build/libs/*.jar /app/app.jar

WORKDIR /app

CMD java -jar app.jar
