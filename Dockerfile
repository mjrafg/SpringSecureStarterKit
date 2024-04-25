FROM openjdk:17-slim

WORKDIR /app

COPY target/SpringBootStarterKit.jar /app/myapp.jar

EXPOSE 8085

# Run the jar file
CMD ["java", "-jar", "/app/myapp.jar"]
