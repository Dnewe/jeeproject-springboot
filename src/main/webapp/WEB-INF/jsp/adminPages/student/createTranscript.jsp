<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Créer Cours</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="../../layout/header.jsp" />
<div class="container mt-5">
  <h2>Créer Relevé de note pour ${student.firstName} ${student.lastName}.</h2>
  <jsp:include page="../../layout/errorMessage.jsp" />

  <form action="${pageContext.request.contextPath}/transcript" method="post">
    <!-- param caché -->
    <input type="hidden" name="studentId" value="${student.id}">
    <input type="hidden" name="resultPage" value="student?action=details&studentId=${student.id}">

    <c:forEach var="entry" items="${resultsByCourse}">
      <h4>${entry.key.name}</h4>

      <table class="table table-striped table-bordered">
        <thead>
        <tr>
          <th>Evaluation</th>
          <th>Date</th>
          <th>Coefficient</th>
          <th>Note</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="result" items="${entry.value}">
          <tr>
            <td>${result.assessmentName}</td>
            <td><fmt:formatDate value="${result.entryDate}" pattern="dd/MM/yyyy"/></td>
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

    <!-- Appréciation -->
    <div class="mb-3">
      <label for="comment" class="form-label">Appréciation</label>
      <input type="text" class="form-control" id="comment" name="comment" required>
    </div>

    <!-- Boutons -->
    <a href="${pageContext.request.contextPath}/student?action=details&studentId=${student.id}" class="btn btn-danger">Annuler</a>
    <button type="submit" class="btn btn-primary">Télécharger</button>
  </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
<jsp:include page="../../layout/footer.jsp" />
</html>