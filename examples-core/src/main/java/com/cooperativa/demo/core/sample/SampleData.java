package com.cooperativa.demo.core.sample;

import com.merx.sdk.model.baseline.address.CreateAddressRequest;
import com.merx.sdk.model.baseline.deliveryPlace.CreateDeliveryPlaceRequest;
import com.merx.sdk.model.baseline.enums.ClickSignAuthType;
import com.merx.sdk.model.baseline.enums.ClickSignDeliveryType;
import com.merx.sdk.model.baseline.enums.ClickSignSignAsType;
import com.merx.sdk.model.baseline.enums.MaritalStatus;
import com.merx.sdk.model.baseline.enums.SignerType;
import com.merx.sdk.model.baseline.farm.CreateFarmRequest;
import com.merx.sdk.model.baseline.producer.CreateProducerRequest;
import com.merx.sdk.model.baseline.wallet.CreateWalletRequest;
import com.merx.sdk.model.baseline.warehouse.CreateWarehouseRequest;
import com.merx.sdk.model.carbon.cbios.CbiosReportRequest;
import com.merx.sdk.model.carbon.enums.Layer;
import com.merx.sdk.model.carbon.eudr.EudrReportRequest;
import com.merx.sdk.model.carbon.socioambiental.SocioambientalRequest;
import com.merx.sdk.model.identity.producer.CreateProducerUserRequest;
import com.merx.sdk.model.order.commitment.CreateOrderCommitmentRequest;
import com.merx.sdk.model.order.commitment.CreateShippingOrderCommitmentRequest;
import com.merx.sdk.model.order.delivery.CreateDeliveryRequest;
import com.merx.sdk.model.order.enums.DeliveryMode;
import com.merx.sdk.model.order.enums.ModalityType;
import com.merx.sdk.model.order.enums.OrderPaymentType;
import com.merx.sdk.model.order.enums.OrderType;
import com.merx.sdk.model.order.enums.ShippingType;
import com.merx.sdk.model.order.traceability.CreateTraceabilityRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Random;
import java.util.UUID;

/**
 * Fábrica de payloads de exemplo da "Cooperativa Demo".
 *
 * <p>Concentra TODOS os dados fictícios da jornada num só lugar — separados das chamadas ao SDK —
 * para o integrador ver claramente o formato de cada request. Cada instância gera um conjunto
 * coerente e único por execução (documento, CAR, e-mails derivam de um {@code runId} aleatório),
 * evitando colisão com cadastros de execuções anteriores.
 *
 * <p>Valores que precisam ser <b>reais</b> para o sandbox retornar dados (notadamente o CAR usado
 * nos relatórios de carbono) podem ser sobrescritos por variável de ambiente — veja {@link #car()}.
 */
public final class SampleData {

    private final String runId;
    private final String producerDocument; // CNPJ válido (pessoa jurídica)
    private final String userCpf;          // CPF válido do usuário/signatário
    private final String car;

    public SampleData() {
        this(new Random());
    }

    public SampleData(Random random) {
        this.runId = Integer.toHexString(random.nextInt(0x1000000));
        this.producerDocument = Documents.randomCnpj(random);
        this.userCpf = Documents.randomCpf(random);
        String envCar = System.getenv("MERX_SAMPLE_CAR");
        this.car = (envCar != null && !envCar.isBlank())
                ? envCar.trim()
                : "PR-4106902-" + runId.toUpperCase();
    }

    // ---- Valores estáveis reutilizados ao longo da jornada -------------------------------------

    public String runId() {
        return runId;
    }

    public String producerDocument() {
        return producerDocument;
    }

    public String userCpf() {
        return userCpf;
    }

    /**
     * CAR usado nas fazendas e relatórios. Por padrão é fictício; defina {@code MERX_SAMPLE_CAR}
     * com um CAR real cadastrado no sandbox para que os relatórios CBIOS/EUDR/Socioambiental
     * retornem dados de verdade.
     */
    public String car() {
        return car;
    }

    // ---- Baseline ------------------------------------------------------------------------------

    public CreateProducerRequest producer() {
        return CreateProducerRequest.builder()
                .companyName("Cooperativa Demo Produtor " + runId + " LTDA")
                .tradingName("Fazenda Demo " + runId)
                .socialIdentity(producerDocument)
                .email("produtor+" + runId + "@cooperativademo.com")
                .maritalStatus(MaritalStatus.MARRIED)
                .build();
    }

    public CreateAddressRequest producerAddress() {
        return CreateAddressRequest.builder()
                .street("Rodovia BR-277")
                .number("5000")
                .complement("KM 50")
                .neighborhood("Zona Rural")
                .zipCode("85000-000")
                .city("Guarapuava")
                .state("PR")
                .country("Brasil")
                .build();
    }

