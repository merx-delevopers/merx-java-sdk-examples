package com.cooperativa.demo.spring.web;

import com.cooperativa.demo.core.sample.SampleData;
import com.cooperativa.demo.core.service.OnboardingService;
import com.merx.sdk.model.baseline.farm.FarmResponse;
import com.merx.sdk.model.baseline.producer.ProducerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Cadastro de produtores via SDK. Mostra controllers finos delegando ao {@link OnboardingService}
 * (que encapsula as chamadas ao SDK).
 *
 * <p>Para manter o exemplo simples, {@code POST /api/producers} gera um produtor fictício a partir
 * do {@link SampleData} em vez de receber um corpo — o foco é a integração, não a modelagem de
 * formulários.
 */
@RestController
@RequestMapping("/api/producers")
public class ProducerController {

    private final OnboardingService onboarding;

    public ProducerController(OnboardingService onboarding) {
        this.onboarding = onboarding;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createSampleProducer() {
        SampleData sample = new SampleData();
        UUID id = onboarding.createProducer(sample.producer());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id", id,
                "document", sample.producerDocument()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProducerResponse> findById(@PathVariable UUID id) {
        return onboarding.findProducer(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/farms")
    public List<FarmResponse> farms(@PathVariable UUID id) {
        return onboarding.listFarms(id);
    }
}
