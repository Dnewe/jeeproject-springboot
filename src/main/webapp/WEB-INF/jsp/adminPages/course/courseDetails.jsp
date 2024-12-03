<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Détails du Cours</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<%@ include file="../../layout/header.jsp" %>
    <div class="container mt-4">
        <h2>Détails du Cours</h2>

        <%@ include file="../../layout/errorMessage.jsp" %>

        <!-- Afficher les détails du cours -->
        <table class="table table-bordered">
            <tr>
                <th>Nom</th>
                <td>${course.name}</td>
            </tr>
            <tr>
                <th>Description</th>
                <td>${course.description}</td>
            </tr>
            <tr>
                <th>Professeur</th>
                <td>
                    <c:if test="${course.professor != null}">
                        ${course.professor.firstName} ${course.professor.lastName}
                    </c:if>
                    <c:if test="${course.professor == null}">
                        Aucun professeur assigné.
                    </c:if>
                    <form action="${pageContext.request.contextPath}/course" method="post" style="display: ${course.professor != null ? 'inline-block' : 'none'};">
                        <input type="hidden" name="action" value="removeProfessor">
                        <input type="hidden" name="resultPage" value="course?action=details&courseId=${course.id}">
                        <input type="hidden" name="courseId" value="${course.id}">
                        <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Êtes-vous sûr de vouloir retirer le professeur ?');">Supprimer</button>
                    </form>
                </td>
            </tr>
        </table>

        <!-- Formulaire pour retirer et assigner un professeur -->

        <h4>Assigner un professeur</h4>
        <form action="${pageContext.request.contextPath}/course" method="post" style="display: inline;">
            <input type="hidden" name="action" value="assignProfessor">
            <input type="hidden" name="resultPage" value="course?action=details&courseId=${course.id}">
            <input type="hidden" name="courseId" value="${course.id}">

            <div class="form-group">
                <label for="professor">Choisir un Professeur</label>
                <select name="professorId" id="professor" class="form-control">
                    <option value="">Sélectionner un professeur</option>
                    <c:forEach var="professor" items="${availableProfessors}">
                        <option value="${professor.id}">${professor.lastName} ${professor.firstName}</option>
                    </c:forEach>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Assigner</button>
        </form>

        <!-- Liste des étudiants inscrits -->
        <h4>Étudiants Inscrits</h4>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Nom</th>
                    <th>Contact</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="student" items="${enrolledStudents}">
                    <tr>
                        <td>${student.firstName} ${student.lastName}</td>
                        <td>${student.user.email}</td>
                        <td>
                            <form action="enrollment" method="post" style="display: inline-block;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="resultPage" value="course?action=details&courseId=${course.id}">
                                <input type="hidden" name="courseId" value="${course.id}">
                                <input type="hidden" name="studentId" value="${student.id}">
                                <button type="submit" class="btn btn-danger btn-sm">Supprimer</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <!-- Formulaire pour ajouter un étudiant -->
        <h4>Ajouter un Étudiant</h4>
        <form action="enrollment" method="post">
            <input type="hidden" name="action" value="create">
            <input type="hidden" name="resultPage" value="course?action=details&courseId=${course.id}">
            <input type="hidden" name="courseId" value="${course.id}">
            
            <div class="form-group">
                <label for="student">Choisir un Étudiant</label>
                <select name="studentId" id="student" class="form-control">
                    <option value="">Sélectionner un étudiant</option>
                    <c:forEach var="student" items="${availableStudents}">
                        <option value="${student.id}">${student.firstName} ${student.lastName}</option>
                    </c:forEach>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Ajouter</button>
        </form>

        <a href="${pageContext.request.contextPath}/course?action=updateForm&courseId=${course.id}" class="btn btn-warning">Modifier</a>
        <!-- Formulaire pour la suppression en POST -->
        <form action="${pageContext.request.contextPath}/course" method="post" style="display: inline;">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="courseId" value="${course.id}">
            <button type="submit" class="btn btn-danger" onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce cours ?');">Supprimer</button>
        </form>
        <a href="course?action=list" class="btn btn-secondary">Retour à la liste des cours</a>
    </div>

    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
<%@ include file="../../layout/footer.jsp" %>
</html>