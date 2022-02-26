FROM adoptopenjdk/openjdk11:jdk-11.0.13_8-alpine
EXPOSE 9999
ADD target/DIPLOM_Cloud_storage-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "/app.jar"]