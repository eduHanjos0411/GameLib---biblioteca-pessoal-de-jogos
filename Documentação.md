# GameLib - Biblioteca pessoal de jogos

## Definição de Escopo

### 1. Mapeamento de Usuários

Nesta seção, identificamos quem interagirá com o sistema:

- **Jogador (Usuário Comum):** Responsável por pesquisar jogos, adicionar jogos à sua biblioteca pessoal, acompanhar o status de andamento, atribuir notas e remover jogos da biblioteca.

### 2. Requisitos Técnicos

Conforme as bases tecnológicas e habilidades exigidas para o desenvolvimento do sistema:

- **Frontend:** Interface web responsiva para interação do jogador com sua biblioteca de jogos.
- **Backend & API REST:** Servidor para processamento das regras de negócio e exposição de endpoints para consulta e gerenciamento da biblioteca.
- **Persistência de Dados:** Uso de banco de dados relacional para armazenar os usuários, jogos e suas respectivas bibliotecas.
- **Validação:** Implementação de regras no backend para garantir que os dados obrigatórios sejam informados e que cada jogador possa alterar somente os jogos de sua própria biblioteca.
- **Configuração por Ambiente:** Uso de variáveis de ambiente para separar as credenciais do banco de dados e configurações entre os ambientes de desenvolvimento e produção.

### 3. Backlog Inicial

Uma lista priorizada de funcionalidades para guiar o desenvolvimento incremental:

1. **Cadastro e Autenticação:** Cadastro e login para identificação do jogador.
2. **Gerenciamento da Biblioteca:** Adicionar, visualizar, editar e remover jogos da biblioteca pessoal.
3. **Pesquisa de Jogos:** Pesquisa de jogos por título no catálogo.
4. **Filtro por Status:** Filtrar os jogos da biblioteca de acordo com seu status de andamento.
5. **Teste de Integração:** Validar se a adição de um jogo atualiza corretamente os dados da biblioteca no banco de dados.

---

## Prototipagem e Contratos

### 1. Protótipos de Integração (Wireframes)

- **Tela de Busca de Jogos:** Um campo de texto para pesquisa e uma listagem de cards contendo os jogos encontrados. Ao clicar em "Adicionar", o frontend deve permitir a inclusão do jogo na biblioteca.

- **Tela da Biblioteca:** Listagem dos jogos pertencentes ao jogador, permitindo visualizar o título, plataforma, nota e status de andamento.

- **Tela de Cadastro/Edição:** Um formulário contendo o título e a plataforma do jogo, além de campos opcionais como nota e status.

### 2. Contratos da API (Documentação Técnica)

#### Exemplo de Endpoint: Busca de Jogos

- **Rota:** `GET /api/v1/jogos`
- **Parâmetros (Query):** `titulo` (string).

**Resposta de Sucesso (200 OK):**

```json
[
  {
    "id": 101,
    "titulo": "The Witcher 3: Wild Hunt",
    "plataforma": "PC"
  }
]
```

#### Exemplo de Endpoint: Adicionar Jogo à Biblioteca

- **Rota:** `POST /api/v1/biblioteca`

**Corpo da Requisição (JSON):**

```json
{
  "jogoId": 101,
  "plataforma": "PC",
  "status": "JOGANDO",
  "nota": 10
}
```

**Resposta de Erro (400 Bad Request):**

Caso o título ou a plataforma não sejam informados.

```json
{
  "erro": "Título e plataforma são campos obrigatórios."
}
```

### 3. Documentação de Fluxo e Comunicação

Descrição breve de como os componentes se comunicam:

- O **Frontend**, desenvolvido em Angular, utilizará os recursos de comunicação HTTP do framework para consumir os endpoints da API.
- O **Backend** validará os dados recebidos antes de processar as regras de negócio na camada de serviço.
- As mensagens de erro da API serão padronizadas para que o frontend possa exibir informações claras ao jogador.

---

## Definição de Arquitetura

### 1. Visão Geral da Arquitetura

O sistema seguirá uma arquitetura de sistemas distribuídos, separando claramente as responsabilidades entre o cliente (interface) e o servidor (lógica e dados) através de uma API REST.

- **Padrão Arquitetural:** Layered Architecture (Arquitetura em Camadas) no backend para separar a lógica de negócio do acesso aos dados.

### 2. Tecnologias Selecionadas (Stack Tecnológica)

