FROM openjdk:17-jdk-slim
ENV JAVA_HOME=/usr/local/openjdk-17
ADD apache-tomcat-10.1.42.tar.gz /opt
COPY target/pod-dashboard-0.0.1-SNAPSHOT.war /opt/apache-tomcat-10.1.42/webapps/
ENTRYPOINT ["/opt/apache-tomcat-10.1.42/bin/catalina.sh", "run"]
