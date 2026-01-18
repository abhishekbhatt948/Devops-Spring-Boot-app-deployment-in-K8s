# ---------- Build stage ----------
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests


# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre

# Create non-root user
RUN useradd -r -u 1001 appuser

WORKDIR /app

COPY --from=builder /build/target/sample-spring-boot-app-0.0.1-SNAPSHOT.jar app.jar

# Change ownership
RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
