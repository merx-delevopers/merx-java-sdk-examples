package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.order.delivery.CreateDeliveryRequest;
import com.merx.sdk.model.order.delivery.DeliveryResponse;
import com.merx.sdk.model.order.enums.DeliveryMode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Catálogo de exemplos do recurso <b>deliveries</b> ({@code merx.deliveries()}).
 *
 * <p>Um método por endpoint público da SDK, com request de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — fluxo executável em
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class DeliveryExamples {

    private final MerxClient merx;

    public DeliveryExamples(MerxClient merx) {
        this.merx = merx;
    }

    /** Cria uma entrega avulsa contra uma negociação. */
    public DeliveryResponse create(String contractId) {
        return merx.deliveries().create(sampleDelivery(contractId, new BigDecimal("300.00")));
    }

    /** Cria várias entregas em lote. */
    public List<DeliveryResponse> createBatch(String contractId) {
        return merx.deliveries().createBatch(List.of(
                sampleDelivery(contractId, new BigDecimal("100.00")),
                sampleDelivery(contractId, new BigDecimal("200.00"))));
    }

    private static CreateDeliveryRequest sampleDelivery(String contractId, BigDecimal volume) {
        return CreateDeliveryRequest.builder()
                .contractId(contractId)
                .deliveredVolume(volume)
                .deliveredVolumeUnitOfMeasure("sc")
                .deliveryDate(LocalDate.of(2024, 3, 15))
                .car("MT-5107602-A1B2C3D4E5F6")
                .deliveryMode(DeliveryMode.PARTIAL)
                .build();
    }
}
