<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Register</title>
    <jsp:include page="${pageContext.request.contextPath}/layout/errorMessage" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script>
        function toggleFields(role) {
            document.getElementById("studentFields").style.display = (role === "student") ? "block" : "none";
            document.getElementById("professorFields").style.display = (role === "professor") ? "block" : "none";
        }
    </script>
</head>
<body>
<%@ include file="../../layout/header.jsp" %>
<div class="container mt-5">
    <h2>Inscription Utilisateur</h2>
    <%@ include file="../../layout/errorMessage.jsp" %>


    <form action="${pageContext.request.contextPath}/register" method="post">
        <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input type="email" class="form-control" id="email" name="email" required>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Mot de passe</label>
            <input type="password" class="form-control" id="password" name="password" required>
        </div>
        <div class="mb-3">
            <label for="role" class="form-label">Rôle</label>
            <select class="form-select" id="role" name="role" onchange="toggleFields(this.value)" required>
                <option value="admin" >Administrateur</option>
                <option value="student" ${selectedRole == 'student' ? 'selected' : ''}>Étudiant</option>
                <option value="professor" ${selectedRole == 'professor' ? 'selected' : ''}>Enseignant</option>
            </select>
        </div>

        <div id="studentFields" style="display: ${selectedRole == 'student' ? 'block' : 'none'};">
            <div class="mb-3">
                <label for="student-last-name" class="form-label">Nom</label>
                <input type="text" class="form-control" id="student-last-name" name="studentLastName">
            </div>
            <div class="mb-3">
                <label for="student-first-name" class="form-label">Prénom</label>
                <input type="text" class="form-control" id="student-first-name" name="studentFirstName">
            </div>
            <div class="mb-3">
                <label for="student-date-of-birth" class="form-label">Date de naissance</label>
                <input type="date" class="form-control" id="student-date-of-birth" name="dateOfBirth">
            </div>
        </div>

        <div id="professorFields" style="display: ${selectedRole == 'professor' ? 'block' : 'none'};">
            <div class="mb-3">
                <label for="professor-last-name" class="form-label">Nom</label>
                <input type="text" class="form-control" id="professor-last-name" name="professorLastName">
            </div>
            <div class="mb-3">
                <label for="professor-first-name" class="form-label">Prénom</label>
                <input type="text" class="form-control" id="professor-first-name" name="professorFirstName">
            </div>
        </div>

        <a href="${pageContext.request.contextPath}/user?action=list" class="btn btn-danger">Annuler</a>
        <button type="submit" class="btn btn-primary">Enregistrer</button>
    </form>
</div>
<%@ include file="../../layout/footer.jsp" %>
</body>
</html>