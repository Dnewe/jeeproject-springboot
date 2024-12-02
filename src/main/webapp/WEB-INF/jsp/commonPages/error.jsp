<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Changement de mot de passe</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">

    <div class="alert alert-danger">
        ERREUR
        <% if (request.getAttribute("errorMessage") != null) {%>
        <p style="color:red;"><%=request.getAttribute("errorMessage")%></p>
        <% } %>
        <% if (request.getAttribute("errorMessage") == null) {%>
        <p style="color:red;">erreur inconnue</p>
        <% } %>
    </div>

    <a href="home" class="btn btn-primary">Revenir à l'Accueil</a>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <jsp:include page="../layout/footer.jsp" />
</body>
</html>
