FROM openjdk:21-jdk


ENV APP_ID=slgames.store-0.0.1
COPY ./target/${APP_ID}.jar /api/${APP_ID}.jar

WORKDIR /api 


CMD java -jar ${APP_ID}.jar