package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.baseline.wallet.CreateWalletRequest;
import com.merx.sdk.model.baseline.wallet.UpdateWalletRequest;
import com.merx.sdk.model.baseline.wallet.WalletCompanyResponse;
import com.merx.sdk.model.baseline.wallet.WalletIdentityResponse;
import com.merx.sdk.model.baseline.wallet.WalletResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Catálogo de exemplos do recurso <b>wallets</b> ({@code merx.wallets()}).
 *
 * <p>Um método por endpoint público da SDK, com request de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — para um fluxo executável veja
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class WalletExamples {

    private final MerxClient merx;

    public WalletExamples(MerxClient merx) {
        this.merx = merx;
    }

    /** Cria uma carteira. */
    public UUID create(String ownerId) {
        return merx.wallets().create(CreateWalletRequest.builder()
                .name("Carteira Safra 24/25")
                .description("Agrupamento de pagamento da safra 24/25")
                .ownerId(ownerId)
                .build()).getId();
    }

    /** Atualiza uma carteira existente. */
    public void update(UUID walletId, String ownerId) {
        merx.wallets().update(walletId, UpdateWalletRequest.builder()
                .name("Carteira Safra 24/25 (revisada)")
                .description("Descrição atualizada")
                .ownerId(ownerId)
                .build());
    }

    /** Faz soft-delete de uma carteira. */
    public void delete(UUID walletId) {
        merx.wallets().delete(walletId);
    }

    /** Busca uma carteira por id. */
    public Optional<WalletResponse> findById(UUID walletId) {
        return merx.wallets().findById(walletId);
    }

    /** Lista todas as carteiras. */
    public List<WalletResponse> findAll() {
        return merx.wallets().findAll();
    }

    /** Lista as carteiras pelo CPF/CNPJ do dono. */
    public List<WalletResponse> findByOwnerSocialIdentity(String socialIdentity) {
        return merx.wallets().findByOwnerSocialIdentity(socialIdentity);
    }

    /** Lista os participantes (identidades) de uma carteira. */
    public List<WalletIdentityResponse> getParticipants(UUID walletId) {
        return merx.wallets().getParticipants(walletId);
    }

    /** Lista os produtores (empresas) de uma carteira. */
    public List<WalletCompanyResponse> getProducers(UUID walletId) {
        return merx.wallets().getProducers(walletId);
    }

    /** Adiciona um produtor à carteira. */
    public void addProducer(UUID walletId, UUID producerId) {
        merx.wallets().addProducer(walletId, producerId);
    }

    /** Remove um produtor da carteira. */
    public void removeProducer(UUID walletId, UUID producerId) {
        merx.wallets().removeProducer(walletId, producerId);
    }

    /** Adiciona um participante (identidade) à carteira. */
    public void addParticipant(UUID walletId, String participantId) {
        merx.wallets().addParticipant(walletId, participantId);
    }

    /** Remove um participante (identidade) da carteira. */
    public void removeParticipant(UUID walletId, String participantId) {
        merx.wallets().removeParticipant(walletId, participantId);
    }
}
