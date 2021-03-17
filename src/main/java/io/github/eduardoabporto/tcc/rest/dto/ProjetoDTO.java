package io.github.eduardoabporto.tcc.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
public class ProjetoDTO {
    @NotEmpty(message = "{campo.assunto.obrigatorio}")
    private String nome;

    //   @NotEmpty(message = "{campo.data.obrigatorio}")
    private Date data;

    private String horaTrasl;

    private String horaDesc;

    @NotEmpty(message = "{campo.valorHora.obrigatorio}")
    private String valorHora;

    @NotNull(message = "{campo.cliente.obrigatorio}")
    private Integer idCliente;

}
