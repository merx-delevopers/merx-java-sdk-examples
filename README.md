# merx-java-sdk-examples

Aplicação de exemplo de integração com a plataforma **Merx** usando o [SDK Java oficial](https://developers.merx.tech) (`tech.merx.sdk`).

Uma empresa fictícia — a **Cooperativa Demo** — executa a **mesma jornada de negócio** em **três stacks** diferentes (Java puro, Spring Boot e Quarkus), para você comparar lado a lado como a integração fica em cada uma.

> Documentação completa de integração: **https://developers.merx.tech**

## A jornada de negócio

As três aplicações rodam exatamente o mesmo fluxo ponta a ponta, exercitando boa parte da superfície do SDK:

| Domínio | Etapas |
|---|---|
| **Cadastro** (baseline + identity) | produtor → endereço → usuário/signatário → fazenda (com CAR) → armazém → local de entrega → carteira; releitura e listagem |
| **Compliance** (carbono) | relatórios CBIOS, EUDR e socioambiental + consulta de CAR |
| **Negociação** (order) | compromisso de compra → entrega → releitura → histórico de volume → rastreabilidade |
| **Mercado** (somente leitura) | cotações em tempo real (DataFeed) + produtividade por CAR/cultura |

Cada etapa é **resiliente**: uma falha pontual no sandbox (ex.: relatório para um CAR fictício) é registrada e a jornada continua, devolvendo um resumo com `✓ ok / ∅ pulados / ✗ falhas`.

## Estrutura (Maven multi-módulo)

```
merx-java-sdk-examples/        (pom pai)
├── examples-core/             jornada + TODAS as chamadas ao SDK (fonte única, agnóstica de framework)
│   └── .../core/catalog/      catálogo de referência: 1 classe por resource client, 1 método por
│                              endpoint da SDK (todos os ~107 métodos públicos), com Javadoc
├── examples-basic/            Java puro, console: MerxClient manual + main()
├── examples-spring/           Spring Boot: MerxClient @Bean + @RestController + @RestControllerAdvice
└── examples-quarkus/          Quarkus: MerxClient @Produces (CDI) + JAX-RS + ExceptionMapper
```

> O pacote `core/catalog` (`ProducerExamples`, `OrderCommitmentExamples`, `CbiosExamples`, …) é uma
> referência copiável de **todos** os endpoints da SDK — compila contra a SDK, então cobre toda a
> API pública (e quebra o build se a API mudar). Não roda ponta a ponta; para um fluxo executável
> use a jornada (`CooperativaJourney`).

O `examples-core` concentra as chamadas ao SDK; os outros três módulos só diferem em **como o `MerxClient` é construído** (ciclo de vida/config) e **como a jornada é disparada/exposta**. Por isso o `examples-core` é também o canário de DX: se ele parar de compilar, a API pública do SDK mudou.

## Requisitos

- Java 11+
- Maven 3.6+
- Token de API Merx (sandbox) — solicite via `support-api@merx.tech` (assunto: `Chave de Integração - Sandbox`).

## Dependência do SDK

Os exemplos consomem o SDK do **Maven Central**:

```xml
<dependency>
    <groupId>tech.merx.sdk</groupId>
    <artifactId>sdk-api</artifactId>
    <version>1.0.1</version>
</dependency>
```

Nenhum repositório extra precisa ser configurado — o SDK é resolvido direto do Maven Central.

## Configuração

Copie o template de variáveis de ambiente e preencha:

```bash
cp .env.example .env
```

| Variável | Obrigatória | Descrição |
|---|---|---|
| `MERX_API_KEY` | sim | Token de integração (sandbox). |
| `MERX_ENV` | não | `SANDBOX` (default) ou `PRODUCTION`. |
| `MERX_SAMPLE_CAR` | não | CAR **real** do sandbox para os relatórios de carbono retornarem dados. Sem ele, um CAR fictício é usado. |

No Windows (PowerShell): `$env:MERX_API_KEY = "<seu-token>"`.

## Como rodar

Primeiro, faça um build único (instala o `examples-core` no `~/.m2` e compila tudo):

```bash
export MERX_API_KEY=<seu-token>
mvn clean install
```

### 1) Java puro (console)

```bash
mvn -q -pl examples-basic exec:java
```

Snippets mínimos autocontidos (o "olá mundo" do SDK):

```bash
mvn -q -pl examples-basic exec:java -Dexec.mainClass=com.cooperativa.demo.basic.snippets.QuickstartExample
mvn -q -pl examples-basic exec:java -Dexec.mainClass=com.cooperativa.demo.basic.snippets.ReportsExample
```

### 2) Spring Boot (REST, porta 8080)

```bash
mvn -pl examples-spring spring-boot:run
```

| Método & rota | O que faz |
|---|---|
| `POST /api/journey` | Roda a jornada completa e devolve o resumo (JSON). |
| `POST /api/producers` | Cria um produtor fictício e devolve `{id, document}`. |
| `GET /api/producers/{id}` | Lê um produtor (`404` se não existir). |
| `GET /api/producers/{id}/farms` | Lista as fazendas do produtor. |
| `GET /api/market/quotes?symbols=SOJA,MILHO` | Cotações em tempo real. |
| `GET /api/market/productivity?car=...&culture=SOY&harvest=2024/2025` | Produtividade por CAR/cultura. |
| `GET /api/car/{code}` | Consulta um CAR. |

Exemplo: `curl -X POST http://localhost:8080/api/journey`

### 3) Quarkus (REST, porta 8081)

```bash
mvn -pl examples-quarkus quarkus:dev
```

As mesmas rotas do Spring, na porta **8081**. Exemplo: `curl -X POST http://localhost:8081/api/journey`

## Notas ao rodar no sandbox

A jornada é **resiliente**: passos que falham são marcados e o fluxo continua. Algumas etapas
dependem de **dados de referência reais** da cooperativa do seu token — sem eles, falham de forma
esperada (e o exemplo segue):

| Etapa | Requer | Sem isso |
|---|---|---|
| Fazenda, consulta de CAR, produtividade | Um **CAR real** cadastrado (defina `MERX_SAMPLE_CAR`). | `car.not-found` (o backend valida o CAR contra o SICAR). |
| Compromisso de compra (negociação) | Uma **safra registrada** na cooperativa (campo `harvest`). | `harvest mandatory`. |
| Usuário do produtor (signatário) | Integração **ClickSign** ativa para a cooperativa. | `422 clickSign.unprocessable-entity` (pode ser intermitente no sandbox). |

Em uma execução típica com um token de sandbox novo, o cadastro (produtor/endereço/armazém/local
de entrega/usuário/carteira), a releitura e os relatórios EUDR/socioambiental retornam `[ OK ]`.
As etapas acima ficam `[FAIL]`/`[SKIP]` até você fornecer CAR/safra reais. Isso é proposital —
demonstra justamente o tratamento de erro e a resiliência.

## Tratamento de erros

O SDK lança uma hierarquia tipada (`MerxApiException` para respostas HTTP de erro; `MerxClientException` para falhas locais de rede/serialização). Cada exemplo demonstra o tratamento idiomático:

- **Java puro** — capturado no `JourneyReporter` (`examples-core`), que marca a etapa e segue.
- **Spring** — `@RestControllerAdvice` mapeia para o mesmo status HTTP.
- **Quarkus** — `ExceptionMapper`s JAX-RS fazem o mesmo.

## Ambientes

- Sandbox: `https://homolog.api.merx.tech` (`Environment.SANDBOX`)
- Produção: `https://api.merx.tech` (`Environment.PRODUCTION`)

## Links

- SDK Java no Maven Central: [`tech.merx.sdk:sdk-api`](https://central.sonatype.com/artifact/tech.merx.sdk/sdk-api)
- Portal de integração / documentação: https://developers.merx.tech
- Suporte: `support-api@merx.tech`
