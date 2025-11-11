# Exemplos de JSON para Testar os Endpoints

## 1. Registrar Novo Usuário

**Endpoint:** `POST /api/auth/register`

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "nome": "João Silva",
  "email": "joao@example.com",
  "password": "senha123"
}
```

**Resposta de Sucesso (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2FvQGV4YW1wbGUuY29tIiwiaWF0IjoxNzM0MDk2MDAwLCJleHAiOjE3MzQxODI0MDB9...",
  "email": "joao@example.com",
  "nome": "João Silva",
  "role": "USER"
}
```

---

## 2. Login

**Endpoint:** `POST /api/auth/login`

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "email": "joao@example.com",
  "password": "senha123"
}
```

**Resposta de Sucesso (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2FvQGV4YW1wbGUuY29tIiwiaWF0IjoxNzM0MDk2MDAwLCJleHAiOjE3MzQxODI0MDB9...",
  "email": "joao@example.com",
  "nome": "João Silva",
  "role": "USER"
}
```

---

## 3. Criar Parcela (Endpoint Protegido)

**Endpoint:** `POST /api/parcelas`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2FvQGV4YW1wbGUuY29tIiwiaWF0IjoxNzM0MDk2MDAwLCJleHAiOjE3MzQxODI0MDB9...
```

**Body:**
```json
{
  "nome": "Parcela Norte",
  "lat": -15.7975,
  "lon": -47.8919
}
```

---

## 4. Listar Parcelas (Endpoint Protegido)

**Endpoint:** `GET /api/parcelas`

**Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2FvQGV4YW1wbGUuY29tIiwiaWF0IjoxNzM0MDk2MDAwLCJleHAiOjE3MzQxODI0MDB9...
```

---

## 5. Criar Dispositivo (Endpoint Protegido)

**Endpoint:** `POST /api/dispositivos`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2FvQGV4YW1wbGUuY29tIiwiaWF0IjoxNzM0MDk2MDAwLCJleHAiOjE3MzQxODI0MDB9...
```

**Body:**
```json
{
  "deviceKey": "DEVICE-001",
  "tipo": "Sensor de Umidade",
  "modelo": "SM-2024",
  "firmware": "v1.0.0",
  "parcelaId": 1,
  "estado": "ACTIVO"
}
```

---

## 6. Ingestão de Telemetria (Endpoint Público - IoT)

**Endpoint:** `POST /api/telemetria/ingestao`

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "deviceKey": "DEVICE-001",
  "sensorId": 1,
  "ts": "2025-01-11T10:30:00Z",
  "valor": 65.5
}
```

---

## 7. Criar Sensor (Endpoint Protegido)

**Endpoint:** `POST /api/sensores`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2FvQGV4YW1wbGUuY29tIiwiaWF0IjoxNzM0MDk2MDAwLCJleHAiOjE3MzQxODI0MDB9...
```

**Body:**
```json
{
  "dispositivoId": 1,
  "tipo": "UMIDADE_SOLO",
  "unidade": "%"
}
```

---

## 8. Registrar Usuário Admin (Exemplo)

**Endpoint:** `POST /api/auth/register`

**Body:**
```json
{
  "nome": "Administrador",
  "email": "admin@example.com",
  "password": "admin123"
}
```

*Nota: O role será USER por padrão. Para criar um ADMIN, você precisaria modificar o código ou criar manualmente no banco de dados.*

---

## Exemplos de Erro

### Email já em uso (400 Bad Request)
```json
{
  "timestamp": "2025-01-11T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Email já está em uso",
  "path": "/api/auth/register"
}
```

### Credenciais inválidas (401 Unauthorized)
```json
{
  "timestamp": "2025-01-11T10:30:00Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Bad credentials",
  "path": "/api/auth/login"
}
```

### Token inválido ou expirado (401 Unauthorized)
```json
{
  "timestamp": "2025-01-11T10:30:00Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/parcelas"
}
```

---

## Como Testar com cURL

### 1. Registrar usuário:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@example.com",
    "password": "senha123"
  }'
```

### 2. Login:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "password": "senha123"
  }'
```

### 3. Usar token em requisição protegida:
```bash
curl -X GET http://localhost:8080/api/parcelas \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

---

## Como Testar com Postman/Insomnia

1. **Registrar/Login:**
   - Método: POST
   - URL: `http://localhost:8080/api/auth/register` ou `/api/auth/login`
   - Headers: `Content-Type: application/json`
   - Body: JSON (raw) com os exemplos acima

2. **Endpoints Protegidos:**
   - Método: GET/POST/PUT/DELETE conforme necessário
   - URL: `http://localhost:8080/api/...`
   - Headers: 
     - `Content-Type: application/json`
     - `Authorization: Bearer SEU_TOKEN_AQUI`
   - Body: JSON (raw) se necessário

---

## Notas Importantes

- O token JWT expira em 24 horas (configurável em `application.yml`)
- Guarde o token retornado no login/registro para usar nas requisições protegidas
- O endpoint `/api/telemetria/ingestao` é público (não requer autenticação) para permitir que dispositivos IoT enviem dados
- Todos os outros endpoints `/api/**` requerem autenticação via JWT

