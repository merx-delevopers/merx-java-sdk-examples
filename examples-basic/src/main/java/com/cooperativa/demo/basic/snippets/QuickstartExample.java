package com.cooperativa.demo.basic.snippets;

import com.merx.sdk.api.MerxClient;
import com.merx.sdk.core.Environment;
import com.merx.sdk.model.baseline.address.CreateAddressRequest;
import com.merx.sdk.model.baseline.enums.MaritalStatus;
import com.merx.sdk.model.baseline.farm.CreateFarmRequest;
import com.merx.sdk.model.baseline.farm.FarmResponse;
import com.merx.sdk.model.baseline.producer.CreateProducerRequest;
import com.merx.sdk.model.baseline.producer.ProducerResponse;
import com.merx.sdk.model.common.IdResponse;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Snippet mínimo e autocontido: producer → endereço → fazenda, e leitura de volta.
 *
 * <p>É o "olá mundo" do SDK — para o fluxo completo da Cooperativa Demo veja
 * {@link com.cooperativa.demo.basic.App}.
 *
 * <p>Rode com:
 * {@code MERX_API_KEY=<token> mvn -q -pl examples-basic exec:java
 *  -Dexec.mainClass=com.cooperativa.demo.basic.snippets.QuickstartExample}
 */
public class QuickstartExample {

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

        IdResponse producer = merx.producers().create(CreateProducerRequest.builder()
                .companyName("Fazenda Demo LTDA")
                .tradingName("Fazenda Demo")
                .socialIdentity("12345678000199")
                .email("contato@fazendademo.com")
                .dateOfBirth(LocalDate.of(1985, 7, 20))
                .maritalStatus(MaritalStatus.MARRIED)
                .build());

        System.out.println("Producer criado: " + producer.getId());

        merx.producers().addAddress(producer.getId(), CreateAddressRequest.builder()
                .street("Rodovia BR-277")
                .number("KM 50")
                .neighborhood("Zona Rural")
                .zipCode("85000-000")
                .city("Guarapuava")
                .state("PR")
                .country("BR")
                .build());

        IdResponse farm = merx.farms().create(producer.getId(), CreateFarmRequest.builder()
                .name("Fazenda Demo - Sede")
                .own(true)
                .car("PR-1234567890")
                .area("250.50")
                .build());

        System.out.println("Farm criada: " + farm.getId());

        ProducerResponse details = merx.producers().findById(producer.getId())
                .orElseThrow(() -> new IllegalStateException("Producer recém-criado não encontrado"));
        System.out.println("Trading name: " + details.getTradingName());

        FarmResponse farmDetails = merx.farms().findById(farm.getId()).orElseThrow();
        System.out.println("CAR: " + farmDetails.getCar());

        printNetwork(merx, producer.getId());
    }

    private static void printNetwork(MerxClient merx, UUID producerId) {
        var farms = merx.farms().findByProducer(producerId);
        System.out.println("Fazendas vinculadas ao produtor: " + farms.size());
        farms.forEach(f -> System.out.println(" - " + f.getName() + " (CAR=" + f.getCar() + ")"));
    }
}
