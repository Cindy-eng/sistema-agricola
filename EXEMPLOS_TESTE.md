# 📋 Exemplos de JSON para Testar os Endpoints

## 🔐 Autenticação

### 1. Registrar Novo Usuário
```bash
POST http://localhost:8080/api/auth/register
Content-Type: application/json
```

```json
{
  "nome": "João Silva",
  "email": "joao@example.com",
  "password": "senha123"
}
```

**Resposta esperada:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "joao@example.com",
  "nome": "João Silva",
  "role": "USER"
}
```

---

### 2. Login
```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json
```

```json
{
  "email": "joao@example.com",
  "password": "senha123"
}
```

**Resposta esperada:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "joao@example.com",
  "nome": "João Silva",
  "role": "USER"
}
```

---

## 📦 Parcelas (Protegido - requer token)

### 3. Criar Parcela
```bash
POST http://localhost:8080/api/parcelas
Content-Type: application/json
Authorization: Bearer SEU_TOKEN_AQUI
```

```json
{
  "nome": "Parcela Norte",
  "lat": -15.7975,
  "lon": -47.8919
}
```

**Nota:** O campo `usuario` é automaticamente associado ao usuário autenticado. Não é necessário enviá-lo no JSON.

---

### 4. Listar Parcelas
```bash
GET http://localhost:8080/api/parcelas
Authorization: Bearer SEU_TOKEN_AQUI
```

---

### 5. Obter Parcela por ID
```bash
GET http://localhost:8080/api/parcelas/1
Authorization: Bearer SEU_TOKEN_AQUI
```

---

### 6. Atualizar Parcela
```bash
PUT http://localhost:8080/api/parcelas/1
Content-Type: application/json
Authorization: Bearer SEU_TOKEN_AQUI
```

```json
{
  "nome": "Parcela Norte Atualizada",
  "lat": -15.8000,
  "lon": -47.9000
}
```

---

### 7. Deletar Parcela
```bash
DELETE http://localhost:8080/api/parcelas/1
Authorization: Bearer SEU_TOKEN_AQUI
```

---

## 📱 Dispositivos (Protegido - requer token)

### 8. Criar Dispositivo
```bash
POST http://localhost:8080/api/dispositivos
Content-Type: application/json
Authorization: Bearer SEU_TOKEN_AQUI
```

```json
{
  "deviceKey": "DEVICE-001",
  "tipo": "Sensor de Umidade",
  "modelo": "SM-2024",
  "firmware": "v1.0.0",
  "parcela": {
    "id": 1,
    "nome": "Parcela Norte",
    "lat": -15.7975,
    "lon": -47.8919
  },
  "estado": "ACTIVO"
}
```

**Nota:** Você precisa criar uma Parcela primeiro e usar o objeto completo aqui.

---

### 9. Listar Todos os Dispositivos
```bash
GET http://localhost:8080/api/dispositivos
Authorization: Bearer SEU_TOKEN_AQUI
```

---

### 10. Obter Dispositivo por ID
```bash
GET http://localhost:8080/api/dispositivos/1
Authorization: Bearer SEU_TOKEN_AQUI
```

---

### 11. Listar Dispositivos por Parcela
```bash
GET http://localhost:8080/api/dispositivos/por-parcela/1?page=0&size=20
Authorization: Bearer SEU_TOKEN_AQUI
```

---

### 12. Alterar Estado do Dispositivo
```bash
PATCH http://localhost:8080/api/dispositivos/1/estado/INACTIVO
Authorization: Bearer SEU_TOKEN_AQUI
```

---

## 🔧 Sensores (Protegido - requer token)

### 13. Criar Sensor
```bash
POST http://localhost:8080/api/sensores
Content-Type: application/json
Authorization: Bearer SEU_TOKEN_AQUI
```

```json
{
  "dispositivoId": 1,
  "tipo": "UMIDADE_SOLO",
  "unidade": "%"
}
```

---

### 14. Obter Sensor por ID
```bash
GET http://localhost:8080/api/sensores/1
Authorization: Bearer SEU_TOKEN_AQUI
```

---

