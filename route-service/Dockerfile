# Stage 1: build Maven projekta u velikoj JDK slici
FROM maven:3.9-eclipse-temurin-25 AS build-stage
WORKDIR /opt/route-service
COPY pom.xml .
RUN mvn dependency:go-offline
COPY ./src ./src
RUN mvn clean package -Dmaven.test.skip=true

# Stage 2: mala JRE slika samo s prevedenom aplikacijom
FROM eclipse-temurin:25-jre-alpine
WORKDIR /opt/route-service
COPY --from=build-stage /opt/route-service/target/route-service.jar ./app.jar
EXPOSE 8082 8083
ENTRYPOINT ["java","-jar","app.jar"]
