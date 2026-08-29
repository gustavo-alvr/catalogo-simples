package br.com.catalogo.controller;

import br.com.catalogo.model.ItemCatalogo;
import br.com.catalogo.service.CatalogoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller para visualização de detalhes de um item.
 */
@WebServlet(name = "DetalhesServlet", urlPatterns = {"/detalhes"})
public class DetalhesServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(DetalhesServlet.class.getName());
    private CatalogoService service;

    @Override
    public void init() {
        this.service = new CatalogoService();
    }

    public void setService(CatalogoService service) {
        this.service = service;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String idStr = req.getParameter("id");

        if (idStr == null || idStr.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/catalogo");
            return;
        }

        try {
            Long id = Long.parseLong(idStr);
            Optional<ItemCatalogo> opt = service.buscarPorId(id);
            if (opt.isEmpty()) {
                req.setAttribute("mensagemErro", "Item não encontrado.");
                req.getRequestDispatcher("/erro.jsp").forward(req, resp);
                return;
            }
            req.setAttribute("item", opt.get());
            req.getRequestDispatcher("/detalhes.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            req.setAttribute("mensagemErro", "ID inválido.");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar detalhes", e);
            req.setAttribute("mensagemErro", "Não foi possível carregar os detalhes. Tente novamente.");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        }
    }
}
