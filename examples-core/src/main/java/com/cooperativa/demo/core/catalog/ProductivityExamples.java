package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.productivity.Culture;
import com.merx.sdk.model.productivity.Productivity;
import com.merx.sdk.model.productivity.ProductivityByCityResponse;
import com.merx.sdk.model.productivity.Source;

import java.util.List;

/**
 * Catálogo de exemplos do recurso <b>productivity</b> ({@code merx.productivity()}).
 *
 * <p>Um método por endpoint público da SDK, com parâmetros de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — fluxo executável em
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class ProductivityExamples {
    private final MerxClient merx;
    public ProductivityExamples(MerxClient merx) { this.merx = merx; }

    /** Produtividade histórica por CAR, filtrando por ano, cultura, fonte e geometria. */
    public List<Productivity> findByCar(String car, String year, String harvest) {
        return merx.productivity().findByCar(car, year, Culture.SOY, Source.MERX, true, false, harvest);
    }

    /** Produtividade histórica por CAR e cultura específica, com safra e fonte. */
    public List<Productivity> findByCarAndCulture(String car, String harvest) {
        return merx.productivity().findByCarAndCulture(car, Culture.CORN, harvest, Source.MAPBIOMAS, true, false);
    }

    /** Produtividade média por código de cidade (IBGE) e cultura. */
    public List<ProductivityByCityResponse> findByCity(String cityCode, String culture) {
        return merx.productivity().findByCity(cityCode, culture);
    }
}