### 15. Listar Sensores por Dispositivo
```bash
GET http://localhost:8080/api/sensores/por-dispositivo/1
Authorization: Bearer SEU_TOKEN_AQUI
```

---

### 16. Deletar Sensor
```bash
DELETE http://localhost:8080/api/sensores/1
Authorization: Bearer SEU_TOKEN_AQUI
```

---

## 📊 Telemetria

### 17. Ingestão de Telemetria (PÚBLICO - não requer token)
```bash
POST http://localhost:8080/api/telemetria/ingestao
Content-Type: application/json
```

```json
{
  "deviceKey": "DEVICE-001",
  "sensorId": 1,
  "ts": "2025-01-11T10:30:00Z",
  "valor": 65.5
}
```

---

### 18. Obter Telemetria por Janela de Tempo (Protegido)
```bash
GET http://localhost:8080/api/telemetria/janela?sensorId=1&ini=2025-01-11T00:00:00Z&fim=2025-01-11T23:59:59Z
Authorization: Bearer SEU_TOKEN_AQUI
```

---

### 19. Obter Última Telemetria (Protegido)
```bash
GET http://localhost:8080/api/telemetria/ultima?sensorId=1
Authorization: Bearer SEU_TOKEN_AQUI
```

---

### 20. Agregar Telemetria (Protegido)
```bash
GET http://localhost:8080/api/telemetria/agregar?sensorId=1&ini=2025-01-11T00:00:00Z&fim=2025-01-11T23:59:59Z&granularidade=hour
Authorization: Bearer SEU_TOKEN_AQUI
```

---

## 🧪 Sequência de Teste Completa

### Passo 1: Registrar usuário
```json
POST /api/auth/register
{
  "nome": "Teste User",
  "email": "teste@example.com",
  "password": "senha123"
}
```
**Copie o token da resposta!**

### Passo 2: Criar Parcela
```json
POST /api/parcelas
Authorization: Bearer [TOKEN_DO_PASSO_1]
{
  "nome": "Parcela Teste",
  "lat": -15.7975,
  "lon": -47.8919
}
```
**Anote o ID da parcela criada!**

### Passo 3: Criar Dispositivo
```json
POST /api/dispositivos
Authorization: Bearer [TOKEN_DO_PASSO_1]
{
  "deviceKey": "TEST-DEVICE-001",
  "tipo": "Sensor IoT",
  "modelo": "Test Model",
  "firmware": "v1.0",
  "parcela": {
    "id": [ID_DO_PASSO_2],
    "nome": "Parcela Teste",
    "lat": -15.7975,
    "lon": -47.8919
  },
  "estado": "ACTIVO"
}
```
**Anote o ID do dispositivo criado!**

### Passo 4: Criar Sensor
```json
POST /api/sensores
Authorization: Bearer [TOKEN_DO_PASSO_1]
{
  "dispositivoId": [ID_DO_PASSO_3],
  "tipo": "TEMPERATURA",
  "unidade": "°C"
}
```
**Anote o ID do sensor criado!**

### Passo 5: Enviar Telemetria (sem autenticação)
```json
POST /api/telemetria/ingestao
{
  "deviceKey": "TEST-DEVICE-001",
  "sensorId": [ID_DO_PASSO_4],
  "ts": "2025-01-11T10:30:00Z",
  "valor": 25.5
}
```

---

## ⚠️ Erros Comuns

### Email já em uso
```json
{
  "timestamp": "2025-01-11T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Email já está em uso"
}
```

### Credenciais inválidas
```json
{
  "timestamp": "2025-01-11T10:30:00Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Bad credentials"
}
```

### Token inválido ou ausente
```json
{
  "timestamp": "2025-01-11T10:30:00Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource"
}
```

---

## 💡 Dicas

1. **Token JWT**: O token expira em 24 horas. Se receber erro 401, faça login novamente.
2. **Formato de Data**: Use formato ISO 8601: `2025-01-11T10:30:00Z`
3. **Headers**: Sempre inclua `Content-Type: application/json` para POST/PUT
4. **Authorization**: Para endpoints protegidos, sempre inclua `Authorization: Bearer [TOKEN]`
5. **Ordem**: Crie Parcela → Dispositivo → Sensor → Telemetria