- **Frontend:** Desenvolvido em Angular para garantir uma interface web responsiva e consumo assíncrono de dados via API.
- **Backend:** Implementado em Java com Spring Boot, utilizando Maven para gerenciamento de dependências e automação de build.
- **API REST:** Utilização de formato JSON para troca de mensagens, seguindo os verbos HTTP padronizados (GET, POST, PUT, DELETE).
- **Persistência de Dados:** Uso de um banco de dados relacional, como PostgreSQL ou MySQL, para garantir a integridade dos dados dos usuários e suas bibliotecas.

### 3. Organização do Repositório (Estrutura Git)

O repositório será organizado de forma a separar a aplicação frontend, o backend e a documentação:

- **`/frontend`:** Código fonte da interface do usuário desenvolvida em React.
- **`/backend`:** Lógica do servidor, modelos de dados e controladores da API.
- **`/docs`:** Documentação técnica e especificações do sistema.

### 4. Estratégia de Persistência e Integração

- **Camada de Dados:** Uso do Hibernate/Spring Data JPA para mapear as classes do backend para as tabelas do banco de dados.
- **Variáveis de Ambiente:** A arquitetura prevê o uso de arquivos de configuração e variáveis de ambiente para gerenciar credenciais do banco de dados e configurações da aplicação, separando o contexto de desenvolvimento do contexto de produção.

### 5. Defesa de Decisões Técnicas

- "Optamos pelo Spring Boot no backend devido à sua robustez e facilidade de integração com o Maven, além do suporte ao desenvolvimento de APIs REST e persistência de dados."

- "A escolha do Angular para o frontend justifica-se pela estrutura baseada em componentes e pelos recursos integrados para comunicação com APIs, facilitando o desenvolvimento da interface da biblioteca de jogos."

- "A escolha de um banco de dados relacional justifica-se pela necessidade de manter a integridade dos relacionamentos entre usuários, jogos e bibliotecas."

---

## Modelagem e Casos de Uso

### 1. Modelagem de Requisitos Detalhada

- **RF01 - Adicionar Jogo à Biblioteca:** O sistema deve permitir que o jogador adicione um jogo à sua biblioteca informando a plataforma e os demais dados disponíveis.

  - **Regra de Negócio:** O título e a plataforma devem ser informados para que o jogo seja adicionado à biblioteca.

- **RF02 - Atualizar Jogo:** O sistema deve permitir que o jogador atualize as informações de um jogo pertencente à sua biblioteca, como plataforma, nota e status de andamento.

- **RF03 - Remover Jogo:** O sistema deve permitir que o jogador remova um jogo de sua biblioteca.

- **RNF01 - Persistência:** Todos os registros de usuários, jogos e bibliotecas devem ser armazenados em banco de dados relacional para garantir a integridade dos dados.

### 2. Casos de Uso Técnicos

#### Caso de Uso: UC01 - Adicionar Jogo à Biblioteca

- **Ator:** Jogador.
- **Pré-condição:** Jogador identificado no sistema e jogo disponível para cadastro.

- **Fluxo Principal:**
  1. O jogador informa o título do jogo e seleciona a plataforma.
  2. O frontend envia os dados para a API.
  3. O backend valida os dados recebidos.
  4. O backend verifica as regras de negócio.
  5. O sistema cria um novo registro na biblioteca do jogador.
  6. A API retorna `201 Created` com os detalhes do jogo adicionado.

- **Fluxo de Exceção (Dados Obrigatórios Ausentes):** A API retorna `400 Bad Request` com a mensagem "Título e plataforma são campos obrigatórios".

### 3. Organização do Backlog Técnico

- **Tarefa 1:** Criar Migrations/Scripts SQL para as tabelas `Usuarios`, `Jogos` e `BibliotecaJogos`.

- **Tarefa 2:** Implementar as Entidades e os Repositórios de `Usuario`, `Jogo` e `BibliotecaJogo` no backend.

- **Tarefa 3:** Desenvolver o serviço de validação das regras de negócio para adição e edição de jogos.

- **Tarefa 4:** Criar os endpoints `POST /api/v1/biblioteca`, `PUT /api/v1/biblioteca/{id}` e `DELETE /api/v1/biblioteca/{id}` conforme os contratos de API definidos anteriormente.

- **Tarefa 5:** Implementar o tratamento global de erros para retornar mensagens claras em caso de falha nas regras de negócio.

### 4. Modelagem de Dados 

Descrição das principais entidades e seus relacionamentos:

- **Usuario:** `id`, `nome`, `email`, `senha_hash`.
- **Jogo:** `id`, `titulo`, `categoria`, `url_capa`.
- **BibliotecaJogo:** `id`, `id_usuario` (FK), `id_jogo` (FK), `plataforma`, `status`, `nota`, `data_adicao`.
