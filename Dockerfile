FROM maven:3.9.11-eclipse-temurin-24-alpine

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

ARG TEST_RUNNER
ARG ENV

ENTRYPOINT ["mvn", "test", "-Dtest=${TEST_RUNNER}", "-Denv=${ENV}"]