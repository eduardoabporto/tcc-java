package io.github.eduardoabporto.tcc.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class OrdemServicoDTO {
    @NotEmpty(message = "{campo.assunto.obrigatorio}")
    private String assunto;

    //   @NotEmpty(message = "{campo.data.obrigatorio}")
    private LocalDate data;

    @NotEmpty(message = "{campo.descricao.obrigatorio}")
    private String descricao;

    @NotEmpty(message = "{campo.data.obrigatorio}")
    private String horaInicial;

    @NotEmpty(message = "{campo.data.obrigatorio}")
    private String horaFinal;

    private String horaTrasl;

    private String horaDesc;

    private String horaTrab;

    @NotNull(message = "{campo.empresa.obrigatorio}")
    private Integer idEmpresa;

    @NotNull(message = "{campo.cliente.obrigatorio}")
    private Integer idCliente;

    @NotNull(message = "{campo.projeto.obrigatorio}")
    private Integer idProjeto;

    private String userLog;

}
