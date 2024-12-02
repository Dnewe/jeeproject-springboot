<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>



<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des Résultats</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="Liste des cours" />
</jsp:include>

<div class="container">
    <h2 class="mb-4">Liste des Cours assignés</h2>
    <jsp:include page="../layout/errorMessage.jsp" />

    <table class="table table-striped">
        <thead>
        <tr>
            <th>#</th>
            <th>Nom</th>
            <th>Description</th>
            <th>Professeur</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="course" items="${courses}">
            <tr>
                <td>${course.id}</td>
                <td>${course.name}</td>
                <td>${course.description}</td>
                <td>${course.professor != null ? course.professor.firstName: ""} ${course.professor != null ? " ": "Aucun"} ${course.professor != null ? course.professor.lastName: ""}</td>
                <td><a href="student?action=courseList&course-id=${course.id}" class="btn btn-primary">Liste étudiants</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <c:if test="${empty courses}">
        <p class="text-center">Aucun cours trouvé.</p>
    </c:if>
</div>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<jsp:include page="../layout/footer.jsp" />
</body>
</html>