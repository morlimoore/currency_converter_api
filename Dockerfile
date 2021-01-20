FROM openjdk:14-alpine

WORKDIR /build/

COPY pom.xml /build/

COPY src /build/src/

ADD target/currencyconverterapi-v1.jar /build/currencyconverterapi-v1.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/build/currencyconverterapi-v1.jar"]