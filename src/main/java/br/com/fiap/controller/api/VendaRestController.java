package br.com.fiap.controller.api;

import br.com.fiap.controller.assembler.VendaAssembler;
import br.com.fiap.model.entity.Venda;
import br.com.fiap.model.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/vendas")
public class VendaRestController {

    @Autowired
    private VendaService vendaService;

    @Autowired
    private VendaAssembler vendaAssembler;

    @GetMapping
    public CollectionModel<EntityModel<Venda>> listarTodas() {
        var vendas = vendaService.listarTodas()
                .stream()
                .map(vendaAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                vendas,
                linkTo(methodOn(VendaRestController.class).listarTodas()).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Venda>> buscarPorId(@PathVariable Long id) {
        return vendaService.buscarPorId(id)
                .map(vendaAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EntityModel<Venda>> criar(@RequestBody VendaRequest request) {
        try {
            var venda = vendaService.registrarVenda(request.clienteId(), request.valorTotal());
            return ResponseEntity.ok(vendaAssembler.toModel(venda));
        } catch (IllegalArgumentException ex) {
            // Ex.: valorTotal <= 0
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException ex) {
            var msg = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
            if (msg.contains("não encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<Venda>> atualizarParcial(@PathVariable Long id, @RequestBody VendaPatch patch) {
        try {
            var venda = vendaService.atualizarParcial(id, patch.clienteId(), patch.valorTotal());
            return ResponseEntity.ok(vendaAssembler.toModel(venda));
        } catch (IllegalArgumentException ex) {
            // Ex.: valorTotal <= 0 no patch
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException ex) {
            var msg = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
            if (msg.contains("não encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return vendaService.buscarPorId(id)
                .map(v -> {
                    vendaService.excluir(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public record VendaRequest(Long clienteId, Double valorTotal, Double desconto) {}
    public record VendaPatch(Long clienteId, Double valorTotal) {}
}
