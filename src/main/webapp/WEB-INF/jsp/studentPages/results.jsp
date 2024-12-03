<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

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
    <jsp:param name="pageTitle" value="Resultats" />
</jsp:include>

<div class="container">
    <h2 class="mb-4">Liste des Résultats</h2>
    <jsp:include page="../layout/errorMessage.jsp" />

    <c:forEach var="entry" items="${resultsByCourse}">
        <h4>${entry.key.name}</h4>

        <table class="table table-striped table-bordered">
            <thead>
            <tr>
                <th>Evaluation</th>
                <th>Date</th>
                <th>Coefficient</th>
                <th>Note</th>
                <th>Note</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="result" items="${entry.value}">
                <tr>
                    <td>${result.assessmentName}</td>
                    <td>${result.entryDate}"</td>
                    <td>${result.weight}</td>
                    <td>${result.grade} / ${result.maxScore}</td>
                </tr>
            </c:forEach>

            <tr class="table-success">
                <td><strong>Moyenne</strong></td>
                <td></td>
                <td></td>
                <td><strong>${averageByCourse[entry.key]} / 20</strong></td>
            </tr>
            </tbody>
        </table>
    </c:forEach>

    <c:if test="${empty resultsByCourse}">
        <p class="text-center">Aucun cours trouvé.</p>
    </c:if>
</div>


<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<jsp:include page="../layout/footer.jsp" />
</body>
</html>