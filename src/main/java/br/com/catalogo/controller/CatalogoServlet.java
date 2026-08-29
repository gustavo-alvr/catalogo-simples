package br.com.catalogo.controller;

import br.com.catalogo.model.Tipo;
import br.com.catalogo.service.CatalogoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import br.com.catalogo.model.ItemCatalogo;

/**
 * Controller responsável pela listagem, pesquisa por título e filtro por tipo.
 * Mapeado em /catalogo e também atende / (redirect).
 */
@WebServlet(name = "CatalogoServlet", urlPatterns = {"/catalogo"})
public class CatalogoServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CatalogoServlet.class.getName());
    private CatalogoService service;

    @Override
    public void init() {
        this.service = new CatalogoService();
    }

    // Para testes
    public void setService(CatalogoService service) {
        this.service = service;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String q = req.getParameter("q");
        String tipoParam = req.getParameter("tipo");

        Tipo tipo = null;
        if (tipoParam != null && !tipoParam.isBlank() && !"TODOS".equalsIgnoreCase(tipoParam)) {
            try {
                tipo = Tipo.valueOf(tipoParam.toUpperCase());
            } catch (IllegalArgumentException e) {
                req.setAttribute("erro", "Tipo inválido: " + tipoParam);
            }
        }

        try {
            List<ItemCatalogo> itens = service.pesquisar(q, tipo);
            req.setAttribute("itens", itens);
            req.setAttribute("q", q);
            req.setAttribute("tipoSelecionado", tipo != null ? tipo.name() : "TODOS");
            req.getRequestDispatcher("/lista.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar catálogo", e);
            req.setAttribute("mensagemErro", "Não foi possível carregar o catálogo. Tente novamente.");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        }
    }
}
