# Configurações de Deploy - Render

## Arquivos Criados

### 1. `render.yaml`
Arquivo de configuração do Render que define:
- Serviço web (API)
- Banco de dados PostgreSQL
- Variáveis de ambiente
- Comandos de build e start

### 2. `src/main/resources/application-prod.yml`
Perfil de produção com:
- Configuração de banco via variáveis de ambiente
- Porta dinâmica (PORT)
- Logging otimizado para produção
- Pool de conexões configurado

### 3. `.gitignore`
Arquivo atualizado para ignorar arquivos desnecessários

### 4. `DEPLOY.md`
Guia completo passo a passo para deploy

## Variáveis de Ambiente Necessárias

| Variável | Descrição | Exemplo |
|----------|-----------|---------|
| `SPRING_PROFILES_ACTIVE` | Perfil ativo | `prod` |
| `SPRING_DATASOURCE_URL` | URL do banco | `jdbc:postgresql://...` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco | `sistema_user` |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco | `***` |
| `JWT_SECRET` | Chave secreta JWT | *(gerar aleatoriamente)* |
| `JWT_EXPIRATION` | Expiração do token | `86400000` |
| `PORT` | Porta do servidor | *(definido pelo Render)* |

## Comandos de Build e Start

**Build Command:**
```bash
./mvnw clean install -DskipTests
```

**Start Command:**
```bash
java -jar target/projecto-0.0.1-SNAPSHOT.jar
```

## Checklist de Deploy

- [ ] Repositório Git configurado e código commitado
- [ ] Banco de dados PostgreSQL criado no Render
- [ ] Web Service criado no Render
- [ ] Variáveis de ambiente configuradas
- [ ] JWT_SECRET gerado e configurado
- [ ] Build e deploy executados com sucesso
- [ ] Swagger UI acessível
- [ ] Endpoints testados

## URLs Após Deploy

- **API Base**: `https://sua-url.onrender.com`
- **Swagger UI**: `https://sua-url.onrender.com/swagger-ui.html`
- **API Docs**: `https://sua-url.onrender.com/v3/api-docs`

## Notas Importantes

1. **Plano Free**: Serviços entram em sleep após inatividade
2. **Primeira Requisição**: Pode demorar ~30s após sleep
3. **Banco de Dados**: Use Internal URL para melhor performance
4. **Segurança**: Nunca commite credenciais no código

Para instruções detalhadas, consulte o arquivo `DEPLOY.md`.

