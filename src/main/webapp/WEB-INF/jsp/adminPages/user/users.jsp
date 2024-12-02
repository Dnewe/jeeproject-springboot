<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des Utilisateurs</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="../../layout/header.jsp" />
<div class="container mt-5">
    <h2 class="mb-4">Gestion des Utilisateurs</h2>
    <jsp:include page="../../layout/errorMessage.jsp" />

    <!-- Tableau des utilisateurs -->
    <table class="table table-striped">
        <thead class="table-light">
            <tr>
                <th>#</th>
                <th>Email</th>
                <th>Rôle</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="user" items="${users}">
            <tr>
                <td>${user.id}</td>
                <td>${user.email}</td>
                <td>${user.role}</td>
                <td>
                    <!-- Boutons actions -->
                    <a href="${pageContext.request.contextPath}/user?action=details&userId=${user.id}" class="btn btn-info btn-sm">Détails</a>
                    <a href="${pageContext.request.contextPath}/user?action=updateForm&userId=${user.id}" class="btn btn-warning btn-sm">Modifier</a>
                    <form action="${pageContext.request.contextPath}/user" method="post" style="display: inline;">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="userId" value="${user.id}">
                        <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?');">Supprimer</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <!-- Si aucun utilisateur n'est disponible -->
    <c:if test="${empty users}">
        <p class="text-center">Aucun utilisateur trouvé.</p>
    </c:if>

    <a href="user?action=createForm" class="btn btn-success">Ajouter un Utilisateur</a>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
<jsp:include page="../../layout/footer.jsp" />
</html>