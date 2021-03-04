package io.github.eduardoabporto.tcc.rest;

import io.github.eduardoabporto.tcc.model.entity.Cliente;
import io.github.eduardoabporto.tcc.model.entity.OrdemServico;
import io.github.eduardoabporto.tcc.model.repository.ClienteRepository;
import io.github.eduardoabporto.tcc.model.repository.OrdemServicoRepository;
import io.github.eduardoabporto.tcc.rest.dto.OrdemServicoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/ordens-servicos")
@RequiredArgsConstructor
public class OrdemServicoController {

    private final ClienteRepository clienteRepository;
    private final OrdemServicoRepository repository;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdemServico salvar(@RequestBody @Valid OrdemServicoDTO dto ){
        LocalDate data = LocalDate.parse(dto.getData(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Integer idCliente = dto.getIdCliente();

        Cliente cliente =
                clienteRepository
                        .findById(idCliente)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST, "Cliente inexistente."));


        OrdemServico OrdemServico = new OrdemServico();
        OrdemServico.setDescricao(dto.getDescricao());
        OrdemServico.setData( data );
        OrdemServico.setCliente(cliente);

        return repository.save(OrdemServico);
    }

    @GetMapping
    public List<OrdemServico> pesquisar(
            @RequestParam(value = "nome", required = false, defaultValue = "") String nome,
            @RequestParam(value = "mes", required = false) Integer mes
    ) {
        return repository.findByNomeClienteAndMes("%" + nome + "%", mes);
    }
}
