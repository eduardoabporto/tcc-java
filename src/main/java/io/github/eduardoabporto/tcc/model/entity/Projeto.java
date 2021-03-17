package io.github.eduardoabporto.tcc.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @JsonFormat(pattern="yyyy-MM-dd")
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
