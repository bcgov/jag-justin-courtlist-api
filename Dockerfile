#############################################################################################
###              Stage where Docker is building spring boot app using maven               ###
#############################################################################################
FROM maven:3.8.3-openjdk-17 AS build

WORKDIR /

COPY . .

RUN mvn -B clean package \
        -Dmaven.test.skip=true

#############################################################################################
### Stage where Docker is running a java process to run a service built in previous stage ###
#############################################################################################
FROM eclipse-temurin:17-jre-alpine

# fix CVE-2025-65018, CVE-2025-64720, CVE-2025-66293, CVE-2025-59375, CVE-2025-9230
RUN apk update \
    && apk add --upgrade --no-cache libexpat \
    && apk add --upgrade --no-cache libpng \
    && apk add --upgrade --no-cache openssl

ARG SERVICE_NAME=justin-court-list-api

COPY --from=build ./target/${SERVICE_NAME}-*.jar /app/service.jar

CMD ["java", "-jar", "/app/service.jar"]
#############################################################################################
