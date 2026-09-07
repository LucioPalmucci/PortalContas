<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:choose>
    <c:when test="${empty sessionScope.idUsuario}">
        <c:redirect url="/Vistas/Principal.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tituloPagina" value="Estado de resultados" scope="request"/>
        <%@ include file="/Vistas/fragmentos/Header.jsp" %>

        <div class="d-flex align-items-center justify-content-between flex-wrap gap-2 mb-5">
            <div class="d-flex align-items-center gap-3">
                <div class="icono-circulo icono-serie-6" style="width:52px;height:52px;font-size:1.5rem;">
                    <i class="bi bi-bar-chart" aria-hidden="true"></i>
                </div>
                <div>
                    <h1 class="h3 mb-1">Estado de resultados</h1>
                    <p class="text-secondary mb-0">Resumen de ingresos, costos y ganancia para el periodo seleccionado.</p>
                </div>
            </div>
            <div class="d-flex gap-2 d-print-none">
                <c:if test="${mostrarResultados and (not esAdmin or not empty idUsuarioFiltro)}">
                    <a class="btn btn-outline-secondary rounded-pill" href="${pageContext.request.contextPath}/EstadoResultados?action=exportarPdf&desde=${desde}&hasta=${hasta}&idUsuarioFiltro=${idUsuarioFiltro}"><i class="bi bi-file-earmark-pdf" aria-hidden="true"></i> Exportar PDF</a>
                </c:if>
            </div>
        </div>

        <%@ include file="/Vistas/fragmentos/Mensajes.jsp" %>

        <c:if test="${esAdmin}">
            <div class="card mb-5 d-print-none">
                <div class="card-body">
                    <form method="get" action="${pageContext.request.contextPath}/EstadoResultados" class="row g-2 align-items-end">
                        <div class="col-md-6">
                            <label class="form-label d-flex align-items-center gap-2"><i class="bi bi-funnel texto-serie-6" aria-hidden="true"></i>Configurar/ver estado de resultados de</label>
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
                    Seleccione un cliente para ver o configurar su estado de resultados.
                </div>
            </div>
        </c:if>

        <c:if test="${not esAdmin or not empty idUsuarioFiltro}">

        <c:if test="${mostrarResultados}">
        <div class="card mb-5">
            <div class="card-body">
                <h2 class="h5 text-center mb-4 d-flex align-items-center justify-content-center gap-2"><i class="bi bi-calendar-range texto-serie-6" aria-hidden="true"></i>Periodo: <fmt:formatDate value="${estado.periodoInicio}" pattern="dd/MM/yyyy"/> al <fmt:formatDate value="${estado.periodoFin}" pattern="dd/MM/yyyy"/></h2>
                <div class="row row-cols-2 row-cols-md-3 g-4">
                    <div class="col">
                        <div class="border-start border-4 border-primary rounded p-3 h-100 d-flex align-items-center gap-3">
                            <div class="icono-circulo bg-primary-subtle text-primary"><i class="bi bi-cart-check" aria-hidden="true"></i></div>
                            <div>
                                <div class="small text-secondary text-uppercase">Ventas totales</div>
                                <div class="fs-4 fw-bold"><fmt:formatNumber value="${estado.ventasTotal}" type="currency" currencySymbol="$"/></div>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="border-start border-4 border-primary rounded p-3 h-100 d-flex align-items-center gap-3">
                            <div class="icono-circulo bg-primary-subtle text-primary"><i class="bi bi-box-seam" aria-hidden="true"></i></div>
                            <div>
                                <div class="small text-secondary text-uppercase">Costo de mercaderia vendida</div>
                                <div class="fs-4 fw-bold"><fmt:formatNumber value="${estado.cmv}" type="currency" currencySymbol="$"/></div>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="border-start border-4 border-primary rounded p-3 h-100 d-flex align-items-center gap-3">
                            <div class="icono-circulo bg-primary-subtle text-primary"><i class="bi bi-percent" aria-hidden="true"></i></div>
                            <div>
                                <div class="small text-secondary text-uppercase">Margen utilizado</div>
                                <div class="fs-4 fw-bold"><fmt:formatNumber value="${estado.margenCMV}" maxFractionDigits="1"/>%</div>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="border-start border-4 border-primary rounded p-3 h-100 d-flex align-items-center gap-3">
                            <div class="icono-circulo bg-primary-subtle text-primary"><i class="bi bi-graph-up" aria-hidden="true"></i></div>
                            <div>
                                <div class="small text-secondary text-uppercase">Utilidad bruta</div>
                                <div class="fs-4 fw-bold"><fmt:formatNumber value="${estado.utilidadBruta}" type="currency" currencySymbol="$"/></div>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="border-start border-4 border-primary rounded p-3 h-100 d-flex align-items-center gap-3">
                            <div class="icono-circulo bg-primary-subtle text-primary"><i class="bi bi-receipt" aria-hidden="true"></i></div>
                            <div>
                                <div class="small text-secondary text-uppercase">Gastos operativos</div>
                                <div class="fs-4 fw-bold"><fmt:formatNumber value="${estado.gastosTotal}" type="currency" currencySymbol="$"/></div>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="border-start border-4 border-success rounded p-3 h-100 d-flex align-items-center gap-3">
                            <div class="icono-circulo bg-success-subtle text-success"><i class="bi bi-plus-circle" aria-hidden="true"></i></div>
                            <div>
                                <div class="small text-secondary text-uppercase">Otros ingresos</div>
                                <div class="fs-4 fw-bold text-success"><fmt:formatNumber value="${estado.oIngresosTotal}" type="currency" currencySymbol="$"/></div>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="border-start border-4 border-danger rounded p-3 h-100 d-flex align-items-center gap-3">
                            <div class="icono-circulo bg-danger-subtle text-danger"><i class="bi bi-dash-circle" aria-hidden="true"></i></div>
                            <div>
                                <div class="small text-secondary text-uppercase">Otros egresos</div>
                                <div class="fs-4 fw-bold text-danger"><fmt:formatNumber value="${estado.oEgresosTotal}" type="currency" currencySymbol="$"/></div>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="border-start border-4 ${estado.ganancia >= 0 ? 'border-success' : 'border-danger'} rounded p-3 h-100 d-flex align-items-center gap-3">
                            <div class="icono-circulo ${estado.ganancia >= 0 ? 'bg-success-subtle text-success' : 'bg-danger-subtle text-danger'}"><i class="bi bi-graph-up-arrow" aria-hidden="true"></i></div>
                            <div>
                                <div class="small text-secondary text-uppercase">Ganancia del periodo</div>
                                <div class="fs-4 fw-bold ${estado.ganancia >= 0 ? 'text-success' : 'text-danger'}"><fmt:formatNumber value="${estado.ganancia}" type="currency" currencySymbol="$"/></div>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="border-start border-4 ${estado.rentabilidad >= 0 ? 'border-success' : 'border-danger'} rounded p-3 h-100 d-flex align-items-center gap-3">
                            <div class="icono-circulo ${estado.rentabilidad >= 0 ? 'bg-success-subtle text-success' : 'bg-danger-subtle text-danger'}"><i class="bi bi-percent" aria-hidden="true"></i></div>
                            <div>
                                <div class="small text-secondary text-uppercase">Rentabilidad</div>
                                <div class="fs-4 fw-bold ${estado.rentabilidad >= 0 ? 'text-success' : 'text-danger'}"><fmt:formatNumber value="${estado.rentabilidad}" maxFractionDigits="1"/>%</div>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="border-start border-4 ${estado.variacionAnterior >= 0 ? 'border-success' : 'border-danger'} rounded p-3 h-100 d-flex align-items-center gap-3">
                            <div class="icono-circulo ${estado.variacionAnterior >= 0 ? 'bg-success-subtle text-success' : 'bg-danger-subtle text-danger'}"><i class="bi bi-arrow-left-right" aria-hidden="true"></i></div>
                            <div>
                                <div class="small text-secondary text-uppercase">Vs. periodo anterior</div>
                                <div class="fs-4 fw-bold ${estado.variacionAnterior >= 0 ? 'text-success' : 'text-danger'}">${estado.variacionAnterior >= 0 ? '+' : ''}<fmt:formatNumber value="${estado.variacionAnterior}" maxFractionDigits="1"/>%</div>
                            </div>
                        </div>
                    </div>
                </div>
                <p class="text-secondary mt-4 mb-0">${resumenNarrativo}</p>
            </div>
        </div>
        </c:if>

        <c:if test="${mostrarResultados}">
        <div class="card mb-5">
            <div class="card-body">
                <h2 class="h5 border-start border-4 borde-serie-6 ps-2 d-flex align-items-center gap-2"><i class="bi bi-pie-chart texto-serie-6" aria-hidden="true"></i>Composicion financiera</h2>
                <p class="text-secondary small">Distribucion de ingresos y egresos por categoria del periodo.</p>
                <c:forEach var="barra" items="${composicionFinanciera}">
                    <div class="row align-items-center mb-2 g-2">
                        <div class="col-3 small">${barra.nombreCategoria}</div>
                        <div class="col-7">
                            <div class="barra-horizontal-pista">
                                <div class="barra-horizontal-relleno" style="width:${barra.porcentaje}%; min-width:${barra.porcentaje > 0 ? '3px' : '0'}; background:${barra.colorCss};"></div>
                            </div>
                        </div>
                        <div class="col-2 small text-secondary text-end">${barra.valorFormateado}</div>
                    </div>
                </c:forEach>
            </div>
        </div>
        </c:if>

        <c:if test="${mostrarResultados}">
        <div class="row g-4 mb-5 d-print-none">
            <div class="col-lg-6">
                <div class="card h-100">
                    <div class="card-header">
                        <button class="btn btn-link text-decoration-none fw-bold p-0 d-flex align-items-center gap-2 text-success" type="button" data-bs-toggle="collapse" data-bs-target="#tablaIncluidos">
                            <i class="bi bi-check2-circle" aria-hidden="true"></i>
                            Movimientos incluidos (${fn:length(movimientosIncluidos)})
                        </button>
                    </div>
                    <div class="collapse" id="tablaIncluidos">
                        <div class="table-responsive" style="max-height:320px;">
                            <table class="table table-sm table-hover align-middle mb-0">
                                <thead class="table-light"><tr><th>Origen</th><th>Fecha</th><th>Concepto</th><th>Monto</th></tr></thead>
                                <tbody>
                                <c:forEach var="m" items="${movimientosIncluidos}">
                                    <tr>
                                        <td><span class="badge rounded-pill bg-secondary-subtle text-secondary-emphasis">${m.origen}</span></td>
                                        <td><fmt:formatDate value="${m.fecha}" pattern="dd/MM/yyyy"/></td>
                                        <td>${m.concepto}</td>
                                        <td><fmt:formatNumber value="${m.monto}" type="currency" currencySymbol="$"/></td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty movimientosIncluidos}">
                                    <tr><td colspan="4" class="text-center text-secondary py-3">Sin movimientos incluidos.</td></tr>
                                </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-6">
                <div class="card h-100">
                    <div class="card-header">
                        <button class="btn btn-link text-decoration-none fw-bold p-0 d-flex align-items-center gap-2 text-secondary" type="button" data-bs-toggle="collapse" data-bs-target="#tablaExcluidos">
                            <i class="bi bi-slash-circle" aria-hidden="true"></i>
                            Movimientos excluidos (${fn:length(movimientosExcluidos)})
                        </button>
                    </div>
                    <div class="collapse" id="tablaExcluidos">
                        <div class="table-responsive" style="max-height:320px;">
                            <table class="table table-sm table-hover align-middle mb-0">
                                <thead class="table-light"><tr><th>Origen</th><th>Fecha</th><th>Concepto</th><th>Monto</th></tr></thead>
                                <tbody>
                                <c:forEach var="m" items="${movimientosExcluidos}">
                                    <tr>
                                        <td><span class="badge rounded-pill bg-secondary-subtle text-secondary-emphasis">${m.origen}</span></td>
                                        <td><fmt:formatDate value="${m.fecha}" pattern="dd/MM/yyyy"/></td>
                                        <td>${m.concepto}</td>
                                        <td><fmt:formatNumber value="${m.monto}" type="currency" currencySymbol="$"/></td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty movimientosExcluidos}">
                                    <tr><td colspan="4" class="text-center text-secondary py-3">No hay categorias excluidas del calculo, o no tienen movimientos en este periodo.</td></tr>
                                </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <a class="btn btn-primary rounded-pill d-print-none" href="${pageContext.request.contextPath}/EstadoResultados?action=volver&idUsuarioFiltro=${idUsuarioFiltro}"><i class="bi bi-arrow-left" aria-hidden="true"></i> Volver</a>
        </c:if>

        <c:if test="${not mostrarResultados}">
        <div class="card d-print-none">
            <div class="card-header d-flex align-items-center gap-2 texto-serie-6">
                <i class="bi bi-gear" aria-hidden="true"></i>
                <strong>Configurar estado de resultados</strong>
            </div>
            <div class="card-body">
                <form method="post" action="${pageContext.request.contextPath}/EstadoResultados">
                    <input type="hidden" name="action" value="guardarConfiguracion">
                    <c:if test="${esAdmin}"><input type="hidden" name="idUsuarioFiltro" value="${idUsuarioFiltro}"></c:if>
                    <div class="row g-3">
                        <div class="col-md-4">
                            <label for="periodoInicioDefault" class="form-label">Desde</label>
                            <input type="date" class="form-control" id="periodoInicioDefault" name="periodoInicioDefault" value="<fmt:formatDate value="${configuracion.periodoInicioDefault}" pattern="yyyy-MM-dd"/>" required>
                        </div>
                        <div class="col-md-4">
                            <label for="periodoFinDefault" class="form-label">Hasta</label>
                            <input type="date" class="form-control" id="periodoFinDefault" name="periodoFinDefault" value="<fmt:formatDate value="${configuracion.periodoFinDefault}" pattern="yyyy-MM-dd"/>" required>
                        </div>
                        <div class="col-md-4">
                            <label for="margenCMV" class="form-label">Margen de costo de mercaderia vendida (%)</label>
                            <input type="number" class="form-control" id="margenCMV" name="margenCMV" min="0" max="100" step="1" value="${configuracion.margenCMV}" required>
                        </div>
                    </div>
                    <div class="mt-3">
                        <label class="form-label">Categorias excluidas del calculo</label>
                        <div class="row row-cols-2 row-cols-md-4 g-2">
                            <c:forEach var="categoria" items="${categorias}">
                                <div class="col">
                                    <div class="form-check form-switch">
                                        <input type="checkbox" class="form-check-input" role="switch" id="cat${categoria.idCategoria}" name="categoriasExcluidas" value="${categoria.idCategoria}" ${idsExcluidasActuales.contains(categoria.idCategoria) ? 'checked' : ''}>
                                        <label class="form-check-label" for="cat${categoria.idCategoria}">Excluir ${categoria.nombre}</label>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="mt-4">
                        <button type="submit" class="btn btn-primary btn-lg w-100"><i class="bi bi-check-lg me-1" aria-hidden="true"></i>Generar estado de resultados</button>
                    </div>
                </form>
            </div>
        </div>
        </c:if>
        </c:if>

        <%@ include file="/Vistas/fragmentos/Footer.jsp" %>
    </c:otherwise>
</c:choose>
