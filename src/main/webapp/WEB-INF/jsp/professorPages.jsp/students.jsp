<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
    <h2>Liste des étudiants du Cours ${course.name}</h2>
    <jsp:include page="../layout/errorMessage.jsp" />

    <form class="d-flex" action="${pageContext.request.contextPath}/student" method="get">
        <input type="hidden" name="action" value="courseList">
        <input type="hidden" name="course-id" value="${course.id}">
        <input class="form-control form-control-sm mr-sm-2" type="search" name="search" placeholder="Search" aria-label="Search"></input>
        <button class="btn btn-primary" type="submit">Rechercher</button>
    </form>

    <!-- Liste des étudiants inscrits avec moyenne -->
    <table class="table table-striped table-bordered">
        <thead>
        <tr>
            <th>Nom</th>
            <th>Contact</th>
            <th>Moyenne</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="student" items="${students}">
            <tr>
                <td>${student.firstName} ${student.lastName}</td>
                <td>${student.user.email}</td>
                <td>${averageByStudent[student]} / 20</td>
                <td><a href="result?action=studentDetails&course-id=${course.id}&student-id=${student.id}" class="btn btn-primary btn-sm">Détails</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <c:if test="${empty students}">
        <p class="text-center">Aucun étudiant trouvé.</p>
    </c:if>

    <a href="result?action=createMultipleForm&course-id=${course.id}" class="btn btn-success">Ajouter une évaluation commune</a>
    <br><br><br>

    <a href="course?action=professorList&professor-id=${sessionScope.loggedProfessor.id}" class="btn btn-secondary">Retour à la liste des cours</a>
</div>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<jsp:include page="../layout/footer.jsp" />
</body>
</html>