package com.cooperativa.demo.quarkus.web;

import com.cooperativa.demo.core.journey.CooperativaJourney;
import com.cooperativa.demo.core.journey.JourneyResult;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Dispara a jornada de negócio completa da Cooperativa Demo.
 *
 * <p>{@code POST /api/journey} → executa o fluxo ponta a ponta e devolve o resumo em JSON.
 */
@Path("/api/journey")
@Produces(MediaType.APPLICATION_JSON)
public class JourneyResource {

    @Inject
    CooperativaJourney journey;

    @POST
    public JourneyResult run() {
        return journey.run();
    }
}
