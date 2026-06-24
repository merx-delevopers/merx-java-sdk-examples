package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.carbon.bsvs.BsvsReportRequest;
import com.merx.sdk.model.carbon.bsvs.BsvsReportResponse;
import com.merx.sdk.model.carbon.enums.Layer;

import java.util.List;

/**
 * Catálogo de exemplos do recurso <b>bsvs</b> ({@code merx.bsvs()}).
 *
 * <p>Um método por endpoint público da SDK, com request de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — fluxo executável em
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class BsvsExamples {
    private final MerxClient merx;
    public BsvsExamples(MerxClient merx) { this.merx = merx; }

    /** Gera um relatório BSVs (verificação de sustentabilidade de biocombustíveis) síncrono. */
    public BsvsReportResponse createReport() {
        return merx.bsvs().createReport(BsvsReportRequest.builder()
                .producerName("João da Silva")
                .cooperativeName("Cooperativa Agrícola Demo")
                .producerDocument("12345678000190")
                .username("operador@cooperativa.coop.br")
                .name("Relatório BSVs Safra 23/24")
                .cars(List.of("PR-4104808-ABCDEF1234567890ABCDEF1234567890"))
                .civilYear(2024)
                .harvestCode(2324)
                .layer(Layer.CORN)
                .build());
    }
}
