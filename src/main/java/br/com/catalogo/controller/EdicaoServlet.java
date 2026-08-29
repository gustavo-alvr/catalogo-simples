package br.com.catalogo.controller;

import br.com.catalogo.model.ItemCatalogo;
import br.com.catalogo.model.Tipo;
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
 * Controller para edição de itens.
 * GET  /editar?id= - exibe formulário preenchido
 * POST /editar    - processa atualização
 */
@WebServlet(name = "EdicaoServlet", urlPatterns = {"/editar"})
public class EdicaoServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(EdicaoServlet.class.getName());
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
                req.setAttribute("mensagemErro", "Item não encontrado para edição.");
                req.getRequestDispatcher("/erro.jsp").forward(req, resp);
                return;
            }
            req.setAttribute("item", opt.get());
            req.getRequestDispatcher("/editar.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            req.setAttribute("mensagemErro", "ID inválido.");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar edição", e);
            req.setAttribute("mensagemErro", "Não foi possível carregar o item para edição.");
            req.getRequestDispatcher("/erro.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String idStr = req.getParameter("id");
        String titulo = req.getParameter("titulo");
        String tipoStr = req.getParameter("tipo");
        String genero = req.getParameter("genero");
        String anoStr = req.getParameter("anoLancamento");
        String descricao = req.getParameter("descricao");
        String avaliacaoStr = req.getParameter("avaliacao");

        ItemCatalogo item = new ItemCatalogo();
        try {
            item.setId(Long.parseLong(idStr));
        } catch (Exception e) {
            req.setAttribute("erro", "ID inválido.");
            req.setAttribute("item", item);
            req.getRequestDispatcher("/editar.jsp").forward(req, resp);
            return;
        }
        item.setTitulo(titulo);
        item.setGenero(genero);
        item.setDescricao(descricao);

        try {
            if (tipoStr != null && !tipoStr.isBlank()) {
                item.setTipo(Tipo.valueOf(tipoStr.toUpperCase()));
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", "Tipo inválido.");
            req.setAttribute("item", item);
            req.getRequestDispatcher("/editar.jsp").forward(req, resp);
            return;
        }

        try {
            if (anoStr != null && !anoStr.isBlank()) {
                item.setAnoLancamento(Integer.parseInt(anoStr.trim()));
            }
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "Ano de lançamento deve ser um número válido.");
            req.setAttribute("item", item);
            req.getRequestDispatcher("/editar.jsp").forward(req, resp);
            return;
        }

        try {
            if (avaliacaoStr != null && !avaliacaoStr.isBlank()) {
                String norm = avaliacaoStr.trim().replace(",", ".");
                item.setAvaliacao(Double.parseDouble(norm));
            } else {
                item.setAvaliacao(null);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "Avaliação deve ser um número entre 0 e 10.");
            req.setAttribute("item", item);
            req.getRequestDispatcher("/editar.jsp").forward(req, resp);
            return;
        }

        try {
            service.atualizar(item);
            resp.sendRedirect(req.getContextPath() + "/catalogo?msg=atualizado");
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("item", item);
            req.getRequestDispatcher("/editar.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar item", e);
            req.setAttribute("erro", "Não foi possível atualizar o item. Tente novamente.");
            req.setAttribute("item", item);
            req.getRequestDispatcher("/editar.jsp").forward(req, resp);
        }
    }
}
