package com.cooperativa.demo.core.catalog;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.datafeed.QuoteRealtimeResponse;

import java.util.List;

/**
 * Catálogo de exemplos do recurso <b>dataFeed</b> ({@code merx.dataFeed()}).
 *
 * <p>Um método por endpoint público da SDK, com parâmetros de exemplo. Compila contra a SDK
 * (referência copiável + canário de DX); não roda ponta a ponta — fluxo executável em
 * {@code com.cooperativa.demo.core.journey.CooperativaJourney}.
 */
public class DataFeedExamples {
    private final MerxClient merx;
    public DataFeedExamples(MerxClient merx) { this.merx = merx; }

    /** Cotações em tempo real para a lista de tickers (symbols) de commodity informada. */
    public QuoteRealtimeResponse getRealtimeQuotes(List<String> symbols) {
        return merx.dataFeed().getRealtimeQuotes(symbols);
    }
}
