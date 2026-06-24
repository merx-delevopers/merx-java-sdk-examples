package com.cooperativa.demo.core.config;

import com.merx.sdk.api.MerxClient;

/**
 * Constrói um {@link MerxClient} a partir de {@link MerxSettings}.
 *
 * <p>Ponto único onde o cliente é montado, reaproveitado pelas três stacks. O {@code MerxClient}
 * é thread-safe e caro de criar (mantém um pool HTTP): construa <b>uma</b> instância e reutilize
 * pelo tempo de vida da aplicação. É exatamente isso que cada framework faz à sua maneira —
 * {@code static} no Java puro, {@code @Bean} singleton no Spring, {@code @Produces @Singleton}
 * no Quarkus.
 */
public final class MerxClientFactory {

    private MerxClientFactory() {
    }

    public static MerxClient create(MerxSettings settings) {
        MerxClient.Builder builder = MerxClient.builder()
                .apiKey(settings.getApiKey())
                .environment(settings.getEnvironment());

        if (settings.getConnectTimeout() != null) {
            builder.connectTimeout(settings.getConnectTimeout());
        }
        if (settings.getReadTimeout() != null) {
            builder.readTimeout(settings.getReadTimeout());
        }
        if (settings.getMaxRetries() != null) {
            builder.maxRetries(settings.getMaxRetries());
        }
        return builder.build();
    }
}
