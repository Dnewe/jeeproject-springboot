<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="../layout/header.jsp">
    <jsp:param name="pageTitle" value="Tableau de Bord Etudiant" />
</jsp:include>

<div class="container admin-dashboard">
    <h2>Bienvenue sur votre espace enseignant, ${sessionScope.loggedProfessor.firstName} ${sessionScope.loggedProfessor.lastName}!</h2>
    <jsp:include page="../layout/errorMessage.jsp" />
    <h4>Que voulez-vous faire ?</h4>

    <div class="mt-3">
        <a href="course?action=professorList&professorId=${sessionScope.loggedProfessor.id}" class="btn btn-primary">Voir les cours</a>
    </div>
</div>

<jsp:include page="../layout/footer.jsp" />