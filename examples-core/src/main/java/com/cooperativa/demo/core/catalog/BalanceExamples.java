package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.order.balance.CreateBalanceRequest;
import com.merx.sdk.model.order.balance.CropRequest;

import java.math.BigDecimal;
import java.util.List;

/**
 * Catálogo de exemplos do recurso <b>balances</b> ({@code merx.balances()}).
 *
 * <p>Um método por endpoint público da SDK, com request de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — fluxo executável em
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class BalanceExamples {

    private final MerxClient merx;

    public BalanceExamples(MerxClient merx) {
        this.merx = merx;
    }

    /** Envia o saldo de inventário de um produtor (avulso). */
    public void process() {
        merx.balances().process(sampleBalance("12345678909"));
    }

    /** Envia saldos de inventário de vários produtores em lote. */
    public void processBatch() {
        merx.balances().processBatch(List.of(
                sampleBalance("12345678909"),
                sampleBalance("98765432100")));
    }

    private static CreateBalanceRequest sampleBalance(String cpfCnpj) {
        return CreateBalanceRequest.builder()
                .cpfCnpj(cpfCnpj)
                .crops(List.of(CropRequest.builder()
                        .name("SOJA")
                        .depositBalanceTon(new BigDecimal("120.50"))
                        .toFixBalanceTon(new BigDecimal("40.00"))
                        .totalBalanceTon(new BigDecimal("160.50"))
                        .build()))
                .build();
    }
}
