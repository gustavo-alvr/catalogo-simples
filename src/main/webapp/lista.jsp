<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Catálogo - Lista</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header>
    <h1>Catálogo de Livros, Séries e Filmes</h1>
    <p><a href="${pageContext.request.contextPath}/" style="color:#fff; text-decoration:underline;">Início</a></p>
</header>
<div class="container">

    <c:if test="${param.msg == 'cadastrado'}">
        <div class="alert alert-success">Item cadastrado com sucesso!</div>
    </c:if>
    <c:if test="${param.msg == 'atualizado'}">
        <div class="alert alert-success">Item atualizado com sucesso!</div>
    </c:if>
    <c:if test="${param.msg == 'excluido'}">
        <div class="alert alert-success">Item excluído com sucesso!</div>
    </c:if>
    <c:if test="${not empty erro}">
        <div class="alert alert-error"><c:out value="${erro}"/></div>
    </c:if>

    <nav class="actions">
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/cadastro">Cadastrar item</a>

        <form method="get" action="${pageContext.request.contextPath}/catalogo">
            <input type="text" name="q" placeholder="Pesquisar por título" value="<c:out value='${q}'/>"/>
            <select name="tipo">
                <option value="TODOS" ${tipoSelecionado == 'TODOS' ? 'selected' : ''}>Todos</option>
                <option value="LIVRO" ${tipoSelecionado == 'LIVRO' ? 'selected' : ''}>Livros</option>
                <option value="SERIE" ${tipoSelecionado == 'SERIE' ? 'selected' : ''}>Séries</option>
                <option value="FILME" ${tipoSelecionado == 'FILME' ? 'selected' : ''}>Filmes</option>
            </select>
            <button type="submit" class="btn btn-secondary">Pesquisar</button>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/catalogo">Limpar</a>
        </form>
    </nav>

    <h2>Lista de itens <small style="font-weight:normal; color:#666;">(<c:out value="${itens.size()}"/> encontrados)</small></h2>

    <c:if test="${empty itens}">
        <div class="alert alert-info">Nenhum item encontrado. Tente ajustar a pesquisa ou cadastre um novo item.</div>
    </c:if>

    <div class="grid">
        <c:forEach var="item" items="${itens}">
            <div class="card">
                <div>
                    <span class="badge badge-${item.tipo}"><c:out value="${item.tipo}"/></span>
                </div>
                <h3><c:out value="${item.titulo}"/></h3>
                <div class="meta">
                    <span><strong>Gênero:</strong> <c:out value="${item.genero}"/></span><br/>
                    <span><strong>Ano:</strong> <c:out value="${item.anoLancamento}"/></span>
                    <span><strong>Avaliação:</strong>
                        <c:choose>
                            <c:when test="${item.avaliacao != null}">
                                <c:out value="${item.avaliacao}"/> / 10
                            </c:when>
                            <c:otherwise>-</c:otherwise>
                        </c:choose>
                    </span>
                </div>
                <c:if test="${not empty item.descricao}">
                    <p style="font-size:0.88rem; color:#444;">
                        <c:out value="${item.descricao.length() > 100 ? item.descricao.substring(0,100).concat('...') : item.descricao}"/>
                    </p>
                </c:if>
                <div class="card-actions">
                    <a class="btn btn-sm btn-info" href="${pageContext.request.contextPath}/detalhes?id=${item.id}">Detalhes</a>
                    <a class="btn btn-sm btn-warning" href="${pageContext.request.contextPath}/editar?id=${item.id}">Editar</a>
                    <form method="post" action="${pageContext.request.contextPath}/excluir" style="display:inline;" onsubmit="return confirm('Deseja realmente excluir &quot;<c:out value='${item.titulo}'/>&quot;?');">
                        <input type="hidden" name="id" value="${item.id}"/>
                        <button type="submit" class="btn btn-sm btn-danger">Excluir</button>
                    </form>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
<footer>Projeto acadêmico - CRUD didático</footer>
</body>
</html>
