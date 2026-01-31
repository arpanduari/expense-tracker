FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY target/expensewise-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV JAVA_TOOL_OPTIONS="\
-Xms128m \
-Xmx384m \
-XX:+UseContainerSupport \
-XX:MaxRAMPercentage=75.0 \
-XX:+UseSerialGC \
-XX:MaxMetaspaceSize=128m \
-XX:CompressedClassSpaceSize=32m \
-XX:ReservedCodeCacheSize=32m \
-XX:+TieredCompilation \
-XX:TieredStopAtLevel=1 \
-Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["java", "-jar", "/app/app.jar"]