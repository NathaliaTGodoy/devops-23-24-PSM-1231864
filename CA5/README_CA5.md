# Technical Report - Class Assignment 5

## Index

- [Overview](#overview)
- [Task 1: Create a pipeline for "gradle basic demo"](#task-1-create-a-pipeline-for-gradle-basic-demo)
  - [Setting up the environment for Task 1](#setting-up-the-environment-for-task-1)
  - [Step 1: Create a new pipeline in Jenkins for Task 1](#step-1-create-a-new-pipeline-in-jenkins-for-task-1)
  - [Step 2: Create the Jenkinsfile for Task 1](#step-2-create-the-jenkinsfile-for-task-1)
  - [Step 3: Run the pipeline for Task 1](#step-3-run-the-pipeline-for-task-1)
- [Task 2: Create a pipeline for "tutorial spring boot application"](#task-2-create-a-pipeline-for-tutorial-spring-boot-application)
  - [Setting up the environment for Task 2](#setting-up-the-environment-for-task-2)
  - [Step 1: Create a new pipeline in Jenkins for Task 2](#step-1-create-a-new-pipeline-in-jenkins-for-task-2)
  - [Step 2: Create the Jenkinsfile for Task 2](#step-2-create-the-jenkinsfile-for-task-2)
  - [Step 3: Install plugins on Jenkins for Task 2](#step-3-install-plugins-on-jenkins-for-task-2)
  - [Step 4: Add the Docker Hub credentials on Jenkins for Task 2](#step-4-add-the-docker-hub-credentials-on-jenkins-for-task-2)
  - [Step 5: Run the pipeline for Task 2](#step-5-run-the-pipeline-for-task-2)
- [Conclusion](#conclusion)

## Overview
The goal of this assignment is to set up CI/CD pipelines using [Jenkins](https://www.jenkins.io) running in a Docker 
container. The first pipeline should be for the ”gradle basic demo” (CA2Part1) and the second pipeline should be for 
tutorial spring boot application ”react and spring data rest basic” (CA2Part2).

## Task 1: Create a pipeline for ”gradle basic demo”
The first task aims to create a new pipeline job in Jenkins for the ”gradle basic demo” (developed in CA2, Part1).

### Setting up the environment for Task 1
- First you need to install Jenkins. You can do that by using a pre-built docker image with Jenkins:
```bash
    docker run -d -p 8080:8080 -p 50000:50000 -v jenkins-data:/var/jenkins_home --name=jenkins jenkins/jenkins:lts-jdk17
```
- Open [Jenkins](http://localhost:8080) and insert your credentials. The default password can be found at
`/var/jenkins_home/secrets/initialAdminPassword` in docker console.
- Install necessary plugins such as Git, Gradle, NodeJS and HTMLPublisher.

### Step 1: Create a new pipeline in Jenkins for Task 1
- In Jenkins, click on "New Item" to create a new pipeline job. Enter a name for your job, for instance CA5Task1. Select 
"Pipeline" and then "OK".
- To configure the pipeline, in the pipeline section select "Pipeline script from SCM", then choose "Git" as the SCM and
insert your public repository URL (e.g. `https://github.com/NathaliaTGodoy/devops-23-24-PSM-1231864.git`). 
In the Script Path you should specify the relative path to the Jenkinsfile inside your repository (e.g. `CA5/Jenkinsfile`).
- Given that our repository is public, credentials are unnecessary. However, for a private repository, we would utilize 
Jenkins' credentials manager to incorporate them.

### Step 2: Create the Jenkinsfile for Task 1
- In the repository that was indicated when configuring the pipeline, create a new `Jenkinsfile`.
- You should define the following stages: 
  - Checkout: To check out the code form the repository;
  - Assemble: To compile and produces the archive files with the application;
  - Test: Executes the Unit Tests and publish in Jenkins the Test results;
  - Archive: Archives the generated files (during Assemble) in Jenkins.

```groovy
    pipeline {
    agent any

    environment {
        GRADLE_HOME = "${env.WORKSPACE}/CA2/Part1/gradle_basic_demo"
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out...'
                git url: 'https://github.com/NathaliaTGodoy/devops-23-24-PSM-1231864.git'
            }
        }

        stage('Assemble') {
            steps {
                echo 'Assembling...'
                dir("${GRADLE_HOME}") {
                    sh 'chmod +x gradlew'
                    sh './gradlew assemble'
                }
            }
        }

        stage('Test') {
            steps {
                echo 'Running tests...'
                dir("${GRADLE_HOME}") {
                    sh './gradlew test'
                    junit 'build/test-results/test/*.xml'
                }
            }
        }

        stage('Archive') {
            steps {
                echo 'Archiving...'
                dir("${GRADLE_HOME}") {
                    archiveArtifacts artifacts: 'build/libs/*.jar', allowEmptyArchive: true
                }
            }
        }
    }

    post {
       always {
          cleanWs()
       }
    }
}
```
- Ensure that unit tests are included in the project and placed in the correct directory.

### Step 3: Run the pipeline for Task 1
- In the created job for the pipeline CA5Task1, click on "Build Now" and monitor the build progress to ensure successful 
completion of all stages.
- Review the console output for detailed logs after running the pipeline.


## Task 2: Create a pipeline for tutorial spring boot application
 The second task consists of creating a pipeline in Jenkins to build the tutorial spring boot application, gradle
”basic” version (developed in CA2, Part2).

### Setting up the environment for Task 2
- The setup for the second task requires additional steps.

- Create a Docker Network for secure container communication.
```bash
  docker network create jenkins
```

- Run the Docker DinD Container. Docker-in-Docker (DinD) setup is crucial for Jenkins pipelines that build and deploy 
Docker images within a Dockerized environment. Use the following command to start the DinD container:

```bash
  docker run \
  --name jenkins-docker \
  --rm \
  --detach \
  --privileged \
  --network jenkins \
  --network-alias docker \
  --env DOCKER_TLS_CERTDIR=/certs \
  --volume jenkins-docker-certs:/certs/client \
  --volume jenkins-data:/var/jenkins_home \
  --publish 2376:2376 \
  docker:dind \
  --storage-driver overlay2
```
This setup includes assigning a container name, granting necessary privileges, connecting to a Docker network, and 
ensuring persistent storage for Jenkins data and Docker certificates. Exposing specific ports and configuring the storage 
driver optimizes Docker operations within Jenkins, facilitating a robust continuous integration and deployment (CI/CD) 
pipeline setup.

- Create a Dockerfile for Jenkins Blue Ocean:
```bash
  FROM jenkins/jenkins:2.452.2-jdk17

  USER root

  RUN apt-get update && apt-get install -y lsb-release
  RUN curl -fsSLo /usr/share/keyrings/docker-archive-keyring.asc \
  https://download.docker.com/linux/debian/gpg
  RUN echo "deb [arch=$(dpkg --print-architecture) \
  signed-by=/usr/share/keyrings/docker-archive-keyring.asc] \
  https://download.docker.com/linux/debian \
  $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list
  RUN apt-get update && apt-get install -y docker-ce-cli

  RUN jenkins-plugin-cli --plugins "blueocean:1.27.3 docker-workflow:1.28 json-path-api:2.8.0-5.v07cb_a_1ca_738c token-macro:400.v35420b_922dcb_ favorite:2.208.v91d65b_7792a_c github:1.39.0"
```

This Dockerfile customizes a Jenkins image by installing essential components and plugins for Docker-based CI/CD workflows. 
It starts with the base Jenkins image jenkins/jenkins:2.452.2-jdk17, switches to root user for administrative tasks, 
updates package lists, installs lsb-release for Linux Standard Base support, downloads Docker's GPG key for package 
verification, configures Docker repository settings for Debian-based systems, installs Docker CLI (docker-ce-cli) for 
Docker interaction within Jenkins, and finally uses jenkins-plugin-cli to install critical plugins blueocean for a modern 
Jenkins UI and docker-workflow for seamless Docker integration in Jenkins pipelines. This setup ensures Jenkins is 
equipped to manage Docker containers and streamline continuous integration and delivery processes effectively.

- Build Docker image based on the Dockerfile located in the current directory:
```bash
  docker build -t myjenkins-blueocean:2.452.2-2 .
```

This process effectively compiles the Dockerfile instructions into a usable Docker image that incorporates the specified 
configurations and dependencies, enabling the creation of a customized Jenkins instance ready for deployment and use in 
CI/CD pipelines.

- Run the Jenkins Container:
```bash
  docker run \
  --name jenkins-blueocean \
  --restart=on-failure \
  --detach \
  --network jenkins \
  --env DOCKER_HOST=tcp://docker:2376 \
  --env DOCKER_CERT_PATH=/certs/client \
  --env DOCKER_TLS_VERIFY=1 \
  --publish 8080:8080 \
  --publish 50000:50000 \
  --volume jenkins-data:/var/jenkins_home \
  --volume jenkins-docker-certs:/certs/client:ro \
  myjenkins-blueocean:2.452.2-1
```

This Docker command starts a Jenkins container named jenkins-blueocean, based on a specific image (myjenkins-blueocean:2.452.2-2). 
It runs Jenkins in the background, connects it to a Docker network named jenkins, and exposes ports 8080 and 50000 for 
web access and agent communication respectively. It also sets up necessary environment variables and mounts volumes for 
data persistence and Docker client certificates, ensuring Jenkins operates smoothly within a containerized environment 
suitable for CI/CD tasks.

- Open [Jenkins](http://localhost:8080) and insert your credentials. The default password can be found at
  `/var/jenkins_home/secrets/initialAdminPassword` in docker console or using the following command:
```bash
  docker exec jenkins-blueocean cat /var/jenkins_home/secrets/initialAdminPassword
```

### Step 1: Create a new pipeline in Jenkins for Task 2
- In Jenkins, click on "New Item" to create a new pipeline job. Enter a name for your job, for instance CA5Task2. Select
  "Pipeline" and then "OK".
- To configure the pipeline, in the pipeline section select "Pipeline script from SCM", then choose "Git" as the SCM and
  insert your public repository URL (e.g. `https://github.com/NathaliaTGodoy/devops-23-24-PSM-1231864.git`). 
In the Script Path you should specify the relative path to the Jenkinsfile inside your repository (e.g. `CA5/Jenkinsfile2`).

### Step 2: Create the Jenkinsfile for Task 2
- In the repository that was indicated when configuring the pipeline, create a new `Jenkinsfile2`.
- You should define the following stages:
  - Checkout: To check out the code form the repository;
  - Assemble: To compile and produces the archive files with the application;
  - Test: Executes the Unit Tests and publish in Jenkins the Test results;
  - Javadoc. Generates the javadoc of the project and publish it in Jenkins;
  - Archive: Archives the generated files (during Assemble) in Jenkins;
  - Publish Image: Generates a Docker image with Tomcat and the WAR file and publishes it in Docker Hub.

```groovy
    pipeline {
    agent any

    environment {
        GRADLE_HOME = "${env.WORKSPACE}/CA2/Part2/react-and-spring-data-rest-basic"
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out...'
                git url: 'https://github.com/NathaliaTGodoy/devops-23-24-PSM-1231864.git'
            }
        }

        stage('Assemble') {
            steps {
                echo 'Assembling...'
                dir("${GRADLE_HOME}") {
                    sh 'chmod +x gradlew'
                    sh './gradlew assemble'
                }
            }
        }

        stage('Test') {
            steps {
                echo 'Running tests...'
                dir("${GRADLE_HOME}") {
                    sh './gradlew test'
                    junit 'build/test-results/test/*.xml'
                }
            }
        }

        stage('Javadoc') {
                    steps {
                        echo 'Generating Javadocs...'
                        dir("${GRADLE_HOME}") {
                            sh './gradlew javadoc'
                        }
                        publishHTML(target: [
                            allowMissing: false,
                            alwaysLinkToLastBuild: true,
                            keepAll: true,
                            reportDir: 'CA2/Part2/react-and-spring-data-rest-basic/build/docs/javadoc',
                            reportFiles: 'index.html',
                            reportName: 'Javadoc'
                            ])
                    }
                }

        stage('Archive') {
                steps {
                    echo 'Archiving...'
                    dir("${GRADLE_HOME}") {
                        archiveArtifacts artifacts: 'build/libs/*.jar', allowEmptyArchive: true
                    }
                }
            }

        stage('Docker Image') {
                    steps {
                        echo 'Creating Dockerfile...'
                            script {
                                dir("${GRADLE_HOME}") {
                                    def dockerfileContent = """
                                    FROM tomcat:10-jdk17-openjdk-slim
                                    COPY build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war
                                    EXPOSE 8080
                                    CMD ["catalina.sh", "run"]
                                    """
                                    writeFile file: 'DockerfileCA2Part2', text: dockerfileContent
                                }
                            }
                        }
                    }

        stage('Publish Docker Image') {
            steps {
                echo 'Publishing Docker image...'
                    script {
                        dir("${GRADLE_HOME}") {
                            def dockerImage = docker.build("nathaliatiyo/devops:${env.BUILD_ID}", "-f DockerfileCA2Part2 .")
                            docker.withRegistry('https://index.docker.io/v1/', 'docker_id') {
                            dockerImage.push()
                            }
                        }
                    }
                }
            }
        }


    post {
        always {
            cleanWs()
        }
    }
}

```

### Step 3: Install plugins on Jenkins for Task 2
- To successfully complete those stages, it is necessary to install some plugins.
- Go to Manage Jenkins > Manage Plugins > Available
  - Search for "HTML Publisher plugin" and install it;
  - Search for "Docker Pipeline" and install it.

### Step 4: Add the Docker Hub credentials on Jenkins for Task 2
- Go to Jenkins > Manage Jenkins > Credentials
  - Click on the Jenkins store;
  - Click on Global credentials (unrestricted);
  - Click on Add Credentials;
  - Fill in the details by adding the Docker Hub Username, Password and the ID that should match the one indicated in
the Jenkinsfile, and save;
  - Add the credentials to the Jenkinsfile in the 'Publish Docker Image' stage.

```groovy
    dir("${GRADLE_HOME}") {
        def dockerImage = docker.build("nathaliatiyo/devops:${env.BUILD_ID}", "-f DockerfileCA2Part2 .")
        docker.withRegistry('https://index.docker.io/v1/', 'docker_id') {
            dockerImage.push()
        }
    }
```

### Step 5: Run the pipeline for Task 2
- In the created job for the pipeline CA5Task2, click on "Build Now" and monitor the build progress to ensure successful
completion of all stages.
- Review the console output for detailed logs after running the pipeline.
- This step was successful, as the published images can be found in [Docker Hub](https://hub.docker.com/repository/docker/nathaliatiyo/devops/general)


## Conclusion
In conclusion, this assignment showcased CI/CD pipelines for Gradle-based projects using Jenkins. Each pipeline handled 
crucial stages: checkout, build, testing, documentation generation, artifact archiving, and Docker image publishing. 
Jenkins, a powerful automation tool for continuous integration and delivery, orchestrated these steps seamlessly.

Jenkins automates tasks like building, testing, and deploying software, making it invaluable in modern DevOps workflows. 
By defining pipelines in Jenkinsfiles, teams can automate these processes to run sequentially or in response to specific 
triggers, ensuring efficient software delivery without manual intervention. Leveraging Docker for Jenkins setup enhances 
portability and efficiency, supporting agile development practices and facilitating reliable software updates.


