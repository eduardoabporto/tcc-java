package io.github.eduardoabporto.tcc.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String assunto;

    @Column
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date data;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Column
    @JsonFormat(pattern = "H[H]:mm:ss")
    private String horaInicial;

    @Column
    @JsonFormat(pattern = "H[H]:mm:ss")
    private String horaFinal;

    @Column
    @JsonFormat(pattern = "H[H]:mm:ss")
    private String horaTrasl;

    @Column
    @JsonFormat(pattern = "H[H]:mm:ss")
    private String horaDesc;

    @Column
    @JsonFormat(pattern = "H[H]:mm:ss")
    private String horaTrab;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private  Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private  Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "projeto_id")
    private  Projeto projeto;

    private String userLog;

    private String atendimento;

}
