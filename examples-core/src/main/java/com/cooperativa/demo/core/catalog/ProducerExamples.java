package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.baseline.address.CreateAddressRequest;
import com.merx.sdk.model.baseline.enums.MaritalStatus;
import com.merx.sdk.model.baseline.producer.CreateProducerRequest;
import com.merx.sdk.model.baseline.producer.FarmProductionBalanceResponse;
import com.merx.sdk.model.baseline.producer.ProducerResponse;
import com.merx.sdk.model.baseline.producer.UpdateProducerRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Catálogo de exemplos do recurso <b>producers</b> ({@code merx.producers()}).
 *
 * <p>Um método por endpoint público da SDK, com request de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — para um fluxo executável veja
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class ProducerExamples {

    private final MerxClient merx;

    public ProducerExamples(MerxClient merx) {
        this.merx = merx;
    }

    /** Cria um produtor. */
    public UUID create() {
        return merx.producers().create(CreateProducerRequest.builder()
                .companyName("Cooperativa Demo LTDA")
                .tradingName("Fazenda Demo")
                .email("contato@fazendademo.com.br")
                .socialIdentity("12345678000199")
                .dateOfBirth(LocalDate.of(1980, 5, 20))
                .rg("123456789")
                .maritalStatus(MaritalStatus.MARRIED)
                .dapCode("DAP-001")
                .cafCode("CAF-001")
                .ricafCode("RICAF-001")
                .build()).getId();
    }

    /** Atualiza um produtor existente. */
    public void update(UUID producerId) {
        merx.producers().update(producerId, UpdateProducerRequest.builder()
                .companyName("Cooperativa Demo LTDA")
                .tradingName("Fazenda Demo Atualizada")
                .dateOfBirth(LocalDate.of(1980, 5, 20))
                .maritalStatus(MaritalStatus.DIVORCED)
                .rg("123456789")
                .email("novo@fazendademo.com.br")
                .dapCode("DAP-002")
                .cafCode("CAF-002")
                .ricafCode("RICAF-002")
                .build());
    }

    /** Faz soft-delete de um produtor. */
    public void delete(UUID producerId) {
        merx.producers().delete(producerId);
    }

    /** Busca um produtor por id. */
    public Optional<ProducerResponse> findById(UUID producerId) {
        return merx.producers().findById(producerId);
    }

    /** Busca um produtor por CPF/CNPJ. */
    public Optional<ProducerResponse> findBySocialIdentity(String socialIdentity) {
        return merx.producers().findBySocialIdentity(socialIdentity);
    }

    /** Vincula um endereço ao produtor. */
    public void addAddress(UUID producerId) {
        merx.producers().addAddress(producerId, sampleAddress());
    }

    /** Atualiza o endereço do produtor. */
    public void updateAddress(UUID producerId) {
        merx.producers().updateAddress(producerId, sampleAddress());
    }

    /** Reativa um produtor previamente desabilitado. */
    public void enable(UUID producerId) {
        merx.producers().enable(producerId);
    }

    /** Desabilita um produtor sem deletá-lo. */
    public void disable(UUID producerId) {
        merx.producers().disable(producerId);
    }

    /** Retorna o saldo de produção quebrado por produto. */
    public List<FarmProductionBalanceResponse> findProductionBalance(UUID producerId) {
        return merx.producers().findProductionBalance(producerId);
    }

    private static CreateAddressRequest sampleAddress() {
        return CreateAddressRequest.builder()
                .street("Rua das Palmeiras")
                .number("100")
                .complement("Sala 2")
                .neighborhood("Centro")
                .zipCode("79000-000")
                .city("Campo Grande")
                .state("MS")
                .country("Brasil")
                .addressType("PRODUCER")
                .build();
    }
}
