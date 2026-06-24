package com.cooperativa.demo.core.journey;

/** Resultado de uma etapa da jornada (imutável). */
public class JourneyStep {

    public enum Outcome {
        SUCCESS("[ OK ]"),
        SKIPPED("[SKIP]"),
        FAILED("[FAIL]");

        private final String marker;

        Outcome(String marker) {
            this.marker = marker;
        }

        public String marker() {
            return marker;
        }
    }

    private final String label;
    private final Outcome outcome;
    private final String detail;

    public JourneyStep(String label, Outcome outcome, String detail) {
        this.label = label;
        this.outcome = outcome;
        this.detail = detail;
    }

    public String getLabel() {
        return label;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public String getDetail() {
        return detail;
    }

    @Override
    public String toString() {
        return String.format("%s %-32s %s", outcome.marker(), label, detail == null ? "" : detail);
    }
}
