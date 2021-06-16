FROM openjdk:8-jdk-alpine
COPY product-server.jar /home/product-server.jar
COPY ["application.yml", "/home/application.yml"]
CMD ["java","-Dspring.config.location=/home/application.yml","-Djava.net.preferIPv4Stack=true","-jar","/home/product-server.jar"]