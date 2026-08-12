<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="edu.usal.jdbc.servicio.UsuarioServicio" %>
<%@ page import="edu.usal.jdbc.servicio.VencimientoServicio" %>
<%@ page import="edu.usal.jdbc.dominio.Usuario" %>
<%@ page import="edu.usal.jdbc.dominio.Vencimiento" %>
<%@ page import="java.util.List" %>
<c:choose>
    <c:when test="${empty sessionScope.idUsuario or sessionScope.rolUsuario != 'ADMINISTRADOR'}">
        <c:redirect url="/Vistas/Principal.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tituloPagina" value="Panel de administracion" scope="request"/>
        <%@ include file="/Vistas/fragmentos/Header.jsp" %>

        <%
            List<Usuario> usuarios = new UsuarioServicio().obtenerTodosLosUsuarios();
            long activos = usuarios.stream().filter(Usuario::isEstaActivo).count();
            VencimientoServicio vencimientoServicio = new VencimientoServicio();
            List<Vencimiento> vencimientos = vencimientoServicio.obtenerTodosLosVencimientos();
            long pendientes = vencimientos.stream().filter(v -> v.getEstado().name().equals("PENDIENTE")).count();
            long proximos = vencimientos.stream().filter(vencimientoServicio::esProximo).count();
            request.setAttribute("totalUsuarios", usuarios.size());
            request.setAttribute("usuariosActivos", activos);
            request.setAttribute("vencimientosPendientes", pendientes);
            request.setAttribute("vencimientosProximos", proximos);
        %>

        <h1>Bienvenido, ${sessionScope.nombreUsuarioSesion}</h1>
        <p class="text-secondary">Panel de administracion del estudio.</p>

        <div class="row row-cols-1 row-cols-md-3 g-3 mb-4">
            <div class="col">
                <div class="card card-body h-100">
                    <div class="small text-secondary text-uppercase">Usuarios activos</div>
                    <div class="fs-3 fw-bold">${usuariosActivos} <span class="fs-6 text-secondary">/ ${totalUsuarios}</span></div>
                </div>
            </div>
            <div class="col">
                <div class="card card-body h-100">
                    <div class="small text-secondary text-uppercase">Vencimientos pendientes</div>
                    <div class="fs-3 fw-bold ${vencimientosPendientes > 0 ? 'text-danger' : ''}">${vencimientosPendientes}</div>
                </div>
            </div>
            <div class="col">
                <div class="card card-body h-100">
                    <div class="small text-secondary text-uppercase">Vencen en 7 dias o menos</div>
                    <div class="fs-3 fw-bold ${vencimientosProximos > 0 ? 'text-danger' : 'text-success'}">${vencimientosProximos}</div>
                </div>
            </div>
        </div>

        <div class="row row-cols-1 row-cols-md-3 g-3">
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/Usuario">
                    <div class="card-body">
                        <h3 class="h5">Usuarios</h3>
                        <p class="text-secondary mb-0">Crear, editar y activar o desactivar cuentas de clientes.</p>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/Vencimiento">
                    <div class="card-body">
                        <h3 class="h5">Calendario de vencimientos</h3>
                        <p class="text-secondary mb-0">Administrar fechas impositivas, importar y exportar el calendario.</p>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/Catalogo">
                    <div class="card-body">
                        <h3 class="h5">Categorias y medios</h3>
                        <p class="text-secondary mb-0">Configurar los medios de pago/cobro y las categorias de conceptos.</p>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
        </div>

        <%@ include file="/Vistas/fragmentos/Footer.jsp" %>
    </c:otherwise>
</c:choose>
