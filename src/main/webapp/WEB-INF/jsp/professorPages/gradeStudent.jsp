<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
  <h2>Nouvelle notes pour ${student.firstName} ${student.lastName} en ${course.name}</h2>
  <br>
  <h4>Information de l'évaluation</h4>
  <form action="${pageContext.request.contextPath}/result" method="post" >
    <!-- attributs caché -->
    <input type="hidden" name="action" value="create">
    <input type="hidden" name="resultPage" value="result?action=studentDetails&courseId=${course.id}&studentId=${student.id}">
    <input type="hidden" name="studentId" value="${student.id}">
    <input type="hidden" name="courseId" value="${course.id}">

    <!-- Nom -->
    <div class="mb-3">
      <label for="assessment-name" class="form-label">Nom de l'évaluation</label>
      <input type="text" class="form-control" id="assessment-name" name="assessmentName" required>
    </div>
    <br>
    <!-- Note -->
    <div class="mb-3">
      <label for="grade" class="form-label">Note</label>
      <input type="number" step="0.01" class="form-control" id="grade" name="grade" required>
      <label for="max-score"  class="form-label">Note maximale</label>
      <input type="number" step="1" class="form-control" id="max-score" name="maxScore" required>
      <label for="weight" class="form-label">Coefficient</label>
      <input type="number" step="0.01" class="form-control" id="weight" name="weight" required>
    </div>

    <!-- Boutons -->
    <a href="${pageContext.request.contextPath}/result?action=studentDetails&courseId=${course.id}&studentId=${student.id}" class="btn btn-danger">Annuler</a>
    <button type="submit" class="btn btn-primary">Sauvegarder</button>
  </form>
</div>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<jsp:include page="../layout/footer.jsp" />
</body>
</html>