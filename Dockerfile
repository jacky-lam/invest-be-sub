# Running app as single jar file (doesnt work, need to change FROM to use maven:3-jdk-11?)
#FROM openjdk:11-jdk-alpine
## RUN addgroup -S spring && adduser -S spring -G spring
## USER spring:spring
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
##ARG DEPENDENCY=target/dependency
##COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
##COPY ${DEPENDENCY}/META-INF /app/META-INF
##COPY ${DEPENDENCY}/BOOT-INF/classes /app
#ENTRYPOINT ["java","-jar","/app.jar"]




# Docker multi-stage build

## 1. Building the App with Maven
#FROM maven:3-jdk-11

#ADD . /investment
#WORKDIR /investment
#
## Just echo so we can see, if everything is there :)
#RUN ls -l
#
## Run Maven build
#RUN mvn clean install
#
## 2. Just using the build artifact and then removing the build-container
#FROM openjdk:11-jdk
#
#MAINTAINER Jacky Lam
#
#VOLUME /tmp
#
## Add Spring Boot app.jar to Container
#COPY --from=0 "/investment/target/investment-*-SNAPSHOT.jar" app.jar
#
## Fire up our Spring Boot app by default
#CMD [ "sh", "-c", "java $JAVA_OPTS -Dserver.port=8081  -DlogFileLocation=/logs/app.log  -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
#

FROM openjdk:11
RUN apt-get update && apt-get install -y maven
COPY . /app
RUN  cd /app && mvn package

#run the spring boot application
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Dblabla", "-jar","/app/target/investment-1.0-SNAPSHOT.jar"]
