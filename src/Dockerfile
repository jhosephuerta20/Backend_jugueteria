# Usa una imagen base de Java
FROM openjdk:17-jdk-slim AS builder

# Establecer el directorio de trabajo
WORKDIR /backendJugueteria

# Copia los archivos de Gradle y el código fuente
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Copia el código fuente
COPY src ./src

# Da permisos de ejecución al script de Gradle
RUN chmod +x gradlew

# Compila el proyecto
RUN ./gradlew build --no-daemon

# Etapa de ejecución
FROM openjdk:17-jdk-slim

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR desde la etapa de construcción
COPY --from=builder /app/build/libs/*.jar app.jar

# Exponer el puerto que usa la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]