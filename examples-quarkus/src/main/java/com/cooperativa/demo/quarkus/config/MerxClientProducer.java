package com.cooperativa.demo.quarkus.config;

import com.cooperativa.demo.core.config.MerxClientFactory;
import com.cooperativa.demo.core.config.MerxSettings;
import com.cooperativa.demo.core.journey.CooperativaJourney;
import com.cooperativa.demo.core.service.ComplianceService;
import com.cooperativa.demo.core.service.MarketDataService;
import com.cooperativa.demo.core.service.NegotiationService;
import com.cooperativa.demo.core.service.OnboardingService;
import com.merx.sdk.api.MerxClient;
import com.merx.sdk.core.Environment;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

/**
 * Produtor CDI da integração Merx.
 *
 * <p>O {@link MerxClient} é exposto como {@code @Produces @Singleton} — uma única instância para
 * toda a aplicação, injetável em qualquer bean. É o jeito idiomático no Quarkus de cumprir a
 * recomendação do SDK ("uma instância por token, reaproveitada"). Os serviços de domínio do core
 * também viram singletons CDI, recebendo o client por injeção.
 *
 * <p>Config via {@code application.properties} / variáveis de ambiente:
 * {@code merx.api-key} e {@code merx.environment}.
 */
@ApplicationScoped
public class MerxClientProducer {

    @ConfigProperty(name = "merx.api-key")
    String apiKey;

    @ConfigProperty(name = "merx.environment", defaultValue = "SANDBOX")
    String environment;

    @Produces
    @Singleton
    public MerxClient merxClient() {
        MerxSettings settings = MerxSettings.builder()
                .apiKey(apiKey)
                .environment(Environment.valueOf(environment.trim().toUpperCase()))
                .build();
        return MerxClientFactory.create(settings);
    }

    @Produces
    @Singleton
    public OnboardingService onboardingService(MerxClient merx) {
        return new OnboardingService(merx);
    }

    @Produces
    @Singleton
    public ComplianceService complianceService(MerxClient merx) {
        return new ComplianceService(merx);
    }

    @Produces
    @Singleton
    public NegotiationService negotiationService(MerxClient merx) {
        return new NegotiationService(merx);
    }

    @Produces
    @Singleton
    public MarketDataService marketDataService(MerxClient merx) {
        return new MarketDataService(merx);
    }

    @Produces
    @Singleton
    public CooperativaJourney cooperativaJourney(OnboardingService onboarding,
                                                 ComplianceService compliance,
                                                 NegotiationService negotiation,
                                                 MarketDataService market) {
        return new CooperativaJourney(onboarding, compliance, negotiation, market);
    }
}
