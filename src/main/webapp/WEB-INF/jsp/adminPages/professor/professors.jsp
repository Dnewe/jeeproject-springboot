<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Gestion des Professeurs</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="../../layout/header.jsp" />
<div class="container mt-5">
    <h2 class="mb-4">Gestion des Professeurs</h2>
    <jsp:include page="../../layout/errorMessage.jsp" />

    <table class="table table-striped">
        <thead>
        <tr>
            <th>#</th>
            <th>Nom</th>
            <th>Prénom</th>
            <th>Contact</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="professor" items="${professors}">
            <tr>
                <td>${professor.id}</td>
                <td>${professor.lastName}</td>
                <td>${professor.firstName}</td>
                <td>${professor.user.email}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/professor?action=details&professor-id=${professor.id}" class="btn btn-info btn-sm">Détails</a>
                    <a href="${pageContext.request.contextPath}/professor?action=updateForm&professor-id=${professor.id}" class="btn btn-warning btn-sm">Modifier</a>
                    <!-- Formulaire pour la suppression en POST -->
                    <form action="${pageContext.request.contextPath}/professor" method="post" style="display: inline;">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="professor-id" value="${professor.id}">
                        <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce professeur ?');">Supprimer</button>
                    </form>

                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <c:if test="${empty professors}">
        <p class="text-center">Aucun professeur trouvé.</p>
    </c:if>

    <a href="user?action=createForm&role=professor" class="btn btn-success">Ajouter un Professeur</a>

</div>
</body>
<jsp:include page="/WEB-INF/util/footer.jsp" />
</html>