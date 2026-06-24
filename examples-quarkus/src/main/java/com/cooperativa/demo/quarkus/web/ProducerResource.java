package com.cooperativa.demo.quarkus.web;

import com.cooperativa.demo.core.sample.SampleData;
import com.cooperativa.demo.core.service.OnboardingService;
import com.merx.sdk.model.baseline.farm.FarmResponse;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Cadastro de produtores via SDK. Recursos JAX-RS finos delegando ao {@link OnboardingService}.
 *
 * <p>{@code POST /api/producers} gera um produtor fictício a partir do {@link SampleData}
 * (o foco é a integração, não a modelagem de formulários).
 */
@Path("/api/producers")
@Produces(MediaType.APPLICATION_JSON)
public class ProducerResource {

    @Inject
    OnboardingService onboarding;

    @POST
    public Response createSampleProducer() {
        SampleData sample = new SampleData();
        UUID id = onboarding.createProducer(sample.producer());
        return Response.status(Response.Status.CREATED)
                .entity(Map.of("id", id, "document", sample.producerDocument()))
                .build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") UUID id) {
        return onboarding.findProducer(id)
                .map(p -> Response.ok(p).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/{id}/farms")
    public List<FarmResponse> farms(@PathParam("id") UUID id) {
        return onboarding.listFarms(id);
    }
}
