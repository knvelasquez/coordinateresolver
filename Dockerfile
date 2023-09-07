FROM maven:3.8.3-openjdk-17-slim
# Author: Kevin Velasquez

ARG APP_NAME=>CoordinateResolver #App name
ARG APP_VERSION=v1.1 #App version
ARG JAR_FILENAME ${APP_NAME}-${APP_VERSION}.jar #App file name

#Set working directory
WORKDIR /${APP_NAME}

#Copy sources from to container
COPY . /${APP_NAME}

#Build with maven
RUN mvn clean test package -X

CMD ["java", "-jar", "target/CoordinateResolver-v1.1.jar"] #Run with Spring Boot
#CMD ["tail", "-f", "/dev/null"] #Keep container #Run to access it