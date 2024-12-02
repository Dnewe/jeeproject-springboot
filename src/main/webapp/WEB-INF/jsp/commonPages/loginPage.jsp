<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="login-page">
<div class="container">

    <!-- Messages d'erreur ou de succès -->
    <%@ include file="../layout/errorMessage.jsp" %>

    <h2>Connexion</h2><br><br>
    <form action="${pageContext.request.contextPath}/login" method="post">
        <label for="email">Email :</label>
        <input type="email" id="email" name="email" required><br><br>

        <label for="password">Mot de passe :</label>
        <input type="password" id="password" name="password" required><br><br>

        <button type="submit" class="btn btn-primary">Se connecter </button>
        <p class="error-message">
            <% if (request.getAttribute("error") != null) { %>
            <%= request.getAttribute("error") %>
            <% } %>
        </p>
    </form>
</div>
</body>
</html>
