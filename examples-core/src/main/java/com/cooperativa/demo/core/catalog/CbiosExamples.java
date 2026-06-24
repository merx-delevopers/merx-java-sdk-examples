package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.carbon.cbios.CbiosReportRequest;
import com.merx.sdk.model.carbon.cbios.CbiosReportResponse;
import com.merx.sdk.model.carbon.enums.Layer;

import java.util.List;

/**
 * Catálogo de exemplos do recurso <b>cbios</b> ({@code merx.cbios()}).
 *
 * <p>Um método por endpoint público da SDK, com request de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — fluxo executável em
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class CbiosExamples {
    private final MerxClient merx;
    public CbiosExamples(MerxClient merx) { this.merx = merx; }

    /** Gera um relatório CBIOS (Crédito de Descarbonização) síncrono. */
    public CbiosReportResponse createReport() {
        return merx.cbios().createReport(CbiosReportRequest.builder()
                .producerName("João da Silva")
                .producerDocument("12345678000190")
                .username("operador@cooperativa.coop.br")
                .name("Relatório CBIOS Safra 23/24")
                .layer(Layer.SOY)
                .cars(List.of("PR-4104808-ABCDEF1234567890ABCDEF1234567890"))
                .civilYear(2024)
                .harvestCode(2324)
                .build());
    }
}
