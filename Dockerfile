# Étape 1 : Build
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : Image finale avec Java + Spark
FROM openjdk:17-jdk-slim
WORKDIR /app

# Téléchargement de Spark
#RUN apt-get update && apt-get install -y wget tar \
#    && wget https://archive.apache.org/dist/spark/spark-3.5.1/spark-3.5.1-bin-hadoop3.tgz \
#    && tar -xvzf spark-3.5.1-bin-hadoop3.tgz \
#    && mv spark-3.5.1-bin-hadoop3 /opt/spark \
#    && rm spark-3.5.1-bin-hadoop3.tgz
# Copier Spark déjà téléchargé
COPY spark-3.5.1-bin-hadoop3.tgz /tmp/
RUN tar -xvzf /tmp/spark-3.5.1-bin-hadoop3.tgz -C /opt && \
    mv /opt/spark-3.5.1-bin-hadoop3 /opt/spark && \
    rm /tmp/spark-3.5.1-bin-hadoop3.tgz

# Copie du JAR généré
COPY --from=builder /app/target/*.jar app.jar

# Variables d'environnement
ENV SPARK_HOME=/opt/spark
ENV PATH=$SPARK_HOME/bin:$PATH

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
