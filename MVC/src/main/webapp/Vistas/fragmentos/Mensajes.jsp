<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty requestScope.error}">
    <div class="alert alert-danger d-flex align-items-center" role="alert">
        <span class="me-2">&#9888;</span> ${requestScope.error}
    </div>
</c:if>
<c:if test="${not empty requestScope.mensaje}">
    <div class="alert alert-info d-flex align-items-center" role="alert">
        <span class="me-2">&#8505;</span> ${requestScope.mensaje}
    </div>
</c:if>
<c:if test="${not empty requestScope.exito}">
    <div class="alert alert-success d-flex align-items-center" role="alert">
        <span class="me-2">&#10003;</span> ${requestScope.exito}
    </div>
</c:if>
