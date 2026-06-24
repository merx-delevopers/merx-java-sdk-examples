package com.cooperativa.demo.basic.snippets;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.core.Environment;
import com.merx.sdk.model.carbon.cbios.CbiosReportRequest;
import com.merx.sdk.model.carbon.cbios.CbiosReportResponse;
import com.merx.sdk.model.carbon.enums.Layer;
import com.merx.sdk.model.carbon.eudr.EudrReportRequest;
import com.merx.sdk.model.carbon.eudr.SyncEuReportResponse;
import com.merx.sdk.model.carbon.socioambiental.SocioambientalRequest;
import com.merx.sdk.model.carbon.socioambiental.SyncEsgReportResponse;

/**
 * Snippet mínimo e autocontido: geração de relatórios de carbono (CBIOS, EUDR, socioambiental).
 *
 * <p>Rode com:
 * {@code MERX_API_KEY=<token> mvn -q -pl examples-basic exec:java
 *  -Dexec.mainClass=com.cooperativa.demo.basic.snippets.ReportsExample}
 */
public class ReportsExample {

    public static void main(String[] args) {
        String apiKey = System.getenv("MERX_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            System.err.println("Set MERX_API_KEY environment variable before running.");
            System.exit(1);
        }

        MerxClient merx = MerxClient.builder()
                .apiKey(apiKey)
                .environment(Environment.SANDBOX)
                .build();

        CbiosReportResponse cbios = merx.cbios().createReport(CbiosReportRequest.builder()
                .producerName("Produtor Demo")
                .producerDocument("12345678901")
                .username("integration@cliente.com")
                .name("Relatório CBIOS Demo")
                .layer(Layer.SOY)
                .civilYear(2024)
                .harvestCode(2324)
                .build());
        System.out.println("CBIOS report: " + cbios.getReportRequestId() + " status=" + cbios.getStatus());

        SyncEuReportResponse eudr = merx.eudr().createReport(EudrReportRequest.builder()
                .car("PR-1234567890")
                .producerName("Produtor Demo")
                .producerDocument("12345678901")
                .build());
        System.out.println("EUDR report: " + eudr.getReportRequestId() + " status=" + eudr.getEuStatusSummary());

        SyncEsgReportResponse esg = merx.socioambiental().createReport(SocioambientalRequest.builder()
                .car("PR-1234567890")
                .producerName("Produtor Demo")
                .producerDocument("12345678901")
                .build());
        System.out.println("Socioambiental report: " + esg.getReportRequestId() + " status=" + esg.getEsgStatusSummary());
    }
}
