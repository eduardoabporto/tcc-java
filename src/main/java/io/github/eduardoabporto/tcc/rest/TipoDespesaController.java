package io.github.eduardoabporto.tcc.rest;

import io.github.eduardoabporto.tcc.model.entity.TipoDespesa;
import io.github.eduardoabporto.tcc.model.repository.TipoDespesaRepository;
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
@RequestMapping("/api/tipo-despesa")
@RequiredArgsConstructor
public class TipoDespesaController {


    private final ServiceRelatorio serviceRelatorio;
    private final TipoDespesaRepository tipoDespesaRepository;

    @GetMapping
    public List<TipoDespesa> obterTodos(){
        return tipoDespesaRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TipoDespesa salvar(@RequestBody @Valid TipoDespesa tipoDespesa){
        return tipoDespesaRepository.save(tipoDespesa);
    }

    @GetMapping("{id}")
    public TipoDespesa acharPorId(@PathVariable Integer id){
        return tipoDespesaRepository
                .findById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Tipo de Despesa não Encontrada"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id){
        tipoDespesaRepository
                .findById(id)
                .map(tipoDespesa -> {
                    tipoDespesaRepository.delete(tipoDespesa);
                    return Void.TYPE;
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Tipo de Despesa não Encontrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @RequestBody TipoDespesa TipoDespesaAtualizado){
        tipoDespesaRepository
                .findById(id)
                .map(tipoDespesa -> {
                    TipoDespesaAtualizado.setId(tipoDespesa.getId());
                    return tipoDespesaRepository.save(TipoDespesaAtualizado);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Tipo de Despesa não Encontrado"));
    }

    @GetMapping(value="/relatorio", produces = "application/text")
    public ResponseEntity<String> downloadRelatorio(HttpServletRequest request) throws Exception {
        byte[] pdf = serviceRelatorio.gerarRelatorio("relatorio-tipo-despesa",
                request.getServletContext());

        String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);

        return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
    }
}
