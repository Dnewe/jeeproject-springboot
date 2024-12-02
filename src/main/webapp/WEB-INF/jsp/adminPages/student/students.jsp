<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des Étudiants</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="../../layout/header.jsp" />
<div class="container mt-5">
    <h2 class="mb-4">Gestion des Étudiants</h2>
    <jsp:include page="../../layout/errorMessage.jsp" />

    <form class="d-flex" action="${pageContext.request.contextPath}/student" method="get">
        <input type="hidden" name="action" value="list">
        <input class="form-control form-control-sm mr-sm-2" type="search" name="search" placeholder="Search" aria-label="Search"></input>
        <select class="form-select form-select-sm me-1" name="course-id">
            <option value="" ${empty filteredCourse ? 'selected' : ''}>Aucun filtre de cours</option>
            <c:forEach var="course" items="${courses}">
                <option value="${course.id}" ${filteredCourse != null && filteredCourse.id == course.id ? 'selected' : ''}>
                        ${course.name}
                </option>
            </c:forEach>
        </select>
        <button class="btn btn-primary" type="submit">Rechercher</button>
    </form>

    <!-- Messages d'erreur ou de succès -->
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">
            ${errorMessage}
        </div>
    </c:if>
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success">
            ${successMessage}
        </div>
    </c:if>

    <!-- Tableau des étudiants -->
    <table class="table table-striped">
        <thead class="table-light">
            <tr>
                <th>#</th>
                <th>Nom</th>
                <th>Prénom</th>
                <th>Contact</th>
                <th>Date de Naissance</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="student" items="${students}">
            <tr>
                <td>${student.id}</td>
                <td>${student.lastName}</td>
                <td>${student.firstName}</td>
                <td>${student.user.email}</td>
                <td><fmt:formatDate value="${student.dateOfBirth}" pattern="dd/MM/yyyy"/></td>
                <td>
                    <!-- Boutons actions -->
                    <a href="${pageContext.request.contextPath}/student?action=details&student-id=${student.id}" class="btn btn-info btn-sm">Détails</a>
                    <a href="${pageContext.request.contextPath}/student?action=updateForm&student-id=${student.id}" class="btn btn-warning btn-sm">Modifier</a>
                    <form action="${pageContext.request.contextPath}/student" method="post" style="display: inline;">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="student-id" value="${student.id}">
                        <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet étudiant ?');">Supprimer</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <!-- Si aucun étudiant n'est disponible -->
    <c:if test="${empty students}">
        <p class="text-center">Aucun étudiant trouvé.</p>
    </c:if>

    <a href="user?action=createForm&role=student" class="btn btn-success">Ajouter un Etudiant</a>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
<jsp:include page="../../layout/footer.jsp" />
</html>