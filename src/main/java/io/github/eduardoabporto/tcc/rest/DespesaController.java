package io.github.eduardoabporto.tcc.rest;

import io.github.eduardoabporto.tcc.model.entity.*;
import io.github.eduardoabporto.tcc.model.repository.*;
import io.github.eduardoabporto.tcc.rest.dto.DespesaDTO;
import io.github.eduardoabporto.tcc.service.ServiceRelatorio;
import io.github.eduardoabporto.tcc.util.BigDecimalConverter;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/despesa")
@RequiredArgsConstructor
public class DespesaController {

    private final ServiceRelatorio serviceRelatorio;
    private final EmpresaRepository empresaRepository;
    private final ClienteRepository clienteRepository;
    private final DespesaRepository despesaRepository;
    private final ProjetoRepository projetoRepository;
    private final TipoDespesaRepository tipodespesaRepository;
    private final BigDecimalConverter bigDecimalConverter;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Despesa salvar(@RequestBody @Valid DespesaDTO dto ){
        //LocalDate data = LocalDate.parse(dto.getData(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Integer idEmpresa = dto.getIdEmpresa();
        Integer idCliente = dto.getIdCliente();
        Integer idProjeto = dto.getIdProjeto();
        Integer idTipoDespesa = dto.getIdTipoDespesa();

        Empresa empresa =
                empresaRepository
                        .findById(idEmpresa)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST, "Cliente inexistente."));

        Cliente cliente =
                clienteRepository
                        .findById(idCliente)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST, "Cliente inexistente."));

        Projeto projeto =
                projetoRepository
                        .findById(idProjeto)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST, "Projeto inexistente."));

        TipoDespesa tipoDespesa =
                tipodespesaRepository
                        .findById(idTipoDespesa)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST, "Tipo de Despesas inexistente."));

        Despesa Despesa = new Despesa();
        Despesa.setTipoDespesa(tipoDespesa);
        Despesa.setData(dto.getData());
        Despesa.setDescricao(dto.getDescricao());
        Despesa.setValorDespesa(bigDecimalConverter.converter(dto.getValorDespesa()));
        Despesa.setEmpresa(empresa);
        Despesa.setCliente(cliente);
        Despesa.setProjeto(projeto);
        Despesa.setUserLog(dto.getUserLog());
        Despesa.setAtendimento(dto.getAtendimento());

        return despesaRepository.save(Despesa);
    }

    @GetMapping
    public List<Despesa> pesquisar(
            @RequestParam(value = "nome", required = false, defaultValue = "") String nome
    ) {
        return despesaRepository.findByNomeCliente("%" + nome + "%");
    }

    @GetMapping("{id}")
    public Despesa acharPorId(@PathVariable Integer id){
        return despesaRepository
                .findById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Despesa não Encontrada"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id){
        despesaRepository
                .findById(id)
                .map(despesa -> {
                    despesaRepository.delete(despesa);
                    return Void.TYPE;
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Despesa não Encontrada"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @RequestBody Despesa despesaAtualizado){
        despesaRepository
                .findById(id)
                .map(despesa -> {
                    despesaAtualizado.setId(despesa.getId());
                    return despesaRepository.save(despesaAtualizado);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Despesa não Encontrada"));
    }

    @GetMapping(value="/relatorio", produces = "application/text")
    public ResponseEntity<String> downloadRelatorio(HttpServletRequest request) throws Exception {
        byte[] pdf = serviceRelatorio.gerarRelatorio("relatorio-despesa",
                request.getServletContext());

        String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);

        return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
    }


    @GetMapping(value="/relatorio/minhas-despesas", produces = "application/text")
    public ResponseEntity<String> downloadRelatorioMinhasOss(HttpServletRequest request) throws Exception {
        byte[] pdf = serviceRelatorio.gerarRelatorio("relatorio-minha-despesas",
                request.getServletContext());

        String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);

        return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
    }
}

