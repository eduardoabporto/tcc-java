package io.github.eduardoabporto.tcc.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private  Cliente cliente;

    @Column
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date data;

    @Column
    @JsonFormat(pattern = "H[H]:mm:ss")
    private String horaTrasl;

    @Column
    @JsonFormat(pattern = "H[H]:mm:ss")
    private String horaDesc;

    @Column
    private BigDecimal valorHora;
}
