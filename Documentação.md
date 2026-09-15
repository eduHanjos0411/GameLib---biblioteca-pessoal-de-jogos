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

#### Endpoints de Autenticação (UsuarioController)

**POST `/api/v1/usuarios/cadastrar`** - Cadastrar novo usuário

Corpo da Requisição:
```json
{
  "nome": "João Silva",
  "email": "joao@example.com",
  "senha": "senha123"
}
```

Resposta (201 Created):
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@example.com",
  "dataCriacao": "2024-01-15T10:30:00"
}
```

---

**POST `/api/v1/usuarios/login`** - Autenticar usuário

Corpo da Requisição:
```json
{
  "email": "joao@example.com",
  "senha": "senha123"
}
```

Resposta (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "usuarioId": 1,
  "nome": "João Silva"
}
```

---

**GET `/api/v1/usuarios/{id}`** - Buscar usuário por ID

Resposta (200 OK):
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@example.com",
  "dataCriacao": "2024-01-15T10:30:00"
}
```

---

#### Endpoints de Biblioteca (BibliotecaController)

**POST `/api/v1/biblioteca`** - Adicionar jogo à biblioteca

Corpo da Requisição:
```json
{
  "titulo": "The Witcher 3: Wild Hunt",
  "plataforma": "PC",
  "status": "JOGANDO",
  "nota": 9,
  "comentario": "Excelente jogo!",
  "background_image": "https://media.rawg.io/...",
  "id": "123456"
}
```

Resposta (201 Created):
```json
{
  "id": 1,
  "idJogo": 101,
  "titulo": "The Witcher 3: Wild Hunt",
  "urlCapa": "https://media.rawg.io/...",
  "categoria": "Action, RPG",
  "plataforma": "PC",
  "statusJogo": "JOGANDO",
  "nota": 9,
  "opiniao": "Excelente jogo!",
  "dataAdicao": "2024-01-15"
}
```

---

**GET `/api/v1/biblioteca`** - Listar biblioteca do usuário

Resposta (200 OK):
```json
[
  {
    "id": 1,
    "idJogo": 101,
    "titulo": "The Witcher 3: Wild Hunt",
    "urlCapa": "https://media.rawg.io/...",
    "categoria": "Action, RPG",
    "plataforma": "PC",
    "statusJogo": "JOGANDO",
    "nota": 9,
    "opiniao": "Excelente jogo!",
    "dataAdicao": "2024-01-15"
  },
  {
    "id": 2,
    "idJogo": 102,
    "titulo": "Cyberpunk 2077",
    "urlCapa": "https://media.rawg.io/...",
    "categoria": "Action, RPG",
    "plataforma": "PC",
    "statusJogo": "FINALIZADO",
    "nota": 8,
    "opiniao": "Bom, mas com bugs",
    "dataAdicao": "2024-01-20"
  }
]
```

---

**GET `/api/v1/biblioteca?status=JOGANDO`** - Listar biblioteca filtrada por status

Parâmetros de Query:
- `status` (opcional) - Valores: `JOGANDO`, `FINALIZADO`, `ABANDONADO`, `NAO_INICIADO`

Resposta (200 OK):
```json
[
  {
    "id": 1,
    "idJogo": 101,
    "titulo": "The Witcher 3: Wild Hunt",
    "urlCapa": "https://media.rawg.io/...",
    "categoria": "Action, RPG",
    "plataforma": "PC",
    "statusJogo": "JOGANDO",
    "nota": 9,
    "opiniao": "Excelente jogo!",
    "dataAdicao": "2024-01-15"
  }
]
```

---

**PUT `/api/v1/biblioteca/{id}`** - Atualizar jogo da biblioteca

Corpo da Requisição:
```json
{
  "plataforma": "PC",
  "statusJogo": "FINALIZADO",
  "nota": 10,
  "opiniao": "Jogo incrível, recomendo!"
}
```

Resposta (200 OK):
```json
{
  "id": 1,
  "idJogo": 101,
  "titulo": "The Witcher 3: Wild Hunt",
  "urlCapa": "https://media.rawg.io/...",
  "categoria": "Action, RPG",
  "plataforma": "PC",
  "statusJogo": "FINALIZADO",
  "nota": 10,
  "opiniao": "Jogo incrível, recomendo!",
  "dataAdicao": "2024-01-15"
}
```

---

**DELETE `/api/v1/biblioteca/{id}`** - Remover jogo da biblioteca

Resposta (204 No Content):
```
(Sem corpo de resposta)
```

---

#### Endpoints de Jogos Externos (JogoExternoController)

**GET `/api/v1/jogos-externos/buscar?nome=The%20Witcher`** - Pesquisar jogos na RAWG API

Parâmetros de Query:
- `nome` (obrigatório) - Termo de busca para o título do jogo

Resposta (200 OK):
```json
[
  {
    "id": 3328,
    "name": "The Witcher 3: Wild Hunt",
    "background_image": "https://media.rawg.io/media/games/20a/20aa03ad8fea46f74c41fcc210dd2ba9.jpg"
  },
  {
    "id": 23692,
    "name": "The Witcher 3: Wild Hunt - Blood and Wine",
    "background_image": "https://media.rawg.io/media/games/951/951c1a53955b9c3e8a858b6c04e85bbf.jpg"
  }
]
```

---

### 3. Documentação de Fluxo e Comunicação

Descrição breve de como os componentes se comunicam:

- O **Frontend**, desenvolvido em React com TypeScript, utilizará os recursos de comunicação HTTP via Axios para consumir os endpoints da API.
- O **Backend** validará os dados recebidos antes de processar as regras de negócio na camada de serviço.
- As mensagens de erro da API serão padronizadas para que o frontend possa exibir informações claras ao jogador.
- Todos os endpoints protegidos requerem autenticação via token JWT enviado no header `Authorization: Bearer {token}`.

---

## Definição de Arquitetura

### 1. Visão Geral da Arquitetura

O sistema seguirá uma arquitetura de sistemas distribuídos, separando claramente as responsabilidades entre o cliente (interface) e o servidor (lógica e dados) através de uma API REST.

- **Padrão Arquitetural:** Layered Architecture (Arquitetura em Camadas) no backend para separar a lógica de negócio do acesso aos dados.

### 2. Tecnologias Selecionadas (Stack Tecnológica)

- **Frontend:** Desenvolvido em React com TypeScript para garantir uma interface web responsiva e consumo assíncrono de dados via API.
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
- **Variáveis de Ambiente:** A arquitetura prevê o uso de arquivos de configuração e variáveis de ambiente para gerenciar credenciais do banco de dados e configurações da aplicação entre diferentes ambientes.

### 5. Defesa de Decisões Técnicas

- "Optamos pelo Spring Boot no backend devido à sua robustez e facilidade de integração com o Maven, além do suporte ao desenvolvimento de APIs REST e persistência de dados."

- "A escolha do React com TypeScript para o frontend justifica-se pela estrutura baseada em componentes, tipagem estática e pelos recursos integrados para comunicação com APIs, facilitando o desenvolvimento da interface da biblioteca."

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

---

## Backend (Java + Spring Boot)

### 1. Modelo de Dados (Entidade BibliotecaJogo)

Seguindo a modelagem de dados do GameLib, a entidade `BibliotecaJogo` representa o vínculo entre um usuário e um jogo, armazenando informações específicas da biblioteca pessoal, como plataforma, status e nota.

```java
@Entity
@Table(name = "biblioteca_jogos")
public class BibliotecaJogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_jogo", nullable = false)
    private Jogo jogo;

    @NotBlank(message = "A plataforma é obrigatória")
    @Column(nullable = false)
    private String plataforma;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_jogo", nullable = false)
    private StatusJogo statusJogo;

    @Min(value = 0, message = "A nota mínima é 0")
    @Max(value = 10, message = "A nota máxima é 10")
    private Integer nota;

    @Column(columnDefinition = "TEXT")
    private String opiniao;

    @Column(name = "data_adicao", nullable = false, updatable = false)
    private LocalDate dataAdicao;

    @PrePersist
    protected void onCreate() {
        this.dataAdicao = LocalDate.now();

        if (this.statusJogo == null) {
            this.statusJogo = StatusJogo.NAO_INICIADO;
        }
    }

    // Getters e Setters
}
```

A entidade `Jogo` armazena os dados gerais do jogo:

```java
@Entity
@Table(name = "jogos")
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    @Column(nullable = false)
    private String titulo;

    private String categoria;

    @Column(name = "url_capa")
    private String urlCapa;

    @Column(name = "api_external_id")
    private String apiExternalId;

    // Getters e Setters
}
```

A entidade `Usuario` representa o jogador cadastrado no sistema:

```java
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Column(name = "senha_hash", nullable = false)
    private String senha;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    // Getters e Setters
}
```

Os status disponíveis para os jogos são representados pelo enum:

```java
public enum StatusJogo {
    JOGANDO,
    FINALIZADO,
    ABANDONADO,
    NAO_INICIADO
}
```

### 2. Regra de Negócio (Serviço de Biblioteca)

A implementação do caso de uso de adicionar um jogo à biblioteca é realizada pelo `BibliotecaService`.

Antes de criar o registro, o sistema verifica se o usuário já possui o mesmo jogo cadastrado para a mesma plataforma:

```java
@Service
public class BibliotecaService {

