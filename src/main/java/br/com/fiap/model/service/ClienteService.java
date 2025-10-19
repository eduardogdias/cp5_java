package br.com.fiap.model.service;

import br.com.fiap.model.entity.Cliente;
import br.com.fiap.model.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public boolean existsByCpf(String cpf) {
        return clienteRepository.existsByCpf(cpf);
    }

    public Cliente cadastrar(Cliente cliente) {
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new RuntimeException("Cliente com este CPF já existe!");
        }
        return clienteRepository.save(cliente);
    }
    
   

    public Cliente atualizar(Long id, Cliente cliente) {
        return clienteRepository.findById(id)
                .map(c -> {
                    if (cliente.getCpf() != null && !cliente.getCpf().equals(c.getCpf())
                            && clienteRepository.existsByCpf(cliente.getCpf())) {
                        throw new RuntimeException("Cliente com este CPF já existe!");
                    }
                    c.setNome(cliente.getNome());
                    c.setCpf(cliente.getCpf());
                    c.setTelefone(cliente.getTelefone());
                    c.setEndereco(cliente.getEndereco());
                    return clienteRepository.save(c);
                })
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public Cliente atualizarParcial(Long id, Cliente patch) {
        return clienteRepository.findById(id)
                .map(c -> {
                    if (patch.getNome() != null) {
                        c.setNome(patch.getNome());
                    }
                    if (patch.getCpf() != null) {
                        var cpfNovo = patch.getCpf();
                        if (!cpfNovo.equals(c.getCpf()) && clienteRepository.existsByCpf(cpfNovo)) {
                            throw new RuntimeException("Cliente com este CPF já existe!");
                        }
                        c.setCpf(cpfNovo);
                    }
                    if (patch.getTelefone() != null) {
                        c.setTelefone(patch.getTelefone());
                    }
                    if (patch.getEndereco() != null) {
                        c.setEndereco(patch.getEndereco());
                    }
                    return clienteRepository.save(c);
                })
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public void excluir(Long id) {
        clienteRepository.deleteById(id);
    }
}
