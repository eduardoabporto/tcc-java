package io.github.eduardoabporto.tcc.rest;

import io.github.eduardoabporto.tcc.model.entity.Recurso;
import io.github.eduardoabporto.tcc.model.repository.RecursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/recurso")
public class RecursoController {

    private final RecursoRepository recursoRepository;

    @Autowired
    public RecursoController(RecursoRepository recursoRepository) {
        this.recursoRepository = recursoRepository;
    }

    @GetMapping
    public List<Recurso> obterTodos(){
        return recursoRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Recurso salvar(@RequestBody @Valid Recurso recurso){
        return recursoRepository.save(recurso);
    }

    @GetMapping("{id}")
    public Recurso acharPorId(@PathVariable Integer id){
        return recursoRepository
                .findById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Recurso não Encontrado"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id){
        recursoRepository
                .findById(id)
                .map(recurso -> {
                    recursoRepository.delete(recurso);
                    return Void.TYPE;
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Recurso não Encontrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @RequestBody Recurso recursoAtualizado){
        recursoRepository
                .findById(id)
                .map(recurso -> {
                    recursoAtualizado.setId(recurso.getId());
                    return recursoRepository.save(recursoAtualizado);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Recurso não Encontrado"));
    }
}
