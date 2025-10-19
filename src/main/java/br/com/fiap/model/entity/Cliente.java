package br.com.fiap.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity


@Table(name = "TB_CLIENTE")
public class Cliente {
	
	  @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cliente_seq")
	    @SequenceGenerator(name = "cliente_seq", sequenceName = "SEQ_CLIENTE", allocationSize = 1)
	    private Long id;

	    @Column(nullable = false)
	    private String nome;

	    @Column(nullable = false, length = 11)
	    private String cpf;

	    @Column(nullable = false)
	    private String telefone;

	    @Column(nullable = false)
	    private String endereco;



}
