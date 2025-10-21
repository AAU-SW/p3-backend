FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw mvnw
RUN --mount=type=cache,target=/root/.m2 ./mvnw -q -DskipTests dependency:go-offline

COPY src src
RUN --mount=type=cache,target=/root/.m2 ./mvnw -q -DskipTests clean package

FROM eclipse-temurin:21-jre
WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=prod \
    MONGO_URI= \
    MONGO_DB_NAME= \
    JWT_SECRET= \
    JWT_EXPIRES_IN= \
    JAVA_OPTS=""

COPY --from=build /workspace/target/*.jar /app/app.jar

EXPOSE 8080
USER 1001

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
