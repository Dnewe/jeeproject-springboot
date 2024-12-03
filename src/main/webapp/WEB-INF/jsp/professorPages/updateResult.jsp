<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modifier Étudiant</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="../layout/header.jsp" />
<div class="container mt-5">
    <h2>Modifier Résultat</h2>
    <form action="${pageContext.request.contextPath}/result" method="post">
        <!-- attributs caché -->
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="resultId" value="${result.id}">
        <input type="hidden" name="resultPage" value="result?action=studentDetails&courseId=${course.id}&studentId=${student.id}">

        <!-- Nom -->
        <div class="mb-3">
            <label for="assessment-name" class="form-label">Nom de l'évaluation</label>
            <input type="text" class="form-control" id="assessment-name" name="assessmentName" value="${result.assessmentName}" required>
        </div>
        <br>
        <!-- Note -->
        <div class="mb-3">
            <label for="grade" class="form-label">Note</label>
            <input type="number" step="0.01" class="form-control" id="grade" name="grade" value="${result.grade}" required>
            <label for="max-score"  class="form-label">Note maximale</label>
            <input type="number" step="1" class="form-control" id="max-score" name="maxScore" value="${result.maxScore}" required>
            <label for="weight" class="form-label">Coefficient</label>
            <input type="number" step="0.01" class="form-control" id="weight" name="weight" value="${result.weight}" required>
        </div>

        <!-- Boutons -->
        <a href="${pageContext.request.contextPath}/result?action=studentDetails&courseId=${course.id}&studentId=${student.id}" class="btn btn-danger">Annuler</a>
        <button type="submit" class="btn btn-primary">Sauvegarder</button>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
<jsp:include page="../layout/footer.jsp" />
</html>