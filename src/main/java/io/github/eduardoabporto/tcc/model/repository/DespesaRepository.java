package io.github.eduardoabporto.tcc.model.repository;

import io.github.eduardoabporto.tcc.model.entity.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface DespesaRepository extends JpaRepository<Despesa, Integer> {

    @Query(" select s from Despesa s "+
            "join s.cliente c  "+
            "where " +
            "upper( c.nome ) like upper( :nome ) ")
    List<Despesa> findByNomeCliente(@Param("nome") String nome);


}
