# Etapa 1: compila DAO y MVC con Maven
FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: corre el WAR resultante en Tomcat
FROM tomcat:9.0-jdk11-temurin
LABEL authors="Lucio"
RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY --from=build /app/MVC/target/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]