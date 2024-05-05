# Technical Report - Class Assignment 3 Part 2

## Index

- [Overview](#overview)
- [Description of the Requirements Implementation](#description-of-the-requirements-implementation)
    - [Step 1: Experimenting the initial solution](#step-1-experimenting-the-initial-solution)
    - [Step 2: Copy the Vagrantfile to your repository](#step-2-copy-the-vagrantfile-to-your-repository)
    - [Step 3: Update the Vagrantfile configuration](#step-3-update-the-vagrantfile-configuration)
    - [Step 4: Test the Spring Web application and the H2 ](#step-4-test-the-spring-web-application-and-the-h2-console)
- [Issues](#issues)
- [Conclusion](#conclusion)

## Overview
The goal of Part 2 of this assignment is to use Vagrant to setup a virtual environment to execute the tutorial Spring 
Boot Application, gradle ”basic” version (developed in CA2-Part2). The setup includes two virtual machines (VMs) for running 
the application: the *web* VM that executes the web application inside Tomcat9, and the *db* VM, which executes the H2 
database as a server process. The web application connects to this VM.

## Description of the Requirements Implementation

### Step 1: Experimenting the initial solution
- As an initial solution to setup two VMs for running the Spring Basic Tutorial application, the steps described in
https://bitbucket.org/pssmatos/vagrant-multi-spring-tut-demo/ were followed.
- First, the xcode provider needs to be installed using the following command in your terminal:
  ```bash
    sudo xcode-select --install
  ```
- If you do no t have `Homebrew` package manager installed in your system:
  ```bash
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
  ```
- Run these two commands in your terminal to add `Homebrew` to your PATH:
  ```bash
    (echo; echo 'eval "$(/opt/homebrew/bin/brew shellenv)"') >> /Users/nathaliagodoy/.zprofile
    eval "$(/opt/homebrew/bin/brew shellenv)"
  ```
- Run `brew help` to get started
- Install `QEMU` and `libvirt` library:
  ```bash
    brew install qemu
    brew install libvirt
  ```
- Install `vagrant` and plugins with the following commands:
  ```bash
    brew install hashicorp/tap/hashicorp-vagrant
    vagrant plugin install vagrant-qemu
  ```
- Create a local folder in your computer and copy the contents of the macOS folder available in the repository:
  ```bash
    git clone https://bitbucket.org/pssmatos/vagrant-multi-spring-tut-demo/
  ```
- Execute 'sudo vagrant init' followed by 'sudo vagrant up'
- You can test this solution in the browser:
  1. Open the spring web application using one of the following options:
     * http://localhost:8080/basic-0.0.1-SNAPSHOT/
     * http://192.168.56.10:8080/basic-0.0.1-SNAPSHOT/
  2. Open the H2 console using one of the following urls:
     * http://localhost:8080/basic-0.0.1-SNAPSHOT/h2-console
     * http://192.168.56.10:8080/basic-0.0.1-SNAPSHOT/h2-console
  3. For the connection string use: jdbc:h2:tcp://192.168.56.11:9092/./jpadb
- NOTE: Because the machines were created using sudo, all vagrant commands for these machines also need sudo.

### Step 2: Copy the Vagrantfile to your repository
- Create a new folder for this assignment in your repository
  ```bash
    mkdir Documents/DEVOPS/repositorio/CA3/Part2
  ```
- Copy the Vagrantfile to your repository (inside the folder for this assignment)
  ```bash
    cp -R vagrant-multi-spring-tut-demo/macOS/* /Documents/DEVOPS/repositorio/CA3/Part2
  ```

### Step 3: Update the Vagrantfile configuration
- Make your gitHub repository public
- Go to the Vagrantfile to change the configuration
  ```bash
    cd Documents/DEVOPS/repositorio/CA3/Part2
    nano Vagrantfile
  ```
- Change the Vagrantfile configuration so that it uses your own gradle version of the spring application
  ```bash
    git clone https://github.com/NathaliaTGodoy/devops-23-24-PSM-1231864.git
    cd devops-23-24-PSM-1231864/CA2/Part2/react-and-spring-data-rest-basic
    chmod u+x gradlew
    ./gradlew clean build
    nohup ./gradlew bootRun > /home/vagrant/spring-boot-app.log 2>&1 &
    # To deploy the war file to tomcat9 do the following command:
    # sudo cp ./build/libs/basic-0.0.1-SNAPSHOT.war /var/lib/tomcat9/webapps
  ```
- Do not forget to update the jdk version
  ```bash
    sudo apt-get install -y iputils-ping avahi-daemon libnss-mdns unzip \
         openjdk-17-jdk-headless
  ```
- Execute `sudo vagrant up`
- NOTE: If the command above does not work, it may be necessary to run `sudo vagrant destroy` before it. Another error
that may occur is related to "used ports". One solution is to check the running processes using `ps aux|grep qemu` and
kill eventually processes that may show up using `kill -9 PID`, with the correct PID number of the process.

### Step 4: Test the Spring Web application and the H2 console
- Open the Spring Web application in your browser: http://localhost:8080/
- Open the H2 console in your browser: http://localhost:8082/

## Issues
- Port error: An issue encountered during this assignment was when using the command `sudo vagrant up` at the end of
[Step 3: Update the VagrantFile configuration](#step-3-update-the-vagrantfile-configuration). Even when the Vagrant initial 
solution was destroyed, the process was still active and probably did not end correctly. The solution was to check the 
running processes (`ps aux|grep qemu`) and kill the qemu-system-aarch64 processes (`kill -9 PID`, replacing the PID with 
the number of the process)

## Conclusion
In summary, this assignment focused on using Vagrant to create a virtual environment for running a Spring Boot application 
tutorial. By following provided instructions, including setup, configuration, and testing, the assignment effectively 
demonstrated proficiency in virtualization techniques using Vagrant.






