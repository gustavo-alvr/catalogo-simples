package br.com.catalogo.service;

import br.com.catalogo.dao.CatalogoDAO;
import br.com.catalogo.dao.CatalogoDAOImpl;
import br.com.catalogo.model.ItemCatalogo;
import br.com.catalogo.model.Tipo;

import java.time.Year;
import java.util.List;
import java.util.Optional;

/**
 * Camada de serviço: concentra regras de negócio e validações.
 * Servlets devem chamar o Service, nunca o DAO diretamente.
 */
public class CatalogoService {

    private final CatalogoDAO dao;

    public CatalogoService() {
        this.dao = new CatalogoDAOImpl();
    }

    /**
     * Construtor para injeção de DAO (usado em testes unitários com mock).
     */
    public CatalogoService(CatalogoDAO dao) {
        this.dao = dao;
    }

    /**
     * Valida um item. Lança IllegalArgumentException com mensagem amigável se inválido.
     */
    public void validar(ItemCatalogo item) {
        if (item == null) {
            throw new IllegalArgumentException("Item não pode ser nulo.");
        }
        if (item.getTitulo() == null || item.getTitulo().isBlank()) {
            throw new IllegalArgumentException("Título é obrigatório.");
        }
        if (item.getTitulo().trim().length() < 2 || item.getTitulo().trim().length() > 255) {
            throw new IllegalArgumentException("Título deve ter entre 2 e 255 caracteres.");
        }
        if (item.getTipo() == null) {
            throw new IllegalArgumentException("Tipo é obrigatório (LIVRO, SERIE ou FILME).");
        }
        if (item.getGenero() == null || item.getGenero().isBlank()) {
            throw new IllegalArgumentException("Gênero é obrigatório.");
        }
        if (item.getGenero().trim().length() < 2 || item.getGenero().trim().length() > 100) {
            throw new IllegalArgumentException("Gênero deve ter entre 2 e 100 caracteres.");
        }
        if (item.getAnoLancamento() == null) {
            throw new IllegalArgumentException("Ano de lançamento é obrigatório.");
        }
        int anoAtual = Year.now().getValue();
        if (item.getAnoLancamento() < 1900 || item.getAnoLancamento() > anoAtual + 2) {
            throw new IllegalArgumentException("Ano de lançamento deve estar entre 1900 e " + (anoAtual + 2) + ".");
        }
        if (item.getAvaliacao() != null && (item.getAvaliacao() < 0.0 || item.getAvaliacao() > 10.0)) {
            throw new IllegalArgumentException("Avaliação deve estar entre 0 e 10.");
        }
        if (item.getDescricao() != null && item.getDescricao().length() > 5000) {
            throw new IllegalArgumentException("Descrição muito longa (máx 5000 caracteres).");
        }
    }

    public void salvar(ItemCatalogo item) {
        validar(item);
        // normaliza
        item.setTitulo(item.getTitulo().trim());
        item.setGenero(item.getGenero().trim());
        if (item.getDescricao() != null) {
            item.setDescricao(item.getDescricao().trim());
            if (item.getDescricao().isEmpty()) {
                item.setDescricao(null);
            }
        }
        dao.salvar(item);
    }

    public List<ItemCatalogo> listarTodos() {
        return dao.listarTodos();
    }

    public Optional<ItemCatalogo> buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        return dao.buscarPorId(id);
    }

    public void atualizar(ItemCatalogo item) {
        if (item.getId() == null || item.getId() <= 0) {
            throw new IllegalArgumentException("ID é obrigatório para atualização.");
        }
        validar(item);
        item.setTitulo(item.getTitulo().trim());
        item.setGenero(item.getGenero().trim());
        if (item.getDescricao() != null) {
            item.setDescricao(item.getDescricao().trim());
            if (item.getDescricao().isEmpty()) {
                item.setDescricao(null);
            }
        }
        dao.atualizar(item);
    }

    public boolean excluir(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido para exclusão.");
        }
        return dao.excluir(id);
    }

    public List<ItemCatalogo> pesquisar(String titulo, Tipo tipo) {
        // pesquisa com ambos opcionais, delega ao DAO
        boolean tituloVazio = titulo == null || titulo.isBlank();
        if (tituloVazio && tipo == null) {
            return dao.listarTodos();
        }
        return dao.buscarPorTituloETipo(tituloVazio ? null : titulo.trim(), tipo);
    }

    public List<ItemCatalogo> buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            return listarTodos();
        }
        return dao.buscarPorTitulo(titulo.trim());
    }

    public List<ItemCatalogo> buscarPorTipo(Tipo tipo) {
        if (tipo == null) {
            return listarTodos();
        }
        return dao.buscarPorTipo(tipo);
    }
}
