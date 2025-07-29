FROM maven:3.9.11-eclipse-temurin-24-alpine

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

ENV TEST_RUNNER=
ENV ENV=prod
ENV PROXY_FLAG=true

ENTRYPOINT ["sh", "-c", "mvn test -Dtest=$TEST_RUNNER -Denv=$ENV -Dconfig=docker -Dproxy=$PROXY_FLAG"]