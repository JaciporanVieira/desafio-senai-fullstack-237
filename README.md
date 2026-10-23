# Mercado Futebol — Desafio Técnico SENAI/PE

Projeto desenvolvido para o processo seletivo **SENAI/PE — Desenvolvedor I (Fullstack), código 237**.

O objetivo do desafio é desenvolver uma aplicação para gerenciamento de jogadores de futebol, clubes, negociações e funcionalidades relacionadas à competição, utilizando uma API REST e uma interface web.

## 1. Status do projeto

A implementação foi concentrada inicialmente no backend, com foco no módulo de jogadores.

| Componente | Situação |
|---|---|
| Estrutura do projeto | Implementada |
| API REST de jogadores | Implementada |
| Validações e regras de negócio de jogadores | Implementadas |
| Persistência PostgreSQL | Configurada |
| Migrações com Flyway | Configuradas |
| Testes automatizados do backend | Implementados |
| Estrutura inicial do frontend React + Vite | Estrutura inicial criada |

A entrega representa uma implementação parcial do escopo solicitado. Os itens pendentes estão identificados para manter a documentação fiel ao estado atual do código.

## 2. Tecnologias utilizadas

### Backend
- Java 17
- Spring Boot 3.5.16
- Spring Web — criação dos endpoints REST
- Spring Data JPA e Hibernate — persistência e mapeamento objeto-relacional
- Bean Validation — validação de requisições
- Maven — gerenciamento de dependências e build
- Flyway — versionamento das migrações do banco
- PostgreSQL 16 — banco de dados relacional
- JUnit e Spring Boot Test — testes automatizados

### Frontend
- React
- JavaScript
- Vite
- npm

O frontend utiliza React e Vite como tecnologias-base para a camada de apresentação da aplicação, com estrutura inicial para o desenvolvimento da interface e integração com a API REST de jogadores.

## 3. Arquitetura

O backend segue uma organização por responsabilidade no módulo de jogadores:

- **Controller:** recebe as requisições HTTP e define os endpoints.
- **Service:** concentra as operações e regras de negócio.
- **Repository:** fornece acesso aos dados por meio do Spring Data JPA.
- **Entity:** representa a entidade persistida no banco.
- **DTOs:** representam os dados recebidos e retornados pela API.
- **Specifications:** apoiam a construção de consultas e filtros.
- **Testes:** verificam comportamentos das regras e dos serviços.

Essa separação facilita a manutenção, os testes e a evolução da aplicação.

## 4. Funcionalidades implementadas no backend

### Jogadores

A API disponibiliza operações para:

- Cadastrar jogadores.
- Listar jogadores com paginação, filtros e ordenação.
- Consultar um jogador por identificador.
- Atualizar os dados de um jogador.
- Alterar sua condição física.
- Realizar exclusão lógica.

O serviço aplica validações de negócio e contempla cálculos relacionados aos atributos dos jogadores, incluindo valor-base e avaliação geral (overall).

### Persistência

O PostgreSQL é executado por Docker Compose. O backend utiliza Spring Data JPA e Hibernate, enquanto o Flyway gerencia as migrações do esquema do banco.

A propriedade `spring.jpa.hibernate.ddl-auto=validate` permite ao Hibernate verificar a compatibilidade entre as entidades e o esquema existente, sem delegar a ele a criação automática das tabelas.

## 5. Endpoints da API

URL base local: `http://localhost:8080`

