ARG BASEIMAGEREGISTRY=""
ARG FROM_BUILD="${BASEIMAGEREGISTRY}maven:3.9.3-ibm-semeru-11-focal"
ARG FROM_PACKAGE="${BASEIMAGEREGISTRY}bellsoft/liberica-openjre-debian:11"

# ---------------------------- BUILD ----------------------------
FROM $FROM_BUILD as builder
COPY src /build/src
COPY pom.xml /build/pom.xml
COPY settings.xml /build/settings.xml
WORKDIR /build
RUN mvn -s /build/settings.xml clean package

# ---------------------------- PACKAGE ----------------------------
FROM $FROM_PACKAGE
COPY --from=builder /build/target/smev3-push-service-consumer-jar.jar /opt/app/smev3-push-service-consumer-jar.jar
ENV JAVA_OPTS=""
RUN groupadd -g 1001 appuser && useradd -r -u 1001 -g appuser appuser
USER appuser

ENTRYPOINT ["java", "-jar", "/opt/app/smev3-push-service-consumer-jar.jar"]
