FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN adduser -D -h /app appuser

COPY backend/target/backend-0.0.1-SNAPSHOT.jar app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java","-XX:+UseContainerSupport","-XX:MaxRAMPercentage=75.0","-jar","app.jar"]