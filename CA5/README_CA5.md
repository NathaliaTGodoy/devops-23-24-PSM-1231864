# Technical Report - Class Assignment 5

## Index

- [Overview](#overview)
- [Description of the Requirements Implementation](#description-of-the-requirements-implementation)
  - [Setting up the environment](#setting-up-the-environment)
  - [Description of tasks](#description-of-tasks)
- [Task 1: Create a pipeline for "gradle basic demo"](#task-1-create-a-pipeline-for-gradle-basic-demo)
  - [Step 1: Create a new pipeline in Jenkins for Task 1](#step-1-create-a-new-pipeline-in-jenkins-for-task-1)
  - [Step 2: Create a Jenkinsfile for Task 1](#step-2-create-a-jenkins-file-for-task-1)
  - [Step 3: Run the pipeline for Task 1](#step-3-run-the-pipeline-for-task-1)
- [Task 2: Create a pipeline for "tutorial spring boot application"](#task-2-create-a-pipeline-for-tutorial-spring-boot-application)
  - [Step 1: Create a new pipeline in Jenkins for Task 2](#step-1-create-a-new-pipeline-in-jenkins-for-task-2)
  - [Step 2: Create a Jenkinsfile for Task 2](#step-2-create-a-jenkins-file-for-task-2)
  - [Step 3: Install plugins on Jenkins for Task 2](#step-3-install-plugins-on-jenkins-for-task-2)
  - [Step 4: Run the pipeline for Task 1](#step-4-run-the-pipeline-for-task-2)
- [Conclusion](#conclusion)

## Overview
The goal of this assignment is to set up CI/CD pipelines using Jenkins running in a Docker container. The first pipeline
should be for the ”gradle basic demo” (CA2Part1) and the second pipeline of tutorial spring boot application 
”react and spring data rest basic” (CA2Part2).

## Description of the Requirements Implementation
### Setting up the environment
- First you need to install Jenkins. You can do that by using a pre-built docker image with Jenkins:
```bash
    docker run -d -p 8080:8080 -p 50000:50000 -v jenkins-data:/var/jenkins_home --name=jenkins jenkins/jenkins:lts-jdk11
```
- Open [Jenkins](http://localhost:8080) and insert your credentials. You can find the default password in
  `/var/jenkins_home/secrets/initialAdminPassword` in docker console.
- Install necessary plugins such as Git, Gradle, NodeJS and HTMLPublisher.

### Description of tasks
- The first task aims to create a new pipeline job in Jenkins for the ”gradle basic demo” (developed in CA2, Part1).
- The second task consists in creating a pipeline in Jenkins to build the tutorial spring boot application, gradle 
”basic” version (developed in CA2, Part2).

## Task 1: Create a pipeline for ”gradle basic demo”
### Step 1: Create a new pipeline in Jenkins for Task 1
- In Jenkins, clink on "New Item" to create a new pipeline job. Enter a name for your job, for instance CA5Part1. Select 
"Pipeline" and then "OK".
- To configure the pipeline, in the pipeline section select "Pipeline script from SCM", then choose "Git" as the SCM and
insert your public repository URL. In the Script Path you should specify the relative path to the Jenkinsfile inside 
your repository (e.g. `CA5/Jenkinsfile`).

### Step 2: Create a Jenkins file for Task 1
- In the repository that was indicated when configuring the pipeline, create a new `Jenkinsfile`.
- You should define the following stages: 
  - Checkout: To check out the code form the repository
  - Assemble: To compile and produces the archive files with the application.
  - Test: Executes the Unit Tests and publish in Jenkins the Test results.
  - Archive: Archives in Jenkins the archive files (generated during Assemble)

```bash
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
- Remember to add some unit tests to the project, if you do not have it. Another point to verify is if the tests are in
the correct directory.

### Step 3: Run the pipeline for Task 1
- In the created job for the pipeline CA5Part1, click on "Build Now".
- In can monitor the build progress and see if all stages were successfully.
- You can check the console output for detailed logs.


## Task 2: Create a pipeline for tutorial spring boot application
### Step 1: Create a new pipeline in Jenkins for Task 2
- In Jenkins, clink on "New Item" to create a new pipeline job. Enter a name for your job, for instance CA5Part2. Select
  "Pipeline" and then "OK".
- To configure the pipeline, in the pipeline section select "Pipeline script from SCM", then choose "Git" as the SCM and
  insert your public repository URL. In the Script Path you should specify the relative path to the Jenkinsfile inside
  your repository (e.g. `CA5/Jenkinsfile2`).

### Step 2: Create a Jenkins file for Task 2
- In the repository that was indicated when configuring the pipeline, create a new `Jenkinsfile2`.
- You should define the following stages:
  - Checkout: To check out the code form the repository
  - Assemble: To compile and produces the archive files with the application.
  - Test: Executes the Unit Tests and publish in Jenkins the Test results.
  - Javadoc. Generates the javadoc of the project and publish it in Jenkins.
  - Archive: Archives in Jenkins the archive files (generated during Assemble)
  - Publish Image: Generate a docker image with Tomcat and the war file and publish it in the Docker Hub.

```bash
    pipeline {
    agent any

    environment {
        GRADLE_HOME = "${env.WORKSPACE}/CA2/Part2/react-and-spring-data-rest-basic"
        JAVA_HOME = tool 'jdk-17' // Adjust the JDK tool name as per your Jenkins configuration
    }

    options {
        skipDefaultCheckout() // Skip default checkout to manually configure git step
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
                echo 'Generating Javadoc...'
                dir("${GRADLE_HOME}") {
                    sh './gradlew javadoc'
                }
            }
            post {
                success {
                    publishHTML(target: [
                        reportName: 'Javadoc',
                        reportDir: 'build/docs/javadoc',
                        reportFiles: 'index.html',
                        keepAll: true,
                        alwaysLinkToLastBuild: true,
                        allowMissing: false
                    ])
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

        stage('Docker Image') {
                        steps {
                            script {
                                dir("${GRADLE_HOME}") {
                                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_HUB_USER', passwordVariable: 'DOCKER_HUB_PASS')]) {
                                        def customImage = docker.build("nathaliatgodoy/devops:ca5")
                                        docker.withRegistry('https://index.docker.io/v1/', 'docker-hub-credentials') {
                                            customImage.push()

                                            //docker.build("my-image:${env.BUILD_ID}")
                            }
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
  - Search for "HTML Publisher plugin" and install it
  - Search for "Docker pipeline" and install it

### Step 4: Run the pipeline for Task 2
- In the created job for the pipeline CA5Part1, click on "Build Now".
- In can monitor the build progress and see if all stages were successfully.
- You can check the console output for detailed logs.


## Conclusion
In this assignment, CI/CD pipelines were set up using Jenkins in Docker containers. The first pipeline was for the "gradle 
basic demo" (CA2Part1), while the second was for the "react and spring data rest basic" tutorial application (CA2Part2). 
Each pipeline involved creating a Jenkins job, configuring it to fetch code from a Git repository, defining stages for 
building, testing, archiving, and optionally deploying the application, which contributed to enrich our knowledge in CI/CD
pipelines and the Jenkins as a tool to achieve that.

