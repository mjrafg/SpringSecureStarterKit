FROM openjdk:17-slim

LABEL maintainer="mjrafg@yahoo.com"


WORKDIR /app

COPY target/spring-boot-secure-starter-kit.jar /app/myapp.jar

EXPOSE 8085

CMD ["java", "-jar", "/app/myapp.jar"]
