FROM openjdk:17-alpine
VOLUME /tmp
COPY /build/libs/shoppingmall-0.0.1-SNAPSHOT.jar server.jar
ENTRYPOINT ["java" , "-jar" , "server.jar"]
EXPOSE 8080