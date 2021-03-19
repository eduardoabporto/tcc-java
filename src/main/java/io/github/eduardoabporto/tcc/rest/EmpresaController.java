package io.github.eduardoabporto.tcc.rest;

import io.github.eduardoabporto.tcc.model.entity.Empresa;
import io.github.eduardoabporto.tcc.model.repository.EmpresaRepository;
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
@RequestMapping("/api/empresa")
@RequiredArgsConstructor
public class EmpresaController {


    private final ServiceRelatorio serviceRelatorio;
    private final EmpresaRepository empresaRepository;

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

    @GetMapping(value="/relatorio", produces = "application/text")
    public ResponseEntity<String> downloadRelatorio(HttpServletRequest request) throws Exception {
        byte[] pdf = serviceRelatorio.gerarRelatorio("relatorio-empresas",
                request.getServletContext());

        String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);

        return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
    }
}
