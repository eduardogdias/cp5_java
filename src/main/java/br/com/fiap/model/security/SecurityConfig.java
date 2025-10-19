package br.com.fiap.model.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http       	
        		.csrf(csrf -> csrf.disable())
        		.authorizeHttpRequests(       				
        				auth -> {   
        					// liberar tela e envio do form
        					auth.requestMatchers("/cadastro/**").permitAll(); 
        					auth.requestMatchers("/login/**").permitAll(); 
        					auth.requestMatchers("/index/**").permitAll();
        					
        					// regras para web       					
        					auth.requestMatchers("/web/*/editar/*").hasRole("ADMIN");
        					auth.requestMatchers("/web/*/excluir/*").hasRole("ADMIN");
        					
        					// regras para api        					
        	                auth.requestMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN");
        	                auth.requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN");
        	                
        	                auth.anyRequest().authenticated();
        					
        				})
        		//.httpBasic(Customizer.withDefaults()) //login via Basic Auth

        		.formLogin(form -> form //login via formulario na web
        				.loginPage("/index") // pagina personalizada de login
                        .defaultSuccessUrl("/web/clientes/listar", true) 
                        .permitAll()      
                )
        		.exceptionHandling(exception -> exception
        			    .accessDeniedHandler((request, response, accessDeniedException) -> {
        			        String uri = request.getRequestURI();

        			        if (uri.startsWith("/api/")) {
        			            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado"); // response com status code 403 Forbidden
        			        } else {
        			            response.sendRedirect("/403"); // retorna página de Acesso Negado
        			        }
        			    })
        		)
        		.logout(Customizer.withDefaults())
        		.build();
    }

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // senha salva em texto puro
	}


}