Prefixo dos endpoints: `/api/v1/players`

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/api/v1/players` | Cadastrar jogador |
| GET | `/api/v1/players` | Listar jogadores |
| GET | `/api/v1/players/{id}` | Consultar jogador por ID |
| PUT | `/api/v1/players/{id}` | Atualizar jogador |
| PATCH | `/api/v1/players/{id}/condition` | Alterar condição física |
| DELETE | `/api/v1/players/{id}` | Excluir jogador logicamente |

### Paginação, filtros e ordenação

O endpoint de listagem aceita os seguintes parâmetros opcionais:

| Parâmetro | Finalidade |
|---|---|
| `page` | Página solicitada; padrão `0` |
| `limit` | Quantidade de registros; padrão `10` |
| `search` | Pesquisa textual |
| `position` | Filtra pela posição |
| `condition` | Filtra pela condição física |
| `free` | Filtra pela disponibilidade |
| `club_id` | Filtra pelo identificador do clube |
| `min_age` / `max_age` | Intervalo de idade |
| `min_value` / `max_value` | Intervalo de valor |
| `sort` | Campo de ordenação; padrão `id` |

Exemplo de listagem:

```http
GET http://localhost:8080/api/v1/players?page=0&limit=10
```

Exemplo de filtro por posição:

```http
GET http://localhost:8080/api/v1/players?position=ATA
```

O resultado depende dos dados existentes no banco e dos valores aceitos pelas regras de validação.

### Respostas HTTP

O cadastro retorna `201 Created`, incluindo o cabeçalho `Location`. A consulta e a atualização retornam `200 OK` quando concluídas com sucesso. A exclusão retorna `204 No Content`.

Os erros dependem da operação e das validações aplicadas pelo backend.

## 6. Pré-requisitos

Para executar o projeto localmente, instale:

- JDK 17.
- Maven compatível com o projeto.
- Docker Desktop com Docker Compose.
- Node.js e npm, caso queira executar a estrutura inicial do frontend.
- Git, para clonar o repositório.

## 7. Como executar o backend

Os comandos a seguir devem ser executados no terminal PowerShell, na raiz do repositório.

### 7.1. Clonar o repositório

```powershell
git clone https://github.com/JaciporanVieira/desafio-senai-fullstack-237.git
cd desafio-senai-fullstack-237
```

### 7.2. Iniciar o PostgreSQL

```powershell
docker compose up -d postgres
docker compose ps
```

Aguarde o serviço ficar saudável antes de iniciar a aplicação.

### 7.3. Conferir a configuração

A configuração local do backend está em:

`backend/src/main/resources/application.properties`

Por padrão, a aplicação utiliza:

| Propriedade | Valor local |
|---|---|
| Host | `localhost` |
| Porta | `5432` |
| Banco | `mercado_futebol` |
| Usuário | `mercado` |
| Senha | `mercado_dev_123` |

Essas credenciais são destinadas ao ambiente local de desenvolvimento. Não devem ser reutilizadas em produção.

### 7.4. Executar o backend

```powershell
mvn -f .\backend\pom.xml spring-boot:run
```

Com a aplicação iniciada, a API estará disponível em `http://localhost:8080`.

Para interromper a execução, pressione `Ctrl+C` no terminal.

## 8. Testes e validação

Para validar a estrutura do projeto Maven:

```powershell
mvn -f .\backend\pom.xml validate
```

Para executar a suíte de testes automatizados do backend:

```powershell
mvn -f .\backend\pom.xml clean test
```

Na última execução, foram executados 18 testes, todos aprovados, sem falhas, erros ou testes ignorados, conforme o resultado BUILD SUCCESS do Maven.

## 9. Estrutura do repositório

```text
desafio-senai-fullstack-237/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   └── test/
│   └── pom.xml
├── frontend/
│   ├── src/
│   ├── package.json
│   └── README.md
├── docker-compose.yml
└── README.md
```

O arquivo `frontend/README.md` permanece como a documentação original do template React + Vite. A documentação principal deste desafio é este arquivo, na raiz do repositório.

## 10. Decisões técnicas

- Utilização de Java 17 e Spring Boot 3 para implementar a API REST.
- Separação das responsabilidades entre controller, service, repository, entidades e DTOs.
- Uso de validações para proteger as regras de negócio do módulo de jogadores.
- Uso de PostgreSQL para persistência relacional.
- Uso de Flyway para versionar as alterações do esquema do banco.
- Uso de testes automatizados para verificar comportamentos do serviço e dos cálculos.
- Identificação explícita das funcionalidades pendentes para evitar apresentar como concluído aquilo que ainda não foi implementado.

## 11. Funcionalidades pendentes

A implementação ainda não contempla integralmente o escopo do desafio. Permanecem pendentes:

- Interface web funcional e integração do React com a API.
- Módulo completo de clubes e respectivas regras de negócio.
- Fluxos de compra e venda de jogadores.
- Contratos, histórico e demais regras das negociações.
- Funcionalidades de partidas, escalações, campeonato e classificação previstas no enunciado.

Esses itens não são apresentados como concluídos nesta entrega.

## 12. Uso de inteligência artificial

Ferramentas de inteligência artificial, incluindo o ChatGPT, foram utilizadas como apoio durante o desenvolvimento, para planejamento, esclarecimento de conceitos, análise de código, revisão e documentação.

A utilização dessas ferramentas é declarada de forma transparente, em conformidade com as orientações do desafio. O código e o projeto devem ser apresentados e explicados pelo candidato durante a avaliação.

## 13. Repositório

Código-fonte: https://github.com/JaciporanVieira/desafio-senai-fullstack-237

---

**Projeto:** Mercado Futebol
**Processo seletivo:** SENAI/PE — Desenvolvedor I Fullstack, código 237
