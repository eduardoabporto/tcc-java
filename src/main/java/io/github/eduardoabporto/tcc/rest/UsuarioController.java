package io.github.eduardoabporto.tcc.rest;

import io.github.eduardoabporto.tcc.exception.UsuarioCadastradoException;
import io.github.eduardoabporto.tcc.model.entity.Usuario;
import io.github.eduardoabporto.tcc.model.repository.UsuarioRepository;
import io.github.eduardoabporto.tcc.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioRepository repository;


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
}
