package br.com.catalogo.dao;

import br.com.catalogo.model.ItemCatalogo;
import br.com.catalogo.model.Tipo;
import br.com.catalogo.util.ConnectionFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementação JDBC do CatalogoDAO.
 * Usa PreparedStatement e try-with-resources em todas as operações.
 */
public class CatalogoDAOImpl implements CatalogoDAO {

    private static final Logger LOGGER = Logger.getLogger(CatalogoDAOImpl.class.getName());

    private ItemCatalogo mapear(ResultSet rs) throws SQLException {
        ItemCatalogo item = new ItemCatalogo();
        item.setId(rs.getLong("id"));
        item.setTitulo(rs.getString("titulo"));
        item.setTipo(Tipo.valueOf(rs.getString("tipo")));
        item.setGenero(rs.getString("genero"));
        item.setAnoLancamento(rs.getInt("ano_lancamento"));
        item.setDescricao(rs.getString("descricao"));
        double aval = rs.getDouble("avaliacao");
        item.setAvaliacao(rs.wasNull() ? null : aval);
        Timestamp ts = rs.getTimestamp("data_cadastro");
        item.setDataCadastro(ts != null ? ts.toLocalDateTime() : null);
        return item;
    }

    @Override
    public void salvar(ItemCatalogo item) {
        String sql = "INSERT INTO catalogo (titulo, tipo, genero, ano_lancamento, descricao, avaliacao) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, item.getTitulo());
            ps.setString(2, item.getTipo().name());
            ps.setString(3, item.getGenero());
            ps.setInt(4, item.getAnoLancamento());
            ps.setString(5, item.getDescricao());
            if (item.getAvaliacao() != null) {
                ps.setDouble(6, item.getAvaliacao());
            } else {
                ps.setNull(6, Types.DECIMAL);
            }
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar item", e);
            throw new RuntimeException("Não foi possível salvar o item.", e);
        }
    }

    @Override
    public List<ItemCatalogo> listarTodos() {
        String sql = "SELECT * FROM catalogo ORDER BY data_cadastro DESC, id DESC";
        List<ItemCatalogo> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar itens", e);
            throw new RuntimeException("Não foi possível listar os itens.", e);
        }
        return lista;
    }

    @Override
    public Optional<ItemCatalogo> buscarPorId(Long id) {
        String sql = "SELECT * FROM catalogo WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar por id", e);
            throw new RuntimeException("Não foi possível buscar o item.", e);
        }
        return Optional.empty();
    }

    @Override
    public void atualizar(ItemCatalogo item) {
        String sql = "UPDATE catalogo SET titulo=?, tipo=?, genero=?, ano_lancamento=?, descricao=?, avaliacao=? WHERE id=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getTitulo());
            ps.setString(2, item.getTipo().name());
            ps.setString(3, item.getGenero());
            ps.setInt(4, item.getAnoLancamento());
            ps.setString(5, item.getDescricao());
            if (item.getAvaliacao() != null) {
                ps.setDouble(6, item.getAvaliacao());
            } else {
                ps.setNull(6, Types.DECIMAL);
            }
            ps.setLong(7, item.getId());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Item não encontrado para atualização: id=" + item.getId());
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar item", e);
            throw new RuntimeException("Não foi possível atualizar o item.", e);
        }
    }

    @Override
    public boolean excluir(Long id) {
        String sql = "DELETE FROM catalogo WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao excluir item", e);
            throw new RuntimeException("Não foi possível excluir o item.", e);
        }
    }

    @Override
    public List<ItemCatalogo> buscarPorTitulo(String titulo) {
        return buscarPorTituloETipo(titulo, null);
    }

    @Override
    public List<ItemCatalogo> buscarPorTipo(Tipo tipo) {
        return buscarPorTituloETipo(null, tipo);
    }

    @Override
    public List<ItemCatalogo> buscarPorTituloETipo(String titulo, Tipo tipo) {
        StringBuilder sql = new StringBuilder("SELECT * FROM catalogo WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (titulo != null && !titulo.isBlank()) {
            sql.append(" AND LOWER(titulo) LIKE LOWER(?)");
            params.add("%" + titulo.trim() + "%");
        }
        if (tipo != null) {
            sql.append(" AND tipo = ?");
            params.add(tipo.name());
        }
        sql.append(" ORDER BY data_cadastro DESC, id DESC");

        List<ItemCatalogo> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar por titulo/tipo", e);
            throw new RuntimeException("Não foi possível realizar a pesquisa.", e);
        }
        return lista;
    }
}
