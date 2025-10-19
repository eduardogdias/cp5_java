package br.com.fiap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.fiap.model.entity.Usuario;
import br.com.fiap.model.repository.UsuarioRepository;

@SpringBootApplication
public class SuperMarketApplication implements CommandLineRunner {

	@Autowired
    private UsuarioRepository usuarioRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(SuperMarketApplication.class, args);	
	}

	@Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            Usuario u1 = new Usuario(null, "ADMIN", "admin@mercado.com", "{noop}123456", "ADMIN");
            Usuario u2 = new Usuario(null, "USER", "user@mercado.com", "{noop}123456", "USER");
            usuarioRepository.save(u1);
            usuarioRepository.save(u2);
        }
    }
	
	
}

