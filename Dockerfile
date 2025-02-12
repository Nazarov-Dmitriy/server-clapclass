FROM openjdk:21

EXPOSE 5600

ADD target/clap-class-0.0.1-SNAPSHOT.jar claplass.jar

COPY target/clap-class-0.0.1-SNAPSHOT.jar clapclass.jar

#ENTRYPOINT  ["java", "-jar", "/claplass.jar"]
ENTRYPOINT  ["java", "-Xms256m","-Xmx3036m", "-jar", "/claplass.jar"]
CMD ["bash"]

