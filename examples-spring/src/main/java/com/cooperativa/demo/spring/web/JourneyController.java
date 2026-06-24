package com.cooperativa.demo.spring.web;

import com.cooperativa.demo.core.journey.CooperativaJourney;
import com.cooperativa.demo.core.journey.JourneyResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Dispara a jornada de negócio completa da Cooperativa Demo.
 *
 * <p>{@code POST /api/journey} → executa o fluxo ponta a ponta e devolve o resumo (etapas +
 * contadores + ids criados) em JSON.
 */
@RestController
@RequestMapping("/api/journey")
public class JourneyController {

    private final CooperativaJourney journey;

    public JourneyController(CooperativaJourney journey) {
        this.journey = journey;
    }

    @PostMapping
    public JourneyResult run() {
        return journey.run();
    }
}
