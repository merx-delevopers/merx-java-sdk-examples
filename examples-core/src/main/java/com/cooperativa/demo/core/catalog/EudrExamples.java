package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.carbon.eudr.EudrReportByGeomRequest;
import com.merx.sdk.model.carbon.eudr.EudrReportRequest;
import com.merx.sdk.model.carbon.eudr.EudrReportResponse;
import com.merx.sdk.model.carbon.eudr.SyncEuReportResponse;

import java.util.Optional;

/**
 * Catálogo de exemplos do recurso <b>eudr</b> ({@code merx.eudr()}).
 *
 * <p>Um método por endpoint público da SDK, com request de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — fluxo executável em
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class EudrExamples {
    private final MerxClient merx;
    public EudrExamples(MerxClient merx) { this.merx = merx; }

    /** Gera um relatório EUDR (anti-desmatamento UE) síncrono a partir de um CAR. */
    public SyncEuReportResponse createReport() {
        return merx.eudr().createReport(EudrReportRequest.builder()
                .car("PR-4104808-ABCDEF1234567890ABCDEF1234567890")
                .producerName("João da Silva")
                .producerDocument("12345678000190")
                .build());
    }

    /** Busca o relatório EUDR detalhado pelo código do CAR. */
    public Optional<EudrReportResponse> findByCar(String car) {
        return merx.eudr().findByCar(car);
    }

    /** Busca o relatório EUDR resumido pelo código do CAR. */
    public Optional<EudrReportResponse> findResumedByCar(String car) {
        return merx.eudr().findResumedByCar(car);
    }

    /** Busca o relatório EUDR detalhado por geometria (WKT). */
    public Optional<EudrReportResponse> findByGeom() {
        return merx.eudr().findByGeom(EudrReportByGeomRequest.builder()
                .geom("MULTIPOLYGON(((-52.5 -25.4, -52.4 -25.4, -52.4 -25.3, -52.5 -25.3, -52.5 -25.4)))")
                .build());
    }

    /** Busca o relatório EUDR resumido por geometria (WKT). */
    public Optional<EudrReportResponse> findResumedByGeom() {
        return merx.eudr().findResumedByGeom(EudrReportByGeomRequest.builder()
                .geom("MULTIPOLYGON(((-52.5 -25.4, -52.4 -25.4, -52.4 -25.3, -52.5 -25.3, -52.5 -25.4)))")
                .build());
    }
}
