package io.github.eduardoabporto.tcc.util;

import java.time.LocalDate;

public class buscaDataIniFim {
    public static void main(String[] args) {
        System.out.println("primeiro dia do mês: " + principioDoMes(LocalDate.now()));
        System.out.println("ultimo dia do mês: " + fimDoMes(LocalDate.now()));
    }

    public static LocalDate principioDoMes(LocalDate l) {

        String local = l.toString();
        int ano = Integer.parseInt(local.split("-")[0]);
        int mes = Integer.parseInt(local.split("-")[1]);

        return LocalDate.of(ano, mes, 1);
    }

    public static LocalDate fimDoMes(LocalDate l) {

        return principioDoMes(l.plusMonths(1)).minusDays(1);
    }
}
