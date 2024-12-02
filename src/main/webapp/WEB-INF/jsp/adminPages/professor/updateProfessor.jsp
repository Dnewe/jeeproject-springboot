
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modifier Professeur</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="../../layout/header.jsp" />
<div class="container mt-5">
    <h2>Modifier Professeur</h2>
    <jsp:include page="../../layout/errorMessage.jsp" />

    <form action="${pageContext.request.contextPath}/professor?action=update" method="post">
        <!-- ID professeur caché -->
        <input type="hidden" name="professor-id" value="${professor.id}">
        <input type="hidden" name="result-page" value="professor?action=details&professor-id=${professor.id}">

        <!-- Nom -->
        <div class="mb-3">
            <label for="last-name" class="form-label">Nom</label>
            <input type="text" class="form-control" id="last-name" name="last-name" value="${professor.lastName}" required>
        </div>

        <!-- Prénom -->
        <div class="mb-3">
            <label for="first-name" class="form-label">Prénom</label>
            <input type="text" class="form-control" id="first-name" name="first-name" value="${professor.firstName}" required>
        </div>

        <!-- Boutons -->
        <a href="${pageContext.request.contextPath}/professor?action=details&professor-id=${professor.id}" class="btn btn-danger">Annuler</a>
        <button type="submit" class="btn btn-primary">Sauvegarder</button>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
<jsp:include page="../../layout/footer.jsp" />
</html>