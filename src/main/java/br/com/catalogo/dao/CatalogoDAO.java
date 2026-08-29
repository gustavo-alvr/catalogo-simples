package br.com.catalogo.dao;

import br.com.catalogo.model.ItemCatalogo;
import br.com.catalogo.model.Tipo;

import java.util.List;
import java.util.Optional;

/**
 * Interface DAO para operações CRUD no catálogo.
 * Toda operação SQL deve passar por aqui; Servlets e Services não devem conter SQL.
 */
public interface CatalogoDAO {

    /**
     * Salva um novo item e preenche o id gerado.
     */
    void salvar(ItemCatalogo item);

    /**
     * Lista todos os itens ordenados por data de cadastro decrescente.
     */
    List<ItemCatalogo> listarTodos();

    /**
     * Busca item por id.
     */
    Optional<ItemCatalogo> buscarPorId(Long id);

    /**
     * Atualiza um item existente.
     */
    void atualizar(ItemCatalogo item);

    /**
     * Exclui item por id.
     *
     * @return true se algum registro foi removido
     */
    boolean excluir(Long id);

    /**
     * Pesquisa por título (LIKE %titulo%, case-insensitive).
     */
    List<ItemCatalogo> buscarPorTitulo(String titulo);

    /**
     * Filtra por tipo.
     */
    List<ItemCatalogo> buscarPorTipo(Tipo tipo);

    /**
     * Pesquisa combinada: título e/ou tipo (ambos opcionais).
     */
    List<ItemCatalogo> buscarPorTituloETipo(String titulo, Tipo tipo);
}
