# Use an official OpenJDK runtime as a parent image
FROM maven:3.8.5-openjdk-17 AS build

# Copy the JAR file from the target directory to the container
COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/SmartGate-0.0.1-SNAPSHOT.jar SmartGate.jar
EXPOSE 8080
ENTRYPOINT [ "java","-jar","SmartGate.jar" ]