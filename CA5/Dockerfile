FROM jenkins/jenkins:2.452.2-jdk17

USER root

# Install necessary packages and Docker CLI
RUN apt-get update && apt-get install -y lsb-release
RUN curl -fsSLo /usr/share/keyrings/docker-archive-keyring.asc \
  https://download.docker.com/linux/debian/gpg
RUN echo "deb [arch=$(dpkg --print-architecture) \
  signed-by=/usr/share/keyrings/docker-archive-keyring.asc] \
  https://download.docker.com/linux/debian \
  $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list
RUN apt-get update && apt-get install -y docker-ce-cli

# Install Jenkins plugins
RUN jenkins-plugin-cli --plugins "blueocean:1.27.3 docker-workflow:1.28 json-path-api:2.8.0-5.v07cb_a_1ca_738c token-macro:400.v35420b_922dcb_ favorite:2.208.v91d65b_7792a_c github:1.39.0"
