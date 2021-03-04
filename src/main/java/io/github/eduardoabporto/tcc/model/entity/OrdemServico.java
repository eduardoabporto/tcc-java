package io.github.eduardoabporto.tcc.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_cliente", referencedColumnName="ID")
    private  Cliente cliente;

    @Column
    private LocalDate dataServico;

    @Column
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;
}
