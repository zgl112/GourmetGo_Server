FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/GourmetGo_server-1.1.jar
COPY ${JAR_FILE} opt/app.jar

ENV db_conn=""

ENTRYPOINT java -Ddb.connectionString=${db_conn} -jar "opt/app.jar"
