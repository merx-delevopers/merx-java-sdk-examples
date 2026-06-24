package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.baseline.cooperative.NetworkFilter;
import com.merx.sdk.model.baseline.cooperative.NetworkResponse;
import com.merx.sdk.model.baseline.enums.ProducerCooperativeStatus;
import com.merx.sdk.model.baseline.enums.ProducerType;
import com.merx.sdk.model.baseline.enums.States;
import com.merx.sdk.model.common.PageResponse;

import java.util.List;

/**
 * Catálogo de exemplos do recurso <b>cooperatives</b> ({@code merx.cooperatives()}).
 *
 * <p>Um método por endpoint público da SDK, com request de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — para um fluxo executável veja
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class CooperativeExamples {

    private final MerxClient merx;

    public CooperativeExamples(MerxClient merx) {
        this.merx = merx;
    }

    /** Lista paginada da rede da cooperativa, com filtro, página, tamanho e ordenação. */
    public PageResponse<NetworkResponse> findNetworksPaginated() {
        return merx.cooperatives().findNetworksPaginated(sampleFilter(), 0, 20, "companyName,asc");
    }

    /** Lista paginada da rede da cooperativa, com filtro, página e tamanho (ordenação padrão). */
    public PageResponse<NetworkResponse> findNetworksPaginatedNoSort() {
        return merx.cooperatives().findNetworksPaginated(sampleFilter(), 0, 20);
    }

    private static NetworkFilter sampleFilter() {
        return NetworkFilter.builder()
                .searchParam("demo")
                .companyName("Cooperativa Demo")
                .allStatus(Boolean.TRUE)
                .producerTypes(List.of(ProducerType.PRODUCER))
                .analisysStatus(List.of(ProducerCooperativeStatus.APPROVED))
                .states(List.of(States.MS, States.MT))
                .cities(List.of("Campo Grande", "Dourados"))
                .build();
    }
}
