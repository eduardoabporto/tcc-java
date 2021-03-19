package io.github.eduardoabporto.tcc.rest;

import io.github.eduardoabporto.tcc.exception.UsuarioCadastradoException;
import io.github.eduardoabporto.tcc.model.entity.Usuario;
import io.github.eduardoabporto.tcc.model.repository.UsuarioRepository;
import io.github.eduardoabporto.tcc.service.ServiceRelatorio;
import io.github.eduardoabporto.tcc.service.UsuarioService;
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
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UsuarioController {


    private final ServiceRelatorio serviceRelatorio;
    private final UsuarioService service;
    private final UsuarioRepository repository;

    @GetMapping
    public List<Usuario> obterTodos(){
        return repository.findAll();
    }

    @GetMapping("/form/")
    public List<Usuario> buscaUsuario(
            @RequestParam(value = "nomeUser" , required = false, defaultValue = "") String nomeUser
    ) {
        return repository.findByNumCliente(nomeUser);
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
        byte[] pdf = serviceRelatorio.gerarRelatorio("relatorio-usuario",
                request.getServletContext());

        String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);

        return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
    }

}