    private final BibliotecaJogoRepository bibliotecaJogoRepository;
    private final JogoRepository jogoRepository;
    private final UsuarioService usuarioService;
    private final RawgApiService rawgApiService;

    public BibliotecaJogoResponseDTO adicionarJogo(
            Long usuarioId,
            AdicionarJogoBibliotecaDTO dto) {

        Usuario usuario = usuarioService.buscarEntityPorId(usuarioId);

        // Localiza ou cria o jogo
        Jogo jogo = resolverOuCriarJogo(dto);

        // Verifica duplicidade
        boolean jaExiste =
            bibliotecaJogoRepository
                .existsByUsuarioIdAndJogoIdAndPlataformaIgnoreCase(
                    usuario.getId(),
                    jogo.getId(),
                    dto.plataforma()
                );

        if (jaExiste) {
            throw new RegraNegocioException(
                "Este jogo já está cadastrado em sua biblioteca para a plataforma "
                + dto.plataforma()
            );
        }

        // Cria o vínculo entre usuário e jogo
        BibliotecaJogo itemBiblioteca = new BibliotecaJogo();

        itemBiblioteca.setUsuario(usuario);
        itemBiblioteca.setJogo(jogo);
        itemBiblioteca.setPlataforma(dto.plataforma());
        itemBiblioteca.setStatusJogo(
            dto.status() != null
                ? dto.status()
                : StatusJogo.NAO_INICIADO
        );
        itemBiblioteca.setNota(dto.nota());
        itemBiblioteca.setOpiniao(dto.comentario());

        BibliotecaJogo salvo =
            bibliotecaJogoRepository.save(itemBiblioteca);

        return BibliotecaJogoResponseDTO.fromEntity(salvo);
    }
}
```

Além da adição, o serviço permite listar, filtrar, atualizar e remover jogos da biblioteca.

### 3. Validação de Dados no Backend

A validação ocorre tanto na entrada dos dados da API quanto na aplicação das regras de negócio no serviço.

#### Validação no DTO (Entrada da API)

Para adicionar um jogo, o GameLib utiliza Bean Validation diretamente no DTO:

```java
public record AdicionarJogoBibliotecaDTO(

    @NotBlank(message = "O título do jogo é obrigatório.")
    String titulo,

    @NotBlank(message = "A plataforma é obrigatória.")
    String plataforma,

    StatusJogo status,

    @Min(value = 0, message = "A nota mínima é 0.")
    @Max(value = 10, message = "A nota máxima é 10.")
    Integer nota,

    String comentario,
    String background_image,
    Long id
) {
}
```

Dessa forma, o sistema impede, por exemplo, que um jogo seja cadastrado sem título ou plataforma e também impede notas fora do intervalo de 0 a 10.

#### Validação de Regra de Negócio (Serviço)

Além das validações do DTO, o serviço verifica regras relacionadas à biblioteca do usuário.

A principal regra implementada é impedir que o mesmo jogo seja cadastrado novamente para a mesma plataforma:

```java
boolean jaExiste =
    bibliotecaJogoRepository
        .existsByUsuarioIdAndJogoIdAndPlataformaIgnoreCase(
            usuario.getId(),
            jogo.getId(),
            dto.plataforma()
        );

