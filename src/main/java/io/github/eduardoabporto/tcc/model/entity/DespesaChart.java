package io.github.eduardoabporto.tcc.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DespesaChart {

    private String nomeLog;
    private String cliLog;
    private String tipoDesp;
    private String valDespesa;
    private Double horaTotal;



}
