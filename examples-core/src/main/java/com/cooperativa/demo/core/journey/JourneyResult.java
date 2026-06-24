package com.cooperativa.demo.core.journey;

import java.util.List;

/** Resultado consolidado de uma execução da jornada: as etapas e os ids criados. */
public class JourneyResult {

    private final List<JourneyStep> steps;
    private final JourneyContext context;

    public JourneyResult(List<JourneyStep> steps, JourneyContext context) {
        this.steps = steps;
        this.context = context;
    }

    public List<JourneyStep> getSteps() {
        return steps;
    }

    public JourneyContext getContext() {
        return context;
    }

    public long count(JourneyStep.Outcome outcome) {
        return steps.stream().filter(s -> s.getOutcome() == outcome).count();
    }

    public long getSuccessCount() {
        return count(JourneyStep.Outcome.SUCCESS);
    }

    public long getSkippedCount() {
        return count(JourneyStep.Outcome.SKIPPED);
    }

    public long getFailedCount() {
        return count(JourneyStep.Outcome.FAILED);
    }

    /** Mesmo texto de {@link #summary()}, exposto como propriedade para serialização JSON. */
    public String getSummary() {
        return summary();
    }

    /** Texto pronto para imprimir no console ou devolver numa resposta REST. */
    public String summary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Jornada Cooperativa Demo — ")
                .append(count(JourneyStep.Outcome.SUCCESS)).append(" ok / ")
                .append(count(JourneyStep.Outcome.SKIPPED)).append(" pulados / ")
                .append(count(JourneyStep.Outcome.FAILED)).append(" falhas\n");
        sb.append("------------------------------------------------------------\n");
        for (JourneyStep step : steps) {
            sb.append(step).append('\n');
        }
        return sb.toString();
    }
}
