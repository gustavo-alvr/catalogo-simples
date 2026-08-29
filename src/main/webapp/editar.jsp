<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Item</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header>
    <h1>Editar Item</h1>
    <p><a href="${pageContext.request.contextPath}/catalogo" style="color:#fff; text-decoration:underline;">Voltar ao CatÃ¡logo</a></p>
</header>
<div class="container">
    <c:if test="${not empty erro}">
        <div class="alert alert-error"><c:out value="${erro}"/></div>
    </c:if>
    <c:if test="${empty item}">
        <div class="alert alert-error">Item nÃ£o encontrado.</div>
    </c:if>

    <c:if test="${not empty item}">
    <div class="form-card">
        <form method="post" action="${pageContext.request.contextPath}/editar">
            <input type="hidden" name="id" value="<c:out value='${item.id}'/>"/>
            <div class="form-group">
                <label for="titulo">TÃ­tulo *</label>
                <input type="text" id="titulo" name="titulo" required maxlength="255" value="<c:out value='${item.titulo}'/>"/>
            </div>
            <div class="form-group">
                <label for="tipo">Tipo *</label>
                <select id="tipo" name="tipo" required>
                    <option value="LIVRO" ${item.tipo == 'LIVRO' ? 'selected' : ''}>LIVRO</option>
                    <option value="SERIE" ${item.tipo == 'SERIE' ? 'selected' : ''}>SERIE</option>
                    <option value="FILME" ${item.tipo == 'FILME' ? 'selected' : ''}>FILME</option>
                </select>
            </div>
            <div class="form-group">
                <label for="genero">GÃªnero *</label>
                <input type="text" id="genero" name="genero" required maxlength="100" value="<c:out value='${item.genero}'/>"/>
            </div>
            <div class="form-group">
                <label for="anoLancamento">Ano de LanÃ§amento *</label>
                <input type="number" id="anoLancamento" name="anoLancamento" required min="1900" max="2100" value="<c:out value='${item.anoLancamento}'/>"/>
            </div>
            <div class="form-group">
                <label for="avaliacao">AvaliaÃ§Ã£o (0 a 10)</label>
                <input type="text" id="avaliacao" name="avaliacao" value="<c:out value='${item.avaliacao}'/>" pattern="^\d+([.,]\d)?$"/>
            </div>
            <div class="form-group">
                <label for="descricao">DescriÃ§Ã£o</label>
                <textarea id="descricao" name="descricao" rows="4" maxlength="5000"><c:out value="${item.descricao}"/></textarea>
            </div>
            <div style="display:flex; gap:10px;">
                <button type="submit" class="btn btn-primary">Atualizar</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/catalogo">Cancelar</a>
            </div>
        </form>
    </div>
    </c:if>
</div>
</body>
</html>

