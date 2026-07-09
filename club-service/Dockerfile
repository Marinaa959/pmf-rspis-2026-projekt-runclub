# build Maven projekta
FROM maven:3.9-eclipse-temurin-25 AS build-stage
WORKDIR /opt/club-service
COPY pom.xml .
# skini ovisnosti
RUN mvn dependency:go-offline
COPY ./src ./src
RUN mvn clean package -Dmaven.test.skip=true

# mala JRE slika samo s prevedenom aplikacijom
FROM eclipse-temurin:25-jre-alpine
WORKDIR /opt/club-service
COPY --from=build-stage /opt/club-service/target/club-service.jar ./app.jar
EXPOSE 8080 8081
ENTRYPOINT ["java","-jar","app.jar"]
