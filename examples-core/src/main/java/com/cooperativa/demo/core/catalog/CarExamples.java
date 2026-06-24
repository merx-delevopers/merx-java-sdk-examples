package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.carbon.car.CarDto;
import com.merx.sdk.model.carbon.car.CarResponseDto;

import java.util.List;

/**
 * Catálogo de exemplos do recurso <b>car</b> ({@code merx.car()}).
 *
 * <p>Um método por endpoint público da SDK, com request de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — fluxo executável em
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class CarExamples {
    private final MerxClient merx;
    public CarExamples(MerxClient merx) { this.merx = merx; }

    /** Busca um registro de CAR (Cadastro Ambiental Rural) pelo código. */
    public CarDto findByCode(String car) {
        return merx.car().findByCode(car);
    }

    /** Busca os CARs que contêm a coordenada informada (latitude/longitude). */
    public List<CarResponseDto> findByLatLong(double latitude, double longitude) {
        return merx.car().findByLatLong(latitude, longitude);
    }
}
