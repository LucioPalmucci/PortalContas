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

        <div class="panel-bienvenida d-flex align-items-center gap-3">
            <div class="icono-circulo text-secondary" style="width:52px;height:52px;font-size:1.5rem;">
                <i class="bi bi-house-door" aria-hidden="true"></i>
            </div>
            <div>
                <h1 class="h3 mb-1">Hola, ${sessionScope.nombreUsuarioSesion}</h1>
                <p class="mb-0">Este es el resumen de su negocio.</p>
            </div>
        </div>

        <div class="row row-cols-1 row-cols-md-3 g-4 mb-5">
            <div class="col">
                <div class="card card-body h-100 border-start border-4 ${estadoMes.ganancia >= 0 ? 'border-success' : 'border-danger'} d-flex flex-row align-items-center gap-3">
                    <div class="icono-circulo ${estadoMes.ganancia >= 0 ? 'bg-success-subtle text-success' : 'bg-danger-subtle text-danger'}">
                        <i class="bi bi-graph-up-arrow" aria-hidden="true"></i>
                    </div>
                    <div>
                        <div class="small text-secondary text-uppercase">Ganancia del mes</div>
                        <div class="fs-3 fw-bold ${estadoMes.ganancia >= 0 ? 'text-success' : 'text-danger'}"><fmt:formatNumber value="${estadoMes.ganancia}" type="currency" currencySymbol="$"/></div>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card card-body h-100 border-start border-4 border-success d-flex flex-row align-items-center gap-3">
                    <div class="icono-circulo bg-success-subtle text-success">
                        <i class="bi bi-cash-coin" aria-hidden="true"></i>
                    </div>
                    <div>
                        <div class="small text-secondary text-uppercase">Cuentas por cobrar</div>
                        <div class="fs-3 fw-bold text-success"><fmt:formatNumber value="${tesoreria.cuentasPorCobrar}" type="currency" currencySymbol="$"/></div>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card card-body h-100 border-start border-4 border-danger d-flex flex-row align-items-center gap-3">
                    <div class="icono-circulo bg-danger-subtle text-danger">
                        <i class="bi bi-credit-card" aria-hidden="true"></i>
                    </div>
                    <div>
                        <div class="small text-secondary text-uppercase">Cuentas por pagar</div>
                        <div class="fs-3 fw-bold text-danger"><fmt:formatNumber value="${tesoreria.cuentasPorPagar}" type="currency" currencySymbol="$"/></div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row row-cols-1 row-cols-md-2 row-cols-lg-4 g-4">
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/Venta">
                    <div class="card-body d-flex align-items-start gap-3">
                        <div class="icono-circulo icono-serie-5"><i class="bi bi-cart-check" aria-hidden="true"></i></div>
                        <div>
                            <h3 class="h6 mb-1">Ventas</h3>
                            <p class="text-secondary mb-0 small">Registrar y consultar ventas.</p>
                        </div>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/Compra">
                    <div class="card-body d-flex align-items-start gap-3">
                        <div class="icono-circulo icono-serie-1"><i class="bi bi-bag" aria-hidden="true"></i></div>
                        <div>
                            <h3 class="h6 mb-1">Compras</h3>
                            <p class="text-secondary mb-0 small">Registrar y consultar compras.</p>
                        </div>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/Gasto">
                    <div class="card-body d-flex align-items-start gap-3">
                        <div class="icono-circulo icono-serie-3"><i class="bi bi-receipt" aria-hidden="true"></i></div>
                        <div>
                            <h3 class="h6 mb-1">Gastos</h3>
                            <p class="text-secondary mb-0 small">Alquiler, servicios, sueldos y mas.</p>
                        </div>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/OtroIngreso">
                    <div class="card-body d-flex align-items-start gap-3">
                        <div class="icono-circulo icono-serie-4"><i class="bi bi-plus-circle" aria-hidden="true"></i></div>
                        <div>
                            <h3 class="h6 mb-1">Otros ingresos</h3>
                            <p class="text-secondary mb-0 small">Ingresos que no son de una venta.</p>
                        </div>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/OtroEgreso">
                    <div class="card-body d-flex align-items-start gap-3">
                        <div class="icono-circulo icono-serie-2"><i class="bi bi-dash-circle" aria-hidden="true"></i></div>
                        <div>
                            <h3 class="h6 mb-1">Otros egresos</h3>
                            <p class="text-secondary mb-0 small">Salidas de dinero fuera de lo habitual.</p>
                        </div>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/Tesoreria">
                    <div class="card-body d-flex align-items-start gap-3">
                        <div class="icono-circulo icono-serie-7"><i class="bi bi-wallet2" aria-hidden="true"></i></div>
                        <div>
                            <h3 class="h6 mb-1">Tesoreria</h3>
                            <p class="text-secondary mb-0 small">Lo que tiene pendiente de cobrar y pagar.</p>
                        </div>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/EstadoResultados">
                    <div class="card-body d-flex align-items-start gap-3">
                        <div class="icono-circulo icono-serie-6"><i class="bi bi-bar-chart" aria-hidden="true"></i></div>
                        <div>
                            <h3 class="h6 mb-1">Estado de resultados</h3>
                            <p class="text-secondary mb-0 small">Calcule su ganancia de un periodo.</p>
                        </div>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/EstadoFinanciero">
                    <div class="card-body d-flex align-items-start gap-3">
                        <div class="icono-circulo icono-serie-8"><i class="bi bi-graph-up" aria-hidden="true"></i></div>
                        <div>
                            <h3 class="h6 mb-1">Estado financiero</h3>
                            <p class="text-secondary mb-0 small">Tendencias, alertas y comparaciones.</p>
                        </div>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
        </div>

        <%@ include file="/Vistas/fragmentos/Footer.jsp" %>
    </c:otherwise>
</c:choose>
