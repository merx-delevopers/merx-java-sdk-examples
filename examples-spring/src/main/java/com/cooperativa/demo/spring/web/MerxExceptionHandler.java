package com.cooperativa.demo.spring.web;

import com.merx.sdk.core.exception.MerxApiException;
import com.merx.sdk.core.exception.MerxClientException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Traduz a hierarquia tipada de exceções do SDK para respostas HTTP.
 *
 * <p>{@link MerxApiException} carrega o status e o corpo da resposta da Merx — repassamos o mesmo
 * status ao chamador. {@link MerxClientException} é falha local (rede/timeout/serialização) e vira
 * {@code 502 Bad Gateway}. Este é o ponto idiomático no Spring para centralizar o mapeamento de
 * erros do SDK.
 */
@RestControllerAdvice
public class MerxExceptionHandler {

    @ExceptionHandler(MerxApiException.class)
    public ResponseEntity<Map<String, Object>> handleApi(MerxApiException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(body(
                ex.getStatusCode(),
                ex.getClass().getSimpleName(),
                ex.getResponseBody()));
    }

    @ExceptionHandler(MerxClientException.class)
    public ResponseEntity<Map<String, Object>> handleClient(MerxClientException ex) {
        return ResponseEntity.status(502).body(body(
                502,
                ex.getClass().getSimpleName(),
                ex.getMessage()));
    }

    private static Map<String, Object> body(int status, String error, String detail) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("status", status);
        map.put("error", error);
        map.put("merxResponse", detail);
        return map;
    }
}
