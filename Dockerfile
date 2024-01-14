# Docker for Content Vault microserivce 
FROM openjdk:20
ARG env
# local file storage path
RUN mkdir -p /opt/activity-service/logs

COPY src/main/resources/application.${env}.properties /opt/activity-service/application.properties
COPY target/activity-service-*.jar /opt/activity-service/activity-service.jar

RUN ln -sf /dev/stdout /opt/activity-service/logs/activity-service.sys.log
WORKDIR /opt/activity-service

CMD ["java", "-jar", "activity-service.jar", "--spring.config.additional-location=application.properties"]

EXPOSE 8080
