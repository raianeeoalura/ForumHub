# ForumHub

ForumHub é uma API REST desenvolvida em Java com Spring Boot, estruturada seguindo os princípios da Arquitetura Limpa (Clean Architecture). Ela simula as funcionalidades de um fórum de perguntas e respostas, suportando cadastro e autenticação de usuários, criação de tópicos, respostas, e oferece rotas protegidas por JWT.

## Tech Stack
- **Java 21**
- **Spring Boot 3.5**
- **PostgreSQL**
- **Maven**
- **Docker**
- **Spring Security + JWT**
- **Spring Data JPA**
- **OpenAPI (Swagger UI)**

## Funcionalidades
- Cadastro de usuários
- Autenticação via JWT
- Criação, listagem, visualização, edição e remoção de tópicos
- Criação de respostas em tópicos
- Paginação de listagens
- Testes automatizados (unitários e integração)
- Documentação automática das rotas

## Rotas principais

### Autenticação
- `POST /auth`
  - Autentica usuário e retorna o token JWT.

### Usuários
- `POST /users`
  - Cadastro de novo usuário.

### Tópicos
- `POST /topics` (protegida)
  - Cria um novo tópico.
- `GET /topics`
  - Lista todos os tópicos (paginação disponível).
- `GET /topics/{id}`
  - Busca um tópico por id, incluindo suas respostas (paginação nas respostas).
- `PUT /topics/{id}` (protegida)
  - Atualiza um tópico existente.
- `DELETE /topics/{id}` (protegida)
  - Remove um tópico.

### Respostas
- `POST /answers` (protegida)
  - Cria uma resposta para um tópico.

## Como rodar localmente

### 1. Subir o banco de dados com Docker
```bash
docker-compose up -d
```

### 2. Rodar a aplicação
```bash
./mvnw spring-boot:run
```
Ou, para buildar e rodar:
```bash
./mvnw package
java -jar target/ForumHub-0.0.1-SNAPSHOT.jar
```

### 3. Testes
```bash
./mvnw test
```

### 4. Documentação das rotas
Acesse [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) após iniciar o servidor para visualizar e testar todos os endpoints.

## Exemplo de fluxo de uso
1. Cadastre um usuário via `POST /users`
2. Autentique-se via `POST /auth` e obtenha o token JWT
3. Utilize o token para criar, editar ou remover tópicos e respostas nas rotas protegidas

## Estrutura de Pastas
```
src/
 ├─ main/java/br/alura/ForumHub/infra/controller  # Controllers REST
 ├─ main/java/br/alura/ForumHub/application/usecase  # Casos de uso
 ├─ main/java/br/alura/ForumHub/domain/entity        # Entidades de domínio
 ├─ main/java/br/alura/ForumHub/infra/persistence    # Persistência e banco
 ├─ test/java/br/alura/ForumHub/                    # Testes automatizados
```

## Informações adicionais
- Estrutura baseada em Arquitetura Limpa (Clean Architecture)
- Uso de validação (Jakarta Validation)
- Migrations automáticas com Flyway
- Permite fácil deploy via Docker

---

Projeto desenvolvido para fins educacionais utilizando padrões modernos do ecossistema Spring Boot. Para dúvidas, sugestões ou contato, abra uma issue neste repositório.

