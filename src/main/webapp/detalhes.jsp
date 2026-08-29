<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalhes do Item</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header>
    <h1>Detalhes do Item</h1>
    <p><a href="${pageContext.request.contextPath}/catalogo" style="color:#fff; text-decoration:underline;">Voltar ao Catálogo</a></p>
</header>
<div class="container">
    <c:if test="${empty item}">
        <div class="alert alert-error">Item não encontrado.</div>
    </c:if>

    <c:if test="${not empty item}">
        <div class="details-card">
            <h2><c:out value="${item.titulo}"/> <span class="badge badge-${item.tipo}"><c:out value="${item.tipo}"/></span></h2>
            <dl>
                <dt>ID</dt><dd><c:out value="${item.id}"/></dd>
                <dt>Título</dt><dd><c:out value="${item.titulo}"/></dd>
                <dt>Tipo</dt><dd><c:out value="${item.tipo}"/></dd>
                <dt>Gênero</dt><dd><c:out value="${item.genero}"/></dd>
                <dt>Ano</dt><dd><c:out value="${item.anoLancamento}"/></dd>
                <dt>Avaliação</dt>
                <dd>
                    <c:choose>
                        <c:when test="${item.avaliacao != null}"><c:out value="${item.avaliacao}"/> / 10</c:when>
                        <c:otherwise>-</c:otherwise>
                    </c:choose>
                </dd>
                <dt>Descrição</dt>
                <dd>
                    <c:choose>
                        <c:when test="${not empty item.descricao}"><c:out value="${item.descricao}"/></c:when>
                        <c:otherwise><em>Sem descrição</em></c:otherwise>
                    </c:choose>
                </dd>
                <dt>Data Cadastro</dt><dd><c:out value="${item.dataCadastro}"/></dd>
            </dl>
            <div style="margin-top:16px; display:flex; gap:10px;">
                <a class="btn btn-warning" href="${pageContext.request.contextPath}/editar?id=${item.id}">Editar</a>
                <form method="post" action="${pageContext.request.contextPath}/excluir" onsubmit="return confirm('Deseja realmente excluir?');">
                    <input type="hidden" name="id" value="${item.id}"/>
                    <button type="submit" class="btn btn-danger">Excluir</button>
                </form>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/catalogo">Voltar</a>
            </div>
        </div>
    </c:if>
</div>
</body>
</html>
