<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="edu.usal.jdbc.servicio.ReporteServicio" %>
<%@ page import="edu.usal.jdbc.dto.TesoreriaDTO" %>
<%@ page import="edu.usal.jdbc.dto.EstadoResultadosDTO" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="java.util.Date" %>
<c:choose>
    <c:when test="${empty sessionScope.idUsuario}">
        <c:redirect url="/Vistas/Principal.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tituloPagina" value="Inicio" scope="request"/>
        <%@ include file="/Vistas/fragmentos/Header.jsp" %>

        <%
            int idUsuario = (Integer) session.getAttribute("idUsuario");
            ReporteServicio reporteServicio = new ReporteServicio();
            TesoreriaDTO tesoreria = reporteServicio.obtenerTesoreria(idUsuario);
            LocalDate hoy = LocalDate.now();
            Date desdeMes = Date.from(hoy.withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date hastaHoy = Date.from(hoy.atStartOfDay(ZoneId.systemDefault()).toInstant());
            EstadoResultadosDTO estadoMes = reporteServicio.generarEstadoResultados(idUsuario, desdeMes, hastaHoy);
            request.setAttribute("tesoreria", tesoreria);
            request.setAttribute("estadoMes", estadoMes);
        %>

        <h1>Hola, ${sessionScope.nombreUsuarioSesion}</h1>
        <p class="text-secondary">Este es el resumen de su negocio.</p>

        <div class="row row-cols-1 row-cols-md-3 g-3 mb-4">
            <div class="col">
                <div class="card card-body h-100">
                    <div class="small text-secondary text-uppercase">Ganancia del mes</div>
                    <div class="fs-3 fw-bold ${estadoMes.ganancia >= 0 ? 'text-success' : 'text-danger'}"><fmt:formatNumber value="${estadoMes.ganancia}" type="currency" currencySymbol="$"/></div>
                </div>
            </div>
            <div class="col">
                <div class="card card-body h-100">
                    <div class="small text-secondary text-uppercase">Cuentas por cobrar</div>
                    <div class="fs-3 fw-bold text-success"><fmt:formatNumber value="${tesoreria.cuentasPorCobrar}" type="currency" currencySymbol="$"/></div>
                </div>
            </div>
            <div class="col">
                <div class="card card-body h-100">
                    <div class="small text-secondary text-uppercase">Cuentas por pagar</div>
                    <div class="fs-3 fw-bold text-danger"><fmt:formatNumber value="${tesoreria.cuentasPorPagar}" type="currency" currencySymbol="$"/></div>
                </div>
            </div>
        </div>

        <div class="row row-cols-1 row-cols-md-2 row-cols-lg-4 g-4">
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/Venta">
                    <div class="card-body">
                        <h3 class="h6">Ventas</h3>
                        <p class="text-secondary mb-0 small">Registrar y consultar ventas.</p>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/Compra">
                    <div class="card-body">
                        <h3 class="h6">Compras</h3>
                        <p class="text-secondary mb-0 small">Registrar y consultar compras.</p>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/Gasto">
                    <div class="card-body">
                        <h3 class="h6">Gastos</h3>
                        <p class="text-secondary mb-0 small">Alquiler, servicios, sueldos y mas.</p>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/OtroIngreso">
                    <div class="card-body">
                        <h3 class="h6">Otros ingresos</h3>
                        <p class="text-secondary mb-0 small">Ingresos que no son de una venta.</p>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/OtroEgreso">
                    <div class="card-body">
                        <h3 class="h6">Otros egresos</h3>
                        <p class="text-secondary mb-0 small">Salidas de dinero fuera de lo habitual.</p>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/Tesoreria">
                    <div class="card-body">
                        <h3 class="h6">Tesoreria</h3>
                        <p class="text-secondary mb-0 small">Lo que tiene pendiente de cobrar y pagar.</p>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/EstadoResultados">
                    <div class="card-body">
                        <h3 class="h6">Estado de resultados</h3>
                        <p class="text-secondary mb-0 small">Calcule su ganancia de un periodo.</p>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/EstadoFinanciero">
                    <div class="card-body">
                        <h3 class="h6">Estado financiero</h3>
                        <p class="text-secondary mb-0 small">Tendencias, alertas y comparaciones.</p>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
        </div>

        <%@ include file="/Vistas/fragmentos/Footer.jsp" %>
    </c:otherwise>
</c:choose>
