FROM openjdk:17.0.2-slim

ARG SEMANTIC_VERSION

WORKDIR /app
EXPOSE 8080 9090

COPY build/libs/api-service-${SEMANTIC_VERSION:-0.0.1-SNAPSHOT}.jar app.jar

CMD java -Djava.security.egd=file:/dev/urandom -Dcom.sun.jndi.ldap.object.trustURLCodebase=false -jar app.jar -Xms512m -Xmx2G