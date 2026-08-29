package br.com.catalogo.service;

import br.com.catalogo.dao.CatalogoDAO;
import br.com.catalogo.model.ItemCatalogo;
import br.com.catalogo.model.Tipo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para CatalogoService.
 * Não dependem de MySQL real (DAO é mockado).
 */
public class CatalogoServiceTest {

    private CatalogoDAO daoMock;
    private CatalogoService service;

    @BeforeEach
    void setUp() {
        daoMock = mock(CatalogoDAO.class);
        service = new CatalogoService(daoMock);
    }

    private ItemCatalogo itemValido() {
        ItemCatalogo item = new ItemCatalogo();
        item.setTitulo("Harry Potter");
        item.setTipo(Tipo.LIVRO);
        item.setGenero("Fantasia");
        item.setAnoLancamento(1997);
        item.setDescricao("Livro de J.K. Rowling");
        item.setAvaliacao(9.5);
        return item;
    }

    @Test
    @DisplayName("Cadastro válido deve chamar DAO.salvar")
    void cadastroValido() {
        ItemCatalogo item = itemValido();
        service.salvar(item);
        verify(daoMock, times(1)).salvar(item);
        assertEquals("Harry Potter", item.getTitulo());
    }

    @Test
    @DisplayName("Cadastro com título vazio deve falhar")
    void cadastroTituloVazio() {
        ItemCatalogo item = itemValido();
        item.setTitulo("   ");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.salvar(item));
        assertTrue(ex.getMessage().contains("Título"));
        verify(daoMock, never()).salvar(any());
    }

    @Test
    @DisplayName("Cadastro com tipo nulo deve falhar")
    void cadastroTipoNulo() {
        ItemCatalogo item = itemValido();
        item.setTipo(null);
        assertThrows(IllegalArgumentException.class, () -> service.salvar(item));
    }

    @Test
    @DisplayName("Cadastro com ano inválido deve falhar")
    void cadastroAnoInvalido() {
        ItemCatalogo item = itemValido();
        item.setAnoLancamento(1800);
        assertThrows(IllegalArgumentException.class, () -> service.salvar(item));

        item.setAnoLancamento(3000);
        assertThrows(IllegalArgumentException.class, () -> service.salvar(item));
    }

    @Test
    @DisplayName("Cadastro com avaliação fora do intervalo deve falhar")
    void cadastroAvaliacaoInvalida() {
        ItemCatalogo item = itemValido();
        item.setAvaliacao(11.0);
        assertThrows(IllegalArgumentException.class, () -> service.salvar(item));

        item.setAvaliacao(-1.0);
        assertThrows(IllegalArgumentException.class, () -> service.salvar(item));
    }

    @Test
    @DisplayName("Avaliação nula deve ser permitida")
    void avaliacaoNulaPermitida() {
        ItemCatalogo item = itemValido();
        item.setAvaliacao(null);
        service.salvar(item);
        verify(daoMock).salvar(item);
    }

    @Test
    @DisplayName("Atualização válida deve chamar DAO.atualizar")
    void atualizacaoValida() {
        ItemCatalogo item = itemValido();
        item.setId(1L);
        service.atualizar(item);
        verify(daoMock).atualizar(item);
    }

    @Test
    @DisplayName("Atualização sem ID deve falhar")
    void atualizacaoSemId() {
        ItemCatalogo item = itemValido();
        item.setId(null);
        assertThrows(IllegalArgumentException.class, () -> service.atualizar(item));
    }

    @Test
    @DisplayName("Exclusão válida deve chamar DAO.excluir")
    void exclusaoValida() {
        when(daoMock.excluir(1L)).thenReturn(true);
        boolean ok = service.excluir(1L);
        assertTrue(ok);
        verify(daoMock).excluir(1L);
    }

    @Test
    @DisplayName("Exclusão com ID inválido deve falhar")
    void exclusaoIdInvalido() {
        assertThrows(IllegalArgumentException.class, () -> service.excluir(null));
        assertThrows(IllegalArgumentException.class, () -> service.excluir(0L));
    }

    @Test
    @DisplayName("Pesquisar sem filtros deve listar todos")
    void pesquisarSemFiltros() {
        when(daoMock.listarTodos()).thenReturn(List.of(itemValido()));
        List<ItemCatalogo> lista = service.pesquisar(null, null);
        assertEquals(1, lista.size());
        verify(daoMock).listarTodos();
    }

    @Test
    @DisplayName("Pesquisar por título deve delegar ao DAO")
    void pesquisarPorTitulo() {
        when(daoMock.buscarPorTituloETipo(eq("Harry"), isNull())).thenReturn(List.of(itemValido()));
        List<ItemCatalogo> lista = service.pesquisar("Harry", null);
        assertEquals(1, lista.size());
        verify(daoMock).buscarPorTituloETipo("Harry", null);
    }

    @Test
    @DisplayName("Buscar por ID inválido deve falhar")
    void buscarPorIdInvalido() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarPorId(null));
        assertThrows(IllegalArgumentException.class, () -> service.buscarPorId(-1L));
    }

    @Test
    @DisplayName("Buscar por ID válido deve delegar ao DAO")
    void buscarPorIdValido() {
        ItemCatalogo item = itemValido();
        item.setId(1L);
        when(daoMock.buscarPorId(1L)).thenReturn(Optional.of(item));
        Optional<ItemCatalogo> opt = service.buscarPorId(1L);
        assertTrue(opt.isPresent());
        assertEquals("Harry Potter", opt.get().getTitulo());
    }

    @Test
    @DisplayName("Gênero vazio deve falhar")
    void generoVazio() {
        ItemCatalogo item = itemValido();
        item.setGenero(" ");
        assertThrows(IllegalArgumentException.class, () -> service.salvar(item));
    }
}
