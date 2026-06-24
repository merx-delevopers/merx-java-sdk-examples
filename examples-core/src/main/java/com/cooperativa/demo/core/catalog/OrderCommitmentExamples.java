package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.common.PageResponse;
import com.merx.sdk.model.order.commitment.CreateOrderCommitmentRequest;
import com.merx.sdk.model.order.commitment.CreateShippingOrderCommitmentRequest;
import com.merx.sdk.model.order.commitment.DeliveredVolumeRequest;
import com.merx.sdk.model.order.commitment.DigitalContractResponse;
import com.merx.sdk.model.order.commitment.OrderCommitmentDeliveredVolumeHistoryResponse;
import com.merx.sdk.model.order.commitment.OrderCommitmentWorkflowStatusesResponse;
import com.merx.sdk.model.order.commitment.OrdersCommitmentsFarmWorkflowSummarizedResponse;
import com.merx.sdk.model.order.commitment.OrdersCommitmentsSummarizedFilter;
import com.merx.sdk.model.order.commitment.UpdateOrderCommitmentExternalReferenceRequest;
import com.merx.sdk.model.order.commitment.UpdateStatusCommitmentRequest;
import com.merx.sdk.model.order.commitment.WebhookOrderCommitmentOrderResponse;
import com.merx.sdk.model.order.enums.DeliveryMode;
import com.merx.sdk.model.order.enums.ModalityType;
import com.merx.sdk.model.order.enums.OrderPaymentType;
import com.merx.sdk.model.order.enums.OrderType;
import com.merx.sdk.model.order.enums.ShippingType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Catálogo de exemplos do recurso <b>orderCommitments</b> ({@code merx.orderCommitments()}).
 *
 * <p>Um método por endpoint público da SDK, com request de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — fluxo executável em
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class OrderCommitmentExamples {

    private final MerxClient merx;

    public OrderCommitmentExamples(MerxClient merx) {
        this.merx = merx;
    }

    /** Cria uma negociação de compra (COMPRA), incluindo o frete obrigatório. */
    public UUID create(UUID producerId, UUID issuerId, UUID deliveryPlaceId, UUID walletId) {
        return merx.orderCommitments().create(CreateOrderCommitmentRequest.builder()
                .amount(new BigDecimal("1000.00"))
                .producerId(producerId)
                .issuerId(issuerId)
                .currency("BRL")
                .product("SOJA")
                .deliveryPlaceId(deliveryPlaceId)
                .initialDeliveryDate(LocalDate.of(2024, 3, 1))
                .endDeliveryDate(LocalDate.of(2024, 4, 30))
                .harvest("2023/2024")
                .modality(ModalityType.DISPONIVEL)
                .payday(LocalDate.of(2024, 5, 15))
                .paymentType(OrderPaymentType.DINHEIRO)
                .unitOfMeasurement("SC")
                .price(new BigDecimal("130.33"))
                .priceUnitOfMeasurement("SC")
                .shipping(CreateShippingOrderCommitmentRequest.builder()
                        .type(ShippingType.FOB)
                        .city("Rondonópolis")
                        .state("MT")
                        .description("Entrega FOB na unidade do produtor")
                        .price(new BigDecimal("25.00"))
                        .build())
                .internalId("ERP-NEG-001")
                .description("Negociação de compra de soja - safra 2023/2024")
                .orderType(OrderType.COMPRA)
                .orderDate(OffsetDateTime.of(2024, 2, 21, 0, 0, 0, 0, ZoneOffset.UTC))
                .walletId(walletId)
                .build()).getId();
    }

    /** Cancela uma negociação pelo id. */
    public void cancelById(UUID commitmentId) {
        merx.orderCommitments().cancelById(commitmentId);
    }

    /** Cancela uma negociação pelo internal id (código do ERP). */
    public void cancelByInternalId(String internalId) {
        merx.orderCommitments().cancelByInternalId(internalId);
    }

    /** Atualiza a referência externa (código do ERP) da negociação. */
    public void updateExternalReference(UUID commitmentId) {
        merx.orderCommitments().updateExternalReference(commitmentId,
                UpdateOrderCommitmentExternalReferenceRequest.builder()
                        .externalReference("ERP-REF-9988")
                        .build());
    }

    /** Busca uma negociação pelo id. */
    public Optional<WebhookOrderCommitmentOrderResponse> findById(UUID commitmentId) {
        return merx.orderCommitments().findById(commitmentId);
    }

    /** Busca uma negociação pelo código ERP. */
    public Optional<WebhookOrderCommitmentOrderResponse> findByErpCode(String erpCode) {
        return merx.orderCommitments().findByErpCode(erpCode);
    }

    /** Retorna o histórico de volume entregue de uma negociação. */
    public Optional<OrderCommitmentDeliveredVolumeHistoryResponse> findDeliveredVolumeHistory(UUID commitmentId) {
        return merx.orderCommitments().findDeliveredVolumeHistory(commitmentId);
    }

    /** Atualiza o volume entregue de uma negociação. */
    public void updateDeliveredVolume(UUID commitmentId) {
        merx.orderCommitments().updateDeliveredVolume(commitmentId, DeliveredVolumeRequest.builder()
                .totalDelivered(new BigDecimal("500.00"))
                .unitOfMeasurement("SC")
                .deliveryMode(DeliveryMode.PARTIAL)
                .build());
    }

    /** Retorna os status de workflow de uma negociação. */
    public OrderCommitmentWorkflowStatusesResponse findWorkflowStatuses(UUID commitmentId) {
        return merx.orderCommitments().findWorkflowStatuses(commitmentId);
    }

    /** Transiciona o status de workflow de uma negociação. */
    public void updateWorkflowStatus(UUID commitmentId, UUID issuerId) {
        merx.orderCommitments().updateWorkflowStatus(commitmentId, UpdateStatusCommitmentRequest.builder()
                .specialityType("CONTRACTS")
                .targetStatus("APPROVED")
                .userMessage("Aprovado após conferência documental.")
                .reviewed(Boolean.TRUE)
                .issuerId(issuerId)
                .build());
    }

    /** Lista os documentos (contratos digitais) de uma negociação. */
    public List<DigitalContractResponse> findDocuments(UUID commitmentId) {
        return merx.orderCommitments().findDocuments(commitmentId);
    }

    /** Consulta paginada do histórico de negociações com filtro inline. */
    public PageResponse<OrdersCommitmentsFarmWorkflowSummarizedResponse> findHistoryPaginated() {
        OrdersCommitmentsSummarizedFilter filter = OrdersCommitmentsSummarizedFilter.builder()
                .filterByToken(Boolean.TRUE)
                .searchParam("SOJA")
                .orderTypes(List.of(OrderType.COMPRA))
                .build();
        return merx.orderCommitments().findHistoryPaginated(filter, 0, 20, "orderDate,desc");
    }
}
