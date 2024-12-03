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
<jsp:include page="../layout/header.jsp" />
<div class="container mt-5">
    <h2 class="mb-4">Paramètres </h2>
    <jsp:include page="../layout/errorMessage.jsp" />

    <!-- Formulaire pour changer de mot de passe -->
    <h4>Modifier votre mot de passe.</h4>
    <form action="${pageContext.request.contextPath}/settings/changePassword" method="post">
        <!-- attribut caché -->

        <!-- NEW MDP -->
        <div class="mb-3">
            <label for="old-password" class="form-label">Ancien mot de passe</label>
            <input type="password" class="form-control" id="old-password" name="oldPassword" required>
        </div>

        <!-- OLD MDP -->
        <div class="mb-3">
            <label for="new-password" class="form-label">Nouveau mot de passe</label>
            <input type="password" class="form-control" id="new-password" name="newPassword" required>
        </div>

        <!-- Boutons -->
        <button type="submit" class="btn btn-primary">Modifier</button>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<jsp:include page="../layout/footer.jsp" />
</body>
</html>