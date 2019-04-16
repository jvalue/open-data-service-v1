FROM openjdk:8-jdk-slim

######################################################
# install Docker and Git
######################################################
RUN apt-get update && apt-get install -y --no-install-recommends \
    git \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg2 \
    software-properties-common \
    && rm -rf /var/lib/apt/lists/*

RUN curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -
RUN add-apt-repository \
    "deb [arch=amd64] https://download.docker.com/linux/debian \
    $(lsb_release -cs) \
    stable"

RUN apt-get update && apt-get install -y --no-install-recommends \
    docker-ce docker-ce-cli containerd.io \
    && rm -rf /var/lib/apt/lists/*

######################################################
# install docker-compose
######################################################
RUN curl -L "https://github.com/docker/compose/releases/download/1.23.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose \
    && chmod +x /usr/local/bin/docker-compose
