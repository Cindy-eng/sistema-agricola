# Guia de Deploy no Render

Este guia explica como fazer deploy do backend Sistema Agrícola no Render.

## Pré-requisitos

1. Conta no [Render](https://render.com)
2. Repositório Git (GitHub, GitLab ou Bitbucket)
3. Código do projeto commitado e enviado para o repositório

## Passo a Passo

### 1. Preparar o Repositório

Certifique-se de que todos os arquivos necessários estão commitados:

```bash
git add .
git commit -m "Preparar para deploy no Render"
git push
```

### 2. Criar Banco de Dados PostgreSQL no Render

1. Acesse o [Dashboard do Render](https://dashboard.render.com)
2. Clique em **"New +"** → **"PostgreSQL"**
3. Configure:
   - **Name**: `sistema-agricola-db`
   - **Database**: `sistema_agricola`
   - **User**: `sistema_user`
   - **Region**: Escolha a região mais próxima (ex: Oregon)
   - **Plan**: Free (ou outro conforme necessário)
4. Clique em **"Create Database"**
5. **Anote as credenciais** que aparecerão (você precisará delas)

### 3. Criar Web Service no Render

1. No Dashboard, clique em **"New +"** → **"Web Service"**
2. Conecte seu repositório Git
3. Configure o serviço:

#### Configurações Básicas:
- **Name**: `sistema-agricola-api`
- **Region**: Mesma região do banco de dados
- **Branch**: `main` (ou sua branch principal)
- **Root Directory**: (deixe vazio se o projeto está na raiz)

#### Build & Deploy:
- **Environment**: `Java`
- **Build Command**: 
  ```bash
  ./mvnw clean install -DskipTests
  ```
- **Start Command**: 
  ```bash
  java -jar target/projecto-0.0.1-SNAPSHOT.jar
  ```

#### Variáveis de Ambiente:

Adicione as seguintes variáveis de ambiente:

| Key | Value | Descrição |
|-----|-------|-----------|
| `SPRING_PROFILES_ACTIVE` | `prod` | Ativa o perfil de produção |
| `JAVA_VERSION` | `21` | Versão do Java |
| `SPRING_DATASOURCE_URL` | *(veja abaixo)* | URL de conexão do banco |
| `SPRING_DATASOURCE_USERNAME` | *(do banco criado)* | Usuário do banco |
| `SPRING_DATASOURCE_PASSWORD` | *(do banco criado)* | Senha do banco |
| `JWT_SECRET` | *(gere uma chave segura)* | Chave secreta para JWT |
| `JWT_EXPIRATION` | `86400000` | Expiração do token (24h) |
| `PORT` | *(deixe vazio - Render define automaticamente)* | Porta do servidor |

**Para obter a URL do banco de dados:**
- Vá até o banco de dados criado no Render
- Na seção "Connections", copie a **"Internal Database URL"**
- Use essa URL como valor de `SPRING_DATASOURCE_URL`

**Para gerar JWT_SECRET:**
```bash
# No terminal, gere uma chave aleatória de 256 bits (32 caracteres)
openssl rand -base64 32
```

Ou use um gerador online: https://www.allkeysgenerator.com/Random/Security-Encryption-Key-Generator.aspx

### 4. Usando render.yaml (Alternativa)

Se preferir usar o arquivo `render.yaml`:

1. Certifique-se de que o arquivo `render.yaml` está no repositório
2. No Render Dashboard, clique em **"New +"** → **"Blueprint"**
3. Conecte seu repositório
4. O Render detectará automaticamente o `render.yaml` e criará os serviços

**Nota**: Se usar `render.yaml`, você ainda precisará configurar manualmente o `JWT_SECRET` nas variáveis de ambiente do serviço web.

### 5. Deploy

1. Clique em **"Create Web Service"**
2. O Render começará automaticamente o build e deploy
3. Acompanhe os logs para verificar se há erros
4. Quando o deploy terminar, você verá a URL do serviço (ex: `https://sistema-agricola-api.onrender.com`)

### 6. Verificar o Deploy

Após o deploy, teste os endpoints:

1. **Swagger UI**: `https://sua-url.onrender.com/swagger-ui.html`
2. **API Docs**: `https://sua-url.onrender.com/v3/api-docs`
3. **Health Check**: `https://sua-url.onrender.com/api/auth/register` (POST)

## Configurações Importantes

### Porta Dinâmica
O Render atribui uma porta dinâmica através da variável de ambiente `PORT`. O `application-prod.yml` já está configurado para usar essa variável.

### Banco de Dados
- Use a **Internal Database URL** para conexões dentro do Render (mais rápido e seguro)
- Use a **External Database URL** apenas se precisar conectar de fora do Render

### Logs
Os logs estão configurados para produção no `application-prod.yml`:
- Nível INFO para a aplicação
- Nível WARN para Hibernate e Security
- SQL não é exibido em produção

### Segurança
- **NUNCA** commite credenciais no código
- Use sempre variáveis de ambiente
- O `JWT_SECRET` deve ser único e seguro
- Em produção, considere usar um plano pago para melhor segurança

## Troubleshooting

### Erro de Build
- Verifique se o Java 21 está disponível
- Confirme que o `mvnw` tem permissão de execução
- Verifique os logs de build para erros específicos

### Erro de Conexão com Banco
- Verifique se as variáveis de ambiente estão corretas
- Confirme que está usando a Internal Database URL
- Verifique se o banco de dados está ativo

### Erro 503 ou Timeout
- O plano Free do Render coloca serviços em "sleep" após inatividade
- A primeira requisição após sleep pode demorar ~30 segundos
- Considere usar um plano pago para evitar sleep

### Erro de Porta
- Certifique-se de que `PORT` não está definida manualmente
- O Render define automaticamente essa variável

## Monitoramento

1. Acesse o Dashboard do Render
2. Clique no seu serviço
3. Veja a aba **"Logs"** para logs em tempo real
4. Veja a aba **"Metrics"** para métricas de performance

## Atualizações

Para atualizar o serviço:
1. Faça commit das mudanças
2. Push para o repositório
3. O Render detectará automaticamente e fará novo deploy

Ou manualmente:
1. No Dashboard, clique no serviço
2. Clique em **"Manual Deploy"** → **"Deploy latest commit"**

## Suporte

- [Documentação do Render](https://render.com/docs)
- [Spring Boot Deployment](https://spring.io/guides/gs/spring-boot-for-azure/)
- [Render Community](https://community.render.com/)

