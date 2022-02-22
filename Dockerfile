FROM adoptopenjdk/openjdk11:jdk-11.0.13_8-alpine
EXPOSE 5500
ADD target/CURS_Money_Transfer-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "/app.jar"]