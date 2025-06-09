# ————— Etapa de compilación —————
FROM maven:3.9.7-eclipse-temurin-21 AS build
WORKDIR /app
COPY Backend/Spring/demo/pom.xml . 
RUN mvn dependency:go-offline
COPY Backend/Spring/demo/src ./src
RUN mvn clean package -DskipTests

# ————— Etapa de ejecución —————
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copiamos JAR
COPY --from=build /app/target/*.jar app.jar

# Copiamos script de espera
COPY Backend/Spring/demo/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
CMD ["/wait-for-it.sh", "${DB_HOST}:${DB_PORT}", "--", "java", "-jar", "/app/app.jar"]
