FROM openjdk:8-jdk-alpine
COPY ["message-server-0.0.1-SNAPSHOT.jar", "/home/message-server-0.0.1-SNAPSHOT.jar"]
COPY ["application.yml", "/home/application.yml"]
CMD ["java","-Dserver.port=9002","-Dspring.config.location=/home/application.yml","-Djava.net.preferIPv4Stack=true","-jar", "/home/message-server-0.0.1-SNAPSHOT.jar"]