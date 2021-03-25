package io.github.eduardoabporto.tcc.rest;

import io.github.eduardoabporto.tcc.exception.UsuarioCadastradoException;
import io.github.eduardoabporto.tcc.model.entity.DespesaChart;
import io.github.eduardoabporto.tcc.model.entity.OrdemServicoChart;
import io.github.eduardoabporto.tcc.model.entity.Usuario;
import io.github.eduardoabporto.tcc.model.repository.UsuarioRepository;
import io.github.eduardoabporto.tcc.service.ServiceRelatorio;
import io.github.eduardoabporto.tcc.service.UsuarioService;
import io.github.eduardoabporto.tcc.util.buscaDataIniFim;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static io.github.eduardoabporto.tcc.util.buscaDataIniFim.principioDoMes;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UsuarioController {


    private final ServiceRelatorio serviceRelatorio;
    private final UsuarioService service;
    private final UsuarioRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<Usuario> obterTodos(){
        return repository.findAll();
    }

    @GetMapping("/form/")
    public List<Usuario> buscaUsuario(
            @RequestParam(value = "nomeUser" , required = false, defaultValue = "") String nomeUser
    ) {
        return repository.findByNomeUser(nomeUser);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void salvar(@RequestBody @Valid Usuario usuario){
        try{
            service.salvar(usuario);
        }catch (UsuarioCadastradoException e){
            throw new ResponseStatusException( HttpStatus.BAD_REQUEST, e.getMessage() );
        }
    }

    @GetMapping("{id}")
    public Usuario acharPorId(@PathVariable Integer id){
        return  repository
                .findById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuãrio não Encontrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @RequestBody Usuario UsuarioAtualizado){
        repository
                .findById(id)
                .map(usuario -> {
                    UsuarioAtualizado.setId(usuario.getId());
                    return repository.save(UsuarioAtualizado);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Empresa não Encontrado"));
    }


    @GetMapping(value="/relatorio", produces = "application/text")
    public ResponseEntity<String> downloadRelatorio(HttpServletRequest request) throws Exception {
        byte[] pdf = serviceRelatorio.gerarRelatorio("relatorio-usuario", new HashMap(),
                request.getServletContext());

        String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);

        return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
    }

    @GetMapping(value="/grafico/1", produces = "application/json")
    public ResponseEntity<DespesaChart> grafico1(){

        DespesaChart despesaChart = new DespesaChart();
        String dataInicio = principioDoMes(LocalDate.now()).toString();
        String dataFim = buscaDataIniFim.fimDoMes(LocalDate.now()).toString();

        List<String> resultado = jdbcTemplate.queryForList("SELECT cast(array_agg(x.val_desp) as character varying[]) from (\n" +
                "SELECT usu.nome,SUM(desp.valordespesa) val_desp\n" +
                "FROM DESPESA DESP, EMPRESA EMP, CLIENTE CLI, PROJETO PRJ, USUARIO USU, TIPODESPESA TPDESP\n" +
                "WHERE\n" +
                "DESP.EMPRESA_ID = EMP.ID\n" +
                "AND DESP.CLIENTE_ID = CLI.ID\n" +
                "AND DESP.PROJETO_ID = PRJ.ID\n" +
                "AND DESP.USERLOG = USU.USERNAME\n" +
                "AND DESP.TIPODESPESA_ID = TPDESP.ID\n" +
                "AND DESP.DATA BETWEEN '" + dataInicio + "' AND '" + dataFim +"'\n" +
                "GROUP BY usu.nome) x\n" +
                "Union\n" +
                "SELECT array_agg(x.nomeLog) from (\n" +
                "SELECT usu.nome nomeLog,SUM(desp.valordespesa) val_desp\n" +
                "FROM DESPESA DESP, EMPRESA EMP, CLIENTE CLI, PROJETO PRJ, USUARIO USU, TIPODESPESA TPDESP\n" +
                "WHERE\n" +
                "DESP.EMPRESA_ID = EMP.ID\n" +
                "AND DESP.CLIENTE_ID = CLI.ID\n" +
                "AND DESP.PROJETO_ID = PRJ.ID\n" +
                "AND DESP.USERLOG = USU.USERNAME\n" +
                "AND DESP.TIPODESPESA_ID = TPDESP.ID\n" +
                "AND DESP.DATA BETWEEN '" + dataInicio + "' AND '" + dataFim +"'\n" +
                "GROUP BY usu.nome) x\n", String.class);

        if (!resultado.isEmpty()){
            String usersLogs = resultado.get(1).replaceAll("\\{","").replaceAll("\\}","");
            String valsDesps = resultado.get(0).replaceAll("\\{","").replaceAll("\\}","");

            despesaChart.setNomeLog(usersLogs);
            despesaChart.setValDespesa(valsDesps);
        }
        return new ResponseEntity<DespesaChart>(despesaChart, HttpStatus.OK);
    }

    @GetMapping(value="/grafico/2", produces = "application/json")
    public ResponseEntity<DespesaChart> grafico2(){

        DespesaChart despesaChart2 = new DespesaChart();

        String dataInicio = principioDoMes(LocalDate.now()).toString();
        String dataFim = buscaDataIniFim.fimDoMes(LocalDate.now()).toString();

        List<String> resultado2 = jdbcTemplate.queryForList("SELECT cast(array_agg(x.val_desp) as character varying[]) from (\n" +
                "SELECT CLI.nome,SUM(desp.valordespesa) val_desp\n" +
                "FROM DESPESA DESP, EMPRESA EMP, CLIENTE CLI, PROJETO PRJ, USUARIO USU, TIPODESPESA TPDESP\n" +
                "WHERE\n" +
                "DESP.EMPRESA_ID = EMP.ID\n" +
                "AND DESP.CLIENTE_ID = CLI.ID\n" +
                "AND DESP.PROJETO_ID = PRJ.ID\n" +
                "AND DESP.USERLOG = USU.USERNAME\n" +
                "AND DESP.TIPODESPESA_ID = TPDESP.ID\n" +
                "AND DESP.DATA BETWEEN '" + dataInicio + "' AND '" + dataFim +"'\n" +
                "GROUP BY cli.nome) x\n" +
                "Union\n" +
                "SELECT array_agg(x.nomeLog) from (\n" +
                "SELECT cli.nome nomeLog,SUM(desp.valordespesa) val_desp\n" +
                "FROM DESPESA DESP, EMPRESA EMP, CLIENTE CLI, PROJETO PRJ, USUARIO USU, TIPODESPESA TPDESP\n" +
                "WHERE\n" +
                "DESP.EMPRESA_ID = EMP.ID\n" +
                "AND DESP.CLIENTE_ID = CLI.ID\n" +
                "AND DESP.PROJETO_ID = PRJ.ID\n" +
                "AND DESP.USERLOG = USU.USERNAME\n" +
                "AND DESP.TIPODESPESA_ID = TPDESP.ID\n" +
                "AND DESP.DATA BETWEEN '" + dataInicio + "' AND '" + dataFim +"'\n" +
                "GROUP BY cli.nome) x\n", String.class);

        if (!resultado2.isEmpty()){
            String cliLogs = resultado2.get(1).replaceAll("\\{","").replaceAll("\\}","");
            String valsDesps = resultado2.get(0).replaceAll("\\{","").replaceAll("\\}","");

            despesaChart2.setCliLog(cliLogs);
            despesaChart2.setValDespesa(valsDesps);
        }
        return new ResponseEntity<DespesaChart>(despesaChart2, HttpStatus.OK);
    }

    @GetMapping(value="/grafico/3", produces = "application/json")
    public ResponseEntity<DespesaChart> grafico3(){

        DespesaChart despesaChart3 = new DespesaChart();

        String dataInicio = principioDoMes(LocalDate.now()).toString();
        String dataFim = buscaDataIniFim.fimDoMes(LocalDate.now()).toString();

        List<String> resultado3 = jdbcTemplate.queryForList("SELECT cast(array_agg(x.val_desp) as character varying[]) from (\n" +
                "SELECT TPDESP.nome,SUM(desp.valordespesa) val_desp\n" +
                "FROM DESPESA DESP, EMPRESA EMP, CLIENTE CLI, PROJETO PRJ, USUARIO USU, TIPODESPESA TPDESP\n" +
                "WHERE\n" +
                "DESP.EMPRESA_ID = EMP.ID\n" +
                "AND DESP.CLIENTE_ID = CLI.ID\n" +
                "AND DESP.PROJETO_ID = PRJ.ID\n" +
                "AND DESP.USERLOG = USU.USERNAME\n" +
                "AND DESP.TIPODESPESA_ID = TPDESP.ID\n" +
                "AND DESP.DATA BETWEEN '" + dataInicio + "' AND '" + dataFim +"'\n" +
                "GROUP BY TPDESP.nome) x\n" +
                "Union\n" +
                "SELECT array_agg(x.nomeLog) from (\n" +
                "SELECT TPDESP.nome nomeLog,SUM(desp.valordespesa) val_desp\n" +
                "FROM DESPESA DESP, EMPRESA EMP, CLIENTE CLI, PROJETO PRJ, USUARIO USU, TIPODESPESA TPDESP\n" +
                "WHERE\n" +
                "DESP.EMPRESA_ID = EMP.ID\n" +
                "AND DESP.CLIENTE_ID = CLI.ID\n" +
                "AND DESP.PROJETO_ID = PRJ.ID\n" +
                "AND DESP.USERLOG = USU.USERNAME\n" +
                "AND DESP.TIPODESPESA_ID = TPDESP.ID\n" +
                "AND DESP.DATA BETWEEN '" + dataInicio + "' AND '" + dataFim +"'\n" +
                "GROUP BY TPDESP.nome) x\n", String.class);

        if (!resultado3.isEmpty()){
            String tipoDesp = resultado3.get(1).replaceAll("\\{","").replaceAll("\\}","");
            String valsDesps = resultado3.get(0).replaceAll("\\{","").replaceAll("\\}","");

            despesaChart3.setTipoDesp(tipoDesp);
            despesaChart3.setValDespesa(valsDesps);
        }
        return new ResponseEntity<DespesaChart>(despesaChart3, HttpStatus.OK);
    }

    @GetMapping(value="/grafico/4", produces = "application/json")
    public ResponseEntity<OrdemServicoChart> grafico4(){

        OrdemServicoChart ordemServicoChart4 = new OrdemServicoChart();

        String dataInicio = principioDoMes(LocalDate.now()).toString();
        String dataFim = buscaDataIniFim.fimDoMes(LocalDate.now()).toString();

        List<String> resultado4 = jdbcTemplate.queryForList("SELECT cast(array_agg((EXTRACT(hour FROM QTDE_HORAS_TRAB)*60*60\n" +
                "       + EXTRACT(minutes FROM QTDE_HORAS_TRAB)*60\n" +
                "       + EXTRACT(seconds FROM QTDE_HORAS_TRAB))/60/60) as character varying[]) from (\n" +
                "SELECT usu.nome, sum(CAST(OS.HORATRAB AS time))  AS  QTDE_HORAS_TRAB\n" +
                "FROM ORDEMSERVICO OS, EMPRESA EMP, CLIENTE CLI, PROJETO PRJ, USUARIO USU\n" +
                "WHERE\n" +
                "OS.EMPRESA_ID = EMP.ID\n" +
                "AND OS.CLIENTE_ID = CLI.ID\n" +
                "AND OS.PROJETO_ID = PRJ.ID\n" +
                "AND OS.USERLOG = USU.USERNAME\n" +
                "GROUP BY usu.nome) x\n" +
                "Union\n" +
                "SELECT array_agg(x.nomeLog) from (\n" +
                "SELECT usu.nome nomeLog,sum(CAST(OS.HORATRAB AS time))  AS  QTDE_HORAS_TRAB\n" +
                "FROM ORDEMSERVICO OS, EMPRESA EMP, CLIENTE CLI, PROJETO PRJ, USUARIO USU\n" +
                "WHERE\n" +
                "OS.EMPRESA_ID = EMP.ID\n" +
                "AND OS.CLIENTE_ID = CLI.ID\n" +
                "AND OS.PROJETO_ID = PRJ.ID\n" +
                "AND OS.USERLOG = USU.USERNAME\n" +
                "AND OS.DATA BETWEEN '" + dataInicio + "' AND '" + dataFim +"'\n" +
                "GROUP BY usu.nome) x", String.class);

        if (!resultado4.isEmpty()){
            String nomeLog = resultado4.get(1).replaceAll("\\{","").replaceAll("\\}","");
            String horaTotal = resultado4.get(0).replaceAll("\\{","").replaceAll("\\}","");

            ordemServicoChart4.setNomeLog(nomeLog);
            ordemServicoChart4.setHoraTotal(horaTotal.replace(":","."));
        }
        return new ResponseEntity<OrdemServicoChart>(ordemServicoChart4, HttpStatus.OK);
    }

    @GetMapping(value="/grafico/5", produces = "application/json")
    public ResponseEntity<OrdemServicoChart> grafico5(){

        OrdemServicoChart ordemServicoChart5 = new OrdemServicoChart();

        String dataInicio = principioDoMes(LocalDate.now()).toString();
        String dataFim = buscaDataIniFim.fimDoMes(LocalDate.now()).toString();

        List<String> resultado5 = jdbcTemplate.queryForList("SELECT cast(array_agg((EXTRACT(hour FROM QTDE_HORAS_TRAB)*60*60\n" +
                "       + EXTRACT(minutes FROM QTDE_HORAS_TRAB)*60\n" +
                "       + EXTRACT(seconds FROM QTDE_HORAS_TRAB))/60/60) as character varying[]) from (\n" +
                "SELECT cli.nome, sum(CAST(OS.HORATRAB AS time))  AS  QTDE_HORAS_TRAB\n" +
                "FROM ORDEMSERVICO OS, EMPRESA EMP, CLIENTE CLI, PROJETO PRJ, USUARIO USU\n" +
                "WHERE\n" +
                "OS.EMPRESA_ID = EMP.ID\n" +
                "AND OS.CLIENTE_ID = CLI.ID\n" +
                "AND OS.PROJETO_ID = PRJ.ID\n" +
                "AND OS.USERLOG = USU.USERNAME\n" +
                "GROUP BY cli.nome) x\n" +
                "Union\n" +
                "SELECT array_agg(x.nomeLog) from (\n" +
                "SELECT cli.nome nomeLog,sum(CAST(OS.HORATRAB AS time))  AS  QTDE_HORAS_TRAB\n" +
                "FROM ORDEMSERVICO OS, EMPRESA EMP, CLIENTE CLI, PROJETO PRJ, USUARIO USU\n" +
                "WHERE\n" +
                "OS.EMPRESA_ID = EMP.ID\n" +
                "AND OS.CLIENTE_ID = CLI.ID\n" +
                "AND OS.PROJETO_ID = PRJ.ID\n" +
                "AND OS.USERLOG = USU.USERNAME\n" +
                "AND OS.DATA BETWEEN '" + dataInicio + "' AND '" + dataFim +"'\n" +
                "GROUP BY cli.nome) x", String.class);

        if (!resultado5.isEmpty()){
            String cliLog = resultado5.get(1).replaceAll("\\{","").replaceAll("\\}","");
            String horaTotal = resultado5.get(0).replaceAll("\\{","").replaceAll("\\}","");

            ordemServicoChart5.setCliLog(cliLog);
            ordemServicoChart5.setHoraTotal(horaTotal.replace(":","."));
        }
        return new ResponseEntity<OrdemServicoChart>(ordemServicoChart5, HttpStatus.OK);
    }

    @GetMapping(value="/grafico/6", produces = "application/json")
    public ResponseEntity<OrdemServicoChart> grafico6(){

        OrdemServicoChart ordemServicoChart6 = new OrdemServicoChart();

        String dataInicio = principioDoMes(LocalDate.now()).toString();
        String dataFim = buscaDataIniFim.fimDoMes(LocalDate.now()).toString();

        List<String> resultado6 = jdbcTemplate.queryForList("SELECT cast(array_agg(X.QTDE_OS) as character varying[]) from (\n" +
                "SELECT CLI.nome, count(CLI.nome)  AS  QTDE_OS\n" +
                "FROM ORDEMSERVICO OS, EMPRESA EMP, CLIENTE CLI, PROJETO PRJ, USUARIO USU\n" +
                "WHERE\n" +
                "OS.EMPRESA_ID = EMP.ID\n" +
                "AND OS.CLIENTE_ID = CLI.ID\n" +
                "AND OS.PROJETO_ID = PRJ.ID\n" +
                "AND OS.USERLOG = USU.USERNAME\n" +
                "GROUP BY CLI.nome) x\n" +
                "Union\n" +
                "SELECT array_agg(x.nomeLog) from (\n" +
                "SELECT CLI.nome nomeLog,count(CLI.nome)  AS  QTDE_OS\n" +
                "FROM ORDEMSERVICO OS, EMPRESA EMP, CLIENTE CLI, PROJETO PRJ, USUARIO USU\n" +
                "WHERE\n" +
                "OS.EMPRESA_ID = EMP.ID\n" +
                "AND OS.CLIENTE_ID = CLI.ID\n" +
                "AND OS.PROJETO_ID = PRJ.ID\n" +
                "AND OS.USERLOG = USU.USERNAME\n" +
                "AND OS.DATA BETWEEN '" + dataInicio + "' AND '" + dataFim +"'\n" +
                "GROUP BY CLI.nome) x", String.class);

        if (!resultado6.isEmpty()){
            String cliLog = resultado6.get(1).replaceAll("\\{","").replaceAll("\\}","");
            String qtdeTotal = resultado6.get(0).replaceAll("\\{","").replaceAll("\\}","");

            ordemServicoChart6.setCliLog(cliLog);
            ordemServicoChart6.setQtdeTotal(qtdeTotal.replace(":","."));
        }
        return new ResponseEntity<OrdemServicoChart>(ordemServicoChart6, HttpStatus.OK);
    }

}
