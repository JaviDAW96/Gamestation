# ————— Etapa de compilación —————
FROM maven:3.9.7-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml . 
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# ————— Etapa de ejecución —————
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copiamos JAR
COPY --from=build /app/target/*.jar app.jar

# Copiamos script de espera
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Definimos valores por defecto (se podrán sobreescribir desde Render)
ENV DB_HOST=localhost \
    DB_PORT=5432 \
    DB_NAME=gamestation \
    DB_USER=postgres \
    DB_PASS=postgres

# Arrancamos esperando al servicio de BD
CMD ["/wait-for-it.sh", "${DB_HOST}:${DB_PORT}", "--", "java", "-jar", "/app/app.jar"]
