package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.baseline.address.CreateAddressRequest;
import com.merx.sdk.model.baseline.warehouse.CreateWarehouseRequest;
import com.merx.sdk.model.baseline.warehouse.UpdateWarehouseRequest;
import com.merx.sdk.model.baseline.warehouse.WarehouseResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Catálogo de exemplos do recurso <b>warehouses</b> ({@code merx.warehouses()}).
 *
 * <p>Um método por endpoint público da SDK, com request de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — para um fluxo executável veja
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class WarehouseExamples {

    private final MerxClient merx;

    public WarehouseExamples(MerxClient merx) {
        this.merx = merx;
    }

    /** Cria um armazém vinculado a um produtor. */
    public UUID create(UUID producerId) {
        return merx.warehouses().create(producerId, CreateWarehouseRequest.builder()
                .name("Armazém Central")
                .capacity(new BigDecimal("50000.00"))
                .address(sampleAddress())
                .stateSubscription("123456-MS")
                .stateSubscriptionUF("MS")
                .document("12345678000199")
                .build()).getId();
    }

    /** Busca um armazém por id. */
    public Optional<WarehouseResponse> findById(UUID warehouseId) {
        return merx.warehouses().findById(warehouseId);
    }

    /** Lista os armazéns de um produtor. */
    public List<WarehouseResponse> findByProducer(UUID producerId) {
        return merx.warehouses().findByProducer(producerId);
    }

    /** Atualiza um armazém existente. */
    public void update(UUID warehouseId) {
        merx.warehouses().update(warehouseId, UpdateWarehouseRequest.builder()
                .name("Armazém Central Ampliado")
                .capacity(new BigDecimal("75000.00"))
                .stateSubscription("654321-MS")
                .stateSubscriptionUF("MS")
                .build());
    }

    /** Atualiza o endereço do armazém. */
    public void updateAddress(UUID warehouseId) {
        merx.warehouses().updateAddress(warehouseId, sampleAddress());
    }

    private static CreateAddressRequest sampleAddress() {
        return CreateAddressRequest.builder()
                .street("Av. dos Armazéns")
                .number("2000")
                .complement("Galpão 5")
                .neighborhood("Distrito Industrial")
                .zipCode("79110-000")
                .city("Campo Grande")
                .state("MS")
                .country("Brasil")
                .addressType("WAREHOUSE")
                .build();
    }
}
