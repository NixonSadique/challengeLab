FROM maven:3.9.16-eclipse-temurin-21-alpine as build
WORKDIR /app
COPY pom.xml .
COPY src ./src
run mvn clean package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from-build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT["java","-jar", "app.jar"]



