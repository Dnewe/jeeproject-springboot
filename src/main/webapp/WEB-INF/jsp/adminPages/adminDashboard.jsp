<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/src/main/webapp/WEB-INF/jsp/layout/header.jsp">
    <jsp:param name="pageTitle" value="Tableau de Bord Administrateur" />
</jsp:include>

<div class="container admin-dashboard">
    <h2>Bienvenue sur l'espace Administrateur.</h2>
    <jsp:include page="/src/main/webapp/WEB-INF/jsp/layout/errorMessage.jsp" />
    <h4>Que voulez-vous faire ?</h4>

    <div class="mt-3">
        <a href="user?action=list" class="btn btn-primary">Gérer les utilisateurs</a>
        <a href="professor?action=list" class="btn btn-primary">Gérer les professeurs</a>
        <a href="student?action=list" class="btn btn-primary">Gérer les étudiants</a>
        <a href="course?action=list" class="btn btn-primary">Gérer les cours</a>
    </div>
</div>

<jsp:include page="/src/main/webapp/WEB-INF/jsp/layout/footer.jsp" />
