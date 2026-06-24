package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.identity.IdentityResponse;

import java.util.Optional;

/**
 * Catálogo de exemplos do recurso <b>cooperativeUsers</b> ({@code merx.cooperativeUsers()}).
 *
 * <p>Um método por endpoint público da SDK, com request de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — fluxo executável em
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class CooperativeUserExamples {

    private final MerxClient merx;

    public CooperativeUserExamples(MerxClient merx) {
        this.merx = merx;
    }

    /** Busca um colaborador da cooperativa por CPF/CNPJ (social id). */
    public Optional<IdentityResponse> findBySocialId(String socialId) {
        return merx.cooperativeUsers().findBySocialId(socialId);
    }
}
