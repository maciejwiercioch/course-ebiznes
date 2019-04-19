FROM ubuntu:18.04

MAINTAINER Maciej Wiercioch <maciej.wiercioch@student.uj.edu.pl>

RUN useradd maciek --create-home

RUN apt-get update
RUN apt-get install -y vim unzip curl git


RUN apt-get update && \
    apt-get install -y openjdk-8-jdk && \
    apt-get clean;

RUN apt-get update && \
    apt-get install ca-certificates-java && \
    apt-get clean && \
    update-ca-certificates -f;

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/
RUN export JAVA_HOME

RUN apt-get install -y scala
RUN apt-get update && apt-get install -y gnupg2

RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
RUN apt-get update
RUN apt-get install -y sbt

USER maciek

CMD echo "Hello World"
