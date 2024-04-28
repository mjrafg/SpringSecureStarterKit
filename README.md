## About This Repository

This repository has been set up as a **Template Repository** to help you quickly start projects based on the Spring Secure Starter Kit. It includes configurations for JWT, PostgreSQL, JPA, and Docker, making it ideal for building secure Spring Boot applications.

## Using This Template

To create a new repository based on this template:

1. Click the **Use this template** button at the top of this GitHub repository page.
2. Set the owner for the repository you are creating and enter a repository name.
3. Choose to include all branches or just the main branch as needed.
4. Click **Create repository from template** to start with the new repository setup.

## Getting Started

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

