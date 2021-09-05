FROM redhat-openjdk-18/openjdk18-openshift
COPY ./api-service/build/libs/api-service-0.0.1.jar /app/

WORKDIR /app
EXPOSE 8080
ENTRYPOINT java -Xmx1024m -XX:MaxMetaspaceSize=100m -jar api-service-0.0.1.jar