package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.baseline.address.CreateAddressRequest;
import com.merx.sdk.model.baseline.enums.AddressType;
import com.merx.sdk.model.baseline.farm.CreateFarmRequest;
import com.merx.sdk.model.baseline.farm.FarmResponse;
import com.merx.sdk.model.baseline.farm.UpdateFarmRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Catálogo de exemplos do recurso <b>farms</b> ({@code merx.farms()}).
 *
 * <p>Um método por endpoint público da SDK, com request de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — para um fluxo executável veja
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class FarmExamples {

    private final MerxClient merx;

    public FarmExamples(MerxClient merx) {
        this.merx = merx;
    }

    /** Cria uma fazenda vinculada a um produtor. */
    public UUID create(UUID producerId) {
        return merx.farms().create(producerId, CreateFarmRequest.builder()
                .name("Fazenda Boa Esperança")
                .latitude("-20.4697")
                .longitude("-54.6201")
                .own(Boolean.TRUE)
                .address(sampleAddress())
                .car("MS-5002704-ABCD1234EF567890ABCD1234EF567890")
                .stateSubscription("123456-MS")
                .registration("MAT-0001")
                .area("1500.50")
                .addressType(AddressType.FARM)
                .build()).getId();
    }

    /** Busca uma fazenda por id. */
    public Optional<FarmResponse> findById(UUID farmId) {
        return merx.farms().findById(farmId);
    }

    /** Lista as fazendas de um produtor. */
    public List<FarmResponse> findByProducer(UUID producerId) {
        return merx.farms().findByProducer(producerId);
    }

    /** Atualiza uma fazenda existente. */
    public void update(UUID farmId) {
        merx.farms().update(farmId, UpdateFarmRequest.builder()
                .name("Fazenda Boa Esperança II")
                .latitude("-20.4700")
                .longitude("-54.6210")
                .own(Boolean.FALSE)
                .area("1600.00")
                .stateSubscription("654321-MS")
                .registration("MAT-0002")
                .build());
    }

    /** Vincula um endereço à fazenda. */
    public void addAddress(UUID farmId) {
        merx.farms().addAddress(farmId, sampleAddress());
    }

    /** Atualiza o endereço da fazenda. */
    public void updateAddress(UUID farmId) {
        merx.farms().updateAddress(farmId, sampleAddress());
    }

    /** Desvincula a fazenda de um produtor. */
    public void unbind(UUID farmId, UUID producerId) {
        merx.farms().unbind(farmId, producerId);
    }

    private static CreateAddressRequest sampleAddress() {
        return CreateAddressRequest.builder()
                .street("Estrada Rural km 12")
                .number("0")
                .complement("Portão azul")
                .neighborhood("Zona Rural")
                .zipCode("79100-000")
                .city("Campo Grande")
                .state("MS")
                .country("Brasil")
                .addressType("FARM")
                .build();
    }
}
