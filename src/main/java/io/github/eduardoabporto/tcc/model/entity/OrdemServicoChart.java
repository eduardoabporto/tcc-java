package io.github.eduardoabporto.tcc.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrdemServicoChart {

    private String nomeLog;
    private String cliLog;
    private String tipoDesp;
    private String valDespesa;
    private String horaTotal;
    private String QtdeTotal;

}

