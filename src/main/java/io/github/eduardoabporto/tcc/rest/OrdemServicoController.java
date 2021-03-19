package io.github.eduardoabporto.tcc.rest;

import io.github.eduardoabporto.tcc.model.entity.Cliente;
import io.github.eduardoabporto.tcc.model.entity.Empresa;
import io.github.eduardoabporto.tcc.model.entity.OrdemServico;
import io.github.eduardoabporto.tcc.model.entity.Projeto;
import io.github.eduardoabporto.tcc.model.repository.ClienteRepository;
import io.github.eduardoabporto.tcc.model.repository.EmpresaRepository;
import io.github.eduardoabporto.tcc.model.repository.OrdemServicoRepository;
import io.github.eduardoabporto.tcc.model.repository.ProjetoRepository;
import io.github.eduardoabporto.tcc.rest.dto.OrdemServicoDTO;
import io.github.eduardoabporto.tcc.service.ServiceRelatorio;
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
@RequestMapping("/api/ordem-servico")
@RequiredArgsConstructor
public class OrdemServicoController {

    private final ServiceRelatorio serviceRelatorio;
    private final EmpresaRepository empresaRepository;
    private final ClienteRepository clienteRepository;
    private final OrdemServicoRepository repository;
    private final ProjetoRepository projetoRepository;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdemServico salvar(@RequestBody @Valid OrdemServicoDTO dto ){
        //LocalDate data = LocalDate.parse(dto.getData(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Integer idEmpresa = dto.getIdEmpresa();
        Integer idCliente = dto.getIdCliente();
        Integer idProjeto = dto.getIdProjeto();

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


        OrdemServico OrdemServico = new OrdemServico();
        OrdemServico.setAssunto(dto.getAssunto());
        OrdemServico.setData(dto.getData());
        OrdemServico.setDescricao(dto.getDescricao());
        OrdemServico.setHoraInicial(dto.getHoraInicial());
        OrdemServico.setHoraFinal(dto.getHoraFinal());
        OrdemServico.setHoraTrasl(dto.getHoraTrasl());
        OrdemServico.setHoraDesc(dto.getHoraDesc());
        OrdemServico.setHoraTrab(dto.getHoraTrab());
        OrdemServico.setEmpresa(empresa);
        OrdemServico.setCliente(cliente);
        OrdemServico.setProjeto(projeto);
        OrdemServico.setUserLog(dto.getUserLog());
        OrdemServico.setAtendimento(dto.getAtendimento());

        return repository.save(OrdemServico);
    }

    @GetMapping
    public List<OrdemServico> pesquisar(
            @RequestParam(value = "nome", required = false, defaultValue = "") String nome
    ) {
        return repository.findByNomeCliente("%" + nome + "%");
    }

    @GetMapping("{id}")
    public OrdemServico acharPorId(@PathVariable Integer id){
        return repository
                .findById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Ordem de Serviço não Encontrado"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id){
        repository
                .findById(id)
                .map(ordemServico -> {
                    repository.delete(ordemServico);
                    return Void.TYPE;
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Ordem de Serviço não Encontrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @RequestBody OrdemServico ordemServicoAtualizado){
        repository
                .findById(id)
                .map(ordemServico -> {
                    ordemServicoAtualizado.setId(ordemServico.getId());
                    return repository.save(ordemServicoAtualizado);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Ordem de Serviço não Encontrado"));
    }

    @GetMapping(value="/relatorio", produces = "application/text")
    public ResponseEntity<String> downloadRelatorio(HttpServletRequest request) throws Exception {
        byte[] pdf = serviceRelatorio.gerarRelatorio("relatorio-ordens-servicos",
                request.getServletContext());

        String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);

        return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
    }


    @GetMapping(value="/relatorio/minhas-oss", produces = "application/text")
    public ResponseEntity<String> downloadRelatorioMinhasOss(HttpServletRequest request) throws Exception {
        byte[] pdf = serviceRelatorio.gerarRelatorio("relatorio-minha-os",
                request.getServletContext());

        String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);

        return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
    }
}

