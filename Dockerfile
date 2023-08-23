FROM eclipse-temurin:19-alpine
COPY ./target/library-0.9.jar app.jar
CMD ["java","-jar","app.jar"]