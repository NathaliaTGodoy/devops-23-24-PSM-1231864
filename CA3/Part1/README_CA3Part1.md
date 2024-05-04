# Technical Report - Class Assignment 3 Part 1

## Index

- [Overview](#overview)
- [Description of the Requirements Implementation](#description-of-the-requirements-implementation)
    - [Step 1: Creating a VM and configure it](#step-1-creating-a-vm-and-configure-it)
    - [Step 2: Clone your individual repository inside the VM](#step-2-clone-your-individual-repository-inside-the-vm)
    - [Step 3: Try to build and execute Spring Boot Tutorial using Maven](#step-3-try-to-build-and-execute-spring-boot-tutorial-using-maven-ca1)
    - [Step 4: Try to build and execute Spring Boot using Gradle](#step-4-try-to-build-and-execute-spring-boot-using-gradle-ca2part2)
    - [Step 5: Try to build and execute the Gradle Basic Demo project](#step-5-try-to-build-and-execute-the-gradlebasicdemo-project-ca2part1)
- [Issues](#issues)
- [Conclusion](#conclusion)

## Overview
The objective of Part 1 of this assignment was to practice with UTM (VirtualBox in case of not having Apple with arm64),
using the same projects from the previous assignments but now inside a UTM/VirtualBox Virtual Machine (VM) with Linux (Ubuntu).

## Description of the Requirements Implementation

### Step 1: Creating a VM and configure it
- For this step, a hypervisor that runs in many host operating systems is needed. In this case we will use UTM to create
a VM and install Linux on the new VM. However, you can follow similar steps for VirtualBox
- Create a VM with the following settings and establish an SSH protocol
  1. Connect image (ISO) with the [Ubuntu 18.04 minimal installation media] (https: //help.ubuntu.com/community/ Installation/MinimalCD)
  2. The VM needs 2048 MB RAM
  3. For UTM the Network mode should be Shared Network
  4. For Virtual Box it is needed to set the Network Adapter 1 as Nat and to set the Network Adapter 2 as Host-only Adapter
     (vboxnet0), according to following instructions:
     * From the main menu, select File -> Host Network Manager (Ctrl+W)
     * You should see an empty white box with ”Host-only Networks”. Click the create button (Ctrl +A). A new Host-only network will be created and added to the list.
       After that you should be able to select a name for your host-only networks in the VMs network configuration.
     * Check the IP address range of this network. In this case it is 192.168.56.1/24.
     * Select an IP in this range for the second adapter of our VM.
  5. Start the VM and install Ubuntu
  6. After starting the VM it is needed to update the packages repositories using the following commands:
     * `sudo apt install net-tools`
     * `sudo nano /etc/netplan/01-netcfg.yaml` - To edit the network configuration file to set up the IP
     * `sudo netplan apply`
  7. Establish an SSH connection using the following commands:
     * `sudo apt install openssh-server` -  To install openssh-server so that we can use ssh to open secure terminal 
       sessions to the VM (from other hosts)
     * `sudo nano /etc/ssh/sshd_config` - To enable password authentication for ssh uncomment the line 
       PasswordAuthentication yes sudo service ssh restart
     * `sudo apt install vsftpd` - To install a ftp server so that we can use the FTP protocol to transfers files to/from 
       the VM (from other hosts)
     * `sudo nano /etc/vsftpd.conf` - To enable write access for vsftpd uncomment the line write_enable=YES sudo service 
       vsftpd restart
  8. Now that the SSH server is enabled in the VM we can now use ssh to connect to the VM. In the host, in a terminal/console, type:
      `ssh <name>@<IP>`, where the name should be replaced by the username of the VM and the IP should be replaced by the IP of the VM
       (192.168.56.1 if you are using VirtualBox and followed the item 4)
  9. Install Git and Java using the following commands:
  ```bash
    sudo apt install git
    sudo apt install openjdk-8-jdk-headless
  ```

### Step 2: Clone your individual repository inside the VM
- Make a clone of your individual repository with the following command in your terminal in the host:
  1. `ssh <name>@<IP>` - To establish a connection between the host and the guest (VM)
  2. `git clone https://github.com/NathaliaTGodoy/devops-23-24-PSM-1231864` - Use your repository link

### Step 3: Try to build and execute spring boot tutorial using Maven (CA1)
- Assure that you have the Maven installed. If you do not have, then use the command:
  ```bash
    sudo apt install maven
  ```
- Assure that maven have permission to execute, if it does not have, then use the command:
  ```bash
    chmod +x mvnw
  ```
- Try to build and execute the spring boot tutorial basic project using the following commands:
  ```bash
    cd CA1/tut-react-and-spring-data-rest/basic
    ./mvnw spring-boot:run
  ```
- To open the application use the browser in your host computer and the url: `http://<IP>:8080/`

### Step 4: Try to build and execute spring boot using Gradle (CA2Part2)
- First it is necessary to install all dependencies to use gradle.
- Install java version 17
  ```bash
    sudo apt install openjdk-17-jdk openjdk-17-jre
  ```
- Install gradle version 8.6
  ```bash
    wget https://services.gradle.org/distributions/gradle-8.6-bin.zip
    sudo mkdir /opt/gradle
    sudo unzip -d /opt/gradle gradle-8.6-bin.zip
    echo "export GRADLE_HOME=/opt/gradle/gradle-8.6" >> ~/.bashrc
    echo "export PATH=$PATH:$GRADLE_HOME/bin" >> ~/.bashrc
    source ~/.bashrc
    gradle -v
  ```
- Assure that you are using the Ubuntu version 20
- Assure that gradle have permission to execute, if it does not have, then use the command:
  ```bash
    chmod +x gradlew
  ```
- Try to build and execute the spring boot basic project using the following commands:
  ```bash
    cd CA2/Part2/react-and-spring-data-rest-basic
    ./gradlew bootRun
  ```
- To open the application use the browser in your host computer and the url: `http://<IP>:8080/`

### Step 5: Try to build and execute the gradle_basic_demo project (CA2Part1)
- Try to build and execute the gradle_basic_demo project using the following commands:
  ```bash
    cd CA2/Part1/gradle-basic-demo
    ./gradlew build
    ./gradlew runServer
  ```
- To run the client, open another terminal and use the command:
  ```bash
    cd CA2/Part1/gradle-basic-demo
    ./gradlew runClient --args="192.168.56.5 59001"
  ```
- The client needs to run in the host and not inside the VM. This is due to the lack of a graphical interface in the VM.

## Issues
- Dependency: The main issues encountered when completing all tasks of this CA were mainly due to incompatible dependency versions. 
When attempting to follow the tasks in Step 4, Java needed to be version 17, Ubuntu 20, and Gradle 8.6. 
If these dependencies were not properly configured, an exception was thrown, and the build was unsuccessful.
- Permission: Another possible error related to permissions might occur when attempting to execute the Maven and Gradle 
Wrapper, which can be rectified by granting execution permission using the commands `chmod +x mvnw` and `chmod +x gradlew`.
- Network Configuration: When using UTM, it was not possible to configure a specific IP for the VM. Therefore, to establish 
an SSH connection, it was necessary to first check the VMs IP using the `ip addr` or `ifconfig` commands.

## Conclusion
On the whole, this assignment focused on practical tasks involving the setup and utilization of a Virtual Machine (VM) 
with Ubuntu Linux, using hypervisors such as UTM or VirtualBox. Key steps included VM creation, configuration, SSH setup, 
and installing the necessary dependencies. The primary objective of building and running Java projects using Maven and 
Gradle within the Linux environment was successfully achieved. The assignment provided an understanding of VM deployment, 
Linux administration, and Java project management.






