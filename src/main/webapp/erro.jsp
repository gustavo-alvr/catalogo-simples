<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isErrorPage="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Erro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header>
    <h1>Ops! Algo deu errado</h1>
</header>
<div class="container">
    <div class="alert alert-error">
        <c:choose>
            <c:when test="${not empty mensagemErro}">
                <c:out value="${mensagemErro}"/>
            </c:when>
            <c:otherwise>
                NÃ£o foi possÃ­vel processar sua solicitaÃ§Ã£o. Tente novamente.
            </c:otherwise>
        </c:choose>
    </div>
    <p>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/catalogo">Voltar ao CatÃ¡logo</a>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/">InÃ­cio</a>
    </p>
</div>
</body>
</html>

