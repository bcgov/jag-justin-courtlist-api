#############################################################################################
###              Stage where Docker is building spring boot app using maven               ###
#############################################################################################
FROM maven:3.9.4-eclipse-temurin-17 as build

WORKDIR /

COPY . .

RUN mvn -B clean package \
        -Dmaven.test.skip=true

#############################################################################################
### Stage where Docker is running a java process to run a service built in previous stage ###
#############################################################################################
FROM eclipse-temurin:17-jdk

ARG SERVICE_NAME=justin-court-list-api

COPY --from=build ./target/${SERVICE_NAME}-*.jar /app/service.jar

CMD ["java", "-jar", "/app/service.jar"]
#############################################################################################
