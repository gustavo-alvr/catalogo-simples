package br.com.catalogo.controller;

import br.com.catalogo.service.CatalogoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller para exclusão de itens.
 * Aceita POST /excluir?id= para evitar exclusão acidental via GET/crawler.
 * Também aceita GET para compatibilidade com links simples, mas loga aviso.
 */
@WebServlet(name = "ExclusaoServlet", urlPatterns = {"/excluir"})
public class ExclusaoServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ExclusaoServlet.class.getName());
    private CatalogoService service;

    @Override
    public void init() {
        this.service = new CatalogoService();
    }

    public void setService(CatalogoService service) {
        this.service = service;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        excluir(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Permitir GET para facilitar uso didático, mas ideal é POST
        excluir(req, resp);
    }

    private void excluir(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/catalogo?erro=id");
            return;
        }
        try {
            Long id = Long.parseLong(idStr);
            boolean ok = service.excluir(id);
            if (ok) {
                resp.sendRedirect(req.getContextPath() + "/catalogo?msg=excluido");
            } else {
                req.setAttribute("mensagemErro", "Item não encontrado para exclusão.");
                req.getRequestDispatcher("/erro.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("mensagemErro", "ID inválido para exclusão.");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        } catch (IllegalArgumentException e) {
            req.setAttribute("mensagemErro", e.getMessage());
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao excluir item", e);
            req.setAttribute("mensagemErro", "Não foi possível excluir o item. Tente novamente.");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        }
    }
}
