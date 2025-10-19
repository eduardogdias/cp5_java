package br.com.fiap.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErroController {

    @GetMapping("/403")
    public String error403() {
        return "403"; 
    }
}