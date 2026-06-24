# merx-java-sdk-examples

[![build](https://github.com/merx-delevopers/merx-java-sdk-examples/actions/workflows/build.yml/badge.svg)](https://github.com/merx-delevopers/merx-java-sdk-examples/actions/workflows/build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/tech.merx.sdk/sdk-api?label=sdk-api)](https://central.sonatype.com/artifact/tech.merx.sdk/sdk-api)
[![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-11%2B-orange.svg)](https://adoptium.net/)

Exemplos **executáveis** de integração com a plataforma **Merx** usando o [SDK Java oficial](https://developers.merx.tech) (`tech.merx.sdk`): a **mesma jornada de negócio** — cadastro, compliance e negociação — implementada em **três stacks** (Java puro, Spring Boot e Quarkus), para você comparar lado a lado e copiar o que servir.

> 📚 Documentação completa de integração: **https://developers.merx.tech**

<details>
<summary><b>Índice</b></summary>

- [Quickstart](#quickstart)
- [A jornada de negócio](#a-jornada-de-negócio)
- [Escolha sua stack](#escolha-sua-stack)
- [Setup](#setup)
- [Como rodar](#como-rodar)
- [Catálogo de referência da SDK](#catálogo-de-referência-da-sdk)
- [Notas ao rodar no sandbox](#notas-ao-rodar-no-sandbox)
- [Troubleshooting / FAQ](#troubleshooting--faq)
- [Tratamento de erros](#tratamento-de-erros)
- [Contribuindo](#contribuindo)
- [Links & suporte](#links--suporte)
- [Licença](#licença)

</details>

## Quickstart

Java 11+, Maven 3.6+ e um token de sandbox ([solicite aqui](#setup)).

```bash
git clone https://github.com/merx-delevopers/merx-java-sdk-examples.git
cd merx-java-sdk-examples
cp .env.example .env                       # preencha MERX_API_KEY
export MERX_API_KEY=<seu-token-sandbox>    # PowerShell: $env:MERX_API_KEY="<token>"
mvn -q -pl examples-basic -am compile exec:java
```

Isso roda a jornada completa em Java puro e imprime um resumo `[ OK ] / [SKIP] / [FAIL]` de cada etapa. O SDK é baixado do Maven Central — nenhum setup extra.

## A jornada de negócio

As três aplicações rodam exatamente o mesmo fluxo ponta a ponta, exercitando boa parte da superfície do SDK:

| Domínio | Etapas |
|---|---|
| **Cadastro** (baseline + identity) | produtor → endereço → usuário/signatário → fazenda (com CAR) → armazém → local de entrega → carteira; releitura e listagem |
| **Compliance** (carbono) | relatórios CBIOS, EUDR e socioambiental + consulta de CAR |
| **Negociação** (order) | compromisso de compra → entrega → releitura → histórico de volume → rastreabilidade |
| **Mercado** (somente leitura) | cotações em tempo real (DataFeed) + produtividade por CAR/cultura |

Cada etapa é **resiliente**: uma falha pontual no sandbox (ex.: relatório para um CAR fictício) é registrada e a jornada continua, devolvendo o resumo no fim.

## Escolha sua stack

A mesma jornada, três estilos de integração. O que muda entre eles é só **como o `MerxClient` é construído** (ciclo de vida/config) e **como a jornada é exposta**.

| Módulo | Stack | O que demonstra | Porta | Como rodar |
|---|---|---|---|---|
| `examples-basic` | Java puro (console) | `MerxClient` manual + `main()`, fluxo encadeado explícito | — | `mvn -q -pl examples-basic exec:java` |
| `examples-spring` | Spring Boot 2.7 (REST) | `MerxClient` `@Bean` + `@Service` + `@RestController` + `@RestControllerAdvice` | 8080 | `mvn -pl examples-spring spring-boot:run` |
| `examples-quarkus` | Quarkus 2.16 (REST) | `MerxClient` `@Produces` (CDI) + JAX-RS + `ExceptionMapper` | 8081 | `mvn -pl examples-quarkus quarkus:dev` |

> O módulo `examples-core` concentra a jornada e **todas** as chamadas ao SDK (agnóstico de framework) — os três acima só fazem a fiação e a exposição. Por isso o `examples-core` é também o **canário de DX**: se ele parar de compilar, a API pública do SDK mudou.

```
merx-java-sdk-examples/        (pom pai, Maven multi-módulo)
├── examples-core/             jornada + todas as chamadas ao SDK + catálogo de referência
├── examples-basic/            Java puro, console
├── examples-spring/           Spring Boot (REST)
└── examples-quarkus/          Quarkus (REST)
```

## Setup

**Requisitos**

- Java 11+ · Maven 3.6+
- Token de API Merx (sandbox) — solicite via `support-api@merx.tech` (assunto: `Chave de Integração - Sandbox`).

**Configuração** — copie o template e preencha:

```bash
cp .env.example .env
```

| Variável | Obrigatória | Descrição |
|---|---|---|
| `MERX_API_KEY` | sim | Token de integração. |
| `MERX_ENV` | não | `SANDBOX` (default → `homolog.api.merx.tech`) ou `PRODUCTION` (→ `api.merx.tech`). |
| `MERX_SAMPLE_CAR` | não | CAR **real** do sandbox para os relatórios de carbono retornarem dados. Sem ele, um CAR fictício é usado. |

**Dependência do SDK** — resolvida direto do Maven Central, sem repositório extra:

```xml
<dependency>
    <groupId>tech.merx.sdk</groupId>
    <artifactId>sdk-api</artifactId>
    <version>1.0.1</version>
</dependency>
```

## Como rodar

Build único (compila os 4 módulos e roda os testes do core):

```bash
mvn clean install
```

### Java puro (console)

```bash
mvn -q -pl examples-basic exec:java
```

Snippets mínimos autocontidos (o "olá mundo" do SDK):

```bash
mvn -q -pl examples-basic exec:java -Dexec.mainClass=com.cooperativa.demo.basic.snippets.QuickstartExample
mvn -q -pl examples-basic exec:java -Dexec.mainClass=com.cooperativa.demo.basic.snippets.ReportsExample
```

### Spring Boot (porta 8080)

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

```bash
curl -X POST http://localhost:8080/api/journey
```

### Quarkus (porta 8081)

```bash
mvn -pl examples-quarkus quarkus:dev
```

As mesmas rotas do Spring, na porta **8081** — ex.: `curl -X POST http://localhost:8081/api/journey`.

## Catálogo de referência da SDK

Além da jornada, o pacote `examples-core/.../core/catalog/` é uma **referência copiável de todos os endpoints da SDK**: uma classe por resource client (`ProducerExamples`, `OrderCommitmentExamples`, `CbiosExamples`, …), com **um método por endpoint** (todos os ~107 métodos públicos) e Javadoc PT-BR. Compila contra a SDK, então cobre 100% da API pública (e quebra o build se a API mudar). Não roda ponta a ponta — para um fluxo executável use a jornada (`CooperativaJourney`).

## Notas ao rodar no sandbox

A jornada é resiliente: passos que falham são marcados e o fluxo continua. Algumas etapas dependem de **dados de referência reais** da cooperativa do seu token — sem eles, falham de forma esperada:

| Etapa | Requer | Sem isso |
|---|---|---|
| Fazenda, consulta de CAR, produtividade | Um **CAR real** cadastrado (defina `MERX_SAMPLE_CAR`). | `car.not-found` (o backend valida o CAR contra o SICAR). |
| Compromisso de compra (negociação) | Uma **safra registrada** na cooperativa (`harvest`). | `harvest mandatory`. |
| Usuário do produtor (signatário) | Integração **ClickSign** ativa para a cooperativa. | `422 clickSign.unprocessable-entity` (pode ser intermitente no sandbox). |

Com um token de sandbox novo, o cadastro (produtor/endereço/armazém/local de entrega/usuário/carteira), a releitura e os relatórios EUDR/socioambiental tipicamente retornam `[ OK ]`; as etapas acima ficam `[FAIL]`/`[SKIP]` até você fornecer CAR/safra reais. Isso é proposital — demonstra o tratamento de erro e a resiliência.

## Troubleshooting / FAQ

- **`401` / "Authentication refused"** — token inválido, ausente ou de outro ambiente. Confira `MERX_API_KEY` e `MERX_ENV`.
- **Build falha resolvendo `tech.merx.sdk:sdk-api`** — rode `mvn -U clean install` para forçar atualização de metadados do Maven Central.
- **`car.not-found` / `harvest mandatory` / `422 clickSign`** — esperado no sandbox sem dados reais; veja [Notas ao rodar no sandbox](#notas-ao-rodar-no-sandbox).
- **Java 17+ instalado** — o build mira `release 11` e funciona; se houver erro de toolchain, aponte `JAVA_HOME` para um JDK 11. Spring Boot 2.7 / Quarkus 2.16 são usados de propósito (as versões 3.x exigem Java 17).
- **Windows (PowerShell)** — use `$env:MERX_API_KEY="<token>"` em vez de `export`.

## Tratamento de erros

O SDK lança uma hierarquia tipada (`MerxApiException` para respostas HTTP de erro; `MerxClientException` para falhas locais de rede/serialização). Cada exemplo demonstra o tratamento idiomático:

- **Java puro** — capturado no `JourneyReporter` (`examples-core`), que marca a etapa e segue.
- **Spring** — `@RestControllerAdvice` mapeia para o mesmo status HTTP.
- **Quarkus** — `ExceptionMapper`s JAX-RS fazem o mesmo.

## Contribuindo

Issues e PRs são bem-vindos. Abra uma issue descrevendo o caso (ou o endpoint da SDK que falta exemplo) e, para PRs, mantenha as três stacks rodando a **mesma** jornada e adicione ao `catalog/` o método correspondente a qualquer endpoint novo. O build de CI (`mvn clean verify`) precisa passar.

## Links & suporte

- SDK Java no Maven Central: [`tech.merx.sdk:sdk-api`](https://central.sonatype.com/artifact/tech.merx.sdk/sdk-api)
- Portal de integração / documentação: https://developers.merx.tech
- Suporte: `support-api@merx.tech`

## Licença

Distribuído sob a licença **Apache-2.0** — veja [LICENSE](LICENSE).
