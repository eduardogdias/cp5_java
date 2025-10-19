package br.com.fiap.controller.api;

import br.com.fiap.controller.assembler.ClienteAssembler;
import br.com.fiap.model.entity.Cliente;
import br.com.fiap.model.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteAssembler clienteAssembler;

    @GetMapping
    public CollectionModel<EntityModel<Cliente>> listarTodos() {
        var clientes = clienteService.listarTodos()
                .stream()
                .map(clienteAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                clientes,
                linkTo(methodOn(ClienteRestController.class).listarTodos()).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Cliente>> buscarPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id)
                .map(clienteAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EntityModel<Cliente>> cadastrar(@RequestBody Cliente cliente) {
        if (cliente.getCpf() != null && clienteService.existsByCpf(cliente.getCpf())) {
            return ResponseEntity.badRequest().build();
        }
        try {
            var salvo = clienteService.cadastrar(cliente);
            return ResponseEntity.ok(clienteAssembler.toModel(salvo));
        } catch (RuntimeException ex) {
            if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("cpf")) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Cliente>> atualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        try {
            var atualizado = clienteService.atualizar(id, cliente);
            return ResponseEntity.ok(clienteAssembler.toModel(atualizado));
        } catch (RuntimeException ex) {
            if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("não encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<Cliente>> atualizarParcial(@PathVariable Long id, @RequestBody Cliente cliente) {
        try {
            var atualizado = clienteService.atualizarParcial(id, cliente);
            return ResponseEntity.ok(clienteAssembler.toModel(atualizado));
        } catch (RuntimeException ex) {
            var msg = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
            if (msg.contains("não encontrado")) {
                return ResponseEntity.notFound().build();
            }
            if (msg.contains("cpf")) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        var existente = clienteService.buscarPorId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            clienteService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
