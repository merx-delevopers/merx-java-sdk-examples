package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.carbon.socioambiental.SocioambientalRequest;
import com.merx.sdk.model.carbon.socioambiental.SyncEsgReportResponse;

/**
 * Catálogo de exemplos do recurso <b>socioambiental</b> ({@code merx.socioambiental()}).
 *
 * <p>Um método por endpoint público da SDK, com request de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — fluxo executável em
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class SocioambientalExamples {
    private final MerxClient merx;
    public SocioambientalExamples(MerxClient merx) { this.merx = merx; }

    /** Gera um relatório socioambiental detalhado síncrono (agrega IBAMA, FUNAI, MapBiomas, etc.). */
    public SyncEsgReportResponse createReport() {
        return merx.socioambiental().createReport(SocioambientalRequest.builder()
                .car("PR-4104808-ABCDEF1234567890ABCDEF1234567890")
                .producerName("João da Silva")
                .producerDocument("12345678000190")
                .build());
    }
}
