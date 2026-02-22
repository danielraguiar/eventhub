# EventHub API

API RESTful para gestão de eventos e venda de ingressos, desenvolvida como teste técnico para a vaga de Desenvolvedor Pleno.

---

## Tecnologias

| Tecnologia | Versão | Papel |
|---|---|---|
| Java | 17 | Linguagem principal |
| Spring Boot | 4.0.3 | Framework principal |
| Spring Data JPA | (gerenciado pelo BOM) | Persistência |
| Hibernate | 7 (Jakarta JPA 3.2) | ORM |
| H2 Database | (gerenciado pelo BOM) | Banco de dados |
| Lombok | (gerenciado pelo BOM) | Redução de boilerplate |
| JUnit 5 + Mockito | (gerenciado pelo BOM) | Testes unitários |
| Maven | 3.9.12 | Gerenciador de dependências |

---

## Como rodar o projeto

### Pré-requisitos

- Java 17+
- Maven 3.6+ (ou use o wrapper `mvnw` já incluído no repositório)

### Executar a aplicação

```bash
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

### Executar os testes

```bash
./mvnw test
```

### Acessar o console do banco de dados (H2)

Com a aplicação em execução, acesse `http://localhost:8080/h2-console` com as seguintes configurações:

| Campo | Valor |
|---|---|
| JDBC URL | `jdbc:h2:file:./data/eventhub` |
| User Name | `sa` |
| Password | *(vazio)* |

---

## Endpoints da API

### Eventos

| Método | Endpoint | Descrição | Status de sucesso |
|---|---|---|---|
| `GET` | `/events` | Lista todos os eventos | `200 OK` |
| `GET` | `/events/{id}` | Busca evento por ID | `200 OK` |
| `POST` | `/events` | Cria um novo evento | `201 Created` |
| `PUT` | `/events/{id}` | Atualiza um evento | `200 OK` |
| `DELETE` | `/events/{id}` | Remove um evento | `204 No Content` |

### Ingressos

| Método | Endpoint | Descrição | Status de sucesso |
|---|---|---|---|
| `POST` | `/tickets` | Realiza a compra de um ingresso | `201 Created` |
| `GET` | `/tickets/participant/{email}` | Lista ingressos de um participante | `200 OK` |

### Códigos de status utilizados

| Código | Significado |
|---|---|
| `200` | OK |
| `201` | Created |
| `204` | No Content |
| `400` | Bad Request (falha de validação ou regra inválida) |
| `404` | Not Found (evento não encontrado) |
| `409` | Conflict (participante já possui ingresso para o evento) |
| `422` | Unprocessable Entity (evento sem capacidade disponível) |
| `500` | Internal Server Error |

---

## Exemplos de uso (curl)

### Criar evento

```bash
curl -X POST http://localhost:8080/events \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Tech Conference 2026",
    "dateTime": "2026-08-15T09:00:00",
    "location": "São Paulo Convention Center",
    "capacity": 200
  }'
```

### Comprar ingresso

```bash
curl -X POST http://localhost:8080/tickets \
  -H "Content-Type: application/json" \
  -d '{
    "eventId": 1,
    "participantName": "Alice Silva",
    "participantEmail": "alice@example.com"
  }'
```

### Listar ingressos de um participante

```bash
curl http://localhost:8080/tickets/participant/alice@example.com
```

### Exemplo de resposta de erro (evento lotado)

```json
{
  "status": 422,
  "error": "Unprocessable Entity",
  "message": "Event with id 1 is at full capacity. No tickets available.",
  "timestamp": "2026-02-22T10:30:00",
  "fieldErrors": null
}
```

