package com.cooperativa.demo.core.sample;

import java.util.Random;

/**
 * Gera CPF e CNPJ <b>válidos</b> (com dígitos verificadores corretos) para os dados de exemplo.
 *
 * <p>O backend da Merx valida o dígito verificador de CPF/CNPJ — um número puramente aleatório
 * seria rejeitado. Estes documentos são sintaticamente válidos porém fictícios; servem só para
 * o sandbox. <b>Nunca</b> use documentos reais de terceiros em testes.
 */
public final class Documents {

    private Documents() {
    }

    /** Gera um CPF válido (11 dígitos, só números). */
    public static String randomCpf(Random random) {
        int[] n = new int[11];
        for (int i = 0; i < 9; i++) {
            n[i] = random.nextInt(10);
        }
        n[9] = cpfCheckDigit(n, 9, 10);
        n[10] = cpfCheckDigit(n, 10, 11);
        return digitsToString(n);
    }

    /** Gera um CNPJ válido (14 dígitos, só números) com filial {@code 0001}. */
    public static String randomCnpj(Random random) {
        int[] n = new int[14];
        for (int i = 0; i < 8; i++) {
            n[i] = random.nextInt(10);
        }
        // Filial 0001
        n[8] = 0;
        n[9] = 0;
        n[10] = 0;
        n[11] = 1;
        n[12] = cnpjCheckDigit(n, 12, new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
        n[13] = cnpjCheckDigit(n, 13, new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
        return digitsToString(n);
    }

    private static int cpfCheckDigit(int[] n, int upTo, int startWeight) {
        int sum = 0;
        int weight = startWeight;
        for (int i = 0; i < upTo; i++) {
            sum += n[i] * weight--;
        }
        int rest = sum % 11;
        return (rest < 2) ? 0 : 11 - rest;
    }

    private static int cnpjCheckDigit(int[] n, int upTo, int[] weights) {
        int sum = 0;
        for (int i = 0; i < upTo; i++) {
            sum += n[i] * weights[i];
        }
        int rest = sum % 11;
        return (rest < 2) ? 0 : 11 - rest;
    }

    private static String digitsToString(int[] n) {
        StringBuilder sb = new StringBuilder(n.length);
        for (int digit : n) {
            sb.append(digit);
        }
        return sb.toString();
    }
}
