package com.cooperativa.demo.quarkus.web;

import com.merx.sdk.core.exception.MerxApiException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Mapeia {@link MerxApiException} (resposta HTTP de erro da Merx) para uma resposta JAX-RS com o
 * mesmo status. É o ponto idiomático no Quarkus para centralizar o tratamento de erros do SDK.
 */
@Provider
public class MerxApiExceptionMapper implements ExceptionMapper<MerxApiException> {

    @Override
    public Response toResponse(MerxApiException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", ex.getStatusCode());
        body.put("error", ex.getClass().getSimpleName());
        body.put("merxResponse", ex.getResponseBody());
        return Response.status(ex.getStatusCode()).entity(body).build();
    }
}
