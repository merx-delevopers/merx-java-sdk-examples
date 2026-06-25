package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.erp.CreateErpCodeListRequest;
import com.merx.sdk.model.erp.CreateErpCodeRequest;
import com.merx.sdk.model.erp.ErpCodeResponse;

import java.util.List;
import java.util.UUID;

/**
 * Catálogo de exemplos do recurso <b>erp</b> ({@code merx.erp()}).
 *
 * <p>Um método por endpoint público da SDK, com request de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — para um fluxo executável veja
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class ErpExamples {

    private final MerxClient merx;

    public ErpExamples(MerxClient merx) {
        this.merx = merx;
    }

    /** Cria um ou mais códigos ERP vinculados a um produtor. */
    public void create(UUID producerId) {
        merx.erp().create(producerId, CreateErpCodeListRequest.builder()
                .erpCodes(List.of(CreateErpCodeRequest.builder()
                        .id("ERP-001")
                        .description("Cliente ERP Demo")
                        .build()))
                .build());
    }

    /** Lista os códigos ERP de um produtor. */
    public List<ErpCodeResponse> findByProducer(UUID producerId) {
        return merx.erp().findByProducer(producerId);
    }

    /** Remove um código ERP do produtor. */
    public void delete(UUID producerId, String erpCode) {
        merx.erp().delete(producerId, erpCode);
    }
}
