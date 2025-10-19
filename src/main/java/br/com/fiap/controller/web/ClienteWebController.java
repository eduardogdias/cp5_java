package br.com.fiap.controller.web;


import br.com.fiap.model.entity.Cliente;
import br.com.fiap.model.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;



@Controller
@RequestMapping("/web/clientes")
public class ClienteWebController {

    @Autowired
    private ClienteService clienteService;

    //exibe o formulário vazio/preenchido
    @GetMapping("/formulario")
    public String exibirFormulario(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente/cliente-form";
    }

    //recebe os dados do formulário e salva 
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Cliente cliente) {
    	
    	if(cliente.getId() != null) {
    		clienteService.atualizar(cliente.getId(), cliente); //editar cliente
    	}
    	else {
    		clienteService.cadastrar(cliente); //cadastrar cliente
    	}       
        return "redirect:/web/clientes/listar";
    }


    
    
    
    @GetMapping("/listar")
    public String listar(Model model) {
        List<Cliente> clientes = clienteService.listarTodos();
        model.addAttribute("clientes", clientes);
        return "cliente/cliente-listar";
    }
    
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id).get(); // precisa existir no service
        model.addAttribute("cliente", cliente);
        return "cliente/cliente-form"; // reutiliza o mesmo formulário
    }


    
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        clienteService.excluir(id);
        return "redirect:/web/clientes/listar";
    }


}
