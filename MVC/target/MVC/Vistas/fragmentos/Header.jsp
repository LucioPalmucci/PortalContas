<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:if test="${not empty tituloPagina}">${tituloPagina} - </c:if>Contas Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/bootstrap.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/custom.css?v=<%= System.currentTimeMillis() %>">
</head>
<body class="d-flex flex-column min-vh-100">

<c:if test="${not empty sessionScope.idUsuario}">
    <nav class="navbar bg-body-tertiary border-bottom d-print-none sticky-top">
        <div class="container-fluid px-3">
            <button class="btn btn-outline-secondary" type="button" data-bs-toggle="offcanvas" data-bs-target="#sidebarMenu" aria-controls="sidebarMenu" aria-label="Abrir menu">
                <span class="navbar-toggler-icon"></span>
            </button>
            <a class="navbar-brand fw-bold ms-2 me-auto" href="${pageContext.request.contextPath}/Vistas/Principal.jsp">Contas Portal</a>
            <span class="text-secondary small d-none d-sm-inline">${sessionScope.nombreUsuarioSesion}</span>
        </div>
    </nav>

    <div class="offcanvas offcanvas-start" tabindex="-1" id="sidebarMenu" aria-labelledby="sidebarMenuLabel">
        <div class="offcanvas-header">
            <h5 class="offcanvas-title fw-bold" id="sidebarMenuLabel">Menu</h5>
            <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Cerrar"></button>
        </div>
        <div class="offcanvas-body p-0 d-flex flex-column">
            <div class="flex-grow-1">
                <c:choose>
                    <c:when test="${sessionScope.rolUsuario == 'ADMINISTRADOR'}">
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Vistas/OpcionesAdmin.jsp">Panel</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Usuario">Usuarios</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Vencimiento">Vencimientos</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Catalogo">Categorias y medios</a>
                    </c:when>
                    <c:otherwise>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Vistas/OpcionesUsuario/Panel.jsp">Inicio</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Venta">Ventas</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Compra">Compras</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Gasto">Gastos</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/OtroIngreso">Otros ingresos</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/OtroEgreso">Otros egresos</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Tesoreria">Tesoreria</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/EstadoResultados">Estado de resultados</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/EstadoFinanciero">Estado financiero</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Soporte">Soporte</a>
                    </c:otherwise>
                </c:choose>
            </div>
            <a class="menu-lateral-link text-danger" href="${pageContext.request.contextPath}/login?action=logout">Cerrar sesion</a>
        </div>
    </div>
</c:if>

<main class="flex-grow-1 py-4">
    <div class="container">
