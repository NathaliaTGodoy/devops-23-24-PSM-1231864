# Technical Report - Class Assignment 2 Part 1

## Index

- [Overview](#overview)
- [Description of the Requirements Implementation](#description-of-the-requirements-implementation)
    - [Step 1: Download the gradle basic demo](#step-1-download-the-gradle-basic-demo)
    - [Step 2: Read the instructions and experiment ](#step-2-read-the-instructions-and-experiment)
    - [Step 3: Add a new task to execute the server](#step-3-add-a-new-task-to-execute-the-server)
    - [Step 4: Add a unit test and update gradle to execute the test](#step-4-add-a-unit-test-and-update-the-gradle-to-execute-the-test)
    - [Step 5: Add a new task of type Copy as a backup purpose](#step-5-add-a-new-task-of-type-copy-as-a-backup-purpose)
    - [Step 6: Add a new task of type Zip as a archive purpose](#step-6-add-a-new-task-of-type-zip-as-a-archive-purpose)

## Overview
The objective of Part 1 of this assignment is to familiarize ourselves with Gradle, a powerful build automation tool, 
through a straightforward example application. In this section, we will outline the tasks involved in completing Part 1 
and provide an overview of the steps required to accomplish them.

## Description of the Requirements Implementation

### Step 1: Download the gradle basic demo
- Begin by cloning the example application provided at `https://bitbucket.org/pssmatos/gradle_basic_demo/.` and committing it to your repository, 
creating a dedicated folder for Part 1 of the assignment.
  1. `git clone https://bitbucket.org/pssmatos/gradle_basic_demo/.`
  2. `git rm -rf .git`
  3. `git add gradle_basic_demo`
  4. `git commit -m "Message of commiting"`
  5. `git push`

### Step 2: Read the instructions and experiment
- Take the time to review the instructions detailed in the `README.md` file included with the example application. Experiment with the functionalities 
of the application to gain a deeper understanding of its structure and purpose.
- Try to use the following commands:
  1. To build a .jar file with the application: `% ./gradlew build`
  2. Open a terminal and execute the following command from the project's root directory: `% java -cp build/libs/basic_demo-0.1.0.jar basic_demo.ChatServerApp <server port>`.
     Substitute <server port> by a valid por number, e.g. 59001.
  3. Open another terminal and execute the following gradle task from the project's root directory: `% ./gradlew runClient`
     The above task assumes the chat server's IP is "localhost" and its port is "59001". If you wish to use other parameters please edit the runClient task in the "build.gradle" file in the project's root directory.
     To run several clients, you just need to open more terminals and repeat the invocation of the runClient gradle task

### Step 3: Add a new task to execute the server
- Extend the functionality of the application by adding a new task to execute the server. This task will streamline the process of launching the server
  component of the application.
- Try to use the following commands:
  1. Add the following task in build.gradle
  ```java
    task runServer(type: JavaExec, dependsOn: classes) {
     group = "DevOps"
     description = "Launches a chat server on localhost:59001"
     classpath = sourceSets.main.runtimeClasspath
     mainClass = 'basic_demo.ChatServerApp'
     args '59001'
     }
  ```
  2. To execute the task implemented in item 1 use the command `% ./gradlew runServer`

### Step 4: Add a unit test and update the gradle to execute the test
- Enhance the reliability of the application by introducing a simple unit test. Update the Gradle script to enable the execution of this test, ensuring
  the integrity and correctness of the application's codebase.
- Try to use the following commands:
  1. Create a new class to add the unit test [AppTest](gradle_basic_demo/src/main/java/basic_demo/AppTest.java)
  2. Update the dependencies in build.gradle `implementation 'junit:junit:4.12'`
  3. Assure that the version of the Java is compatible

### Step 5: Add a new task of type Copy as a backup purpose.
- Integrate a new task of type Copy to facilitate the creation of backups for the application's source code. This task will copy the contents of the src
  folder to a new folder backup
- Try to use the following commands:
  1. Add the following task in build.gradle, including the creation of the destination directory as `${buildDir}/backup/src`.
  ```java
     task backupSources(type: Copy) {
      group = 'Backup'
      description = 'Make a backup of the application sources'
      def sourceDir = file('src')
      def backupDir = file("${buildDir}/backup/src")
      from sourceDir into backupDir
     }
  ```
  2. To execute the task implemented in item 1 use the command `% ./gradlew backupSources`

### Step 6: Add a new task of type Zip as a archive purpose.
- Further enhance the manageability and portability of the application by implementing a task of type Zip. This task will package the contents of the src
  folder into a compressed zip archive, simplifying the distribution and sharing of the application's source code.
  1. Add the following task in build.gradle, indicating the destination zip file path as `${buildDir}/archives/sources.zip`.
  ```java
     task zipSources(type: Zip) {
      group = 'Archive'
      description = 'Creates a zip archive of the application sources'
      def sourceDir = file('src')
      def zipFile = file("${buildDir}/archives/sources.zip")
      from sourceDir
      archiveFileName = zipFile.name
      destinationDirectory = zipFile.parentFile
     }
  ```
  2. To execute the task implemented in item 1 use the command `% ./gradlew zipSources`

- Conclude Part 1 of the assignment by marking the repository with the tag ca2-part1, signifying the completion of the specified tasks within this phase of the project.
