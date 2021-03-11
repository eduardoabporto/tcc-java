package io.github.eduardoabporto.tcc.rest;

import io.github.eduardoabporto.tcc.model.entity.Empresa;
import io.github.eduardoabporto.tcc.model.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/empresa")
public class EmpresaController {

    private final EmpresaRepository empresaRepository;

    @Autowired
    public EmpresaController(EmpresaRepository repository) {
        this.empresaRepository = repository;
    }

    @GetMapping
    public List<Empresa> obterTodos(){
        return empresaRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Empresa salvar(@RequestBody @Valid Empresa empresa){
        return empresaRepository.save(empresa);
    }

    @GetMapping("{id}")
    public Empresa acharPorId(@PathVariable Integer id){
        return empresaRepository
                .findById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Empresa não Encontrado"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id){
        empresaRepository
                .findById(id)
                .map(empresa -> {
                    empresaRepository.delete(empresa);
                    return Void.TYPE;
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Empresa não Encontrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @RequestBody Empresa EmpresaAtualizado){
        empresaRepository
                .findById(id)
                .map(empresa -> {
                    EmpresaAtualizado.setId(empresa.getId());
                    return empresaRepository.save(EmpresaAtualizado);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Empresa não Encontrado"));
    }
}