    public CreateProducerUserRequest producerUser() {
        return CreateProducerUserRequest.builder()
                .fullName("Maria Demo " + runId)
                .email("maria+" + runId + "@cooperativademo.com")
                .phoneNumber("+5542999990000")
                .socialId(userCpf)
                .auth(ClickSignAuthType.EMAIL)
                .delivery(ClickSignDeliveryType.NONE)
                .signAs(ClickSignSignAsType.SIGN)
                .type(SignerType.PRODUCER)
                .defaultSigner(true)
                .build();
    }

    public CreateFarmRequest farm() {
        return CreateFarmRequest.builder()
                .name("Fazenda Demo " + runId + " - Sede")
                .own(true)
                .car(car)
                .area("250.50")
                .latitude("-25.3935")
                .longitude("-51.4567")
                .build();
    }

    public CreateWarehouseRequest warehouse() {
        return CreateWarehouseRequest.builder()
                .name("Armazém Demo " + runId)
                .capacity(new BigDecimal("15000.00"))
                .document(producerDocument)
                .address(CreateAddressRequest.builder()
                        .street("Estrada do Armazém")
                        .number("100")
                        .neighborhood("Distrito Industrial")
                        .zipCode("85000-100")
                        .city("Guarapuava")
                        .state("PR")
                        .country("Brasil")
                        .build())
                .build();
    }

    public CreateDeliveryPlaceRequest deliveryPlace() {
        return CreateDeliveryPlaceRequest.builder()
                .place("Terminal Cooperativa Demo " + runId)
                .description("Local de entrega padrão da Cooperativa Demo")
                .build();
    }

    public CreateWalletRequest wallet(String ownerId) {
        return CreateWalletRequest.builder()
                .name("Carteira Demo " + runId)
                .description("Carteira de negociação da Cooperativa Demo")
                .ownerId(ownerId)
                .build();
    }

    // ---- Carbono / Compliance ------------------------------------------------------------------

    public CbiosReportRequest cbiosReport() {
        return CbiosReportRequest.builder()
                .producerName("Cooperativa Demo Produtor " + runId)
                .producerDocument(producerDocument)
                .username("integration+" + runId + "@cooperativademo.com")
                .name("Relatório CBIOS - Cooperativa Demo " + runId)
                .layer(Layer.SOY)
                .civilYear(2024)
                .harvestCode(2324)
                .build();
    }

    public EudrReportRequest eudrReport() {
        return EudrReportRequest.builder()
                .car(car)
                .producerName("Cooperativa Demo Produtor " + runId)
                .producerDocument(producerDocument)
                .build();
    }

    public SocioambientalRequest socioambientalReport() {
        return SocioambientalRequest.builder()
                .car(car)
                .producerName("Cooperativa Demo Produtor " + runId)
                .producerDocument(producerDocument)
                .build();
    }

    // ---- Order / Negociação --------------------------------------------------------------------

    /**
     * Negociação de COMPRA de soja. Os ids (produtor, emissor, local de entrega, carteira) vêm
     * das etapas anteriores da jornada, mantendo o cadastro internamente consistente.
     */
    public CreateOrderCommitmentRequest orderCommitment(UUID producerId,
                                                        UUID issuerId,
                                                        UUID deliveryPlaceId,
                                                        UUID walletId) {
        LocalDate today = LocalDate.now();
        return CreateOrderCommitmentRequest.builder()
                .amount(new BigDecimal("1000"))
                .producerId(producerId)
                .issuerId(issuerId)
                .currency("BRL")
                .product("SOJA")
                .deliveryPlaceId(deliveryPlaceId)
                .initialDeliveryDate(today.plusDays(30))
                .endDeliveryDate(today.plusDays(60))
                .harvest("2023/2024")
                .modality(ModalityType.DISPONIVEL)
                .payday(today.plusDays(90))
                .paymentType(OrderPaymentType.DINHEIRO)
                .unitOfMeasurement("SC")
                .price(new BigDecimal("120.50"))
                .priceUnitOfMeasurement("SC")
                .orderType(OrderType.COMPRA)
                .orderDate(OffsetDateTime.now())
                .walletId(walletId)
                .internalId("ERP-" + runId)
                .description("Compra de soja - Cooperativa Demo " + runId)
                .shipping(CreateShippingOrderCommitmentRequest.builder()
                        .type(ShippingType.CIF)
                        .price(new BigDecimal("5.00"))
                        .description("Frete CIF até o terminal da cooperativa")
                        .build())
                .build();
    }

    public CreateDeliveryRequest delivery(UUID commitmentId) {
        return CreateDeliveryRequest.builder()
                .contractId(commitmentId.toString())
                .deliveredVolume(new BigDecimal("200"))
                .deliveredVolumeUnitOfMeasure("sc")
                .deliveryDate(LocalDate.now())
                .car(car)
                .deliveryMode(DeliveryMode.PARTIAL)
                .build();
    }

    public CreateTraceabilityRequest traceability(UUID producerId, UUID userId, UUID farmId) {
        return CreateTraceabilityRequest.builder()
                .producerId(producerId)
                .userId(userId)
                .farmId(farmId)
                .allocatedVolume(new BigDecimal("200"))
                .unitOfMeasurement("SC")
                .build();
    }
}
