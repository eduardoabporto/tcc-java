package io.github.eduardoabporto.tcc.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, name = "username")
    @NotEmpty(message = "{campo.login.obrigatorio}")
    private String username;

    @Column(name = "password")
    @NotEmpty(message = "{campo.senha.obrigatorio}")
    private String password;

    @Column(length = 150)
    private String nome;

    @Column(length = 150)
    private String perfil;

}
