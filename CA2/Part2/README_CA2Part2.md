# Technical Report - Class Assignment 2 Part 2

## Index
- [Overview](#overview)
- [Description of the Requirements Implementation](#description-of-the-requirements-implementation)
    - [Step 1: Start a new Gradle Spring Boot project](#step-1-start-a-new-gradle-spring-boot-project)
    - [Step 2: Modify the Gradle Spring Boot project](#step-2-modify-the-gradle-spring-boot-project)
    - [Step 3: Add Gradle plugin frontend](#step-3-add-gradle-plugin-frontend)
    - [Step 4: Develop new tasks to copy and delete files using Gradle](#step-4-develop-new-tasks-to-copy-and-delete-files-using-gradle)
- [Alternative solution using Ant](#alternative-solution-using-ant)
    - [Analysis of Ant as an alternative solution](#analysis-of-ant-as-an-alternative-solution)
    - [Implementation of Ant as an alternative solution](#implementation-of-ant-as-an-alternative-solution)
        - [Step 1 - Alternative: Start a new Apache Ant project](#step-1---alternative-start-a-new-apache-ant-project)
        - [Step 2 - Alternative: Modify the Apache Ant project](#step-2---alternative-modify-the-apache-ant-project)
        - [Step 3 - Alternative: Define Ant build.xml file](#step-3---alternative-define-ant-buildxml-file)
        - [Step 4 - Alternative: Execute Ant build](#step-4---alternative-execute-ant-build)
- [Conclusion](#conclusion)

## Overview
The objective of Part 2 of this assignment was to convert the basic version of a Spring Boot application to [Gradle](https://gradle.org),
instead of Maven. To accomplish this goal, some modifications in the build scripts and an integration of frontend processing tools
was necessary. This section outlines the tasks involved and provides an overview of the steps required to complete them.

## Description of the Requirements Implementation

### Step 1: Start a new Gradle Spring Boot project
- Begin by creating a new branch called 'tut-basic-gradle' in the repository to work on the assignment. After creating the branch,
migrate to it, using the following commands:
  1. `git branch tut-basic-gradle`
  2. `git checkout tut-basic-gradle`
- Generate a new gradle spring boot project using the [Spring Initializr](https://start.spring.io) with the following dependencies to aligning the project with its requirements: 
Rest Repositories, Thymeleaf, JPA, H2;
- Extract the generated zip file inside the folder 'CA2/Part2/' of the repository, which will result in an empty Spring application that can be built using gradle;
- Check the available gradle tasks by executing `./gradlew tasks`.

### Step 2: Modify the Gradle Spring Boot project
- Delete the original 'src' folder in your IntelliJ repository;
- Copy the 'src' folder and all its subfolders from the basic folder of the tutorial (Class Assignment 1 Part 1) into this new folder;
- Copy [webpack.config.js](react-and-spring-data-rest-basic/webpack.config.js) and [package.json](react-and-spring-data-rest-basic%2Fpackage.json) files from Class Assignment 1 Part 1 to this new folder;
- Delete the 'src/main/resources/static/built/' folder to avoid conflicts with Gradle's build process;
- Change the imports of [Employee class](react-and-spring-data-rest-basic/src/main/java/com/greglturnquist/payroll/Employee.java) from *javax* to *jakarta*, as following:
   ```java
     import jakarta.persistence.Entity;
     import jakarta.persistence.GeneratedValue;
     import jakarta.persistence.Id;
     }
  ```
- Experiment the application by using `./gradlew bootRun`;
- Open the browser using http://localhost:8080. The page will be empty, since Gradle is missing the plugin for dealing with the frontend code.

### Step 3: Add Gradle plugin frontend
- Add the version of the gradle plugin 'org.siouan.frontend' in [build.gradle](react-and-spring-data-rest-basic/build.gradle) to manage the frontend https://github.com/Siouan/frontend-gradle-plugin.
The version needs to be compatible with Java version. For Java version 17, use the following:
   ```java
       id 'org.siouan.frontend-jdk17' version '8.0.0'
   ```
- Configure the plugin in the same [build.gradle](react-and-spring-data-rest-basic/build.gradle) file:
   ```java
      frontend {
         nodeVersion = '16.20.2'
         assembleScript = 'run build'
         cleanScript = 'run clean'
         checkScript = 'run check'
      }
  ```
- Update the scripts in [package.json](react-and-spring-data-rest-basic/package.json) to configure the execution of webpack:
   ```java
      "scripts": {
        "webpack": "webpack",
        "watch": "webpack --watch -d --output ./target/classes/static/built/bundle.js",
        "build": "npm run webpack",
        "check": "echo Checking frontend",
        "clean": "echo Cleaning frontend",
        "lint": "echo Linting frontend",
        "test": "echo Testing frontend"
      },
  ```
- Add a new configuration of package-manager in [package.json](react-and-spring-data-rest-basic/package.json):
   ```java
      "packageManager": "npm@9.6.7",
   ```
- Execute `./gradlew build`. The tasks related to the frontend are also executed, and the frontend code is generated.
- Execute the application by using `./gradlew bootRun`.

### Step 4: Develop new tasks to copy and delete files using Gradle
- Add a new task to [build.gradle](react-and-spring-data-rest-basic/build.gradle) to copy the generated Jar file to a folder named 'dist' located at the project root folder level:
   ```java
      task copyJarToDist(type: Copy) {
        from 'build/libs/'
        into 'dist'
        include '*.jar'
      }
   ```
- Add a task to [build.gradle](react-and-spring-data-rest-basic/build.gradle) to delete all the files generated by webpack. This new task should be executed automatically by gradle before the task clean:
   ```java
      task cleanWebpackFiles(type: Delete) {
         delete 'src/main/resources/static/built/'
      }
      clean.dependsOn cleanWebpackFiles
   ```
- Compile the project using `./gradlew build`;
- Experiment all the developed features with the command `./gradlew copyJarToDist` and `./gradlew cleanWebpackFiles`;
- Commit the code and merge into the master branch using the following commands:
  1. `git add .`
  2. `git commit -m "Message to commit"`
  3. `git push origin tut-basic-gradle`
  4. `git checkout master`
  5. `git merge --no-ff tut-basic-gradle`
  6. `git push`

## Alternative Solution Using Ant
### Analysis of ANT as an alternative solution
In considering an alternative solution for build automation, we evaluated [Apache Ant](https://ant.apache.org). Below is an analysis of how it compares to Gradle regarding build automation features:
1. Extensibility:
  - Gradle: Gradle offers high extensibility through its plugin ecosystem, allowing developers to easily add new functionality through custom plugins or tasks.
  - Apache Ant: Apache Ant provides extensibility through its 'build.xml' file, allowing developers to define custom tasks using Java or scripting languages like Groovy.
2. Usability:
  - Gradle: Gradle provides a user-friendly DSL (Domain Specific Language) for defining build scripts, making it relatively easy to understand and use.
  - Apache Ant: Apache Ant uses XML-based build files, which may be less intuitive for some developers compared to Gradle's DSL.
3. Dependency management:
  - Gradle: Gradle excels in managing project dependencies, offering robust tools for handling libraries and dependencies within the build process.
  - Apache Ant: Apache Ant does not have native dependency management. This can be achieved through external tools, adding complexity to the build process.

### Implementation of ANT as an alternative solution
#### Step 1 - Alternative: Start a new Apache Ant project
- Create a new branch called 'tut-basic-ant' in the repository to work on the alternative solution. After creating the branch, switch to it. Follow the same commands described in
[Step 1: Start a new Gradle Spring Boot project](#step-1-start-a-new-gradle-spring-boot-project) to complete this task;
- Set up a new Apache Ant project structure. You can create a new directory for this project;
- Apache Ant does not have built-in dependency management like Gradle or Maven. Therefore, manually download the necessary dependencies for your project, such as Spring Boot libraries, 
and place them in the appropriate directories within your Ant project structure. You could also use external tools such as Apache Ivy to manage dependencies.

#### Step 2 - Alternative: Modify the Apache Ant project
- Follow the same tasks described in [Step 2: Modify the Gradle Spring Boot project](#step-2-modify-the-gradle-spring-boot-project) to prepare the Ant directory 
and make sure to adjust the configuration to ensure compatibility with Apache Ant. Unlike Gradle, which uses a 'build.gradle' file, Apache Ant relies on a 'build.xml' 
file for configuration and task definitions.

#### Step 3 - Alternative: Define Ant build.xml file
- Create a 'build.xml' file in the root directory of the Apache Ant project;
- It is possible to define targets within the 'build.xml' file to execute various tasks such as compilations, testing, packaging, and deployment. Utilize Ant tasks such as `javac`
for compilation, `delete` for cleaning up files, and any other tasks required for the build process.

- To copy the Jar files from the build/libs directory to the dist directory use the following command:
   ```java
      <target name="copyJarToDist">
          <copy todir="dist">
              <fileset dir="build/libs" includes="*.jar"/>
          </copy>
      </target>
  ```
  
- To deletes the directory `src/main/resources/static/built` use the following command:
  ```java
      <target name="cleanWebpackFiles" depends="init">
          <delete dir="src/main/resources/static/built"/>
      </target>
  ```

#### Step 4 - Alternative: Execute Ant build
- Run the Ant build using the command `ant` in the terminal from the root directory of the project. Ant will automatically look for the build.xml file in the 
current directory and execute the default target defined within it;
- Experiment with running specific tasks described in [Step 3: Define Ant build.xml file](#step-3---alternative-define-ant-buildxml-file) using the following commands:
  1. `ant copyJarToDist`
  2. `ant cleanWebpackFiles`
- Finally, commit the code and merge into the master branch using the commands described in [Step 4: Develop new tasks to copy and delete files using Gradle](#step-4-develop-new-tasks-to-copy-and-delete-files-using-gradle)

## Conclusion
In conclusion, Part 2 of the assignment involved converting the basic Tutorial application to use Gradle instead of Maven. 
This included setting up a new Gradle Spring Boot project, configuring the Gradle plugin for managing frontend code, 
and developing tasks to copy the generated JAR to a 'dist' folder and delete webpack-generated files. These steps ensured 
efficient project management and build processes using Gradle. 
Finally, when comparing Apache Ant as an alternative solution to Gradle, it was verified that while Gradle offers a 
more modern and user-friendly DSL, Apache Ant provides flexibility and extensibility through its XML-based build files.
However, due to Apache Ant's lack of dependency management capabilities, it requires external tools such as Apache Ivy. 
Consequently, adding a plugin to manage frontend and completing the assignment was not feasible with Ant.





