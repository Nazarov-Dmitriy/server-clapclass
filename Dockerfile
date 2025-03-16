FROM openjdk:21

EXPOSE 5600

ADD target/clap-class-0.0.1-SNAPSHOT.jar claplass.jar

COPY target/clap-class-0.0.1-SNAPSHOT.jar clapclass.jar

ENTRYPOINT  ["sh", "-c", "java $JAVA_OPTS -jar /claplass.jar"]




