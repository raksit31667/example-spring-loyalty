FROM openjdk:17.0.2-slim

WORKDIR /app
EXPOSE 8081

COPY build/libs/batch-job-0.0.1.jar app.jar

CMD java -Djava.security.egd=file:/dev/urandom -Dcom.sun.jndi.ldap.object.trustURLCodebase=false -jar app.jar -Xms512m -Xmx2G