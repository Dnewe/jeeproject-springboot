<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des étudiants</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="Détail du cours" />
</jsp:include>

<div class="container mt-4">
    <h2>Notes de l'étudiant ${student.firstName} ${student.lastName} pour le cours ${course.name}</h2>
    <jsp:include page="../layout/errorMessage.jsp" />

    <div class="card mt-4">
        <div class="card-header">
            Informations sur l'étudiant
        </div>
        <div class="card-body">
            <p><strong>Nom :</strong> ${student.lastName}</p>
            <p><strong>Prénom :</strong> ${student.firstName}</p>
            <p><strong>Contact :</strong> ${student.user.email}</p>
            <p><strong>Date de naissance :</strong> ${student.dateOfBirth}</p>
        </div>
    </div>

    <!-- Liste des étudiants inscrits avec moyenne -->
    <table class="table table-striped table-bordered">
        <thead>
        <tr>
            <th>Evaluation</th>
            <th>Date</th>
            <th>Coefficient</th>
            <th>Note</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="result" items="${results}">
            <tr>
                <td>${result.assessmentName}</td>
                <td>${result.entryDate}</td>
                <td>${result.weight}</td>
                <td>${result.grade} / ${result.maxScore}</td>
                <td><a href="${pageContext.request.contextPath}/result?action=updateForm&resultId=${result.id}&courseId=${course.id}&studentId=${student.id}" class="btn btn-warning btn-sm">Modifier</a>
                <form action="${pageContext.request.contextPath}/result" method="post" style="display: inline;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="resultPage" value="result?action=studentDetails&courseId=${course.id}&studentId=${student.id}">
                    <input type="hidden" name="resultId" value="${result.id}">
                    <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette note ?');">Supprimer</button>
                </form></td>
            </tr>
        </c:forEach>

        <tr class="table-success">
            <td><strong>Moyenne</strong></td>
            <td></td>
            <td></td>
            <td><strong>${average} / 20</strong></td>
        </tr>
        </tbody>
    </table>

    <a href="result?action=createForm&studentId=${student.id}&courseId=${course.id}" class="btn btn-success">Ajouter une note</a>
    <br><br>

    <a href="student?action=courseList&courseId=${course.id}" class="btn btn-secondary">Retour à la liste des étudiants</a>
</div>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<jsp:include page="../layout/footer.jsp" />
</body>
</html>