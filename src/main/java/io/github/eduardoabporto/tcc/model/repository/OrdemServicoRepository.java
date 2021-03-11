package io.github.eduardoabporto.tcc.model.repository;

import io.github.eduardoabporto.tcc.model.entity.OrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Integer> {

    @Query(" select s from OrdemServico s "+
            "join s.cliente c  "+
            "where " +
            "upper( c.nome ) like upper( :nome ) ")
    List<OrdemServico> findByNomeCliente(@Param("nome") String nome);

}
