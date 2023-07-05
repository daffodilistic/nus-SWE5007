FROM maven:3.9.3-eclipse-temurin-17-focal as builder
ARG SERVICE_NAME=info-collection-system
RUN mkdir /build
COPY . /build
WORKDIR /build/rigsg/${SERVICE_NAME}/src/main/resources
RUN rm application.yml
RUN mv application_docker.yml application.yml
WORKDIR /build/rigsg
RUN apt-get update && \
    apt-get install dos2unix
# Just in case the file is not in Unix format
RUN dos2unix mvnw
# NOTE MAVEN_CONFIG needs to be unset.
# See https://issues.jenkins.io/browse/JENKINS-47890
# Also, note that env vars are reset when moving to the next step/layer
RUN unset MAVEN_CONFIG && \
    ./mvnw clean package spring-boot:repackage -DskipTests=true
WORKDIR /build/rigsg/${SERVICE_NAME}/target
RUN mkdir /application
# NOTE COPY <src> is with reference to the local folder!
RUN cp ./*.jar /application && \
    mv /application/*.jar /application/application.jar
WORKDIR /application
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:17-jre
WORKDIR /application
COPY --from=builder /application/dependencies/ ./
COPY --from=builder /application/spring-boot-loader/ ./
COPY --from=builder /application/snapshot-dependencies/ ./
COPY --from=builder /application/application/ ./
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
