<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

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
