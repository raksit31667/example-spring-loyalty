FROM openjdk:17-slim

WORKDIR /app
EXPOSE 8080

COPY build/libs/batch-job-0.0.1.jar app.jar

CMD java -Djava.security.egd=file:/dev/urandom -jar app.jar -Xms512m -Xmx2G