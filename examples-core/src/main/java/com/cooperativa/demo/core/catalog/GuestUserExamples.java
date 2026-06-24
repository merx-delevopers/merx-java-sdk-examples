package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.baseline.address.CreateAddressRequest;
import com.merx.sdk.model.baseline.enums.ClickSignAuthType;
import com.merx.sdk.model.baseline.enums.ClickSignDeliveryType;
import com.merx.sdk.model.baseline.enums.ClickSignSignAsType;
import com.merx.sdk.model.baseline.enums.MaritalStatus;
import com.merx.sdk.model.baseline.enums.SignerType;
import com.merx.sdk.model.baseline.guestUser.CreateGuestUserRequest;
import com.merx.sdk.model.baseline.guestUser.CreateGuestUserSignerRequest;
import com.merx.sdk.model.baseline.guestUser.GuestUserResponse;
import com.merx.sdk.model.baseline.guestUser.UpdateGuestUserRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Catálogo de exemplos do recurso <b>guest-users</b> ({@code merx.guestUsers()}).
 *
 * <p>Um método por endpoint público da SDK, com request de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — para um fluxo executável veja
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class GuestUserExamples {

    private final MerxClient merx;

    public GuestUserExamples(MerxClient merx) {
        this.merx = merx;
    }

    /** Cria um usuário convidado (signatário) vinculado a um produtor. */
    public UUID create(UUID producerId) {
        return merx.guestUsers().create(producerId, CreateGuestUserRequest.builder()
                .name("João da Silva")
                .socialSecurity("12345678909")
                .phone("+5567999990000")
                .email("joao.silva@example.com")
                .identityCard("123456789")
                .maritalStatus(MaritalStatus.MARRIED)
                .address(sampleAddress())
                .signer(sampleSigner())
                .build()).getId();
    }

    /** Atualiza um usuário convidado vinculado a um produtor. */
    public void update(UUID guestUserId, UUID producerId) {
        merx.guestUsers().update(guestUserId, producerId, UpdateGuestUserRequest.builder()
                .name("João da Silva Júnior")
                .socialSecurity("12345678909")
                .phone("+5567999991111")
                .email("joao.junior@example.com")
                .identityCard("987654321")
                .maritalStatus(MaritalStatus.DIVORCED)
                .address(sampleAddress())
                .signer(sampleSigner())
                .build());
    }

    /** Faz soft-delete de um usuário convidado. */
    public void delete(UUID guestUserId) {
        merx.guestUsers().delete(guestUserId);
    }

    /** Busca um usuário convidado por id. */
    public Optional<GuestUserResponse> findById(UUID guestUserId) {
        return merx.guestUsers().findById(guestUserId);
    }

    /** Lista os usuários convidados de um produtor. */
    public List<GuestUserResponse> findByProducer(UUID producerId) {
        return merx.guestUsers().findByProducer(producerId);
    }

    private static CreateGuestUserSignerRequest sampleSigner() {
        return CreateGuestUserSignerRequest.builder()
                .auth(ClickSignAuthType.EMAIL)
                .delivery(ClickSignDeliveryType.EMAIL)
                .name("João da Silva")
                .type(SignerType.PRODUCER)
                .defaultSigner(Boolean.TRUE)
                .email("joao.silva@example.com")
                .defaultAuth(Boolean.TRUE)
                .signAs(ClickSignSignAsType.LEGAL_REPRESENTATIVE)
                .hasDocumentation(true)
                .selfieEnabled(false)
                .handwrittenEnabled(false)
                .officialDocumentEnabled(true)
                .phoneNumber("+5567999990000")
                .build();
    }

    private static CreateAddressRequest sampleAddress() {
        return CreateAddressRequest.builder()
                .street("Rua das Acácias")
                .number("45")
                .complement("Apto 101")
                .neighborhood("Jardim dos Estados")
                .zipCode("79020-000")
                .city("Campo Grande")
                .state("MS")
                .country("Brasil")
                .addressType("USER")
                .build();
    }
}
