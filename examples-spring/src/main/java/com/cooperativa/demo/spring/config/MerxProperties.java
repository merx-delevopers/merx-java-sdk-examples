package com.cooperativa.demo.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuração da integração Merx, ligada às propriedades {@code merx.*}
 * ({@code application.yml} / variáveis de ambiente).
 *
 * <pre>
 * merx:
 *   api-key: ${MERX_API_KEY}
 *   environment: SANDBOX
 * </pre>
 */
@ConfigurationProperties(prefix = "merx")
public class MerxProperties {

    /** Token de integração (UUID). Obrigatório. */
    private String apiKey;

    /** SANDBOX (default) ou PRODUCTION. */
    private String environment = "SANDBOX";

    /** Opcionais — caem nos defaults do SDK quando nulos. */
    private Integer maxRetries;
    private Long connectTimeoutSeconds;
    private Long readTimeoutSeconds;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public Long getConnectTimeoutSeconds() {
        return connectTimeoutSeconds;
    }

    public void setConnectTimeoutSeconds(Long connectTimeoutSeconds) {
        this.connectTimeoutSeconds = connectTimeoutSeconds;
    }

    public Long getReadTimeoutSeconds() {
        return readTimeoutSeconds;
    }

    public void setReadTimeoutSeconds(Long readTimeoutSeconds) {
        this.readTimeoutSeconds = readTimeoutSeconds;
    }
}
