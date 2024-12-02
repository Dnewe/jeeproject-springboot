<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="false" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <title>Cy-Tech Gestion</title>
</head>
<body>
<nav class="navbar navbar-expand-lg header-custom">
    <div class="container-fluid">
        <a class="navbar-brand" href="#">
            <img src="${pageContext.request.contextPath}/img/logo.png" alt="Logo" width="75" height="50" class="d-inline-block align-text-top logo">
            <span class="brand-text">Cy-Tech Gestion</span>
            <a href="home" class="btn btn-primary">Accueil</a>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/settings?action=settingsForm" class="btn btn-primary">Paramètres</a>
                    <form action="logout" method="post" style="display: inline;">
                        <button type="submit" class="btn btn-dark logout-btn">Déconnexion</button>
                    </form>
                </li>
            </ul>
        </div>
    </div>
</nav>
</body>
</html>