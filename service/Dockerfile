FROM gradle:9.1-jdk25-alpine AS setup

WORKDIR /app

COPY . .

RUN chmod 755 ./gradlew
RUN ./gradlew :service:help

FROM gradle:9.1-jdk25-alpine AS build

COPY --from=setup /app /app

WORKDIR /app

RUN ./gradlew :service:build

FROM eclipse-temurin:25-alpine AS final

COPY --from=build /app/service/build/libs/*.jar /app/app.jar

WORKDIR /app

CMD java -jar app.jar
