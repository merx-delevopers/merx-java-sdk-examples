package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.financial.AnalysisCreditResponse;
import com.merx.sdk.model.financial.ConsentStatusResponse;
import com.merx.sdk.model.financial.CreateAnalysisCreditRequest;
import com.merx.sdk.model.financial.CreditScoreRequest;
import com.merx.sdk.model.financial.CreditScoreResponse;
import com.merx.sdk.model.financial.ReportsAndStatusResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Catálogo de exemplos do recurso <b>financial</b> ({@code merx.financial()}).
 *
 * <p>Um método por endpoint público da SDK, com request de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — para um fluxo executável veja
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class FinancialExamples {

    private final MerxClient merx;

    public FinancialExamples(MerxClient merx) {
        this.merx = merx;
    }

    /** Gera um score de crédito para uma pessoa/empresa. */
    public CreditScoreResponse generateCreditScore() {
        return merx.financial().generateCreditScore(CreditScoreRequest.builder()
                .name("Fulano de Tal")
                .socialId("12345678900")
                .reports(List.of("AGRO", "SIGEF"))
                .build());
    }

    /** Busca os relatórios e status de um score de crédito. */
    public Optional<ReportsAndStatusResponse> findCreditScoreReports(UUID creditScoreId) {
        return merx.financial().findCreditScoreReports(creditScoreId);
    }

    /** Verifica o status de consentimento SCR (corporateIdentity opcional). */
    public ConsentStatusResponse checkConsentStatus() {
        return merx.financial().checkConsentStatus("12345678900", "PF", null);
    }

    /** Cria uma análise de crédito (consulta SCR) e retorna o id. */
    public UUID createAnalysisCredit() {
        return merx.financial().createAnalysisCredit(CreateAnalysisCreditRequest.builder()
                .requesterId(UUID.randomUUID())
                .requesterName("Cooperativa Demo")
                .requesterEmail("ops@demo.coop")
                .name("Fulano de Tal")
                .identity("12345678900")
                .authType("DIGITAL")
                .personType("PF")
                .periodConsult("12")
                .build()).getId();
    }

    /** Busca uma análise de crédito por id, com os dados do SCR. */
    public Optional<AnalysisCreditResponse> findAnalysisCreditById(UUID analysisCreditId) {
        return merx.financial().findAnalysisCreditById(analysisCreditId);
    }
}
