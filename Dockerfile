FROM azul/zulu-openjdk:17.0.10-jre

ARG JAR_FILE=/build/libs/pfit-1.0.jar

COPY ${JAR_FILE} /pfit-1.0.jar

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev", "/pfit-1.0.jar"]