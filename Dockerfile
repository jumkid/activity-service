# Docker for Content Vault microserivce 
FROM openjdk:11.0.15
ARG env
# local file storage path
RUN mkdir -p /opt/activity-service/log
COPY src/main/resources/application.${env}.properties /opt/activity-service/application.properties
COPY target/activity-service-*.jar /opt/activity-service/activity-service.jar
RUN ln -sf /dev/stdout /opt/activity-service/log/activity-service.sys.log

CMD ["java", "-jar", "/opt/activity-service/activity-service.jar", "--spring.config.additional-location=/opt/activity-service/application.properties"]

EXPOSE 8080
