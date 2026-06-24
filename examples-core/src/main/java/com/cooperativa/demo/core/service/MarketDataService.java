package com.cooperativa.demo.core.service;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.datafeed.QuoteRealtimeResponse;
import com.merx.sdk.model.productivity.Culture;
import com.merx.sdk.model.productivity.Productivity;
import com.merx.sdk.model.productivity.Source;

import java.util.List;

/**
 * Dados de mercado e produtividade (somente leitura): cotações em tempo real (DataFeed) e
 * histórico de produtividade por CAR/cultura. Útil para enriquecer telas e decisões de compra.
 */
public class MarketDataService {

    private final MerxClient merx;

    public MarketDataService(MerxClient merx) {
        this.merx = merx;
    }

    public QuoteRealtimeResponse realtimeQuotes(List<String> symbols) {
        return merx.dataFeed().getRealtimeQuotes(symbols);
    }

    public List<Productivity> productivityByCarAndCulture(String car, Culture culture, String harvest) {
        return merx.productivity()
                .findByCarAndCulture(car, culture, harvest, Source.MERX, false, false);
    }
}
