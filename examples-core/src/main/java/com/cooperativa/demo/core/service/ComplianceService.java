package com.cooperativa.demo.core.service;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.model.carbon.car.CarDto;
import com.merx.sdk.model.carbon.cbios.CbiosReportRequest;
import com.merx.sdk.model.carbon.cbios.CbiosReportResponse;
import com.merx.sdk.model.carbon.eudr.EudrReportRequest;
import com.merx.sdk.model.carbon.eudr.SyncEuReportResponse;
import com.merx.sdk.model.carbon.socioambiental.SocioambientalRequest;
import com.merx.sdk.model.carbon.socioambiental.SyncEsgReportResponse;

/**
 * Compliance / carbono: relatórios CBIOS, EUDR e socioambiental, além da consulta de CAR.
 * Esses relatórios cruzam o CAR da fazenda com camadas geoespaciais e podem ser assíncronos —
 * o status vem no próprio response.
 */
public class ComplianceService {

    private final MerxClient merx;

    public ComplianceService(MerxClient merx) {
        this.merx = merx;
    }

    public CbiosReportResponse cbiosReport(CbiosReportRequest request) {
        return merx.cbios().createReport(request);
    }

    public SyncEuReportResponse eudrReport(EudrReportRequest request) {
        return merx.eudr().createReport(request);
    }

    public SyncEsgReportResponse socioambientalReport(SocioambientalRequest request) {
        return merx.socioambiental().createReport(request);
    }

    public CarDto findCar(String car) {
        return merx.car().findByCode(car);
    }
}
