package com.cooperativa.demo.basic;

import com.cooperativa.demo.core.config.MerxClientFactory;
import com.cooperativa.demo.core.config.MerxSettings;
import com.cooperativa.demo.core.journey.CooperativaJourney;
import com.cooperativa.demo.core.journey.JourneyResult;
import com.merx.sdk.api.MerxClient;

/**
 * Exemplo Java puro (sem framework).
 *
 * <p>Mostra o caminho mais direto de integração:
 * <ol>
 *   <li>lê a configuração de variáveis de ambiente ({@link MerxSettings#fromEnv()});</li>
 *   <li>constrói <b>um</b> {@link MerxClient} e o reaproveita ({@link MerxClientFactory});</li>
 *   <li>roda a jornada de negócio compartilhada e imprime o resumo.</li>
 * </ol>
 *
 * <p>Rode com: {@code MERX_API_KEY=<token> mvn -q -pl examples-basic exec:java}
 */
public final class App {

    private App() {
    }

    public static void main(String[] args) {
        final MerxSettings settings;
        try {
            settings = MerxSettings.fromEnv();
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            System.exit(1);
            return;
        }

        // O MerxClient é thread-safe e deve viver pelo tempo de vida da aplicação — construa uma vez.
        MerxClient merx = MerxClientFactory.create(settings);

        System.out.println("== Cooperativa Demo — integração Merx (Java puro) ==");
        System.out.println("Ambiente: " + settings.getEnvironment());
        System.out.println();

        JourneyResult result = CooperativaJourney.fromClient(merx).run();

        System.out.println();
        System.out.println(result.summary());
    }
}
