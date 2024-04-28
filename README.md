# Getting Started with Spring Secure Starter Kit

## Prerequisites

Ensure you have the following installed:
- Java 17
- Maven
- PostgreSQL
- Docker (optional for Docker deployment)

## Configuration

Modify the `application.yml` to include your PostgreSQL credentials and adjust other settings as necessary.

## Build and Run

### Building the Application

To compile the project and build the executable JAR file, execute:

```bash
mvn clean install
```

### Running Locally

Start the application using:

```bash
java -jar target/spring-boot-secure-starter-kit.jar
```

### Using Docker (Optional)

To build the Docker image and run it as a container:

1.Build the Docker image with:

```bash
docker build -t spring-secure-starter-kit .
```

2.Run the Docker container with:

```bash
docker run -p 8085:8085 spring-secure-starter-kit
```

This will start the application and make it accessible on `http://localhost:8085`.

### Accessing Swagger UI
Once the application is running, you can access the Swagger UI to interact with the API by navigating to:
[http://localhost:8085/swagger-ui/index.html](http://localhost:8085/swagger-ui/index.html)


## Contact

[mjrafg@yahoo.com](mailto:mjrafg@yahoo.com)

