#!/bin/bash
# Script de build para Render

# Torna o mvnw executável
chmod +x ./mvnw 2>/dev/null || true

# Tenta usar mvnw, se falhar usa mvn
if [ -f "./mvnw" ]; then
    ./mvnw clean install -DskipTests
else
    mvn clean install -DskipTests
fi

