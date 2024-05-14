# Technical Report - Class Assignment 3 Part 2

## Index

- [Overview](#overview)
- [Description of the Requirements Implementation Using QEMU](#description-of-the-requirements-implementation-using-qemu)
    - [Step 1: Experimenting the initial solution](#step-1-experimenting-the-initial-solution)
    - [Step 2: Copy the Vagrantfile to your repository](#step-2-copy-the-vagrantfile-to-your-repository)
    - [Step 3: Update the Vagrantfile configuration](#step-3-update-the-vagrantfile-configuration)
    - [Step 4: Change the Spring Application to use the H2 server](#step-4-change-the-spring-application-to-use-the-h2-server)
  - [Step 5: Test the Spring web application and the H2](#step-5-test-the-spring-web-application-and-the-h2)
- [Final version of Vagrantfile](#final-version-of-vagrantfile)
- [Issues](#issues)
- [Alternative solution using VirtualBox](#alternative-solution-using-virtual-box)
    - [Analysis of VirtualBox as an alternative solution](#analysis-of-virtual-box-as-an-alternative-solution)
    - [Implementation of VirtualBox as an alternative solution](#implementation-of-virtual-box-as-an-alternative-solution)
- [Conclusion](#conclusion)

## Overview
The goal of Part 2 of this assignment is to use Vagrant to set up a virtual environment to execute the tutorial Spring 
Boot Application, gradle ”basic” version (developed in CA2-Part2). The setup includes two virtual machines (VMs) for running 
the application: the *web* VM that executes the web application inside Tomcat, and the *db* VM, which executes the H2 
database as a server process. The web application connects to this VM. This assignment is also focus in the configuration
of the Spring Application.

## Description of the Requirements Implementation Using QEMU

### Step 1: Experimenting the initial solution
- As an initial solution to set up two VMs for running the Spring Basic Tutorial application, the steps described in
https://bitbucket.org/pssmatos/vagrant-multi-spring-tut-demo/ were followed.
- First, the xcode provider needs to be installed using the following command in your terminal:
  ```bash
    sudo xcode-select --install
  ```
- If you do not have `Homebrew` package manager installed in your system:
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
- Execute `sudo vagrant init` to start the environment of Vagrant followed by `sudo vagrant up` to execute the script
in the Vagrantfile.
- You can test this initial solution in the browser to understand how it works.
  1. Open the spring web application using one of the following options:
     * http://localhost:8080/basic-0.0.1-SNAPSHOT/
     * http://192.168.56.10:8080/basic-0.0.1-SNAPSHOT/
  2. Open the H2 console using one of the following urls:
     * http://localhost:8080/basic-0.0.1-SNAPSHOT/h2-console
     * http://192.168.56.10:8080/basic-0.0.1-SNAPSHOT/h2-console
  3. For the connection string in the console use: jdbc:h2:tcp://192.168.56.11:9092/./jpadb
- NOTE: Because the machines were created using sudo, all vagrant commands for these machines also need sudo.

### Step 2: Copy the Vagrantfile to your repository
- Create a new folder for this assignment in your repository
  ```bash
    mkdir Documents/DEVOPS/repositorio/CA3/Part2
  ```
- Copy the Vagrantfile of the initial solution to your repository (inside the folder for this assignment)
  ```bash
    cp -R vagrant-multi-spring-tut-demo/macOS/* /Documents/DEVOPS/repositorio/CA3/Part2
  ```

### Step 3: Update the Vagrantfile configuration
- Make your gitHub repository public so that Vagrant can clone.
- Go to the Vagrantfile to change the configurations:
  ```bash
    cd Documents/DEVOPS/repositorio/CA3/Part2
    nano Vagrantfile
  ```
- Change the Vagrantfile configuration so that it uses your own gradle version of the spring application:
  ```bash
    git clone https://github.com/NathaliaTGodoy/devops-23-24-PSM-1231864.git
    cd devops-23-24-PSM-1231864/CA2/Part2/react-and-spring-data-rest-basic
    chmod u+x gradlew
    ./gradlew clean build
    nohup ./gradlew bootRun > /home/vagrant/spring-boot-app.log 2>&1 &
  ```
- Update the jdk version to jdk17, so it is compatible with all dependencies
  ```bash
    sudo apt-get install -y iputils-ping avahi-daemon libnss-mdns unzip \
         openjdk-17-jdk-headless
  ```
- Execute `sudo vagrant up` to start the VMs define in Vagrantfile
- NOTE: If the command above does not work, it may be necessary to run `sudo vagrant destroy` before it to destroy any VM
that may be running. Another error that may occur is related to "used ports". One solution is to check the running 
processes using `ps aux|grep qemu` and kill eventually processes that may show up using `kill -9 PID`, with the correct 
PID number of the process.

### Step 4: Change the spring application to use the H2 server
- Update the application.properties class in CA2/Part2 (React and Spring Data Application):
  - The string jdbc:h2:tcp://192.168.56.11:9092/./jpadb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE represents the connection 
string to be used when accessing the H2 database.
  - Assure that the data source IP and port is the same defined for the VM in the Vagrantfile.
  - The username and password to be used to access the database of H2 can be specified, in this case the password is blank.
  ```bash
    server.servlet.context-path=/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT

    spring.datasource.url=jdbc:h2:tcp://192.168.56.11:9092/./jpadb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    spring.datasource.driverClassName=org.h2.Driver
    spring.datasource.username=sa
    spring.datasource.password=
    spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

    spring.jpa.hibernate.ddl-auto=update
    spring.h2.console.enabled=true
    spring.h2.console.path=/h2-console
    spring.h2.console.settings.web-allow-others=true
  ```

- Update the app.js in CA2/Part2 to change the path for the endpoint when the 'GET' is invoked in the frontend:
  ```bash
    componentDidMount() { // <2>
         client({method: 'GET', path: '/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/api/employees'}).done(response => {
             this.setState({employees: response.entity._embedded.employees});
         });
     }
  ```

- It is also possible to implement the web server and servlet container [Apache Tomcat](https://tomcat.apache.org) 
to deploy our application:
  - Create a ServletInitializer class in CA2/Part2 to enable the deployment of the Spring web application on the Tomcat server.
  ```bash
    public class ServletInitializer extends SpringBootServletInitializer {
      @Override
      protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ReactAndSpringDataRestApplication.class);
      }
    }
  ```
  - Update the build.gradle class in CA2/Part2 to add a new plugin, so a war (Web Archive file) instead of a jar will be
generated when running the `./gradlew build`
    ```bash
      plugins {
        id 'java'
        id 'org.springframework.boot' version '3.2.4'
        id 'io.spring.dependency-management' version '1.1.4'
        id 'org.siouan.frontend-jdk17' version '8.0.0'
        id 'war'
      }
    ```
  - To enable communication between Spring and Tomcat, it is necessary to specify this connection in the application.properties 
file in CA2/Part2 by adding a line that informs Tomcat about the resource's name.
    ```bash
    server.servlet.context-path=/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT
    ```
  - Update the Vagrantfile with the Tomcat:
  ```bash
    #INSTALL TOMCAT10
    wget https://archive.apache.org/dist/tomcat/tomcat-10/v10.0.18/bin/apache-tomcat-10.0.18.tar.gz
    tar -xvf apache-tomcat-10.0.18.tar.gz
    sudo mv apache-tomcat-10.0.18 /opt/tomcat10
    sudo ln -s /opt/tomcat10 /usr/local/tomcat10
    sudo chown -R vagrant:vagrant /opt/tomcat10
  
    #COPY WAR FILE GENERATED AFTER GRADLE BUILD INTO TOMCAT WEBAPPS
    sudo cp ./build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war /opt/tomcat10/webapps

    #STARTUP TOMCAT
    /opt/tomcat10/bin/startup.sh
  ```

### Step 5: Test the Spring Web application and the H2
- Make sure your repository is updated with all modifications made in the previously steps.
- Execute `sudo vagrant up`
- You can test the implemented solution in the browser:
  1. Open the spring web application using the following option:
    * http://192.168.56.10:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/
  2. Open the H2 console using the following url:
    * http://192.168.56.11:8082/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/
  3. For the connection string use: jdbc:h2:tcp://192.168.56.11:9092/./jpadb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
  4. For the username use: sa
  5. The password will be blank

- You can modify the data in the console and see the changes in the web application to validate the communication between
the two VMs:
  1. Go to the H2 console and execute the following query to add a new entry
  ```sql
     INSERT INTO EMPLOYEE (ID, DESCRIPTION, EMAIL, FIRST_NAME, JOB_YEARS, LAST_NAME) VALUES (3, 'Wizard', 'harry@hogwards.com', 'Harry', 100, 'Potter');
  ```
  2. Reload the web application browser page, and you can see that the new entry was added to the table
  3. You can also execute a post command using curl in your terminal to add new data
  ```bash
     curl -X POST http://192.168.56.10:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/api/employees -H 'Content-Type: 
     application/json' -d '{"firstName": "Ron", "lastName": "Weasley", "description": "Wizard", "jobYears": 500, "email": "legolas@mirkwood.com"}'
  ```
  4. Reload the web application browser page again to see the new entry

- With these implementation, it is now possible to stop the VMs using the `vagrant halt` and data will continue to be persisted
in the database.

## Final version of Vagrantfile
```ruby
  Vagrant.configure("2") do |config|
  config.vm.box = "perk/ubuntu-2204-arm64"

  # This provision is common for both VMs
  config.vm.provision "shell", inline: <<-SHELL
    sudo apt-get -y update
    sudo apt-get install -y iputils-ping avahi-daemon libnss-mdns unzip \
         openjdk-17-jdk-headless
    # ifconfig
  SHELL

  #============
  # Configurations specific to the database VM
  config.vm.define "db" do |db|
    db.vm.box = "perk/ubuntu-2204-arm64"
    db.vm.hostname = "db"

    db.vm.provider "qemu" do |qe|
      qe.arch = "aarch64"
      qe.machine = "virt,accel=hvf,highmem=off"
      qe.cpu = "cortex-a72"
      qe.net_device = "virtio-net-pci"
      qe.memory = "512"
      qe.ssh_port = 50122
      qe.extra_qemu_args = %w(-netdev vmnet-host,id=vmnet,start-address=192.168.56.1,end-address=192.168.56.255,subnet-mask=255.255.255.0 -device virtio-net-pci,mac=52:54:00:12:34:50,netdev=vmnet)
    end

    # We want to access H2 console from the host using port 8082
    # We want to connect to the H2 server using port 9092
    db.vm.network "forwarded_port", guest: 8082, host: 8082
    db.vm.network "forwarded_port", guest: 9092, host: 9092

    # We need to download H2 and configure host-network
    config.vm.provision "shell", inline: <<-SHELL
      wget https://repo1.maven.org/maven2/com/h2database/h2/1.4.200/h2-1.4.200.jar
    SHELL

    # The following provision shell will run ALWAYS so that we can execute the H2 server process
    # This could be done in a different way, for instance, setting H2 as as service, like in the following link:
    # How to setup java as a service in ubuntu: http://www.jcgonzalez.com/ubuntu-16-java-service-wrapper-example
    #
    # To connect to H2 use: jdbc:h2:tcp://192.168.56.11:9092/./jpadb
    db.vm.provision "file", source: "provision/netcfg-db.yaml", destination: "/home/vagrant/01-netcfg.yaml"
    db.vm.provision "shell", :run => 'always', inline: <<-SHELL
      sudo mv /home/vagrant/01-netcfg.yaml /etc/netplan
      chmod 600 /etc/netplan/01-netcfg.yaml
      sudo netplan apply
    
      java -cp ./h2*.jar org.h2.tools.Server -web -webAllowOthers -tcp -tcpAllowOthers -ifNotExists > ~/out.txt &
    SHELL
  end

  #============
  # Configurations specific to the webserver VM
  config.vm.define "web" do |web|
    web.vm.box = "perk/ubuntu-2204-arm64"
    web.vm.hostname = "web"

    web.vm.provider "qemu" do |qe|
      qe.arch = "aarch64"
      qe.machine = "virt,accel=hvf,highmem=off"
      qe.cpu = "cortex-a72"
      qe.net_device = "virtio-net-pci"
      qe.memory = "1G"
      qe.ssh_port = 50222
      qe.extra_qemu_args = %w(-netdev vmnet-host,id=vmnet,start-address=192.168.56.1,end-address=192.168.56.255,subnet-mask=255.255.255.0 -device virtio-net-pci,mac=52:54:00:12:34:51,netdev=vmnet)
    end

    # We want to access tomcat from the host using port 8080
    web.vm.network "forwarded_port", guest: 8080, host: 8080

    web.vm.provision "file", source: "provision/netcfg-web.yaml", destination: "/home/vagrant/01-netcfg.yaml"
    web.vm.provision "shell", inline: <<-SHELL, privileged: false
      sudo mv /home/vagrant/01-netcfg.yaml /etc/netplan
      chmod 600 /etc/netplan/01-netcfg.yaml
      sudo netplan apply
    
      #INSTALL TOMCAT10
      wget https://archive.apache.org/dist/tomcat/tomcat-10/v10.0.18/bin/apache-tomcat-10.0.18.tar.gz
      tar -xvf apache-tomcat-10.0.18.tar.gz
      sudo mv apache-tomcat-10.0.18 /opt/tomcat10
      sudo ln -s /opt/tomcat10 /usr/local/tomcat10
      sudo chown -R vagrant:vagrant /opt/tomcat10

      # Change the following command to clone your own repository!
      git clone https://github.com/NathaliaTGodoy/devops-23-24-PSM-1231864.git
      cd devops-23-24-PSM-1231864/CA2/Part2/react-and-spring-data-rest-basic
      chmod u+x gradlew
      ./gradlew clean build
      nohup ./gradlew bootRun > /home/vagrant/spring-boot-app.log 2>&1 &
      # To deploy the war file to tomcat9 do the following command:
      # sudo cp ./build/libs/basic-0.0.1-SNAPSHOT.war /var/lib/tomcat9/webapps

      #COPY WAR FILE GENERATED AFTER GRADLE BUILD INTO TOMCAT WEBAPPS
      sudo cp ./build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war /opt/tomcat10/webapps

      #STARTUP TOMCAT
      /opt/tomcat10/bin/startup.sh

    SHELL
  end
end
```

## Issues
- Integration of H2 database: The major issues encountered during this assignment were related to the H2 database integration.
More than understand how the Vagrant works, we also had to understand and configure the Spring application and make all the
adjustment needed. 
- Cloning the repository: While the Vagrantfile included the task of cloning the personal repository on GitHub, when 
experimenting with the Spring Application, it was necessary to push every minor change to the repository before testing,
instead of only experimenting locally.
- Performance: The need to destroy and recreate VMs each time changes are made to the development environment 
adds complexity and overhead to the workflow. It involves running multiple Vagrant commands and scripts, having to destroy
and recreate VMs, which can be time-consuming and challenging to manage, especially for complex environments.
- Port error: A minor issue at the start of this assignment was when using the command `sudo vagrant up` at the end of
[Step 3: Update the VagrantFile configuration](#step-3-update-the-vagrantfile-configuration). Even when the Vagrant initial 
solution was destroyed, the process was still active and probably did not end correctly. The solution was to check the 
running processes (`ps aux|grep qemu`) and kill the qemu-system-aarch64 processes (`kill -9 PID`, replacing the PID with 
the number of the process).

## Alternative Solution Using Virtual Box
### Analysis of Virtual Box as an alternative solution
- [UTM](https://mac.getutm.app) is tailored for Apple silicon-based Macs, offering native ARM support, good performance, 
and compatibility with ARM-based VMs. It features a simple interface and supports a wide range of guest operating systems 
(OSs), with additional functionalities like snapshotting and USB pass-through. However, UTM may lack some advanced 
features compared to established platforms like VirtualBox, potentially impacting performance and compatibility with 
x86-based solutions. Its development cycle may also lag behind VirtualBox, leading to delayed feature updates.
- [VirtualBox](https://www.virtualbox.org) is widely used across multiple OSs, being an open-source hypervisor developed 
by Oracle. It offers good performance and extensive features, including snapshotting, virtual networking, and support for 
various guest OSs. It boasts a user-friendly interface, simplified setup, and an active development community ensuring 
compatibility with new technologies.

### Implementation of Virtual Box as an alternative solution
- When considering UTM for use with Vagrant, it is important to note that native support is not available, needing 
additional steps or plugins for seamless integration. Manual configuration is required, as described in
[Step 1](#step-1-experimenting-the-initial-solution). Given UTM's primary focus on macOS with ARM architecture, 
compatibility with Vagrant might be limited due to a smaller user base and less community-driven development in this area.
- VirtualBox, on the other hand, is fully compatible with Vagrant out of the box and offers extensive features for 
managing virtual environments. Its seamless integration with Vagrant ensures a straightforward setup process and consistent 
workflow across different operating systems. Additionally, the active VirtualBox community provides numerous pre-built 
Vagrant boxes, simplifying VM deployment with specific configurations.
- The VagrantFile to use in [Step 3](#step-3-update-the-vagrantfile-configuration) with all the necessary modifications
is the following:
```ruby
    Vagrant.configure("2") do |config|
    config.vm.box = "ubuntu/focal64"
    config.ssh.insert_key = false

    # This provision is common for both VMs
    config.vm.provision "shell", inline: <<-SHELL
    sudo apt-get update -y
    sudo apt-get install -y iputils-ping avahi-daemon libnss-mdns unzip \
    openjdk-17-jdk-headless
    # ifconfig
    SHELL

    #============
    # Configurations specific to the database VM
    config.vm.define "db" do |db|
    db.vm.box = "ubuntu/focal64"
    db.vm.hostname = "db"
    db.vm.network "private_network", ip: "192.168.56.11"

      # We want to access H2 console from the host using port 8082
      # We want to connect to the H2 server using port 9092
      db.vm.network "forwarded_port", guest: 8082, host: 8082
      db.vm.network "forwarded_port", guest: 9092, host: 9092

      # We need to download H2
      db.vm.provision "shell", inline: <<-SHELL
        wget https://repo1.maven.org/maven2/com/h2database/h2/1.4.200/h2-1.4.200.jar
      SHELL

      # The following provision shell will run ALWAYS so that we can execute the H2 server process
      # This could be done in a different way, for instance, setting H2 as as service, like in the following link:
      # How to setup java as a service in ubuntu: http://www.jcgonzalez.com/ubuntu-16-java-service-wrapper-example
      #
      # To connect to H2 use: jdbc:h2:tcp://192.168.33.11:9092/./jpadb
      db.vm.provision "shell", :run => 'always', inline: <<-SHELL
        java -cp ./h2*.jar org.h2.tools.Server -web -webAllowOthers -tcp -tcpAllowOthers -ifNotExists > ~/out.txt &
      SHELL
    end

    #============
    # Configurations specific to the webserver VM
    config.vm.define "web" do |web|
    web.vm.box = "ubuntu/focal64"
    web.vm.hostname = "web"
    web.vm.network "private_network", ip: "192.168.56.10"

      # We set more ram memory for this VM
      web.vm.provider "virtualbox" do |v|
        v.memory = 1024
      end

      # We want to access tomcat from the host using port 8080
      web.vm.network "forwarded_port", guest: 8080, host: 8080

      web.vm.provision "shell", inline: <<-SHELL, privileged: false
        wget https://archive.apache.org/dist/tomcat/tomcat-10/v10.0.18/bin/apache-tomcat-10.0.18.tar.gz
        tar -xvf apache-tomcat-10.0.18.tar.gz
        sudo mv apache-tomcat-10.0.18 /opt/tomcat10
        sudo ln -s /opt/tomcat10 /usr/local/tomcat10
        sudo chown -R vagrant:vagrant /opt/tomcat10

        # Change the following command to clone your own repository!
        git clone https://github.com/NathaliaTGodoy/devops-23-24-PSM-1231864.git
        cd devops-23-24-PSM-1231864/CA2/Part2/react-and-spring-data-rest-basic
        chmod u+x gradlew
        ./gradlew clean build
        nohup ./gradlew bootRun > /home/vagrant/spring-boot-app.log 2>&1 &
        # To deploy the war file to tomcat9 do the following command:
        # sudo cp ./build/libs/basic-0.0.1-SNAPSHOT.war /var/lib/tomcat9/webapps
      
        #COPY WAR FILE GENERATED AFTER GRADLE BUILD INTO TOMCAT WEBAPPS
        sudo cp ./build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war /opt/tomcat10/webapps
      
        #STARTUP TOMCAT
        /opt/tomcat10/bin/startup.sh
      SHELL
     end
    end
```
- As the machine does not need to be created using sudo, all vagrant commands for these machines does not need sudo.
- Execute `vagrant up`.
- Make all changes described in [Step 4](#step-4-change-the-spring-application-to-use-the-h2-server) to integrate the
database in our Spring Application.
- Test the Spring Web application and the H2 console as described in [Step 5](#step-5-test-the-spring-web-application-and-the-h2).

## Conclusion
In summary, this assignment focused on using Vagrant to create a virtual environment for running a Spring Boot application 
tutorial. By following provided instructions, including setup, configuration of Vagrant and Spring Application, testing, 
and deployment the assignment effectively demonstrated proficiency in virtualization techniques using Vagrant.






