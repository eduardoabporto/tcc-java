package io.github.eduardoabporto.tcc.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
public class DespesaDTO {

    //   @NotEmpty(message = "{campo.data.obrigatorio}")
    private Date data;

    @NotEmpty(message = "{campo.descricao.obrigatorio}")
    private String descricao;

    @NotEmpty(message = "{campo.valorDespesa.obrigatorio}")
    private String valorDespesa;

    @NotNull(message = "{campo.empresa.obrigatorio}")
    private Integer idEmpresa;

    @NotNull(message = "{campo.cliente.obrigatorio}")
    private Integer idCliente;

    @NotNull(message = "{campo.projeto.obrigatorio}")
    private Integer idProjeto;

    @NotNull(message = "{campo.despesa.obrigatorio}")
    private Integer idTipoDespesa;

    private String userLog;

    private String atendimento;

}
