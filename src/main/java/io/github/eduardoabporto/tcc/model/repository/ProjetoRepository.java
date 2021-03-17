package io.github.eduardoabporto.tcc.model.repository;

import io.github.eduardoabporto.tcc.model.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjetoRepository extends JpaRepository<Projeto, Integer> {

    @Query(" select s from Projeto s join s.cliente c where upper( c.nome ) like upper( :nome ) " +
            "and c.id = s.cliente.id")
    List<Projeto> findByNomeCliente(@Param("nome") String nome);

    @Query(" select s from Projeto s where s.cliente.id = :numCliente")
    List<Projeto> findByNumCliente(@Param("numCliente") Integer numCliente);

    @Query(" select s from Projeto s where s.id = :numProjeto")
    List<Projeto> findByhoraProjeto(@Param("numProjeto") Integer numProjeto);


}
