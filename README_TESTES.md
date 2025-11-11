# 🧪 Guia de Testes dos Endpoints

Este guia explica como testar os endpoints do Sistema Agrícola usando diferentes ferramentas.

## 📋 Pré-requisitos

1. **Servidor rodando**: Certifique-se de que a aplicação Spring Boot está rodando em `http://localhost:8080`
2. **Banco de dados**: PostgreSQL deve estar configurado e acessível

## 🛠️ Opções de Teste

### 1. REST Client (VS Code) - Recomendado ⭐

**Arquivo:** `test-endpoints.http`

**Como usar:**
1. Instale a extensão "REST Client" no VS Code
2. Abra o arquivo `test-endpoints.http`
3. Execute cada requisição clicando em "Send Request" acima de cada bloco
4. **Importante**: Após fazer login, copie o token e cole na variável `@token` no topo do arquivo

**Vantagens:**
- Interface visual no VS Code
- Fácil de usar
- Pode salvar múltiplas requisições
- Suporta variáveis

---

### 2. Script Bash (curl)

**Arquivo:** `test-endpoints.sh`

**Como usar:**
```bash
# Dar permissão de execução
chmod +x test-endpoints.sh

# Executar
./test-endpoints.sh
```

**Requisitos:**
- Bash instalado
- `curl` instalado
- `jq` instalado (para formatar JSON) - opcional mas recomendado

**Instalar jq:**
- **Linux**: `sudo apt-get install jq` ou `sudo yum install jq`
- **macOS**: `brew install jq`
- **Windows**: Use WSL ou Git Bash

**Vantagens:**
- Executa todos os testes automaticamente
- Mostra resultados coloridos
- Não precisa de ferramentas externas (apenas curl e jq)

---

### 3. Script Python

**Arquivo:** `test-endpoints.py`

**Como usar:**
```bash
# Instalar requests (se não tiver)
pip install requests

# Executar
python test-endpoints.py
```

**Requisitos:**
- Python 3.6+
- Biblioteca `requests`: `pip install requests`

**Vantagens:**
- Executa todos os testes automaticamente
- Fácil de modificar e estender
- Output colorido
- Tratamento de erros

---

### 4. Postman/Insomnia

**Como usar:**
1. Importe a coleção (crie manualmente baseado nos exemplos em `EXEMPLOS_TESTE.md`)
2. Configure a variável de ambiente `baseUrl` = `http://localhost:8080`
3. Execute a requisição de login primeiro
4. Copie o token e configure como variável de ambiente `token`
5. Use `{{token}}` nas requisições que precisam de autenticação

**Vantagens:**
- Interface gráfica completa
- Pode salvar coleções
- Suporta variáveis de ambiente
- Pode exportar/importar coleções

---

### 5. cURL Manual

**Exemplo básico:**

```bash
# 1. Registrar
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@example.com",
    "password": "senha123"
  }'

# 2. Login (copie o token da resposta)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "password": "senha123"
  }'

# 3. Criar Parcela (substitua TOKEN pelo token obtido)
curl -X POST http://localhost:8080/api/parcelas \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{
    "nome": "Parcela Norte",
    "lat": -15.7975,
    "lon": -47.8919
  }'
```

---

## 🔄 Sequência de Teste Recomendada

1. **Registrar usuário** ou **Login**
2. **Criar Parcela** (anote o ID)
3. **Criar Cultura**
4. **Criar Dispositivo** (use o ID da parcela)
5. **Criar Sensor** (use o ID do dispositivo)
6. **Enviar Telemetria** (use o deviceKey e sensorId)
7. **Consultar Telemetria**

---

## ✅ Verificações

Após executar os testes, verifique:

1. **Status HTTP**: Deve ser `200 OK` para sucesso, `201 Created` para criação
2. **Token JWT**: Deve ser retornado no login/registro
3. **Dados**: Devem ser retornados corretamente nas listagens
4. **Segurança**: Endpoints protegidos devem retornar `401 Unauthorized` sem token
5. **Multi-tenancy**: Cada usuário só vê seus próprios dados

---

## 🐛 Troubleshooting

### Erro de conexão
- Verifique se o servidor está rodando: `curl http://localhost:8080/api/auth/register`
- Verifique a porta no `application.yml`

### Erro 401 Unauthorized
- Verifique se o token está correto
- Verifique se o token não expirou (24 horas)
- Faça login novamente

### Erro 400 Bad Request
- Verifique o formato do JSON
- Verifique se todos os campos obrigatórios estão presentes
- Verifique se os IDs referenciados existem

### Erro 404 Not Found
- Verifique se o endpoint está correto
- Verifique se o recurso existe e pertence ao usuário

### Erro 500 Internal Server Error
- Verifique os logs do servidor
- Verifique a conexão com o banco de dados
- Verifique se as tabelas foram criadas

---

## 📝 Notas

- O token JWT expira em 24 horas (configurável em `application.yml`)
- O endpoint `/api/telemetria/ingestao` é público (não requer autenticação)
- Todos os outros endpoints requerem autenticação
- Cada usuário só vê seus próprios dados (Parcelas, Culturas, Dispositivos)

---

## 🔗 Links Úteis

- [Documentação Spring Security](https://spring.io/projects/spring-security)
- [Documentação JWT](https://jwt.io/)
- [REST Client Extension](https://marketplace.visualstudio.com/items?itemName=humao.rest-client)

