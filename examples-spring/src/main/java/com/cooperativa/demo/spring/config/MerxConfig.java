package com.cooperativa.demo.spring.config;

import com.cooperativa.demo.core.config.MerxClientFactory;
import com.cooperativa.demo.core.config.MerxSettings;
import com.cooperativa.demo.core.journey.CooperativaJourney;
import com.cooperativa.demo.core.service.ComplianceService;
import com.cooperativa.demo.core.service.MarketDataService;
import com.cooperativa.demo.core.service.NegotiationService;
import com.cooperativa.demo.core.service.OnboardingService;
import com.merx.sdk.api.MerxClient;
import com.merx.sdk.core.Environment;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Fiação Spring da integração Merx.
 *
 * <p>O {@link MerxClient} é um {@code @Bean} <b>singleton</b> — criado uma vez na subida da
 * aplicação e injetado em todos os serviços. É o jeito idiomático no Spring de cumprir a
 * recomendação do SDK ("uma instância por token, reaproveitada por toda a vida da app").
 * Os serviços de domínio do core viram beans gerenciados, prontos para injeção nos controllers.
 */
@Configuration
@EnableConfigurationProperties(MerxProperties.class)
public class MerxConfig {

    @Bean
    public MerxClient merxClient(MerxProperties props) {
        MerxSettings.Builder settings = MerxSettings.builder()
                .apiKey(props.getApiKey())
                .environment(Environment.valueOf(props.getEnvironment().trim().toUpperCase()))
                .maxRetries(props.getMaxRetries());
        if (props.getConnectTimeoutSeconds() != null) {
            settings.connectTimeout(Duration.ofSeconds(props.getConnectTimeoutSeconds()));
        }
        if (props.getReadTimeoutSeconds() != null) {
            settings.readTimeout(Duration.ofSeconds(props.getReadTimeoutSeconds()));
        }
        return MerxClientFactory.create(settings.build());
    }

    @Bean
    public OnboardingService onboardingService(MerxClient merxClient) {
        return new OnboardingService(merxClient);
    }

    @Bean
    public ComplianceService complianceService(MerxClient merxClient) {
        return new ComplianceService(merxClient);
    }

    @Bean
    public NegotiationService negotiationService(MerxClient merxClient) {
        return new NegotiationService(merxClient);
    }

    @Bean
    public MarketDataService marketDataService(MerxClient merxClient) {
        return new MarketDataService(merxClient);
    }

    @Bean
    public CooperativaJourney cooperativaJourney(OnboardingService onboarding,
                                                 ComplianceService compliance,
                                                 NegotiationService negotiation,
                                                 MarketDataService market) {
        return new CooperativaJourney(onboarding, compliance, negotiation, market);
    }
}
