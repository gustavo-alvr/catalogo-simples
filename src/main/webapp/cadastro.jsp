<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastrar Item</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header>
    <h1>Cadastrar Item</h1>
    <p><a href="${pageContext.request.contextPath}/catalogo" style="color:#fff; text-decoration:underline;">Voltar ao Catálogo</a></p>
</header>
<div class="container">
    <c:if test="${not empty erro}">
        <div class="alert alert-error"><c:out value="${erro}"/></div>
    </c:if>

    <div class="form-card">
        <form method="post" action="${pageContext.request.contextPath}/cadastro">
            <div class="form-group">
                <label for="titulo">Título *</label>
                <input type="text" id="titulo" name="titulo" required maxlength="255" value="<c:out value='${item.titulo}'/>" placeholder="Ex: Harry Potter"/>
            </div>
            <div class="form-group">
                <label for="tipo">Tipo *</label>
                <select id="tipo" name="tipo" required>
                    <option value="">Selecione</option>
                    <option value="LIVRO" ${item.tipo == 'LIVRO' ? 'selected' : ''}>LIVRO</option>
                    <option value="SERIE" ${item.tipo == 'SERIE' ? 'selected' : ''}>SERIE</option>
                    <option value="FILME" ${item.tipo == 'FILME' ? 'selected' : ''}>FILME</option>
                </select>
            </div>
            <div class="form-group">
                <label for="genero">Gênero *</label>
                <input type="text" id="genero" name="genero" required maxlength="100" value="<c:out value='${item.genero}'/>" placeholder="Ex: Fantasia"/>
            </div>
            <div class="form-group">
                <label for="anoLancamento">Ano de Lançamento *</label>
                <input type="number" id="anoLancamento" name="anoLancamento" required min="1900" max="2100" value="<c:out value='${item.anoLancamento}'/>" placeholder="Ex: 2023"/>
            </div>
            <div class="form-group">
                <label for="avaliacao">Avaliação (0 a 10)</label>
                <input type="text" id="avaliacao" name="avaliacao" value="<c:out value='${item.avaliacao}'/>" placeholder="Ex: 8.5" pattern="^\d+([.,]\d)?$" title="0 a 10, ex: 8.5"/>
                <small>Use ponto ou vírgula. Deixe em branco se não quiser avaliar.</small>
            </div>
            <div class="form-group">
                <label for="descricao">Descrição</label>
                <textarea id="descricao" name="descricao" rows="4" maxlength="5000" placeholder="Breve descrição"><c:out value="${item.descricao}"/></textarea>
            </div>
            <div style="display:flex; gap:10px;">
                <button type="submit" class="btn btn-primary">Salvar</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/catalogo">Cancelar</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
