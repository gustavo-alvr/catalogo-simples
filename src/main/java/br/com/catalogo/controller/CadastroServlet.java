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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller para cadastro de novos itens.
 * GET  /cadastro - exibe formulário
 * POST /cadastro - processa cadastro
 */
@WebServlet(name = "CadastroServlet", urlPatterns = {"/cadastro"})
public class CadastroServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CadastroServlet.class.getName());
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
        req.getRequestDispatcher("/cadastro.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String titulo = req.getParameter("titulo");
        String tipoStr = req.getParameter("tipo");
        String genero = req.getParameter("genero");
        String anoStr = req.getParameter("anoLancamento");
        String descricao = req.getParameter("descricao");
        String avaliacaoStr = req.getParameter("avaliacao");

        ItemCatalogo item = new ItemCatalogo();
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
            req.getRequestDispatcher("/cadastro.jsp").forward(req, resp);
            return;
        }

        try {
            if (anoStr != null && !anoStr.isBlank()) {
                item.setAnoLancamento(Integer.parseInt(anoStr.trim()));
            }
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "Ano de lançamento deve ser um número válido.");
            req.setAttribute("item", item);
            req.getRequestDispatcher("/cadastro.jsp").forward(req, resp);
            return;
        }

        try {
            if (avaliacaoStr != null && !avaliacaoStr.isBlank()) {
                // aceita vírgula como separador
                String norm = avaliacaoStr.trim().replace(",", ".");
                item.setAvaliacao(Double.parseDouble(norm));
            }
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "Avaliação deve ser um número entre 0 e 10.");
            req.setAttribute("item", item);
            req.getRequestDispatcher("/cadastro.jsp").forward(req, resp);
            return;
        }

        try {
            service.salvar(item);
            resp.sendRedirect(req.getContextPath() + "/catalogo?msg=cadastrado");
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("item", item);
            req.getRequestDispatcher("/cadastro.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao cadastrar item", e);
            req.setAttribute("erro", "Não foi possível cadastrar o item. Tente novamente.");
            req.setAttribute("item", item);
            req.getRequestDispatcher("/cadastro.jsp").forward(req, resp);
        }
    }
}
