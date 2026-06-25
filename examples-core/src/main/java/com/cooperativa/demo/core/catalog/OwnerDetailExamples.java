package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.ownerdetail.CreateOwnerDetailRequest;
import com.merx.sdk.model.ownerdetail.OwnerDetailResponse;

import java.util.Optional;
import java.util.UUID;

/**
 * Catálogo de exemplos do recurso <b>ownerDetails</b> ({@code merx.ownerDetails()}).
 *
 * <p>Um método por endpoint público da SDK, com request de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — para um fluxo executável veja
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class OwnerDetailExamples {

    private final MerxClient merx;

    public OwnerDetailExamples(MerxClient merx) {
        this.merx = merx;
    }

    /** Cria o owner detail (segredo HMAC de webhook) da cooperativa. */
    public void create() {
        merx.ownerDetails().create(CreateOwnerDetailRequest.builder()
                .hmacSecret("seu-segredo-hmac")
                .build());
    }

    /** Atualiza o owner detail identificado por id. */
    public void update(UUID id) {
        merx.ownerDetails().update(id, CreateOwnerDetailRequest.builder()
                .hmacSecret("novo-segredo")
                .build());
    }

    /** Remove (soft) o owner detail identificado por id. */
    public void delete(UUID id) {
        merx.ownerDetails().delete(id);
    }

    /** Retorna o owner detail da cooperativa do chamador. */
    public Optional<OwnerDetailResponse> get() {
        return merx.ownerDetails().get();
    }
}
