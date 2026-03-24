# API for Evaluating Cinematographic Works 🎥
API para análise de filmes/séries com Watchlist e comentários da comunidade.

## Configuração application.properties ⚙️

```java
# Conexão com o banco de dados PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:<porta>/seu-banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=org.postgresql.Driver

# Configurações recomendadas para o Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true

# Ignora a versão mais recente do PostgreSQL
spring.flyway.plugin-register.database-type-ignore-categories=true

# Chave Secreta para criação e validação de Token com JWT
api_ecw.token.secret="sua_chave_ultra_mega_secreta"
```
