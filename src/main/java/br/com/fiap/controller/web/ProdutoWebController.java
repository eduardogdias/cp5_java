package br.com.fiap.controller.web;



import br.com.fiap.model.entity.Produto;

import br.com.fiap.model.service.ProdutoService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;



@Controller
@RequestMapping("/web/produtos")
public class ProdutoWebController {

    @Autowired
    private ProdutoService produtoService;

    //exibe o formulário vazio/preenchido
    @GetMapping("/formulario")
    public String exibirFormulario(Model model) {
        model.addAttribute("produto", new Produto());
        return "produto/produto-form";
    }

    //recebe os dados do formulário e salva 
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Produto produto) {	
    	if(produto.getId() != null) {
    		//produtoService.salvarEdicao(produto); 
    		produtoService.atualizar(produto.getId(), produto); //editar produto
    	}
    	else {
    		produtoService.cadastrar(produto); //cadastrar produto
    	}       
        return "redirect:/web/produtos/listar";
    }


    
    
    
    @GetMapping("/listar")
    public String listar(Model model) {
        List<Produto> produtos = produtoService.listarTodos();
        model.addAttribute("produtos", produtos);
        return "produto/produto-listar";
    }
    
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
    	Produto produto = produtoService.buscarPorId(id).get(); // precisa existir no service
        model.addAttribute("produto", produto);
        return "produto/produto-form"; // reutiliza o mesmo formulário
    }


    
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
    	produtoService.excluir(id);
        return "redirect:/web/produtos/listar";
    }


}
