package io.github.eduardoabporto.tcc.model.repository;

import io.github.eduardoabporto.tcc.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByUsername(String username);
    boolean existsByUsername(String username);

    @Query(" select s from Usuario s where s.username = :nomeUser")
    List<Usuario> findByNomeUser(@Param("nomeUser") String nomeUser);

}
