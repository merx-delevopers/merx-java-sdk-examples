package com.cooperativa.demo.core.journey;

import com.merx.sdk.core.exception.MerxApiException;
import com.merx.sdk.core.exception.MerxClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Executa as etapas da jornada de forma resiliente: registra início/fim, captura exceções e
 * traduz cada desfecho em um {@link JourneyStep}. Um passo que falha (comum no sandbox, ex.: um
 * relatório de carbono para um CAR fictício) <b>não aborta</b> a demonstração — é marcado como
 * {@code FAILED} e a jornada segue.
 *
 * <p>Demonstra também o tratamento idiomático da hierarquia tipada de exceções do SDK
 * ({@link MerxApiException} para respostas HTTP de erro, {@link MerxClientException} para falhas
 * locais de rede/serialização).
 */
public class JourneyReporter {

    /** Ação de uma etapa: executa e devolve um resumo legível (ex.: o id criado). */
    @FunctionalInterface
    public interface StepAction {
        String run() throws Exception;
    }

    private static final Logger log = LoggerFactory.getLogger(JourneyReporter.class);

    private final List<JourneyStep> steps = new ArrayList<>();

    public void note(String message) {
        log.info(message);
    }

    /** Executa uma etapa, captura o desfecho e o acumula. Nunca propaga exceção. */
    public JourneyStep step(String label, StepAction action) {
        log.info("-> {}", label);
        JourneyStep step;
        try {
            String detail = action.run();
            step = new JourneyStep(label, JourneyStep.Outcome.SUCCESS, detail);
            log.info("   {} {}", step.getOutcome().marker(), detail);
        } catch (SkippedStepException e) {
            step = new JourneyStep(label, JourneyStep.Outcome.SKIPPED, e.getMessage());
            log.warn("   {} {}", JourneyStep.Outcome.SKIPPED.marker(), e.getMessage());
        } catch (MerxApiException e) {
            String detail = "HTTP " + e.getStatusCode() + " - " + firstLine(e.getResponseBody());
            step = new JourneyStep(label, JourneyStep.Outcome.FAILED, detail);
            log.error("   {} {}", JourneyStep.Outcome.FAILED.marker(), detail);
        } catch (MerxClientException e) {
            String detail = "falha local (rede/serializacao): " + e.getMessage();
            step = new JourneyStep(label, JourneyStep.Outcome.FAILED, detail);
            log.error("   {} {}", JourneyStep.Outcome.FAILED.marker(), detail);
        } catch (Exception e) {
            step = new JourneyStep(label, JourneyStep.Outcome.FAILED, e.getMessage());
            log.error("   {} {}", JourneyStep.Outcome.FAILED.marker(), e.getMessage());
        }
        steps.add(step);
        return step;
    }

    public List<JourneyStep> steps() {
        return List.copyOf(steps);
    }

    public JourneyResult result(JourneyContext context) {
        return new JourneyResult(steps(), context);
    }

    private static String firstLine(String body) {
        if (body == null || body.isBlank()) {
            return "(sem corpo)";
        }
        String trimmed = body.strip();
        int nl = trimmed.indexOf('\n');
        String line = (nl > 0 ? trimmed.substring(0, nl) : trimmed);
        return line.length() > 180 ? line.substring(0, 180) + "…" : line;
    }
}
