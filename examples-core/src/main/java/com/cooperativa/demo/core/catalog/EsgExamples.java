package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.carbon.esg.CarResultEsg;
import com.merx.sdk.model.carbon.esg.ProducerEsgResume;

import java.util.Optional;

/**
 * Catálogo de exemplos do recurso <b>esg</b> ({@code merx.esg()}).
 *
 * <p>Um método por endpoint público da SDK, com request de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — fluxo executável em
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class EsgExamples {
    private final MerxClient merx;
    public EsgExamples(MerxClient merx) { this.merx = merx; }

    /** Busca o snapshot ESG resumido pelo código do CAR (fazenda). */
    public Optional<CarResultEsg> findResumeByCar(String car) {
        return merx.esg().findResumeByCar(car);
    }

    /** Busca o snapshot ESG resumido pela social identity (CPF/CNPJ do produtor). */
    public Optional<ProducerEsgResume> findResumeBySocialIdentity(String cpfCnpj) {
        return merx.esg().findResumeBySocialIdentity(cpfCnpj);
    }
}
