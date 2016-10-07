FROM java:8

LABEL Description="Image for Spring Todo web project" Version="0.9.1"
MAINTAINER Andrey Belikov <andrey.s.belikov@gmail.com>

WORKDIR /tmp/
ADD spring-todo-web-project-0.9.1.jar /tmp/
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "spring-todo-web-project-0.9.1.jar"]

