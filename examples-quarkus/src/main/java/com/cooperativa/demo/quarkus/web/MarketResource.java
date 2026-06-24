package com.cooperativa.demo.quarkus.web;

import com.cooperativa.demo.core.service.ComplianceService;
import com.cooperativa.demo.core.service.MarketDataService;
import com.merx.sdk.model.carbon.car.CarDto;
import com.merx.sdk.model.datafeed.QuoteRealtimeResponse;
import com.merx.sdk.model.productivity.Culture;
import com.merx.sdk.model.productivity.Productivity;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

/**
 * Consultas de mercado e compliance (somente leitura): cotações em tempo real, produtividade por
 * CAR/cultura e dados de CAR.
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class MarketResource {

    @Inject
    MarketDataService market;

    @Inject
    ComplianceService compliance;

    @GET
    @Path("/market/quotes")
    public QuoteRealtimeResponse quotes(@QueryParam("symbols") @DefaultValue("SOJA,MILHO") String symbols) {
        return market.realtimeQuotes(Arrays.asList(symbols.split(",")));
    }

    @GET
    @Path("/market/productivity")
    public List<Productivity> productivity(@QueryParam("car") String car,
                                           @QueryParam("culture") @DefaultValue("SOY") Culture culture,
                                           @QueryParam("harvest") @DefaultValue("2024/2025") String harvest) {
        return market.productivityByCarAndCulture(car, culture, harvest);
    }

    @GET
    @Path("/car/{code}")
    public CarDto car(@PathParam("code") String code) {
        return compliance.findCar(code);
    }
}
