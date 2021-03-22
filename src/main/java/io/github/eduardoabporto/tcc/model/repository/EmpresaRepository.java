package io.github.eduardoabporto.tcc.model.repository;

import io.github.eduardoabporto.tcc.model.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {

    @Query(" select s from Empresa s where upper( s.nome ) like upper( :nome ) " )
    List<Empresa> findByNomeEmpresa(@Param("nome") String nome);
}

