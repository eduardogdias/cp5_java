package br.com.fiap.controller.api;

import br.com.fiap.controller.assembler.ProdutoAssembler;
import br.com.fiap.model.entity.Produto;
import br.com.fiap.model.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoRestController {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ProdutoAssembler produtoAssembler;

    @GetMapping
    public CollectionModel<EntityModel<Produto>> listar() {
        var produtos = produtoService.listarTodos()
                .stream()
                .map(produtoAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                produtos,
                linkTo(methodOn(ProdutoRestController.class).listar()).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Produto>> buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id)
                .map(produtoAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EntityModel<Produto>> criar(@RequestBody Produto produto) {
        try {
            var salvo = produtoService.cadastrar(produto);
            return ResponseEntity.ok(produtoAssembler.toModel(salvo));
        } catch (RuntimeException ex) {
            var msg = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
            if (msg.contains("código")) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Produto>> atualizar(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {
        try {
            var atualizado = produtoService.atualizar(id, produtoAtualizado);
            return ResponseEntity.ok(produtoAssembler.toModel(atualizado));
        } catch (RuntimeException ex) {
            var msg = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
            if (msg.contains("não encontrado")) {
                return ResponseEntity.notFound().build();
            }
            if (msg.contains("código")) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<Produto>> atualizarParcial(@PathVariable Long id, @RequestBody Produto patch) {
        try {
            var atualizado = produtoService.atualizarParcial(id, patch);
            return ResponseEntity.ok(produtoAssembler.toModel(atualizado));
        } catch (RuntimeException ex) {
            var msg = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
            if (msg.contains("não encontrado")) {
                return ResponseEntity.notFound().build();
            }
            if (msg.contains("código")) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        var existente = produtoService.buscarPorId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
