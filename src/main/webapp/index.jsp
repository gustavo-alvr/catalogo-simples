<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CatÃ¡logo Simples - InÃ­cio</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header>
    <h1>CatÃ¡logo de Livros, SÃ©ries e Filmes</h1>
    <p>Gerencie seu acervo pessoal de forma simples e didÃ¡tica</p>
</header>
<div class="container">
    <nav class="actions" style="justify-content:center; gap:20px;">
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/catalogo">Ver CatÃ¡logo</a>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/cadastro">Cadastrar item</a>
    </nav>

    <div class="alert alert-info">
        <strong>Bem-vindo!</strong> Use o botÃ£o <em>Ver CatÃ¡logo</em> para listar, pesquisar por tÃ­tulo e filtrar por tipo (Livro, SÃ©rie, Filme).
    </div>

    <h2 style="margin-top:20px;">Funcionalidades</h2>
    <ul style="margin: 10px 20px;">
        <li>Cadastrar livros, sÃ©ries e filmes</li>
        <li>Visualizar todos os itens cadastrados</li>
        <li>Consultar detalhes, editar e excluir</li>
        <li>Pesquisar por tÃ­tulo e filtrar por tipo</li>
    </ul>
</div>
<footer>
    Projeto acadÃªmico - Java Servlets + JSP + JDBC + MySQL
</footer>
</body>
</html>