if (jaExiste) {
    throw new RegraNegocioException(
        "Este jogo já está cadastrado em sua biblioteca para a plataforma "
        + dto.plataforma()
    );
}
```

O serviço também garante que operações de atualização e remoção sejam realizadas somente sobre jogos pertencentes ao usuário autenticado:

```java
BibliotecaJogo item =
    bibliotecaJogoRepository
        .findByIdAndUsuarioId(itemBibliotecaId, usuarioId)
        .orElseThrow(() ->
            new ResourceNotFoundException(
                "Item não encontrado ou sem permissão para alteração."
            )
        );
```

### 4. Testes Automatizados no Backend

A suíte de testes utiliza **JUnit 5 e Mockito** para validar as principais regras de negócio do sistema.

Os testes devem ser executados durante o processo de build utilizando Maven:

```bash
mvn test
```

#### Teste Unitário para Adição de Jogo

O teste abaixo verifica se um jogo pode ser adicionado corretamente à biblioteca:

```java
@ExtendWith(MockitoExtension.class)
public class BibliotecaServiceTest {

    @Mock
    private BibliotecaJogoRepository bibliotecaJogoRepository;

    @Mock
    private JogoRepository jogoRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private BibliotecaService bibliotecaService;

    @Test
    @DisplayName("Deve adicionar jogo na biblioteca com sucesso")
    void adicionarJogoComSucesso() {

        when(usuarioService.buscarEntityPorId(1L))
            .thenReturn(usuario);

        when(jogoRepository
            .findByTituloIgnoreCase("Cyberpunk 2077"))
            .thenReturn(Optional.of(jogo));

        when(bibliotecaJogoRepository
            .existsByUsuarioIdAndJogoIdAndPlataformaIgnoreCase(
                1L, 10L, "PC"))
            .thenReturn(false);

        when(bibliotecaJogoRepository
            .save(any(BibliotecaJogo.class)))
            .thenReturn(bibliotecaJogo);

        BibliotecaJogoResponseDTO response =
            bibliotecaService.adicionarJogo(
                1L,
                adicionarDTO
            );

        assertNotNull(response);
        assertEquals("Cyberpunk 2077", response.titulo());
        assertEquals("PC", response.plataforma());
        assertEquals(
            StatusJogo.JOGANDO,
            response.statusJogo()
        );

        verify(
            bibliotecaJogoRepository,
            times(1)
        ).save(any(BibliotecaJogo.class));
    }
}
```

Também existe um teste para garantir que o sistema bloqueie jogos duplicados:

```java
@Test
@DisplayName(
    "Deve lançar RegraNegocioException se o jogo já estiver "
    + "cadastrado para a mesma plataforma"
)
void adicionarJogoDuplicadoNaMesmaPlataformaLancaExcecao() {

    when(usuarioService.buscarEntityPorId(1L))
        .thenReturn(usuario);

    when(jogoRepository
        .findByTituloIgnoreCase("Cyberpunk 2077"))
        .thenReturn(Optional.of(jogo));

    when(bibliotecaJogoRepository
        .existsByUsuarioIdAndJogoIdAndPlataformaIgnoreCase(
            1L, 10L, "PC"))
        .thenReturn(true);

    RegraNegocioException exception =
        assertThrows(
            RegraNegocioException.class,
            () -> bibliotecaService
                .adicionarJogo(1L, adicionarDTO)
        );

    assertTrue(
        exception.getMessage()
            .contains("já está cadastrado")
    );

    verify(
        bibliotecaJogoRepository,
        never()
    ).save(any(BibliotecaJogo.class));
}
```

### 5. Ambiente e Automação (Backend)

#### pom.xml (Maven - Backend)

O projeto utiliza Maven para gerenciamento das dependências e automação do build. A configuração atual utiliza **Java 21** e Spring Boot.

As principais dependências utilizadas são:

```xml
<properties>
    <java.version>21</java.version>
