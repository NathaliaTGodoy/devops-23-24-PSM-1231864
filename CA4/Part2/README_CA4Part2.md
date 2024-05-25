# Technical Report - Class Assignment 4 Part 2

## Index

- [Overview](#overview)
- [Description of the Requirements Implementation](#description-of-the-requirements-implementation)
  - [Setting up the environment](#setting-up-the-environment)
  - [Description of the tasks](#description-of-the-tasks)
  - [Step 1: Creating a Dockerfile for db](#step-1-creating-a-dockerfile-for-db)
  - [Step 2: Creating a Dockerfile for web](#step-2-creating-a-dockerfile-for-web)
  - [Step 3: Creating a docker-compose](#step-3-creating-a-docker-compose)
  - [Step 4: Test the solution](#step-4-test-the-solution)
  - [Step 5: Publish the images db and web](#step-5-publish-the-images-db-and-web)
  - [Step 6: Use a volume with the db container](#step-6-use-a-volume-with-the-db-container)
- [Issues](#issues)
- [Conclusion](#conclusion)

## Overview
The goal of Part 2 of this assignment is to use Docker to set up a containerized environment using docker compose. 
Specifically to execute your version of the gradle version of the spring basic tutorial application.

## Description of the Requirements Implementation
### Setting up the environment
- For this assignment, first it is necessary to install [Docker Desktop](https://www.docker.com/products/docker-desktop/).
- To push the created image to your Docker Hub account first you need to sign in to your account. You can associate the
  Docker Hub  with your GitHub account.

### Description of the Tasks
- To complete this assignment, you need to produce a solution similar to the CA3/Part2 but now using Docker instead of Vagrant.
- Use docker-compose to produce 2 services/containers: 
  - web: this container is used to run Tomcat and the spring application 
  - db: this container is used to execute the H2 server database
- Publish the images (db and web) to Docker Hub (https://hub.docker.com)
- Use a volume with the db container to get a copy of the database file by using the exec to run a shell in the container 
and copying the database file to the volume.

### Step 1: Creating a Dockerfile for db
- Create a Dockerfile with the following content:
```bash
    FROM openjdk:17-jdk-slim

    LABEL authors="nathaliagodoy"

    RUN apt-get update && \
        apt-get install -y openjdk-17-jdk-headless wget unzip

    RUN mkdir -p /usr/src/app

    WORKDIR /usr/src/app/

    # Download H2 Database and run it
    RUN wget https://repo1.maven.org/maven2/com/h2database/h2/1.4.200/h2-1.4.200.jar -O /opt/h2.jar

    EXPOSE 8082 9092

    # Start H2 Server
    CMD ["java", "-cp", "/opt/h2.jar", "org.h2.tools.Server", "-web", "-webAllowOthers", "-tcp", "-tcpAllowOthers", "-ifNotExists"]
```

### Step 2: Creating a Dockerfile for web
- Create a Dockerfile with the following content:
```bash
    FROM tomcat:10-jdk17-openjdk-slim

    LABEL authors="nathaliagodoy"

    # Install necessary packages
    RUN apt-get update && apt-get install -y \
       iputils-ping avahi-daemon libnss-mdns unzip wget git

    # Clone the repository
    RUN git clone https://github.com/NathaliaTGodoy/devops-23-24-PSM-1231864.git /usr/src/app

    # Navigate to the project directory and build the project
    WORKDIR /usr/src/app/CA2/Part2/react-and-spring-data-rest-basic

    RUN chmod u+x gradlew

    RUN ./gradlew build

    # Copy the built WAR file to Tomcat webapps directory
    RUN cp ./build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps

    # Expose port
    EXPOSE 8080

    # Start Tomcat
    CMD ["catalina.sh", "run"]
```

### Step 3: Creating a docker-compose
```bash
services:
  db:
    build:
      context: .
      dockerfile: DockerfileDB
    ports:
      - "8082:8082"
      - "9092:9092"
    networks:
      default:
        ipv4_address: 192.168.56.11

  web:
    build:
      context: .
      dockerfile: DockerfileWeb
    ports:
      - "8080:8080"
    networks:
      default:
        ipv4_address: 192.168.56.10
    depends_on:
      - "db"

networks:
  default:
    ipam:
      driver: bridge
      config:
        - subnet: 192.168.56.0/24
```

### Step 4: Test the solution
- Execute `docker-compose up --build`
- You can test the implemented solution in the browser:
  1. Open the spring web application using:
  * http://localhost:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT
  2. Open the H2 console using the following url:
  * http://localhost:8082
  3. For the connection string use: jdbc:h2:tcp://192.168.56.11:9092/./jpadb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
  4. For the username use: sa
  5. The password will be blank

### Step 5: Publish the images (db and web)
- Publish the images (db and web) to [Docker Hub](https://hub.docker.com) using the following commands:
 ```bash
    docker tag part2-db:latest nathaliatgodoy/part2-db:latest
    docker push nathaliatgodoy/part2-db:latest
    docker tag part2-web:latest nathaliatgodoy/part2-web:latest
    docker push nathaliatgodoy/part2-web:latest
 ```

### Step 6: Use a volume with the db container
- Update the docker-compose to use a volume with the db container to get a copy of the database file:
```bash
services:
  db:
    build:
      context: .
      dockerfile: DockerfileDB
    ports:
      - "8082:8082"
      - "9092:9092"
    networks:
      default:
        ipv4_address: 192.168.56.11
    volumes:
      - db_data:/usr/src/app/data

  web:
    build:
      context: .
      dockerfile: DockerfileWeb
    ports:
      - "8080:8080"
    networks:
      default:
        ipv4_address: 192.168.56.10
    depends_on:
      - "db"

networks:
  default:
    ipam:
      driver: bridge
      config:
        - subnet: 192.168.56.0/24

volumes:
  db_data:
```

- Using volumes in Docker Compose allows data to persist even if the container is deleted. It duplicates the file that 
stores the database data to a designated folder (/usr/src/app/data) on your computer. Other containers can also use this 
volume to access the data.
- Test again the application by using the steps described in [Step 5](#step-5-publish-the-images-db-and-web).

## Issues
- An encountered issue was related to the networks setting. The containers were well created and running. However, when
the containers were deleted and the command `docker-compose up --build` was executed, the following error message appeared:
"Error response from daemon: plugin "bridge" not found". To resolve this problem, the "bridge" definition in docker-compose
networks needed to be replaced by "default".
- Another conflict of Ports were faced. To resolve this conflict, it was necessary to use the following commands, where
the PID needs to be replaced by the actual process ID.
 ```bash
    sudo lsof -i :8080
    sudo kill -9 <PID>
 ```

## Conclusion
In this assignment, we containerized a Spring application and an H2 database using Docker. We created Dockerfiles for 
both services, orchestrated them with docker-compose, and verified their functionality. The Docker images were published 
to Docker Hub for easy sharing and reuse. Additionally, we used Docker volumes to persist database data, demonstrating 
Docker's efficiency in managing and deploying applications consistently.

