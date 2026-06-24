package com.cooperativa.demo.core.journey;

import java.util.UUID;

/**
 * Guarda os identificadores criados ao longo da jornada, conforme cada etapa os produz.
 * As etapas seguintes leem daqui os ids de que dependem (ex.: a fazenda precisa do
 * {@code producerId}; a negociação precisa de {@code walletId} e {@code deliveryPlaceId}).
 */
public class JourneyContext {

    public UUID producerId;
    public UUID producerUserId;
    public UUID farmId;
    public UUID warehouseId;
    public UUID deliveryPlaceId;
    public UUID walletId;
    public UUID commitmentId;

    public boolean has(UUID id) {
        return id != null;
    }
}
