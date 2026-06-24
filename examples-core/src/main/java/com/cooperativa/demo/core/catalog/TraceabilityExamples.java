package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.common.PageResponse;
import com.merx.sdk.model.order.traceability.CreateTraceabilityRequest;
import com.merx.sdk.model.order.traceability.SoftDeleteTraceabilityRequest;
import com.merx.sdk.model.order.traceability.TraceabilityResponse;
import com.merx.sdk.model.order.traceability.TraceabilityUpdateRequest;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Catálogo de exemplos do recurso <b>traceability</b> ({@code merx.traceability()}).
 *
 * <p>Um método por endpoint público da SDK, com request de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — fluxo executável em
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class TraceabilityExamples {

    private final MerxClient merx;

    public TraceabilityExamples(MerxClient merx) {
        this.merx = merx;
    }

    /** Cria uma entrada de rastreabilidade vinculada a uma negociação. */
    public UUID create(UUID commitmentId, UUID farmId, UUID producerId, UUID userId) {
        return merx.traceability().create(commitmentId, CreateTraceabilityRequest.builder()
                .farmId(farmId)
                .allocatedVolume(new BigDecimal("250.00"))
                .unitOfMeasurement("TON")
                .producerId(producerId)
                .userId(userId)
                .build()).getId();
    }

    /** Lista paginada das rastreabilidades de uma negociação. */
    public PageResponse<TraceabilityResponse> findByCommitment(UUID commitmentId) {
        return merx.traceability().findByCommitment(commitmentId, 0, 20, "createdAt,desc");
    }

    /** Faz soft-delete de uma rastreabilidade. */
    public void softDelete(UUID traceabilityId, UUID commitmentId, UUID userId) {
        merx.traceability().softDelete(traceabilityId, commitmentId, SoftDeleteTraceabilityRequest.builder()
                .description("Lote realocado para outra negociação.")
                .userId(userId)
                .build());
    }

    /** Atualiza o volume alocado de uma rastreabilidade. */
    public void updateAllocatedVolume(UUID traceabilityId, UUID commitmentId, UUID userId) {
        merx.traceability().updateAllocatedVolume(traceabilityId, commitmentId, TraceabilityUpdateRequest.builder()
                .allocatedVolume(new BigDecimal("300.00"))
                .unitOfMeasurement("TON")
                .userId(userId)
                .origin("INTEGRATION")
                .build());
    }
}
