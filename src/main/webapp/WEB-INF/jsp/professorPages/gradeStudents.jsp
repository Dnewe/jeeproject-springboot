<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nouvelle évaluation pour ${course.name}</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="Détail du cours" />
</jsp:include>

<div class="container mt-4">
    <h2>Nouvelle évaluation pour le cours ${course.name}</h2>

    <h2>Informations générales</h2>
    <form action="${pageContext.request.contextPath}/result" method="post">
        <!-- Attributs cachés -->
        <input type="hidden" name="action" value="createMultiple">
        <input type="hidden" name="resultPage" value="student?action=courseList&courseId=${course.id}">
        <input type="hidden" name="courseId" value="${course.id}">

        <!-- Nom de l'évaluation -->
        <div class="mb-3">
            <label for="assessment-name" class="form-label">Nom de l'évaluation</label>
            <input type="text" class="form-control" id="assessment-name" name="assessmentName" required>
        </div>
        <br>
        <!-- Note maximale -->
        <div class="mb-3">
            <label for="max-score" class="form-label">Note maximale</label>
            <input type="number" step="1" class="form-control" id="max-score" name="maxScore" required>
            <label for="weight" class="form-label">Coefficient</label>
            <input type="number" step="0.01" class="form-control" id="weight" name="weight" required>
        </div>
        <br>

        <!-- Tableau des étudiants -->
        <h3>Liste des étudiants</h3>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>Prénom</th>
                <th>Nom</th>
                <th>Note</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="student" items="${students}">
                <tr>
                    <td>${student.firstName}</td>
                    <td>${student.lastName}</td>
                    <td>
                        <input type="hidden" name="studentId" value="${student.id}">
                        <input type="number" step="0.01" class="form-control" name="grades[${student.id}]" placeholder="Entrez la note">
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <!-- Boutons -->
        <a href="student?action=courseList&courseId=${course.id}" class="btn btn-danger">Annuler</a>
        <button type="submit" class="btn btn-primary">Sauvegarder</button>
    </form>
</div>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<jsp:include page="../layout/footer.jsp" />
</body>
</html>