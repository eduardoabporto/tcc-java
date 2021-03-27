package io.github.eduardoabporto.tcc.rest;

import io.github.eduardoabporto.tcc.model.entity.Cliente;
import io.github.eduardoabporto.tcc.model.repository.ClienteRepository;
import io.github.eduardoabporto.tcc.service.ServiceRelatorio;
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
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ServiceRelatorio serviceRelatorio;
    private final ClienteRepository repository;

    @GetMapping
    public List<Cliente> obterTodos(){
        return repository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente salvar(@RequestBody @Valid Cliente cliente){
        return repository.save(cliente);
    }

    @GetMapping("/{id}")
    public Cliente acharPorId(@PathVariable Integer id){
        return repository
                .findById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Cliente não Encontrado"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id){
        repository
                .findById(id)
                .map(cliente -> {
                    repository.delete(cliente);
                    return Void.TYPE;
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Cliente não Encontrado"));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @RequestBody Cliente clienteAtualizado){
        repository
                .findById(id)
                .map(cliente -> {
                    clienteAtualizado.setId(cliente.getId());
                    return repository.save(clienteAtualizado);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Cliente não Encontrado"));
    }

    @GetMapping(value="/relatorio", produces = "application/text")
    public ResponseEntity<String> downloadRelatorio(HttpServletRequest request) throws Exception {
        byte[] pdf = serviceRelatorio.gerarRelatorio("relatorio-clientes", new HashMap(),
                request.getServletContext());

        String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);

        return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
    }

}
