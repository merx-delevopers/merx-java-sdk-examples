package com.cooperativa.demo.core.journey;

/**
 * Lançada por uma ação de etapa quando uma dependência não está disponível
 * (ex.: tentar criar a fazenda sem ter o {@code producerId}). O {@link JourneyReporter}
 * marca a etapa como {@code SKIPPED} em vez de {@code FAILED}.
 */
public class SkippedStepException extends RuntimeException {

    public SkippedStepException(String reason) {
        super(reason);
    }
}
