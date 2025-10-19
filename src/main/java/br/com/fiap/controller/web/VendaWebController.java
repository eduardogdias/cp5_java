package br.com.fiap.controller.web;


import br.com.fiap.model.entity.Venda;

import br.com.fiap.model.service.VendaService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;



@Controller
@RequestMapping("/web/vendas")
public class VendaWebController {

    @Autowired
    private VendaService vendaService;

    //exibe o formulário vazio/preenchido
    @GetMapping("/formulario")
    public String exibirFormulario(Model model) {
        model.addAttribute("venda", new Venda());
        return "venda/venda-form";
    }

    //recebe os dados do formulário e salva 
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Venda venda) {
    	
    	if(venda.getId() != null) {
    		vendaService.atualizarParcial(venda.getId(), venda.getCliente().getId(), venda.getValorTotal()); //editar venda
    	}
    	else {
    		vendaService.registrarVenda(venda.getCliente().getId(), venda.getValorTotal()); //cadastrar venda
    	}       
        return "redirect:/web/vendas/listar";
    }


    
    
    
    @GetMapping("/listar")
    public String listar(Model model) {
        List<Venda> vendas = vendaService.listarTodas();
        model.addAttribute("vendas", vendas);
        return "venda/venda-listar";
    }
    
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
    	Venda venda = vendaService.buscarPorId(id).get(); // precisa existir no service
        model.addAttribute("venda", venda);
        return "venda/venda-form"; // reutiliza o mesmo formulário
    }


    
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        vendaService.excluir(id);
        return "redirect:/web/vendas/listar";
    }


}
