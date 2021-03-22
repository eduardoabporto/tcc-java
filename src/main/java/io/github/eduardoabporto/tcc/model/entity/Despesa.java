package io.github.eduardoabporto.tcc.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class Despesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date data;

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
    @JoinColumn(name = "tipoDespesa_id")
    private TipoDespesa tipoDespesa;

    @Column
    private BigDecimal valorDespesa;

    private String userLog;

    private String atendimento;

}
