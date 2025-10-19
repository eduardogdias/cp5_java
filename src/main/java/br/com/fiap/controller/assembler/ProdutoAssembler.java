package br.com.fiap.controller.assembler;

import br.com.fiap.controller.api.ProdutoRestController;
import br.com.fiap.model.entity.Produto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProdutoAssembler implements RepresentationModelAssembler<Produto, EntityModel<Produto>> {

    @Override
    public EntityModel<Produto> toModel(Produto produto) {
        return EntityModel.of(produto,
                linkTo(methodOn(ProdutoRestController.class).buscarPorId(produto.getId())).withSelfRel(),
                linkTo(methodOn(ProdutoRestController.class).listar()).withRel("produtos")
        );
    }
}
