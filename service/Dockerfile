FROM gradle:8.10-jdk21-alpine AS setup

WORKDIR /app

COPY . .

RUN chmod 755 ./gradlew
RUN ./gradlew :service:help

FROM gradle:8.10-jdk21-alpine AS build

COPY --from=setup /app /app

WORKDIR /app

RUN ./gradlew :service:build

FROM eclipse-temurin:21-alpine AS final

COPY --from=build /app/service/build/libs/*.jar /app/app.jar

WORKDIR /app

CMD java -jar app.jar
