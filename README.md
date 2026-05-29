# API for Evaluating Cinematographic Works (API-ECW)

Uma API RESTful robusta desenvolvida para o gerenciamento, análise e avaliação de obras cinematográficas. O projeto vai além do CRUD tradicional, focando em uma arquitetura Cloud-Native com consumo de APIs externas (TMDB), segurança avançada, testes automatizados e uma esteira de CI/CD com deploy real na nuvem da AWS.

---

## 🚀 Funcionalidades Principais

- **Catálogo de Obras:** Gerenciamento abrangente de filmes e séries, integrado com dados reais de mercado.
- **Sistema de Avaliações e Interações:** Criação de posts, comentários e notas analíticas.
- **Consumo de API Externa (TMDB):** Sincronização automática de dados de filmes e séries utilizando a API do The Movie Database, com otimização via cache.
- **Autenticação Segura:** Proteção de endpoints utilizando **Spring Security** e tokens **JWT**.
- **Infraestrutura Automatizada:** Banco de dados versionado com **Flyway**.

## 🛠️ Tecnologias e Ferramentas

**Backend & Segurança:**
- Java 25 | Spring Boot 4.x
- Spring Data JPA | Hibernate
- Spring Security | JWT
- Flyway Migration

**Infraestrutura Cloud & DevOps:**
- PostgreSQL & Amazon RDS (Produção)
- AWS ECS com AWS Fargate (Computação Serverless)
- Docker & Docker Compose
- GitHub Actions (CI/CD Pipeline automatizado)
- Docker Hub (Registry de imagens)

**Qualidade de Código:**
- JUnit 5 & Mockito

## 🔀 Arquitetura e Esteira de CI/CD

O projeto foi construído sob o princípio de **Infraestrutura Imutável**. O pipeline do GitHub Actions garante a qualidade e entrega do software em duas etapas principais:

1. **Integração Contínua (CI):** A cada Pull Request para a `main`, o GitHub provisiona um banco temporário, isola as chamadas externas com Mockito e roda a bateria de testes unitários.
2. **Entrega Contínua (CD):** Após a aprovação do PR, a imagem Docker é compilada, tagueada com o SHA do commit (para garantia de rollback) e enviada automaticamente para o Docker Hub, pronta para atualização *Zero Downtime* na AWS.

## 📦 Como Executar o Projeto Localmente

### Pré-requisitos
- Docker e Docker Compose instalados.

### Passos
1. Clone o repositório:
   ```bash
   git clone https://github.com/Pedro-areis/api-for-evaluating-cinematographic-works.git
   cd api-for-evaluating-cinematographic-works
   ```
2. Crie um arquivo .env na raiz do projeto contendo as variáveis de ambiente necessárias (banco de dados e chaves do TMDB). Exemplo:
    ```bash
    DB_PORT=5432
    DB_PASSWORD=sua_senha_local
    TMDB_SECRET_TOKEN=seu_token
    TMDB_API_PUBLIC_KEY=sua_chave
    ```
3. Suba a infraestrutura local:
   ```bash
    docker compose up -d
    ```
A API estará disponível na porta `8080`.

## 🧪 Como Testar a API

Você pode explorar e testar todos os endpoints da aplicação de duas maneiras:

**1. Swagger UI (Interface Interativa):**
Com a aplicação rodando (local ou na nuvem), acesse a documentação automatizada do OpenAPI/Swagger, onde é possível executar requisições diretamente pelo navegador:
- [Acessar Swagger UI](http://localhost:8080/swagger-ui/index.html)

**2. Postman Collection:**
Para testes mais profundos ou integração com sua própria workspace, disponibilizamos a coleção completa de endpoints (já configurada com exemplos de Payload e autenticação).
- Baixe o arquivo: [`/docs/API-ECW-Postman-Collection.json`](./docs/API-ECW-Postman-Collection.json)
- Importe diretamente no seu Postman e comece a testar.

## 🚧 Próximos Passos (Em Desenvolvimento)
O projeto está em evolução contínua para simular um ambiente de produção real. As próximas entregas incluem:

[ ] Mapeamento e refinamento da documentação de todos os endpoints via Swagger/OpenAPI.

[ ] Script de popularização do banco com massa de dados (usuários, posts e avaliações fictícias) para facilitar testes práticos.

[ ] Expansão da cobertura de testes unitários e de integração na camada de serviço.

## 👨‍💻 Autor
Desenvolvido por: Pedro Augusto Reis.