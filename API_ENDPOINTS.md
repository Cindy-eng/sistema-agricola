# Endpoints da API

## Base URL: `http://localhost:8080`

---

## 🔐 Autenticação (Público)

### POST `/api/auth/register`
**Body:**
```json
{
  "nome": "string",
  "email": "string",
  "password": "string"
}
```

### POST `/api/auth/login`
**Body:**
```json
{
  "email": "string",
  "password": "string"
}
```

---

## 📦 Parcelas (Requer Auth)

### POST `/api/parcelas`
**Body:**
```json
{
  "nome": "string",
  "lat": 0.0,
  "lon": 0.0
}
```

### GET `/api/parcelas`
**Response:** `List<Parcela>`

### GET `/api/parcelas/{id}`
**Path:** `id` (Long)

### PUT `/api/parcelas/{id}`
**Path:** `id` (Long)
**Body:**
```json
{
  "nome": "string",
  "lat": 0.0,
  "lon": 0.0
}
```

### DELETE `/api/parcelas/{id}`
**Path:** `id` (Long)

---

## 🌾 Culturas (Requer Auth)

### POST `/api/culturas`
**Body:**
```json
{
  "nome": "string"
}
```

### GET `/api/culturas`
**Response:** `List<Cultura>`

### GET `/api/culturas/{id}`
**Path:** `id` (Long)

### PUT `/api/culturas/{id}`
**Path:** `id` (Long)
**Body:**
```json
{
  "nome": "string"
}
```

### DELETE `/api/culturas/{id}`
**Path:** `id` (Long)

---

## 📱 Dispositivos (Requer Auth)

### POST `/api/dispositivos`
**Body:**
```json
{
  "deviceKey": "string",
  "tipo": "string",
  "modelo": "string",
  "firmware": "string",
  "parcela": {
    "id": 0,
    "nome": "string",
    "lat": 0.0,
    "lon": 0.0
  },
  "estado": "ACTIVO" | "INACTIVO"
}
```

### GET `/api/dispositivos`
**Response:** `List<Dispositivo>`

### GET `/api/dispositivos/{id}`
**Path:** `id` (Long)

### GET `/api/dispositivos/por-parcela/{parcelaId}`
**Path:** `parcelaId` (Long)
**Query:** `page` (int, default: 0), `size` (int, default: 20)
**Response:** `Page<Dispositivo>`

### PATCH `/api/dispositivos/{id}/estado/{estado}`
**Path:** `id` (Long), `estado` ("ACTIVO" | "INACTIVO")

---

## 🔧 Sensores (Requer Auth)

### POST `/api/sensores`
**Body:**
```json
{
  "dispositivoId": 0,
  "tipo": "string",
  "unidade": "string"
}
```

### GET `/api/sensores/{id}`
**Path:** `id` (Long)

### GET `/api/sensores/por-dispositivo/{dispositivoId}`
**Path:** `dispositivoId` (Long)
**Response:** `List<Sensor>`

### DELETE `/api/sensores/{id}`
**Path:** `id` (Long)

---

## 📊 Telemetria

### POST `/api/telemetria/ingestao` (Público)
**Body:**
```json
{
  "deviceKey": "string",
  "sensorId": 0,
  "ts": "2025-01-11T10:30:00Z",
  "valor": 0.0
}
```

### GET `/api/telemetria/janela` (Requer Auth)
**Query:** `sensorId` (Long), `ini` (Instant ISO), `fim` (Instant ISO)
**Response:** `List<Telemetria>`

### GET `/api/telemetria/ultima` (Requer Auth)
**Query:** `sensorId` (Long)
**Response:** `Optional<Telemetria>`

### DELETE `/api/telemetria/purgar` (Requer Auth)
**Query:** `sensorId` (Long), `antesDe` (Instant ISO)
**Response:** `long` (quantidade deletada)

### GET `/api/telemetria/agregar` (Requer Auth)
**Query:** `sensorId` (Long), `ini` (Instant ISO), `fim` (Instant ISO), `granularidade` (String, default: "hour")
**Response:** `List<TelemetriaAgg>`

---

## 📡 Eventos IoT (Requer Auth)

### POST `/api/eventos`
**Body:**
```json
{
  "dispositivo": {
    "id": 0
  },
  "tipo": "string",
  "ts": "2025-01-11T10:30:00Z",
  "payload": "string"
}
```

### GET `/api/eventos/por-dispositivo/{dispositivoId}`
**Path:** `dispositivoId` (Long)
**Query:** `ini` (Instant ISO), `fim` (Instant ISO)
**Response:** `List<EventoIot>`

### DELETE `/api/eventos/purgar/{dispositivoId}`
**Path:** `dispositivoId` (Long)
**Query:** `antesDe` (Instant ISO)
**Response:** `long` (quantidade deletada)

---

## 🔧 Manutenções (Requer Auth)

### POST `/api/manutencoes`
**Body:**
```json
{
  "dispositivo": {
    "id": 0
  },
  "tipo": "string",
  "estado": "string"
}
```

### PATCH `/api/manutencoes/{id}/fechar`
**Path:** `id` (Long)

### GET `/api/manutencoes/por-dispositivo/{dispositivoId}`
**Path:** `dispositivoId` (Long)
**Query:** `estado` (String)
**Response:** `List<Manutencao>`

---

## ⚠️ Regras de Alerta (Requer Auth)

### POST `/api/regras`
**Body:**
```json
{
  "parcela": {
    "id": 0
  },
  "expressao": "string",
  "severidade": "string",
  "destino": "string",
  "activo": true
}
```

### PATCH `/api/regras/{id}/activo/{activo}`
**Path:** `id` (Long), `activo` (boolean)

### GET `/api/regras/activas/por-parcela/{parcelaId}`
**Path:** `parcelaId` (Long)
**Response:** `List<RegraAlerta>`

### DELETE `/api/regras/{id}`
**Path:** `id` (Long)

---

## 🔑 Autenticação

**Headers para endpoints protegidos:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Endpoints públicos:**
- `/api/auth/**`
- `/api/telemetria/ingestao`

**Todos os outros endpoints requerem autenticação JWT.**


