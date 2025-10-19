package br.com.fiap.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.fiap.model.entity.Usuario;
import br.com.fiap.model.repository.UsuarioRepository;

@Controller
public class CadastroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {	
        model.addAttribute("usuario", new Usuario());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String salvarCadastro(@ModelAttribute Usuario usuario) {
        usuario.setSenha("{noop}" + usuario.getSenha()); // adiciona-se {noop} pois nao estamos usando encoder
        usuario.setRole("USER"); // role padrão

        usuarioRepository.save(usuario);
        return "redirect:/login";
    }
}

