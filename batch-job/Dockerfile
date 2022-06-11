FROM openjdk:17.0.2-slim

ARG SEMANTIC_VERSION

WORKDIR /app
EXPOSE 8081

COPY build/libs/batch-job-${SEMANTIC_VERSION:-0.0.1-SNAPSHOT}.jar app.jar

CMD java -Djava.security.egd=file:/dev/urandom -Dcom.sun.jndi.ldap.object.trustURLCodebase=false -jar app.jar -Xms512m -Xmx2G