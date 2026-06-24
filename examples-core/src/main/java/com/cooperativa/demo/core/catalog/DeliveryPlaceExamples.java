package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.baseline.deliveryPlace.CreateDeliveryPlaceRequest;
import com.merx.sdk.model.baseline.deliveryPlace.DeliveryPlaceResponse;
import com.merx.sdk.model.baseline.deliveryPlace.UpdateDeliveryPlaceRequest;

import java.util.List;
import java.util.UUID;

/**
 * Catálogo de exemplos do recurso <b>delivery-places</b> ({@code merx.deliveryPlaces()}).
 *
 * <p>Um método por endpoint público da SDK, com request de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — para um fluxo executável veja
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class DeliveryPlaceExamples {

    private final MerxClient merx;

    public DeliveryPlaceExamples(MerxClient merx) {
        this.merx = merx;
    }

    /** Cria um local de entrega. */
    public UUID create() {
        return merx.deliveryPlaces().create(CreateDeliveryPlaceRequest.builder()
                .place("Porto de Santos")
                .description("Terminal de embarque de grãos")
                .build()).getId();
    }

    /** Atualiza um local de entrega existente. */
    public void update(UUID deliveryPlaceId) {
        merx.deliveryPlaces().update(deliveryPlaceId, UpdateDeliveryPlaceRequest.builder()
                .place("Porto de Paranaguá")
                .description("Terminal de embarque de grãos (atualizado)")
                .build());
    }

    /** Lista todos os locais de entrega. */
    public List<DeliveryPlaceResponse> findAll() {
        return merx.deliveryPlaces().findAll();
    }
}
