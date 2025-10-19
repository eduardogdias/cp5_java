package br.com.fiap.controller.assembler;

import br.com.fiap.controller.api.ClienteRestController;
import br.com.fiap.model.entity.Cliente;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ClienteAssembler implements RepresentationModelAssembler<Cliente, EntityModel<Cliente>> {

    @Override
    public EntityModel<Cliente> toModel(Cliente cliente) {
        return EntityModel.of(cliente,
                linkTo(methodOn(ClienteRestController.class).buscarPorId(cliente.getId())).withSelfRel(),
                linkTo(methodOn(ClienteRestController.class).listarTodos()).withRel("clientes")
        );
    }
}