</properties>

<dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webmvc</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>

    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

</dependencies>
```

O projeto também utiliza:

* Spring Data JPA/Hibernate para persistência;
* Spring Security para autenticação;
* JWT para autorização;
* H2 para desenvolvimento/testes;
* PostgreSQL para produção;
* Lombok;
* JUnit e Mockito para testes.

#### application.properties (Spring Boot - Backend)

No ambiente de desenvolvimento, o sistema utiliza banco H2 em memória:

```properties
spring.application.name=GameLib
server.port=9000

# Configuração do Banco H2 em memória
spring.datasource.url=jdbc:h2:mem:gamelibdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Console H2
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA / Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# RAWG API
rawg.api.url=https://api.rawg.io/api
rawg.api.key=${RAWG_API_KEY}

# JWT
api.security.token.secret=${JWT_SECRET}
api.security.token.expiration-ms=86400000
```

Para produção, o projeto possui configurações específicas para PostgreSQL:

```properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

api.security.token.secret=${JWT_SECRET}

rawg.api.key=${RAWG_API_KEY}
rawg.api.url=https://api.rawg.io/api
```

> **Observação de segurança:** As chaves de API e segredos JWT devem ser armazenados como variáveis de ambiente e nunca commitados no repositório. No arquivo enviado, essas credenciais devem ser substituídas por variáveis de ambiente (`${VAR_NAME}`).

---

## Implementação do Frontend (React)

O frontend do GameLib utiliza **React com TypeScript**, Vite, Axios e Tailwind CSS.

### 1. Consumo da API (Serviço)

A comunicação com o backend é centralizada utilizando Axios:

```typescript
import axios from 'axios';

