package io.github.eduardoabporto.tcc.model.repository;

import io.github.eduardoabporto.tcc.model.entity.TipoDespesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TipoDespesaRepository extends JpaRepository<TipoDespesa, Integer> {
    @Query(" select s from TipoDespesa s where upper( s.nome ) like upper( :nome ) " )
    List<TipoDespesa> findByNomeTipoDespesa(@Param("nome") String nome);
}

