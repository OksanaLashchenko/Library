FROM amazoncorretto:15.0.2
MAINTAINER Oksana Lashchenko <lashchenkooksana@gmail.com>
COPY build/libs/*.jar demo-0.0.1-SNAPSHOT-plain.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/demo-0.0.1-SNAPSHOT-plain.jar"]