### Exemplo de resposta de erro (validação)

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "timestamp": "2026-02-22T10:30:00",
  "fieldErrors": [
    "name: Event name must not be blank",
    "dateTime: Event date must be in the future"
  ]
}
```

---

## Decisões técnicas

### Banco de dados: H2 file-based

Escolhi o H2 em modo de arquivo (`jdbc:h2:file:./data/eventhub`) pois:
- **Nenhuma dependência externa**: o projeto sobe com `./mvnw spring-boot:run` sem instalar nada além do JDK.
- **Persistência entre restarts**: diferente do modo in-memory, os dados sobrevivem a reinicializações durante o desenvolvimento e os testes manuais.
- **H2 Console**: permite inspecionar as tabelas diretamente no browser.

Para um ambiente de produção, a troca para PostgreSQL ou MySQL exigiria apenas alterar o driver e a URL no `application.yaml`.

### DTOs: Java Records

Utilizei `record` do Java 16+ em todos os DTOs por:
- **Imutabilidade**: registros são imutáveis por design, o que é o comportamento correto para um DTO de request/response.
- **Bean Validation nativa**: as anotações como `@NotBlank` e `@Future` funcionam diretamente nos componentes do record a partir do Jakarta Validation 3.0.
- **Zero boilerplate**: `equals`, `hashCode`, `toString` e o construtor canônico são gerados automaticamente pelo compilador, sem depender de processadores de anotação.

### Mapeamento de entidades para DTOs: manual (factory `from()`)

Cada DTO de resposta possui um método estático `from(Entity entity)`. Optei por não usar MapStruct pois:
- Para o escopo deste projeto (3 entidades, 5 DTOs), a lógica de mapeamento é trivial e totalmente legível.
- Evita a complexidade de configurar `annotationProcessorPaths` com dois processadores (Lombok + MapStruct) na ordem correta, o que é um ponto de falha comum.

### Lombok: apenas em entidades

Lombok foi aplicado somente nas classes `@Entity` (`Event`, `Participant`, `Ticket`), onde a mutabilidade é necessária (o JPA exige setters e um construtor sem argumentos). Os DTOs (records) e os services não usam Lombok.

**Nota sobre Spring Boot 4 + Lombok**: o Spring Boot 4 utiliza o Spring Framework 7, que exige declaração explícita do Lombok em `<annotationProcessorPaths>` no `maven-compiler-plugin`. Sem isso, o compilador não encontra os símbolos gerados pelo Lombok.

### Contador desnormalizado `soldTickets`

A entidade `Event` armazena um campo `soldTickets` em vez de contar os tickets via `COUNT(*)` a cada verificação de capacidade. Isso garante uma verificação de disponibilidade em **O(1)** e evita uma query adicional no caminho crítico de compra de ingresso.

### Tratamento de exceções centralizado

O `GlobalExceptionHandler` (`@RestControllerAdvice`) centraliza o mapeamento de exceções para respostas HTTP com o DTO `ErrorResponse`. Isso mantém os controllers e services livres de lógica de serialização de erros.

**Nota**: `HttpStatus.UNPROCESSABLE_ENTITY` foi depreciado no Spring Framework 7. O status 422 é referenciado como literal inteiro no handler para evitar o aviso de compilação.

### Estrutura de pacotes por camada (layer-based)

O projeto adota a estrutura convencional com pacotes por camada técnica (`controller`, `service`, `repository`, `model`, `dto`, `exception`), mantendo as responsabilidades de cada camada explicitamente visíveis na hierarquia de pacotes.

---

## Estrutura do projeto

```
src/
  main/
    java/com/danielaguiar/eventhub/
      controller/        EventController, TicketController
      service/           EventService, TicketService
      repository/        EventRepository, ParticipantRepository, TicketRepository
      model/             Event, Participant, Ticket
      dto/
        request/         CreateEventRequest, UpdateEventRequest, PurchaseTicketRequest
        response/        EventResponse, TicketResponse, ErrorResponse
      exception/         EventNotFoundException, EventFullException,
                         DuplicateTicketException, GlobalExceptionHandler
  test/
    java/com/danielaguiar/eventhub/
      service/           TicketServiceTest (12 casos de teste)
```
