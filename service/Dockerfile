FROM gradle:9.1.0-jdk21-alpine AS setup

WORKDIR /app

COPY ./service ./

RUN chmod 755 ./gradlew
RUN ./gradlew

FROM gradle:9.1.0-jdk21-alpine AS build

COPY --from=setup /app /app

WORKDIR /app

RUN ./gradlew build

FROM openjdk:21-ea-1-jdk-slim AS final

COPY --from=build /app/build/libs/*.jar /app/app.jar

WORKDIR /app

CMD java -jar app.jar
