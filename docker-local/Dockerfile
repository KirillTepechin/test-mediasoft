FROM eclipse-temurin:17-jdk-alpine
ADD build/libs/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=local" ,"-jar","/app.jar"]