# Dockerfile para Spring Boot Application - Sistema Agrícola
# Multi-stage build para otimizar tamanho da imagem final

# ============================================
# Stage 1: Build
# ============================================
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copiar arquivos Maven (wrapper e configuração)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Dar permissão de execução ao mvnw
RUN chmod +x ./mvnw

# Baixar dependências (cache layer - acelera builds futuros)
RUN ./mvnw dependency:go-offline -B

# Copiar código fonte
COPY src ./src

# Build da aplicação (cria o JAR)
RUN ./mvnw clean package -DskipTests

# ============================================
# Stage 2: Runtime
# ============================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Criar usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar JAR da etapa de build
COPY --from=build /app/target/projecto-0.0.1-SNAPSHOT.jar app.jar

# Expor porta (Render define PORT dinamicamente via variável de ambiente)
EXPOSE 8080

# Variáveis de ambiente padrão (podem ser sobrescritas)
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Health check (opcional - Render pode usar)
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:${PORT:-8080}/swagger-ui.html || exit 1

# Comando para iniciar a aplicação
# Render define PORT automaticamente, Spring Boot lê via ${PORT:8080}
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

