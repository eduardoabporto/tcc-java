package io.github.eduardoabporto.tcc.rest;

import io.github.eduardoabporto.tcc.model.entity.Cliente;
import io.github.eduardoabporto.tcc.model.entity.Projeto;
import io.github.eduardoabporto.tcc.model.repository.ClienteRepository;
import io.github.eduardoabporto.tcc.model.repository.ProjetoRepository;
import io.github.eduardoabporto.tcc.rest.dto.ProjetoDTO;
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
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/projeto")
@RequiredArgsConstructor
public class ProjetoController {

    private final ServiceRelatorio serviceRelatorio;
    private final ClienteRepository clienteRepository;
    private final ProjetoRepository projetoRepository;
    private final BigDecimalConverter bigDecimalConverter;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Projeto salvar(@RequestBody @Valid ProjetoDTO dto ){
        //LocalDate data = LocalDate.parse(dto.getData(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Integer idCliente = dto.getIdCliente();

        Cliente cliente =
                clienteRepository
                        .findById(idCliente)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST, "Cliente inexistente."));

        Projeto Projeto = new Projeto();
        Projeto.setNome(dto.getNome());
        Projeto.setData(dto.getData());
        Projeto.setHoraTrasl(dto.getHoraTrasl());
        Projeto.setHoraDesc(dto.getHoraDesc());
        Projeto.setValorHora(bigDecimalConverter.converter(dto.getValorHora()));
        Projeto.setCliente(cliente);

        return projetoRepository.save(Projeto);
    }

    @GetMapping
    public List<Projeto> pesquisar(
            @RequestParam(value = "nome", required = false, defaultValue = "") String nome
    ) {
        return projetoRepository.findByNomeCliente("%" + nome + "%");
    }

    @GetMapping("/{id}")
    public Projeto acharPorId(@PathVariable Integer id){
        return projetoRepository
                .findById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Ordem de Serviço não Encontrado"));
    }

    @GetMapping("/hora-projeto/")
    public List<Projeto> buscaProjeto(
            @RequestParam(value = "numProjeto" , required = false, defaultValue = "") String numProjeto
    ) {
        return projetoRepository.findByhoraProjeto(Integer.parseInt(numProjeto));
    }

    @GetMapping("/form/")
    public List<Projeto> buscaCliente(
            @RequestParam(value = "numCliente" , required = false, defaultValue = "") String numCliente
    ) {
        return projetoRepository.findByNumCliente(Integer.parseInt(numCliente));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id){
        projetoRepository
                .findById(id)
                .map(projeto -> {
                    projetoRepository.delete(projeto);
                    return Void.TYPE;
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Projeto não Encontrado"));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @RequestBody Projeto projetoAtualizado){
        projetoRepository
                .findById(id)
                .map(projeto -> {
                    projetoAtualizado.setId(projeto.getId());
                    return projetoRepository.save(projetoAtualizado);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Projeto não Encontrado"));
    }

    @GetMapping(value="/relatorio", produces = "application/text")
    public ResponseEntity<String> downloadRelatorio(HttpServletRequest request) throws Exception {
        byte[] pdf = serviceRelatorio.gerarRelatorio("relatorio-projeto", new HashMap(),
                request.getServletContext());

        String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);

        return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
    }

}
