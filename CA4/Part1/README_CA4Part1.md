# Technical Report - Class Assignment 4 Part 1

## Index

- [Overview](#overview)
- [Description of the Requirements Implementation](#description-of-the-requirements-implementation)
    - [Setting up the environment](#setting-up-the-environment) 
    - [Version 1: Building the Chat Server inside the Dockerfile](#version-1-building-the-chat-server-inside-the-dockerfile)
    - [Version 2: Building the Chat Server with a generated jar](#version-2-building-the-chat-server-with-a-generated-jar)
- [Conclusion](#conclusion)

## Overview
The goal of Part 1 of this assignment is to practice using Docker by creating Docker images and running containers. 
Specifically, using the chat application from CA2-Part1, packaging the chat server into a container and executing it.

## Description of the Requirements Implementation
### Setting up the environment
- For this assignment, first it is necessary to install [Docker Desktop](https://www.docker.com/products/docker-desktop/).
- You could log in to your Docker Hub account using the following command:
```bash
    docker login
```

### Description of the Tasks
- To complete this assignment, you need to:
  - Create a docker image (Dockerfile) to execute the chat server using the CA2/Part1, which is based on the 
[Gradle Application](https://bitbucket.org/pssmatos/gradle_basic_demo/).
  - Tag the Docker image and publish it on Docker Hub.
  - Ensure that you can execute the chat client on your host computer and connect to the chat server running in the container
- To explore the concept of docker images you should create two versions of your solution:
  - Version 1: Build the chat server inside Docker by executing the command `./gradlew build`. You can achieve this by 
either using the CA2/Part1 application directly or by cloning the [Gradle Application](https://bitbucket.org/pssmatos/gradle_basic_demo/)
into the directory of this assignment.
  - Version 2: Build the chat server on the host computer and then copy the JAR file into the Docker image.

### Version 1: Building the Chat Server inside the Dockerfile
#### 1. Create a Dockerfile with the following content:
```bash
    # Use the official OpenJDK image with Java 21
    FROM gradle:jdk11 as builder

    LABEL authors="nathaliagodoy"

    # Copy the project files to the container
    COPY CA2/Part1/gradle_basic_demo /app/gradle_basic_demo

    # Change to the project directory
    WORKDIR /app/gradle_basic_demo

    # Build the project using Gradle
    RUN chmod u+x ./gradlew
    RUN ./gradlew build

    # Define the command to run the application
    CMD ["./gradlew", "runServer"]
```

#### 2. Build the Docker image
- In your root, run the following command to build the Docker image:
```bash
    docker build -t chat-server -f CA4/Part1/DockerfileVersion1 .
```

#### 3. Tag and Publish the Image on Docker Hub
- Tag and push the image to Docker Hub:

```bash
docker tag chat-server chat-server:ca4-part1-v1
docker push chat-server:ca4-part1-v1
```

#### 4. Run the Docker Container
- Run the container from the created image to ensure everything is working correctly:
```bash
    docker run --name chat-version1 chat-server:ca4-part1-v1 
```

### Version 2: Building the Chat Server with a generated jar
#### 1. Create a Dockerfile with the following content:
```bash
    # Use the official Tomcat image with Java 17
    FROM openjdk:21-jdk-slim

    LABEL authors="nathaliagodoy"

    # Copy the JAR file into the container at /app
    COPY /CA2/Part1/gradle_basic_demo/build/libs/basic_demo-0.1.0.jar /app/basic_demo-0.1.0.jar

    ENTRYPOINT ["java", "-cp", "/app/basic_demo-0.1.0.jar", "basic_demo.ChatServerApp", "59001"]
```

#### 2. Build the Docker image
- In your root, run the following command to build the Docker image:
```bash
    docker build -t chat-server:ca4-part1-v2 -f CA4/Part1/DockerfileVersion2 .
```

#### 3. Tag and Publish the Image on Docker Hub
- Tag and push the image to Docker Hub:

```bash
docker tag chat-server chat-server:ca4-part1-v2
docker push chat-server:ca4-part1-v2
```

#### 4. Run the Docker Container
- Run the container from the created image to ensure everything is working correctly:
```bash
    docker run --name chat-version2 chat-server:ca4-part1-v2 
```

## Conclusion
In this assignment, we practiced using Docker to containerize a chat server application. We created two versions of the 
Docker image: one building the server inside the Dockerfile and one using a pre-built JAR file. We tagged and published 
the images on Docker Hub, demonstrating the benefits of Docker in simplifying deployment and ensuring consistency across 
environments.





