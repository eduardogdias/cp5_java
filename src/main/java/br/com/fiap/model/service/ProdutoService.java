package br.com.fiap.model.service;


import br.com.fiap.model.entity.Produto;
import br.com.fiap.model.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    public Produto cadastrar(Produto produto) {
        if (produtoRepository.existsByCodigo(produto.getCodigo())) {
            throw new RuntimeException("Produto com este código já existe!");
        }
        return produtoRepository.save(produto);
    }

    
   
    
    public Produto atualizar(Long id, Produto produto) {
        return produtoRepository.findById(id)
                .map(p -> {
                    if (produto.getCodigo() != null && !produto.getCodigo().equals(p.getCodigo())
                            && produtoRepository.existsByCodigo(produto.getCodigo())) {
                        throw new RuntimeException("Produto com este código já existe!");
                    }
                    p.setNome(produto.getNome());
                    p.setCodigo(produto.getCodigo());
                    p.setCategoria(produto.getCategoria());
                    p.setPreco(produto.getPreco());
                    p.setDataValidade(produto.getDataValidade());
                    return produtoRepository.save(p);
                })
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public Produto atualizarParcial(Long id, Produto patch) {
        return produtoRepository.findById(id)
                .map(p -> {
                    if (patch.getNome() != null) {
                        p.setNome(patch.getNome());
                    }
                    if (patch.getCodigo() != null) {
                        var novoCodigo = patch.getCodigo();
                        if (!novoCodigo.equals(p.getCodigo()) && produtoRepository.existsByCodigo(novoCodigo)) {
                            throw new RuntimeException("Produto com este código já existe!");
                        }
                        p.setCodigo(novoCodigo);
                    }
                    if (patch.getCategoria() != null) {
                        p.setCategoria(patch.getCategoria());
                    }
                    if (patch.getPreco() != null) {
                        p.setPreco(patch.getPreco());
                    }
                    if (patch.getDataValidade() != null) {
                        p.setDataValidade(patch.getDataValidade());
                    }
                    return produtoRepository.save(p);
                })
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public void excluir(Long id) {
        produtoRepository.deleteById(id);
    }
}
