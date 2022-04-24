FROM openjdk:17.0.2-slim

WORKDIR /app
EXPOSE 8080

COPY build/libs/api-service-0.0.1.jar app.jar

CMD java -Djava.security.egd=file:/dev/urandom -jar app.jar -Xms512m -Xmx2G