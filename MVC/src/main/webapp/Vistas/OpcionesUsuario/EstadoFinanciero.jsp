<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:choose>
    <c:when test="${empty sessionScope.idUsuario}">
        <c:redirect url="/Vistas/Principal.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tituloPagina" value="Estado financiero" scope="request"/>
        <%@ include file="/Vistas/fragmentos/Header.jsp" %>

        <h1>Estado financiero</h1>
        <p class="text-secondary">Panorama de los ultimos 12 meses: tendencias, composicion de gastos y alertas.</p>

        <%@ include file="/Vistas/fragmentos/Mensajes.jsp" %>

        <c:if test="${esAdmin}">
            <div class="card mb-4 d-print-none">
                <div class="card-body">
                    <form method="get" action="${pageContext.request.contextPath}/EstadoFinanciero" class="row g-2 align-items-end">
                        <div class="col-md-6">
                            <label class="form-label">Ver estado financiero de</label>
                            <select class="form-select" name="idUsuarioFiltro" onchange="this.form.submit()">
                                <option value="">Seleccione un cliente...</option>
                                <c:forEach var="cliente" items="${clientes}">
                                    <c:if test="${cliente.rol == 'USUARIO'}">
                                        <option value="${cliente.idUsuario}" ${idUsuarioFiltro == cliente.idUsuario ? 'selected' : ''}>${cliente.nombreCompleto}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>
                    </form>
                </div>
            </div>
        </c:if>

        <c:if test="${esAdmin and empty idUsuarioFiltro}">
            <div class="card">
                <div class="card-body text-center text-secondary py-5">
                    Seleccione un cliente para ver su estado financiero.
                </div>
            </div>
        </c:if>

        <c:if test="${not esAdmin or not empty idUsuarioFiltro}">

        <ul class="nav nav-tabs mb-3 d-print-none" id="tabsEstadoFinanciero" role="tablist">
            <li class="nav-item" role="presentation">
                <button class="nav-link active" data-bs-toggle="tab" data-bs-target="#panel-dashboard" type="button" role="tab">Dashboard</button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link" data-bs-toggle="tab" data-bs-target="#panel-tendencias" type="button" role="tab">Tendencias</button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link" data-bs-toggle="tab" data-bs-target="#panel-comparativa" type="button" role="tab">Comparativa</button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link" data-bs-toggle="tab" data-bs-target="#panel-composicion" type="button" role="tab">Composicion</button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link" data-bs-toggle="tab" data-bs-target="#panel-criticos" type="button" role="tab">Puntos criticos</button>
            </li>
        </ul>

        <div class="tab-content">
            <div class="tab-pane fade show active" id="panel-dashboard" role="tabpanel">
                <div class="row row-cols-1 row-cols-md-3 g-3 mb-3">
                    <div class="col">
                        <div class="card card-body h-100">
                            <div class="small text-secondary text-uppercase">Meses con perdida (12m)</div>
                            <div class="fs-3 fw-bold ${estadoFinanciero.mesesPerdida > 0 ? 'text-danger' : 'text-success'}">${estadoFinanciero.mesesPerdida}</div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="card card-body h-100">
                            <div class="small text-secondary text-uppercase">Mejor mes</div>
                            <div class="fs-3 fw-bold">${estadoFinanciero.mejorMes}</div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="card card-body h-100">
                            <div class="small text-secondary text-uppercase">Peor mes</div>
                            <div class="fs-3 fw-bold">${estadoFinanciero.peorMes}</div>
                        </div>
                    </div>
                </div>
                <div class="card">
                    <div class="card-body">
                        <h2 class="h5">Resumen ejecutivo</h2>
                        <p class="text-secondary mb-0">${resumenEjecutivo}</p>
                        <c:if test="${not empty estadoFinanciero.alertas}">
                            <div class="alert alert-warning d-flex align-items-center mt-3 mb-0" role="alert">
                                <span class="me-2">&#9888;</span> Hay ${fn:length(estadoFinanciero.alertas)} alerta(s) activa(s). Vea la pestana "Puntos criticos" para el detalle.
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>

            <div class="tab-pane fade" id="panel-tendencias" role="tabpanel">
                <div class="card mb-4">
                    <div class="card-body">
                        <h2 class="h5">Ganancia mensual (ultimos 12 meses)</h2>
                        <div class="d-flex gap-3 small text-secondary mb-2">
                            <span><span class="grafico-leyenda-punto bg-success"></span> Ganancia</span>
                            <span><span class="grafico-leyenda-punto bg-danger"></span> Perdida</span>
                        </div>
                        <div class="grafico-divergente">
                            <c:forEach var="barra" items="${barrasGanancia}">
                                <div class="barra-col" title="${barra.etiqueta}: ${barra.valorFormateado}">
                                    <div class="mitad-arriba">
                                        <c:if test="${barra.positivo}"><div class="barra positivo" style="height:${barra.alturaPorcentaje}%;"></div></c:if>
                                    </div>
                                    <div class="linea-base"></div>
                                    <div class="mitad-abajo">
                                        <c:if test="${!barra.positivo}"><div class="barra negativo" style="height:${barra.alturaPorcentaje}%;"></div></c:if>
                                    </div>
                                    <div class="etiqueta">${barra.etiqueta}</div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>

                <div class="card">
                    <div class="card-body">
                        <h2 class="h5">Ventas mensuales (ultimos 12 meses)</h2>
                        <div class="grafico-barras">
                            <c:forEach var="barra" items="${barrasVentas}">
                                <div class="barra-col" title="${barra.etiqueta}: ${barra.valorFormateado}">
                                    <div class="barra-valor">${barra.valorFormateado}</div>
                                    <div class="barra" style="height:${barra.alturaPorcentaje}%;"></div>
                                </div>
                            </c:forEach>
                        </div>
                        <div class="grafico-etiquetas">
                            <c:forEach var="barra" items="${barrasVentas}"><span>${barra.etiqueta}</span></c:forEach>
                        </div>
                    </div>
                </div>
            </div>

            <div class="tab-pane fade" id="panel-comparativa" role="tabpanel">
                <div class="card d-print-none">
                    <div class="card-body">
                        <h2 class="h5">Comparar dos periodos</h2>
                        <form method="get" action="${pageContext.request.contextPath}/EstadoFinanciero" class="row g-3 align-items-end">
                            <c:if test="${esAdmin}"><input type="hidden" name="idUsuarioFiltro" value="${idUsuarioFiltro}"></c:if>
                            <div class="col-6 col-md-3">
                                <label class="form-label">Periodo 1 - desde</label>
                                <input type="date" class="form-control" name="desde1" required>
                            </div>
                            <div class="col-6 col-md-3">
                                <label class="form-label">Periodo 1 - hasta</label>
                                <input type="date" class="form-control" name="hasta1" required>
                            </div>
                            <div class="col-6 col-md-3">
                                <label class="form-label">Periodo 2 - desde</label>
                                <input type="date" class="form-control" name="desde2" required>
                            </div>
                            <div class="col-6 col-md-3">
                                <label class="form-label">Periodo 2 - hasta</label>
                                <input type="date" class="form-control" name="hasta2" required>
                            </div>
                            <div class="col-12">
                                <button type="submit" class="btn btn-primary">Comparar</button>
                            </div>
                        </form>

                        <c:if test="${not empty comparacion}">
                            <hr>
                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0">
                                    <thead class="table-light"><tr><th></th><th>Periodo 1</th><th>Periodo 2</th><th>Variacion</th></tr></thead>
                                    <tbody>
                                    <tr>
                                        <td>Ventas</td>
                                        <td><fmt:formatNumber value="${comparacion.periodo1.ventasTotal}" type="currency" currencySymbol="$"/></td>
                                        <td><fmt:formatNumber value="${comparacion.periodo2.ventasTotal}" type="currency" currencySymbol="$"/></td>
                                        <td class="${comparacion.variacionVentas >= 0 ? 'text-success' : 'text-danger'}">${comparacion.variacionVentas >= 0 ? '+' : ''}<fmt:formatNumber value="${comparacion.variacionVentas}" maxFractionDigits="1"/>%</td>
                                    </tr>
                                    <tr>
                                        <td>Ganancia</td>
                                        <td><fmt:formatNumber value="${comparacion.periodo1.ganancia}" type="currency" currencySymbol="$"/></td>
                                        <td><fmt:formatNumber value="${comparacion.periodo2.ganancia}" type="currency" currencySymbol="$"/></td>
                                        <td class="${comparacion.variacionGanancia >= 0 ? 'text-success' : 'text-danger'}">${comparacion.variacionGanancia >= 0 ? '+' : ''}<fmt:formatNumber value="${comparacion.variacionGanancia}" maxFractionDigits="1"/>%</td>
                                    </tr>
                                    <tr>
                                        <td>Rentabilidad</td>
                                        <td><fmt:formatNumber value="${comparacion.periodo1.rentabilidad}" maxFractionDigits="1"/>%</td>
                                        <td><fmt:formatNumber value="${comparacion.periodo2.rentabilidad}" maxFractionDigits="1"/>%</td>
                                        <td>-</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>

            <div class="tab-pane fade" id="panel-composicion" role="tabpanel">
                <div class="card">
                    <div class="card-body">
                        <h2 class="h5">Composicion de gastos (mes actual)</h2>
                        <c:forEach var="barra" items="${barrasComposicion}">
                            <div class="row align-items-center mb-2 g-2">
                                <div class="col-3 col-md-2 small">${barra.nombreCategoria}</div>
                                <div class="col-7 col-md-8">
                                    <div class="barra-horizontal-pista">
                                        <div class="barra-horizontal-relleno" style="width:${barra.porcentaje}%; background:var(${barra.colorCss});"></div>
                                    </div>
                                </div>
                                <div class="col-2 small text-secondary text-end"><fmt:formatNumber value="${barra.porcentaje}" maxFractionDigits="1"/>%</div>
                            </div>
                        </c:forEach>
                        <c:if test="${empty barrasComposicion}">
                            <p class="text-secondary mb-0">Todavia no hay gastos registrados este mes.</p>
                        </c:if>
                    </div>
                </div>
            </div>

            <div class="tab-pane fade" id="panel-criticos" role="tabpanel">
                <c:if test="${not empty estadoFinanciero.alertas}">
                    <div class="card mb-4">
                        <div class="card-header fw-bold">Alertas</div>
                        <div class="card-body">
                            <c:forEach var="alerta" items="${estadoFinanciero.alertas}">
                                <div class="alert alert-danger d-flex align-items-center mb-2" role="alert">
                                    <span class="me-2">&#9888;</span> ${alerta}
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
                <c:if test="${empty estadoFinanciero.alertas}">
                    <div class="alert alert-success">No hay alertas activas en los ultimos 12 meses.</div>
                </c:if>

                <div class="card">
                    <div class="card-header fw-bold">Ranking de categorias de gasto por impacto (mes actual)</div>
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="table-light"><tr><th>#</th><th>Categoria</th><th>Monto</th><th>% del total</th></tr></thead>
                            <tbody>
                            <c:forEach var="barra" items="${barrasComposicion}" varStatus="fila">
                                <tr>
                                    <td>${fila.count}</td>
                                    <td>${barra.nombreCategoria}</td>
                                    <td>${barra.valorFormateado}</td>
                                    <td><fmt:formatNumber value="${barra.porcentaje}" maxFractionDigits="1"/>%</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty barrasComposicion}">
                                <tr><td colspan="4" class="text-center text-secondary py-3">Todavia no hay gastos registrados este mes.</td></tr>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        </c:if>

        <%@ include file="/Vistas/fragmentos/Footer.jsp" %>
    </c:otherwise>
</c:choose>
