# Variáveis de Ambiente para Render

## Configuração Manual no Dashboard

Se você não estiver usando o `render.yaml`, configure estas variáveis de ambiente manualmente no Dashboard do Render:

### Variáveis Obrigatórias

1. **SPRING_PROFILES_ACTIVE**
   - Valor: `prod`
   - Descrição: Ativa o perfil de produção

2. **SPRING_DATASOURCE_URL**
   - Valor: Copie a "Internal Database URL" do seu banco de dados no Render
   - Exemplo: `jdbc:postgresql://dpg-xxxxx-a.oregon-postgres.render.com/sistema_agricola`
   - Descrição: URL de conexão com o banco de dados

3. **SPRING_DATASOURCE_USERNAME**
   - Valor: Nome de usuário do banco (geralmente `sistema_user`)
   - Descrição: Usuário para conexão com o banco

4. **SPRING_DATASOURCE_PASSWORD**
   - Valor: Senha do banco de dados (fornecida pelo Render)
   - Descrição: Senha para conexão com o banco

5. **JWT_SECRET**
   - Valor: Gere uma chave aleatória de 32+ caracteres
   - Como gerar:
     ```bash
     openssl rand -base64 32
     ```
   - Ou use: https://www.allkeysgenerator.com/Random/Security-Encryption-Key-Generator.aspx
   - Descrição: Chave secreta para assinatura de tokens JWT

### Variáveis Opcionais

6. **JWT_EXPIRATION**
   - Valor: `86400000` (padrão: 24 horas em milissegundos)
   - Descrição: Tempo de expiração dos tokens JWT

7. **JAVA_VERSION**
   - Valor: `21`
   - Descrição: Versão do Java (Render geralmente detecta automaticamente)

8. **PORT**
   - Valor: *(deixe vazio - Render define automaticamente)*
   - Descrição: Porta do servidor (definida automaticamente pelo Render)

## Como Configurar no Render

1. Acesse seu serviço web no Dashboard do Render
2. Vá para a aba **"Environment"**
3. Clique em **"Add Environment Variable"**
4. Adicione cada variável conforme a lista acima
5. Clique em **"Save Changes"**
6. O serviço será automaticamente reiniciado

## Importante

- **NUNCA** commite valores reais de variáveis de ambiente no código
- Use sempre variáveis de ambiente para credenciais
- O `JWT_SECRET` deve ser único e seguro
- Use a **Internal Database URL** para melhor performance (conexões dentro do Render)
- Use a **External Database URL** apenas se precisar conectar de fora do Render

## Verificação

Após configurar, verifique nos logs se as variáveis estão sendo lidas corretamente:
- Procure por mensagens como "The following profiles are active: prod"
- Verifique se não há erros de conexão com o banco
- Confirme que a aplicação está rodando na porta correta

