FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/yritysten.jar /yritysten/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/yritysten/app.jar"]
