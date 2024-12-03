<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des Cours</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="../../layout/header.jsp" />
    <div class="container">
        <h2 class="mb-4">Liste des Cours</h2>
        <jsp:include page="../../layout/errorMessage.jsp" />

        <table class="table table-striped">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Nom</th>
                    <th>Description</th>
                    <th>Professeur</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="course" items="${courses}">
                    <tr>
                        <td>${course.id}</td>
                        <td>${course.name}</td>
                        <td>${course.description}</td>
                        <td>${course.professor != null ? course.professor.firstName: ""} ${course.professor != null ? " ": "Aucun"} ${course.professor != null ? course.professor.lastName: ""}</td>
                        <td>
                            <!-- Détails du cours -->
                            <a href="course?action=details&courseId=${course.id}" class="btn btn-info">Détails</a>

                            <!-- Mise à jour du cours -->
                            <a href=${pageContext.request.contextPath}/course?action=updateForm&courseId=${course.id} class="btn btn-warning">Modifier</a>

                            <!-- Suppression du cours -->
                            <form action="course" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="courseId" value="${course.id}">
                                <button type="submit" class="btn btn-danger" onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce cours ?');">Supprimer</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <c:if test="${empty courses}">
            <p class="text-center">Aucun cours trouvé.</p>
        </c:if>

        <a href="course?action=createForm" class="btn btn-success">Ajouter un Cours</a>

    </div>

    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
<jsp:include page="../../layout/footer.jsp" />
</html>