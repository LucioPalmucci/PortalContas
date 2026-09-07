<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:if test="${not empty tituloPagina}">${tituloPagina} - </c:if>Contas Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/custom.css?v=<%= System.currentTimeMillis() %>">
</head>
<body class="d-flex flex-column min-vh-100">
<div id="barraCarga" class="barra-carga"></div>

<c:if test="${not empty sessionScope.idUsuario}">
    <nav class="navbar bg-body-tertiary d-print-none sticky-top">
        <div class="container-fluid px-3">
            <button class="btn btn-outline-secondary" type="button" data-bs-toggle="offcanvas" data-bs-target="#sidebarMenu" aria-controls="sidebarMenu" aria-label="Abrir menu">
                <i class="bi bi-list" style="font-size:1.2rem;" aria-hidden="true"></i>
            </button>
            <a class="navbar-brand fw-bold ms-2 me-auto" href="${pageContext.request.contextPath}/Vistas/Principal.jsp">Contas Portal</a>
        </div>
    </nav>

    <div class="offcanvas offcanvas-start" tabindex="-1" id="sidebarMenu" aria-labelledby="sidebarMenuLabel">
        <div class="offcanvas-header border-bottom">
            <div class="d-flex align-items-center gap-2">
                <i class="bi bi-person-circle fs-4 text-primary" aria-hidden="true"></i>
                <div>
                    <h5 class="offcanvas-title fw-bold mb-0" id="sidebarMenuLabel">${sessionScope.nombreUsuarioSesion}</h5>
                    <small class="text-secondary">${sessionScope.rolUsuario == 'ADMINISTRADOR' ? 'Administrador' : 'Usuario'}</small>
                </div>
            </div>
            <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Cerrar"></button>
        </div>
        <div class="offcanvas-body p-0 d-flex flex-column">
            <div class="flex-grow-1 py-2">
                <c:choose>
                    <c:when test="${sessionScope.rolUsuario == 'ADMINISTRADOR'}">
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Vistas/OpcionesAdmin.jsp"><i class="bi bi-house-door text-secondary" aria-hidden="true"></i>Panel</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Usuario"><i class="bi bi-people texto-serie-1" aria-hidden="true"></i>Usuarios</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Vencimiento"><i class="bi bi-calendar-event texto-serie-2" aria-hidden="true"></i>Vencimientos</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Catalogo"><i class="bi bi-tags texto-serie-7" aria-hidden="true"></i>Categorias y medios</a>
                        <hr class="my-2">
                        <div class="px-3 text-secondary small text-uppercase fw-bold mb-1">Movimientos de usuarios</div>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Venta"><i class="bi bi-cart-check texto-serie-5" aria-hidden="true"></i>Ventas</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Compra"><i class="bi bi-bag texto-serie-1" aria-hidden="true"></i>Compras</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Gasto"><i class="bi bi-receipt texto-serie-3" aria-hidden="true"></i>Gastos</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/OtroIngreso"><i class="bi bi-plus-circle texto-serie-4" aria-hidden="true"></i>Otros ingresos</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/OtroEgreso"><i class="bi bi-dash-circle texto-serie-2" aria-hidden="true"></i>Otros egresos</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Tesoreria"><i class="bi bi-wallet2 texto-serie-7" aria-hidden="true"></i>Tesoreria</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/EstadoResultados"><i class="bi bi-bar-chart texto-serie-6" aria-hidden="true"></i>Estado de resultados</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/EstadoFinanciero"><i class="bi bi-graph-up texto-serie-8" aria-hidden="true"></i>Estado financiero</a>
                    </c:when>
                    <c:otherwise>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Vistas/OpcionesUsuario/Panel.jsp"><i class="bi bi-house-door text-secondary" aria-hidden="true"></i>Inicio</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Venta"><i class="bi bi-cart-check texto-serie-5" aria-hidden="true"></i>Ventas</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Compra"><i class="bi bi-bag texto-serie-1" aria-hidden="true"></i>Compras</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Gasto"><i class="bi bi-receipt texto-serie-3" aria-hidden="true"></i>Gastos</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/OtroIngreso"><i class="bi bi-plus-circle texto-serie-4" aria-hidden="true"></i>Otros ingresos</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/OtroEgreso"><i class="bi bi-dash-circle texto-serie-2" aria-hidden="true"></i>Otros egresos</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Tesoreria"><i class="bi bi-wallet2 texto-serie-7" aria-hidden="true"></i>Tesoreria</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/EstadoResultados"><i class="bi bi-bar-chart texto-serie-6" aria-hidden="true"></i>Estado de resultados</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/EstadoFinanciero"><i class="bi bi-graph-up texto-serie-8" aria-hidden="true"></i>Estado financiero</a>
                        <a class="menu-lateral-link" href="${pageContext.request.contextPath}/Soporte"><i class="bi bi-question-circle text-secondary" aria-hidden="true"></i>Soporte</a>
                    </c:otherwise>
                </c:choose>
            </div>
            <a class="menu-lateral-link text-danger border-top" href="${pageContext.request.contextPath}/login?action=logout"><i class="bi bi-box-arrow-right" aria-hidden="true"></i>Cerrar sesion</a>
        </div>
    </div>
</c:if>

<main class="flex-grow-1 py-4 d-flex flex-column justify-content-center">
    <div class="container">
