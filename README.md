# api-for-evaluating-cinematographic-works
API para análise de filmes/séries com Watchlist e comentários da comunidade.

# aplication.properties
## Conexão com o banco de dados PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/ecw_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=org.postgresql.Driver

## Configurações recomendadas para o Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true