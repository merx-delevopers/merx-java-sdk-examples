package com.cooperativa.demo.core.sample;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Garante que os documentos gerados são sintaticamente válidos (dígito verificador correto) —
 * o backend Merx os rejeitaria caso contrário. A validação aqui é independente da geração.
 */
class DocumentsTest {

    private final Random random = new Random(42); // semente fixa: teste determinístico

    @RepeatedTest(50)
    void shouldGenerateValidCpf() {
        String cpf = Documents.randomCpf(random);
        assertThat(cpf).hasSize(11).containsOnlyDigits();
        assertThat(isValidCpf(cpf)).as("CPF %s deve ser válido", cpf).isTrue();
    }

    @RepeatedTest(50)
    void shouldGenerateValidCnpj() {
        String cnpj = Documents.randomCnpj(random);
        assertThat(cnpj).hasSize(14).containsOnlyDigits();
        assertThat(cnpj.substring(8, 12)).as("filial deve ser 0001").isEqualTo("0001");
        assertThat(isValidCnpj(cnpj)).as("CNPJ %s deve ser válido", cnpj).isTrue();
    }

    @Test
    void shouldGenerateDistinctDocuments() {
        Random r = new Random(7);
        assertThat(Documents.randomCpf(r)).isNotEqualTo(Documents.randomCpf(r));
        assertThat(Documents.randomCnpj(r)).isNotEqualTo(Documents.randomCnpj(r));
    }

    // ---- Validadores de referência (algoritmo da Receita Federal) ------------------------------

    private static boolean isValidCpf(String cpf) {
        int d1 = cpfCheck(cpf, 9, 10);
        int d2 = cpfCheck(cpf, 10, 11);
        return d1 == (cpf.charAt(9) - '0') && d2 == (cpf.charAt(10) - '0');
    }

    private static int cpfCheck(String cpf, int len, int startWeight) {
        int sum = 0;
        int weight = startWeight;
        for (int i = 0; i < len; i++) {
            sum += (cpf.charAt(i) - '0') * weight--;
        }
        int rest = sum % 11;
        return rest < 2 ? 0 : 11 - rest;
    }

    private static boolean isValidCnpj(String cnpj) {
        int[] w1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] w2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int d1 = cnpjCheck(cnpj, 12, w1);
        int d2 = cnpjCheck(cnpj, 13, w2);
        return d1 == (cnpj.charAt(12) - '0') && d2 == (cnpj.charAt(13) - '0');
    }

    private static int cnpjCheck(String cnpj, int len, int[] weights) {
        int sum = 0;
        for (int i = 0; i < len; i++) {
            sum += (cnpj.charAt(i) - '0') * weights[i];
        }
        int rest = sum % 11;
        return rest < 2 ? 0 : 11 - rest;
    }
}