export const api = axios.create({
  baseURL:
    'https://gamelib-biblioteca-pessoal-de-jogos.onrender.com/api/v1',

  headers: {
    'Content-Type': 'application/json',
  },
});
```

O token JWT armazenado no navegador é automaticamente enviado nas requisições:

```typescript
api.interceptors.request.use((config) => {

  const token =
    localStorage.getItem('@GameLib:token');

  if (token) {
    config.headers.Authorization =
      `Bearer ${token}`;
  }

  return config;
});
```

O serviço de jogos disponibiliza as operações principais:

```typescript
export const gamesService = {

  async listarMeusJogos(): Promise<JogoColecao[]> {
    const { data } =
      await api.get<JogoColecao[]>("/jogos");

    return data;
  },

  async buscarJogosExternos(
    query: string
  ): Promise<JogoExternoDTO[]> {

    const { data } =
      await api.get<JogoExternoDTO[]>(
        "/jogos-externos/buscar",
        {
          params: {
            nome: query
          }
        }
      );

    return data;
  },

  async adicionarJogo(
    jogo: CriarJogoDTO
  ): Promise<JogoColecao> {

    const { data } =
      await api.post<JogoColecao>(
        "/jogos",
        jogo
      );

    return data;
  },

  async removerJogo(id: number): Promise<void> {
    await api.delete(`/jogos/${id}`);
  }
};
```

### 2. Componente de Busca (Interface)

O componente `AddGameModal` permite ao usuário pesquisar jogos através da API externa RAWG antes de adicioná-los à biblioteca.

O fluxo de busca pode ser representado da seguinte forma:

```tsx
const handleSearch = async (
  e: React.SubmitEvent<HTMLFormElement>
) => {

  e.preventDefault();

  if (!query.trim()) return;

  setSearching(true);

  try {

    const results =
      await gamesService.buscarJogosExternos(query);

    setSearchResults(results);

  } catch (err) {

    console.error(
      'Erro ao buscar jogo:',
      err
    );

  } finally {

    setSearching(false);
  }
};
```

Depois da pesquisa, o usuário pode selecionar um jogo e configurar:

* plataforma;
* status;
* nota;
* comentário.

O cadastro é realizado através do serviço:

```tsx
await gamesService.adicionarJogo({
    name: selectedGame.name,
    background_image:
        selectedGame.background_image,
    id: selectedGame.id,
    plataforma,
    status,
    nota: Number(nota),
    comentario
});
```

Na tela principal, o componente `Dashboard` apresenta a coleção do usuário em cards, permitindo visualizar capa, nome, plataforma, status, nota e comentário.

Também são disponibilizados filtros por status:

```tsx
const jogosFiltrados =
    filterStatus === 'TODOS'
        ? jogos
        : jogos.filter(
            j => j.status === filterStatus
          );
```

### 3. Validação de Dados no Frontend

A validação no frontend evita o envio de dados incompletos e fornece feedback imediato ao usuário.

No formulário de adição de jogos, por exemplo, a plataforma é obrigatória:

```tsx
<Input
    value={plataforma}
    onChange={(e) =>
        setPlataforma(e.target.value)
    }
    placeholder="Ex: PC, PS5, Switch"
    required
/>
```

A interface também limita a nota entre 0 e 10:

```tsx
<input
    type="range"
    min="0"
    max="10"
    step="0.5"
    value={nota}
    onChange={(e) =>
        setNota(Number(e.target.value))
    }
/>
```

O frontend realiza essas validações para melhorar a experiência do usuário, mas as regras também são obrigatoriamente verificadas no backend.

### 4. Ambiente e Automação (Frontend)

O frontend utiliza Vite para desenvolvimento e build da aplicação React.

As principais tecnologias utilizadas são:

* React 19;
* TypeScript;
* Vite;
* Axios;
* React Router;
* Tailwind CSS;
* Lucide React;
* ESLint.

O build é realizado através de:

```bash
npm run build
```

E a aplicação pode ser executada em ambiente de desenvolvimento através de:

```bash
npm run dev
```

#### `.env` (React - Frontend)

A URL da API deve ser configurada através de variável de ambiente para evitar que o endereço do backend fique fixo no código.

Para o Vite, a configuração recomendada é:

```env
# URL da API REST
VITE_API_URL=http://localhost:9000/api/v1

# Ambiente da aplicação
VITE_ENVIRONMENT=development
```

No código do Axios:

```typescript
export const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});
```

Dessa forma, é possível utilizar diferentes URLs para desenvolvimento e produção sem alterar o código-fonte da aplicação.
