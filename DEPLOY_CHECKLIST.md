# Checklist de Deploy - Render

## ✅ Arquivos Criados/Modificados

### Arquivos de Configuração
- [x] `render.yaml` - Configuração do Render (Blueprint)
- [x] `src/main/resources/application-prod.yml` - Perfil de produção
- [x] `src/main/resources/application.yml` - Atualizado para usar variáveis de ambiente
- [x] `build.sh` - Script alternativo de build
- [x] `.gitignore` - Atualizado

### Documentação
- [x] `DEPLOY.md` - Guia completo passo a passo
- [x] `README_DEPLOY.md` - Resumo rápido
- [x] `RENDER_ENV_VARS.md` - Referência de variáveis de ambiente
- [x] `DEPLOY_CHECKLIST.md` - Este arquivo

## 📋 Pré-Deploy

### 1. Preparar Código
- [ ] Verificar que todos os arquivos estão commitados
- [ ] Fazer push para o repositório Git
- [ ] Verificar que não há credenciais hardcoded no código
- [ ] Testar build localmente: `./mvnw clean install -DskipTests`

### 2. Preparar Render Account
- [ ] Criar conta no Render (se ainda não tiver)
- [ ] Conectar repositório Git ao Render
- [ ] Escolher região (recomendado: Oregon para melhor performance)

## 🗄️ Banco de Dados

### Criar PostgreSQL
- [ ] Criar novo PostgreSQL no Render
- [ ] Nome: `sistema-agricola-db`
- [ ] Database: `sistema_agricola`
- [ ] User: `sistema_user`
- [ ] Plan: Free (ou outro conforme necessário)
- [ ] Anotar credenciais (URL, username, password)

## 🚀 Web Service

### Opção 1: Usando Blueprint (render.yaml)
- [ ] Criar novo Blueprint no Render
- [ ] Conectar repositório
- [ ] Render detectará automaticamente o `render.yaml`
- [ ] Revisar configurações geradas
- [ ] Adicionar `JWT_SECRET` manualmente nas variáveis de ambiente

### Opção 2: Configuração Manual
- [ ] Criar novo Web Service
- [ ] Conectar repositório
- [ ] Configurar:
  - Name: `sistema-agricola-api`
  - Environment: `Java`
  - Region: Mesma do banco
  - Build Command: `chmod +x ./mvnw && ./mvnw clean install -DskipTests || mvn clean install -DskipTests`
  - Start Command: `java -jar target/projecto-0.0.1-SNAPSHOT.jar`

### Variáveis de Ambiente
- [ ] `SPRING_PROFILES_ACTIVE` = `prod`
- [ ] `SPRING_DATASOURCE_URL` = (Internal Database URL)
- [ ] `SPRING_DATASOURCE_USERNAME` = (do banco)
- [ ] `SPRING_DATASOURCE_PASSWORD` = (do banco)
- [ ] `JWT_SECRET` = (gerar chave aleatória de 32+ caracteres)
- [ ] `JWT_EXPIRATION` = `86400000` (opcional)
- [ ] `JAVA_VERSION` = `21` (opcional)

## 🔍 Verificação Pós-Deploy

### Build
- [ ] Build completou com sucesso
- [ ] Sem erros de compilação
- [ ] JAR gerado corretamente

### Aplicação
- [ ] Aplicação iniciou sem erros
- [ ] Logs mostram "Started ProjectoApplication"
- [ ] Perfil `prod` está ativo
- [ ] Conexão com banco estabelecida

### Endpoints
- [ ] Swagger UI acessível: `https://sua-url.onrender.com/swagger-ui.html`
- [ ] API Docs acessível: `https://sua-url.onrender.com/v3/api-docs`
- [ ] Endpoint de registro funciona: `POST /api/auth/register`
- [ ] Endpoint de login funciona: `POST /api/auth/login`

### Banco de Dados
- [ ] Tabelas criadas automaticamente (ddl-auto: update)
- [ ] Pode criar usuário via API
- [ ] Pode fazer login e receber token JWT

## 🐛 Troubleshooting

### Se o build falhar:
- [ ] Verificar logs de build
- [ ] Confirmar que Java 21 está disponível
- [ ] Verificar se `mvnw` tem permissão de execução
- [ ] Tentar usar `mvn` diretamente se `mvnw` falhar

### Se a aplicação não iniciar:
- [ ] Verificar logs de runtime
- [ ] Confirmar que todas as variáveis de ambiente estão configuradas
- [ ] Verificar conexão com banco de dados
- [ ] Confirmar que o JAR foi gerado corretamente

### Se houver erro de conexão com banco:
- [ ] Verificar se está usando Internal Database URL
- [ ] Confirmar credenciais (username, password)
- [ ] Verificar se o banco está ativo
- [ ] Testar conexão externa se necessário

### Se endpoints retornarem 503:
- [ ] Aguardar ~30 segundos (serviço pode estar em sleep)
- [ ] Verificar se o serviço está ativo no Dashboard
- [ ] Considerar upgrade para plano pago (evita sleep)

## 📝 Notas Finais

- **Primeira requisição**: Pode demorar ~30s após sleep (plano Free)
- **Sleep mode**: Serviços Free entram em sleep após 15min de inatividade
- **Upgrade**: Considere plano pago para produção real
- **Monitoramento**: Use a aba Metrics no Dashboard para monitorar performance
- **Logs**: Sempre verifique os logs para diagnosticar problemas

## 🔗 Links Úteis

- Dashboard: https://dashboard.render.com
- Documentação: https://render.com/docs
- Suporte: https://community.render.com/

## ✅ Deploy Completo!

Após completar todos os itens acima, seu backend estará deployado e funcionando no Render! 🎉

