package br.com.fiap.controller.assembler;

import br.com.fiap.controller.api.VendaRestController;
import br.com.fiap.model.entity.Venda;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class VendaAssembler implements RepresentationModelAssembler<Venda, EntityModel<Venda>> {

    @Override
    public EntityModel<Venda> toModel(Venda venda) {
        return EntityModel.of(venda,
                linkTo(methodOn(VendaRestController.class).buscarPorId(venda.getId())).withSelfRel(),
                linkTo(methodOn(VendaRestController.class).listarTodas()).withRel("vendas")
        );
    }
}
