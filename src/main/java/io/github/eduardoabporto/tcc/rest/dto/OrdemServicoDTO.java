package io.github.eduardoabporto.tcc.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
public class OrdemServicoDTO {
    @NotEmpty(message = "{campo.assunto.obrigatorio}")
    private String assunto;

    @NotEmpty(message = "{campo.descricao.obrigatorio}")
    private String descricao;

    @NotEmpty(message = "{campo.data.obrigatorio}")
    private Date data;

    @NotEmpty(message = "{campo.data.obrigatorio}")
    private String horaInicial;

    @NotEmpty(message = "{campo.data.obrigatorio}")
    private String horaFinal;

    private String horaTrab;

    private String horaTrasl;

    private String horaDesc;


    @NotNull(message = "{campo.cliente.obrigatorio}")
    private Integer idCliente;
}
