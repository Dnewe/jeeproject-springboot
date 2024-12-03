<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Détails de l'utilisateur</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="../../layout/header.jsp" />
    <div class="container">
        <h2 class="mt-4">Détails de l'Utilisateur</h2>
        <jsp:include page="../../layout/errorMessage.jsp" />

        <div class="card mt-4">
            <div class="card-header">
                Informations sur l'utilisateur
            </div>
            <div class="card-body">
                <p><strong>Email :</strong> ${user.email}</p>
                <p><strong>Rôle :</strong> ${user.role}</p>
                <p><strong>Nom :</strong> ${firstName}</p>
                <p><strong>Prénom :</strong> ${lastName}</p>
            </div>
        </div>

        <div class="mt-4">
            <a href="${pageContext.request.contextPath}/user?action=updateForm&userId=${user.id}" class="btn btn-warning">Modifier</a>
            <!-- Formulaire pour la suppression en POST -->
            <form action="${pageContext.request.contextPath}/user" method="post" style="display: inline;">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="userId" value="${user.id}">
                <button type="submit" class="btn btn-danger" onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?');">Supprimer</button>
            </form>
            <a href="${pageContext.request.contextPath}/user?action=list" class="btn btn-secondary">Retour à la liste</a>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
<jsp:include page="../../layout/footer.jsp" />
</html>
