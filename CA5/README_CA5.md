# Technical Report - Class Assignment 5

## Index

- [Overview](#overview)
- [Description of the Requirements Implementation](#description-of-the-requirements-implementation)
  - [Setting up the environment](#setting-up-the-environment)

- [Alternative Solution: Kubernetes](#alternative-solution-kubernetes)
- [Conclusion](#conclusion)

## Overview
The first goal of this assignment is to practice with Jenkins using the ”gradle basic demo” project that should already 
be present in the student’s individual repository. The goal is to create a very simple pipeline similar to the example from the lectures.

## Description of the Requirements Implementation
### Setting up the environment
The goal is to create a very simple pipeline similar to the example from the lectures, see instructions on page 6.
Note that this project should already be present in a folder inside your individual repository!
When configuring the pipeline, in the Script Path field, you should specify the relative path (inside your repository) for the Jenkinsfile. For instance, ’ca2/part1/gradle-basic-demo/Jenkinsfile’.

You should define the following stages in your pipeline: Checkout. To checkout the code form the repository
Assemble. Compiles and Produces the archive files with the application. Do not use the build task of gradle (because it also executes the tests)!
Test. Executes the Unit Tests and publish in Jenkins the Test results. See the junit step for further information on how to archive/publish test results.
Do not forget to add some unit tests to the project (maybe you already have done it). Archive. Archives in Jenkins the archive files (generated during Assemble)

After practicing with the Create ”gradle basic demo” project, you should create a pipeline in Jenkins to build the tutorial spring boot application, gradle ”basic” version (developed in CA2, Part2)
3 You should define the following stages in your pipeline: Checkout. To checkout the code from the repository
Assemble. Compiles and Produces the archive files with the application. Do not use the build task of gradle (because it also executes the tests)!
Test. Executes the Unit Tests and publish in Jenkins the Test results. See the junit step for further information on how to archive/publish test results.
Do not forget to add some unit tests to the project (maybe you already have done it). Javadoc. Generates the javadoc of the project and publish it in Jenkins. See the
publishHTML step for further information on how to archive/publish html reports. Archive. Archives in Jenkins the archive files (generated during Assemble, i.e., the war file)
Publish Image. Generate a docker image with Tomcat and the war file and publish it in the Docker Hub.
See https://jenkins.io/doc/book/pipeline/docker/, section Building Containers.
See also the next slide for an example. In the example, docker.build will build an image from a Dockerfile in the same folder as the Jenkinsfile. The tag for the image will be the job build number of Jenkins.
4 At the end of this assignment mark your repository with the tag ca5.


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


## Alternative Solution: Kubernetes
Kubernetes is a robust container orchestration system ideal for large-scale applications, providing features beyond 
Docker Compose. It offers load balancing to distribute traffic across containers, dynamic scaling to adjust container 
numbers based on demand, and self-healing to automatically restart failed containers. Additionally, Kubernetes manages 
storage, ensuring efficient use and mounting of volumes. It supports automated rollouts and rollbacks for smooth updates, 
simplifies container communication, and securely handles sensitive information and configurations. Kubernetes also excels
in job scheduling and batch processing.
While Kubernetes introduces complexity, this is necessary for managing large applications with unpredictable traffic 
and load. Its capabilities ensure high availability, efficient resource utilization, consistency, and flexibility, making 
it essential for modern cloud-native development.

## Conclusion
In this assignment, we containerized a Spring application and an H2 database using Docker. We created Dockerfiles for 
both services, orchestrated them with docker-compose, and verified their functionality. The Docker images were published 
to Docker Hub for easy sharing and reuse. Additionally, we used Docker volumes to persist database data, demonstrating 
Docker's efficiency in managing and deploying applications consistently.

