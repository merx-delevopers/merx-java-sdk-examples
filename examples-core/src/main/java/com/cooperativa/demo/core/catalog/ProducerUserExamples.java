package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.baseline.enums.ClickSignAuthType;
import com.merx.sdk.model.baseline.enums.ClickSignDeliveryType;
import com.merx.sdk.model.baseline.enums.ClickSignSignAsType;
import com.merx.sdk.model.identity.IdentityResponse;
import com.merx.sdk.model.identity.producer.CreateProducerUserRequest;
import com.merx.sdk.model.identity.producer.UpdateProducerUserRequest;
import com.merx.sdk.model.identity.producer.UserResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Catálogo de exemplos do recurso <b>producerUsers</b> ({@code merx.producerUsers()}).
 *
 * <p>Um método por endpoint público da SDK, com request de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — fluxo executável em
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class ProducerUserExamples {

    private final MerxClient merx;

    public ProducerUserExamples(MerxClient merx) {
        this.merx = merx;
    }

    /** Cria um usuário vinculado a um produtor (signatário ClickSign). */
    public UUID create(UUID producerId) {
        return merx.producerUsers().create(producerId, CreateProducerUserRequest.builder()
                .fullName("João da Silva")
                .email("joao.silva@fazendademo.com.br")
                .phoneNumber("+5511999999999")
                .socialId("12345678909")
                .auth(ClickSignAuthType.EMAIL)
                .delivery(ClickSignDeliveryType.EMAIL)
                .signAs(ClickSignSignAsType.LEGAL_REPRESENTATIVE)
                .defaultSigner(Boolean.TRUE)
                .build()).getId();
    }

    /** Atualiza um usuário de produtor existente. */
    public void update(UUID producerUserId, UUID producerId) {
        merx.producerUsers().update(producerUserId, producerId, UpdateProducerUserRequest.builder()
                .fullName("João da Silva Júnior")
                .phoneNumber("+5511988888888")
                .email("joao.junior@fazendademo.com.br")
                .auth(ClickSignAuthType.SMS)
                .delivery(ClickSignDeliveryType.EMAIL)
                .signAs(ClickSignSignAsType.WITNESS)
                .defaultSigner(Boolean.FALSE)
                .build());
    }

    /** Remove um usuário de produtor. */
    public void remove(UUID producerUserId, UUID producerId) {
        merx.producerUsers().remove(producerUserId, producerId);
    }

    /** Lista os usuários de um produtor. */
    public List<UserResponse> findByProducer(UUID producerId) {
        return merx.producerUsers().findByProducer(producerId);
    }

    /** Busca um usuário do produtor por CPF (social id). */
    public Optional<IdentityResponse> findBySocialId(UUID producerId, String socialId) {
        return merx.producerUsers().findBySocialId(producerId, socialId);
    }
}
