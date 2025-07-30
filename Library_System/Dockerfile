FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/Library_System-1.0-SNAPSHOT.jar app.jar
COPY .env .env
CMD ["java", "-jar", "app.jar"]
