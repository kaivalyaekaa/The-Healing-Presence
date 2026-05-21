# =====================================================================
# The Healing Presence — production Dockerfile for Render / any container host.
#
# Two-stage build:
#   1) maven:3.9-eclipse-temurin-21 compiles + packages the WAR
#   2) eclipse-temurin:21-jre runs it with `java -jar target/healing-presence.war`
#      (Spring Boot's embedded Tomcat starts the WAR as if it were a JAR)
#
# Final image is ~250 MB. The build skips tests for faster CI cycles.
# =====================================================================

# ---------- Stage 1: build ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copy pom.xml first so the dependency layer caches across code-only changes.
COPY pom.xml ./
RUN mvn -B -e dependency:go-offline

# Now the source. Any change here re-triggers compile but not dep download.
COPY src ./src
RUN mvn -B -DskipTests clean package

# ---------- Stage 2: runtime ----------
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Non-root user for security.
RUN groupadd --system thp && useradd --system --gid thp --no-create-home thp
USER thp

# Copy the built WAR from the build stage.
COPY --from=build --chown=thp:thp /workspace/target/healing-presence.war /app/healing-presence.war

# Render injects PORT; default 8080 for local docker-compose.
ENV PORT=8080
EXPOSE 8080

# Spring Boot honours the PORT env var via server.port in application.properties.
# Memory-cap for Render's 512MB free tier.
ENTRYPOINT ["sh", "-c", "java -Xmx384m -XX:+UseG1GC -Dserver.port=${PORT:-8080} -jar /app/healing-presence.war"]
