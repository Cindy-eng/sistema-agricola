#!/bin/bash

# Script de teste para os endpoints
# Uso: ./test-endpoints.sh

BASE_URL="http://localhost:8080"
TOKEN=""

echo "=== Testando Endpoints do Sistema Agrícola ==="
echo ""

# Cores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Função para fazer requisições
make_request() {
    local method=$1
    local endpoint=$2
    local data=$3
    local use_token=$4
    
    if [ "$use_token" = "true" ] && [ -z "$TOKEN" ]; then
        echo -e "${RED}Erro: Token não disponível. Faça login primeiro.${NC}"
        return 1
    fi
    
    if [ "$use_token" = "true" ]; then
        if [ -z "$data" ]; then
            response=$(curl -s -w "\n%{http_code}" -X $method \
                -H "Authorization: Bearer $TOKEN" \
                "$BASE_URL$endpoint")
        else
            response=$(curl -s -w "\n%{http_code}" -X $method \
                -H "Content-Type: application/json" \
                -H "Authorization: Bearer $TOKEN" \
                -d "$data" \
                "$BASE_URL$endpoint")
        fi
    else
        if [ -z "$data" ]; then
            response=$(curl -s -w "\n%{http_code}" -X $method \
                "$BASE_URL$endpoint")
        else
            response=$(curl -s -w "\n%{http_code}" -X $method \
                -H "Content-Type: application/json" \
                -d "$data" \
                "$BASE_URL$endpoint")
        fi
    fi
    
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')
    
    echo -e "${YELLOW}Status: $http_code${NC}"
    echo "$body" | jq '.' 2>/dev/null || echo "$body"
    echo ""
    
    return 0
}

# 1. Registrar usuário
echo -e "${GREEN}1. Registrando novo usuário...${NC}"
register_data='{
  "nome": "Teste User",
  "email": "teste@example.com",
  "password": "senha123"
}'
make_request "POST" "/api/auth/register" "$register_data" false

# Extrair token da resposta (assumindo que o token está no campo "token")
TOKEN=$(echo "$body" | jq -r '.token' 2>/dev/null)
if [ -z "$TOKEN" ] || [ "$TOKEN" = "null" ]; then
    echo -e "${YELLOW}Token não encontrado. Tentando fazer login...${NC}"
    
    # 2. Login
    echo -e "${GREEN}2. Fazendo login...${NC}"
    login_data='{
      "email": "teste@example.com",
      "password": "senha123"
    }'
    make_request "POST" "/api/auth/login" "$login_data" false
    TOKEN=$(echo "$body" | jq -r '.token' 2>/dev/null)
fi

if [ -z "$TOKEN" ] || [ "$TOKEN" = "null" ]; then
    echo -e "${RED}Erro: Não foi possível obter token. Verifique se o servidor está rodando.${NC}"
    exit 1
fi

echo -e "${GREEN}Token obtido: ${TOKEN:0:50}...${NC}"
echo ""

# 3. Criar Parcela
echo -e "${GREEN}3. Criando parcela...${NC}"
parcela_data='{
  "nome": "Parcela Teste",
  "lat": -15.7975,
  "lon": -47.8919
}'
make_request "POST" "/api/parcelas" "$parcela_data" true

# Extrair ID da parcela
PARCELA_ID=$(echo "$body" | jq -r '.id' 2>/dev/null)
echo -e "${GREEN}Parcela criada com ID: $PARCELA_ID${NC}"
echo ""

# 4. Listar Parcelas
echo -e "${GREEN}4. Listando parcelas...${NC}"
make_request "GET" "/api/parcelas" "" true

# 5. Criar Cultura
echo -e "${GREEN}5. Criando cultura...${NC}"
cultura_data='{
  "nome": "Milho"
}'
make_request "POST" "/api/culturas" "$cultura_data" true

# 6. Listar Culturas
echo -e "${GREEN}6. Listando culturas...${NC}"
make_request "GET" "/api/culturas" "" true

# 7. Criar Dispositivo
echo -e "${GREEN}7. Criando dispositivo...${NC}"
dispositivo_data="{
  \"deviceKey\": \"DEVICE-TEST-001\",
  \"tipo\": \"Sensor de Umidade\",
  \"modelo\": \"SM-2024\",
  \"firmware\": \"v1.0.0\",
  \"parcela\": {
    \"id\": $PARCELA_ID,
    \"nome\": \"Parcela Teste\",
    \"lat\": -15.7975,
    \"lon\": -47.8919
  },
  \"estado\": \"ACTIVO\"
}"
make_request "POST" "/api/dispositivos" "$dispositivo_data" true

# Extrair ID do dispositivo
DISPOSITIVO_ID=$(echo "$body" | jq -r '.id' 2>/dev/null)
echo -e "${GREEN}Dispositivo criado com ID: $DISPOSITIVO_ID${NC}"
echo ""

# 8. Listar Dispositivos
echo -e "${GREEN}8. Listando dispositivos...${NC}"
make_request "GET" "/api/dispositivos" "" true

# 9. Criar Sensor
echo -e "${GREEN}9. Criando sensor...${NC}"
sensor_data="{
  \"dispositivoId\": $DISPOSITIVO_ID,
  \"tipo\": \"UMIDADE_SOLO\",
  \"unidade\": \"%\"
}"
make_request "POST" "/api/sensores" "$sensor_data" true

# Extrair ID do sensor
SENSOR_ID=$(echo "$body" | jq -r '.id' 2>/dev/null)
echo -e "${GREEN}Sensor criado com ID: $SENSOR_ID${NC}"
echo ""

# 10. Ingestão de Telemetria (público)
echo -e "${GREEN}10. Enviando telemetria...${NC}"
telemetria_data="{
  \"deviceKey\": \"DEVICE-TEST-001\",
  \"sensorId\": $SENSOR_ID,
  \"ts\": \"$(date -u +"%Y-%m-%dT%H:%M:%SZ")\",
  \"valor\": 65.5
}"
make_request "POST" "/api/telemetria/ingestao" "$telemetria_data" false

# 11. Obter Última Telemetria
echo -e "${GREEN}11. Obtendo última telemetria...${NC}"
make_request "GET" "/api/telemetria/ultima?sensorId=$SENSOR_ID" "" true

echo -e "${GREEN}=== Testes concluídos! ===${NC}"

