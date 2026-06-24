package com.cooperativa.demo.core.config;

import com.merx.sdk.core.Environment;

import java.time.Duration;
import java.util.Objects;

/**
 * Configuração de integração da Cooperativa Demo com a Merx, agnóstica de framework.
 *
 * <p>É o "contrato de configuração" que cada stack (Java puro, Spring, Quarkus) preenche
 * à sua maneira — variável de ambiente, {@code application.yml}, {@code application.properties} —
 * e entrega ao {@link MerxClientFactory} para construir um {@code MerxClient}.
 *
 * <p>Apenas {@link #getApiKey()} é obrigatório; os demais campos caem nos defaults do SDK
 * quando {@code null}.
 */
public final class MerxSettings {

    private final String apiKey;
    private final Environment environment;
    private final Duration connectTimeout;
    private final Duration readTimeout;
    private final Integer maxRetries;

    private MerxSettings(Builder b) {
        this.apiKey = b.apiKey;
        this.environment = b.environment;
        this.connectTimeout = b.connectTimeout;
        this.readTimeout = b.readTimeout;
        this.maxRetries = b.maxRetries;
    }

    public String getApiKey() {
        return apiKey;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Resolve as configurações a partir de variáveis de ambiente. Usado pelo exemplo Java puro;
     * Spring/Quarkus preferem injeção via arquivos de configuração.
     *
     * <ul>
     *   <li>{@code MERX_API_KEY} — token de integração (obrigatório).</li>
     *   <li>{@code MERX_ENV} — {@code SANDBOX} (default) ou {@code PRODUCTION}.</li>
     * </ul>
     */
    public static MerxSettings fromEnv() {
        String apiKey = System.getenv("MERX_API_KEY");
        String env = System.getenv("MERX_ENV");
        Environment environment = (env == null || env.isBlank())
                ? Environment.SANDBOX
                : Environment.valueOf(env.trim().toUpperCase());
        return builder()
                .apiKey(apiKey)
                .environment(environment)
                .build();
    }

    public static final class Builder {
        private String apiKey;
        private Environment environment = Environment.SANDBOX;
        private Duration connectTimeout;
        private Duration readTimeout;
        private Integer maxRetries;

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder environment(Environment environment) {
            if (environment != null) {
                this.environment = environment;
            }
            return this;
        }

        public Builder connectTimeout(Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder readTimeout(Duration readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public MerxSettings build() {
            if (apiKey == null || apiKey.isBlank()) {
                throw new IllegalStateException(
                        "Token Merx ausente. Defina MERX_API_KEY (ou merx.api-key na config do framework). "
                                + "Solicite um token de sandbox via support-api@merx.tech.");
            }
            return new MerxSettings(this);
        }

        @Override
        public String toString() {
            return "MerxSettings.Builder{environment=" + environment + ", apiKey=" + mask(apiKey) + '}';
        }

        private static String mask(String value) {
            if (value == null || value.length() < 6) {
                return "***";
            }
            return value.substring(0, 4) + "…" + value.substring(value.length() - 2);
        }
    }

    @Override
    public String toString() {
        return "MerxSettings{environment=" + environment + ", apiKey set=" + (apiKey != null) + '}';
    }

    public boolean sameApiKey(MerxSettings other) {
        return other != null && Objects.equals(this.apiKey, other.apiKey);
    }
}
