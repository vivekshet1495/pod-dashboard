FROM openjdk:17-jdk-slim
ENV JAVA_HOME /usr
ADD apache-tomcat-10.1.42.tar.gz /opt
COPY target/microservice1-1.0.0.war /opt/apache-tomcat-10.1.42/webapps/
ENTRYPOINT ["/opt/apache-tomcat-10.1.42/bin/catalina.sh", "run"]
