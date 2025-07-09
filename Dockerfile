FROM maven:3-eclipse-temurin-17-alpine

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

ARG TEST_RUNNER
ARG ENV

ENTRYPOINT ["mvn", "test", "-Dtest=${TEST_RUNNER}", "-Denv=${ENV}"]