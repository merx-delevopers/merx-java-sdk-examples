package com.cooperativa.demo.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicação Spring Boot de exemplo da Cooperativa Demo integrando com a Merx.
 *
 * <p>Suba com: {@code MERX_API_KEY=<token> mvn -pl examples-spring spring-boot:run}
 * e acesse os endpoints sob {@code /api} (ver os controllers em {@code web/}).
 */
@SpringBootApplication
public class MerxIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MerxIntegrationApplication.class, args);
    }
}
