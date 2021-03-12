package io.github.eduardoabporto.tcc.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String assunto;

    @Column(nullable = false, length = 500)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private  Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private  Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "projeto_id")
    private  Projeto projeto;

    @ManyToOne
    @JoinColumn(name = "recurso_id")
    private  Recurso recurso;

    @Column
    private LocalDate dataServico;

    @Column
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date data;

    @Column
    @JsonFormat(pattern = "H[H]:mm:ss")
    private String horaInicial;

    @Column
    @JsonFormat(pattern = "H[H]:mm:ss")
    private String horaFinal;

    @Column
    @JsonFormat(pattern = "H[H]:mm:ss")
    private String horaTrab;

    @Column
    @JsonFormat(pattern = "H[H]:mm:ss")
    private String horaTrasl;

    @Column
    @JsonFormat(pattern = "H[H]:mm:ss")
    private String horaDesc;

}
