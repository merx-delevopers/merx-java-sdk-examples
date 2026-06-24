package com.cooperativa.demo.spring.web;

import com.cooperativa.demo.core.service.ComplianceService;
import com.cooperativa.demo.core.service.MarketDataService;
import com.merx.sdk.model.carbon.car.CarDto;
import com.merx.sdk.model.datafeed.QuoteRealtimeResponse;
import com.merx.sdk.model.productivity.Culture;
import com.merx.sdk.model.productivity.Productivity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Consultas de mercado e compliance (somente leitura): cotações em tempo real, produtividade por
 * CAR/cultura e dados de CAR.
 */
@RestController
public class MarketController {

    private final MarketDataService market;
    private final ComplianceService compliance;

    public MarketController(MarketDataService market, ComplianceService compliance) {
        this.market = market;
        this.compliance = compliance;
    }

    @GetMapping("/api/market/quotes")
    public QuoteRealtimeResponse quotes(@RequestParam(defaultValue = "SOJA,MILHO") List<String> symbols) {
        return market.realtimeQuotes(symbols);
    }

    @GetMapping("/api/market/productivity")
    public List<Productivity> productivity(@RequestParam String car,
                                           @RequestParam(defaultValue = "SOY") Culture culture,
                                           @RequestParam(defaultValue = "2024/2025") String harvest) {
        return market.productivityByCarAndCulture(car, culture, harvest);
    }

    @GetMapping("/api/car/{code}")
    public CarDto car(@PathVariable String code) {
        return compliance.findCar(code);
    }
}
