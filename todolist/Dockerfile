# Build stage
FROM maven:3.9.4-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:23-slim
WORKDIR /app
COPY --from=build /app/target/todolist-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh","-c","java \
  -Dspring.datasource.url=${DB_URL} \
  -Dspring.datasource.username=${DB_USER} \
  -Dspring.datasource.password=${DB_PASSWORD} \
  -jar app.jar"]
