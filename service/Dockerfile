FROM gradle:9.1-jdk25-alpine AS build

WORKDIR /app

COPY . .

RUN ./gradlew :service:build -x :service:test

FROM eclipse-temurin:25-alpine AS final

COPY --from=build /app/service/build/libs/*.jar /app/app.jar

WORKDIR /app

CMD java -jar app.jar
