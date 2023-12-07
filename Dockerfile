FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/GourmetGo_server-1.1.jar
COPY ${JAR_FILE} opt/app.jar

ENV db_conn=""
ENV mapKey=""
ENV jwtExpiration=""
ENV jwtSecret=""

ENTRYPOINT java -Ddb.connectionString=${db_conn} -Dgoogle.maps.apiKey=${mapKey} -Djwt.secret=${jwtSecret} -Djwt.expiration=${jwtExpiration} -jar "opt/app.jar"
