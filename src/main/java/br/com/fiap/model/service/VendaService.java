package br.com.fiap.model.service;

import br.com.fiap.model.entity.Cliente;
import br.com.fiap.model.entity.Venda;
import br.com.fiap.model.repository.ClienteRepository;
import br.com.fiap.model.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Venda> listarTodas() {
        return vendaRepository.findAll();
    }

    public Optional<Venda> buscarPorId(Long id) {
        return vendaRepository.findById(id);
    }

    @Transactional
    public Venda registrarVenda(Long clienteId, Double valorTotal) {
        if (valorTotal == null || valorTotal <= 0) {
            throw new IllegalArgumentException("O valor da venda deve ser maior que zero.");
        }

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        double desconto = (valorTotal >= 200.0) ? 15.0 : 0.0;
        double totalFinal = valorTotal - desconto;

        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setDataVenda(LocalDate.now());
        venda.setDesconto(desconto);
        venda.setValorTotal(totalFinal);

        return vendaRepository.save(venda);
    }

    @Transactional
    public Venda atualizarParcial(Long id, Long clienteId, Double valorTotal) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));

        if (clienteId != null) {
            Cliente cliente = clienteRepository.findById(clienteId)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            venda.setCliente(cliente);
        }

        if (valorTotal != null) {
            if (valorTotal <= 0) {
                throw new IllegalArgumentException("O valor da venda deve ser maior que zero.");
            }
            double desconto = (valorTotal >= 200.0) ? 15.0 : 0.0;
            double totalFinal = valorTotal - desconto;
            venda.setDesconto(desconto);
            venda.setValorTotal(totalFinal);
        }

        return vendaRepository.save(venda);
    }

    @Transactional
    public void excluir(Long id) {
        vendaRepository.deleteById(id);
    }
}
