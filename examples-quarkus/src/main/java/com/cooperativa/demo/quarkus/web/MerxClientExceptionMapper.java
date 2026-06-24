package com.cooperativa.demo.quarkus.web;

import com.merx.sdk.core.exception.MerxClientException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Mapeia {@link MerxClientException} (falha local: rede/timeout/serialização) para
 * {@code 502 Bad Gateway}.
 */
@Provider
public class MerxClientExceptionMapper implements ExceptionMapper<MerxClientException> {

    @Override
    public Response toResponse(MerxClientException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 502);
        body.put("error", ex.getClass().getSimpleName());
        body.put("merxResponse", ex.getMessage());
        return Response.status(502).entity(body).build();
    }
}
