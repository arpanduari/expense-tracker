FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/expense-tracker-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV JAVA_TOOL_OPTIONS="-Xms256m -Xmx480m -XX:+UseContainerSupport -XX:MaxRAMPercentage=85.0"

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
