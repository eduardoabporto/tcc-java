package io.github.eduardoabporto.tcc.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,length = 150)
    @NotEmpty(message = "{campo.nome.obrigatorio}")
    private String nome;

    @Column(unique = true, nullable = false, length = 15)
    @NotNull
    @NotEmpty(message = "{campo.cnpj.obrigatorio}")
    @CNPJ(message = "{campo.cnpj.invalido}")
    private String cnpj;

    @Column(updatable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dataCadastro;

    @Column(length = 70)
    private String nomeFantasia;

    @Column(length = 70)
    private String pessoaResp;

    @Column(length = 40)
    private String email;

    @Column(length = 11)
    private String telefone;

    @Column(length = 70)
    private String endereco;

    @Column(length = 4)
    private String numero;

    @Column(length = 30)
    private String complemento;

    @Column(length = 8)
    private String cep;

    @Column(length = 30)
    private String bairro;

    @Column(length = 30)
    private String cidade;

    @Column(length = 2)
    private String estado;

    @PrePersist
    public void prePersist(){
        setDataCadastro(LocalDate.now());
    }

}
