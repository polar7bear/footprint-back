FROM openjdk:21
WORKDIR /app

COPY . .

CMD ["./gradlew", "build"]

COPY build/libs/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]