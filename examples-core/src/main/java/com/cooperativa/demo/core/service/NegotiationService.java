package com.cooperativa.demo.core.service;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.order.commitment.CreateOrderCommitmentRequest;
import com.merx.sdk.model.order.commitment.OrderCommitmentDeliveredVolumeHistoryResponse;
import com.merx.sdk.model.order.commitment.WebhookOrderCommitmentOrderResponse;
import com.merx.sdk.model.order.delivery.CreateDeliveryRequest;
import com.merx.sdk.model.order.delivery.DeliveryResponse;
import com.merx.sdk.model.order.traceability.CreateTraceabilityRequest;

import java.util.Optional;
import java.util.UUID;

/**
 * Negociação (order): compromisso de compra (order commitment), registro de entrega (delivery),
 * leitura do compromisso, histórico de volume entregue e rastreabilidade (traceability ligando
 * o volume entregue à fazenda de origem).
 */
public class NegotiationService {

    private final MerxClient merx;

    public NegotiationService(MerxClient merx) {
        this.merx = merx;
    }

    public UUID createCommitment(CreateOrderCommitmentRequest request) {
        return merx.orderCommitments().create(request).getId();
    }

    public DeliveryResponse recordDelivery(CreateDeliveryRequest request) {
        return merx.deliveries().create(request);
    }

    public Optional<WebhookOrderCommitmentOrderResponse> findCommitment(UUID commitmentId) {
        return merx.orderCommitments().findById(commitmentId);
    }

    public Optional<OrderCommitmentDeliveredVolumeHistoryResponse> deliveredVolumeHistory(UUID commitmentId) {
        return merx.orderCommitments().findDeliveredVolumeHistory(commitmentId);
    }

    public UUID addTraceability(UUID commitmentId, CreateTraceabilityRequest request) {
        return merx.traceability().create(commitmentId, request).getId();
    }
}
