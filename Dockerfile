FROM maven:3.9.5 As build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:17-oracle
EXPOSE 80

COPY --from=build /home/app/target/tracker-auth-0.1.jar  app.jar
ENTRYPOINT exec java $JAVA_OPTS  -jar /app.jar