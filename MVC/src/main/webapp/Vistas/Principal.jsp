<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
    <c:when test="${not empty sessionScope.idUsuario and sessionScope.rolUsuario == 'ADMINISTRADOR'}">
        <c:redirect url="/Vistas/OpcionesAdmin.jsp"/>
    </c:when>
    <c:when test="${not empty sessionScope.idUsuario}">
        <c:redirect url="/Vistas/OpcionesUsuario/Panel.jsp"/>
    </c:when>
    <c:otherwise>
        <!DOCTYPE html>
        <html lang="es">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Iniciar sesion - Contas Portal</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
            <link rel="preconnect" href="https://fonts.googleapis.com">
            <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
            <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/custom.css?v=<%= System.currentTimeMillis() %>">
        </head>
        <body>
        <div class="login-fondo">
            <div class="card shadow" style="width:100%; max-width:400px;">
                <div class="card-body p-4">
                    <h1 class="h3 text-center mb-1">Contas Portal</h1>
                    <p class="text-secondary text-center small mb-4">Ingrese sus datos para acceder a su cuenta</p>

                    <c:if test="${not empty requestScope.error}">
                        <div class="alert alert-danger" role="alert">&#9888; ${requestScope.error}</div>
                    </c:if>

                    <form method="get" action="${pageContext.request.contextPath}/login">
                        <div class="mb-3">
                            <label for="usuario" class="form-label">Usuario</label>
                            <input type="text" class="form-control" id="usuario" name="usuario" required autofocus>
                        </div>
                        <div class="mb-3">
                            <label for="contrasenia" class="form-label">Contraseña</label>
                            <input type="password" class="form-control" id="contrasenia" name="contrasenia" required>
                        </div>
                        <div class="form-check mb-3">
                            <input type="checkbox" class="form-check-input" id="recordarme" name="recordarme">
                            <label class="form-check-label" for="recordarme">Recordarme en este dispositivo</label>
                        </div>
                        <button type="submit" class="btn btn-primary w-100" name="action" value="login">Iniciar sesion</button>
                    </form>

                    <p class="text-secondary small text-center mt-4 mb-0">
                        &iquest;Problemas para ingresar? Comuniquese con su contador en Lofrano Sanchez Estudio Contable.
                    </p>
                </div>
            </div>
        </div>
        </body>
        </html>
    </c:otherwise>
</c:choose>
