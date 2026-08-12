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

        <div class="d-flex align-items-center justify-content-between flex-wrap gap-2 mb-2">
            <h1 class="mb-0">Estado de resultados</h1>
            <c:if test="${not esAdmin or not empty idUsuarioFiltro}">
                <a class="btn btn-outline-secondary d-print-none" href="${pageContext.request.contextPath}/EstadoResultados?action=exportarPdf&desde=${desde}&hasta=${hasta}&idUsuarioFiltro=${idUsuarioFiltro}">Exportar PDF</a>
            </c:if>
        </div>
        <p class="text-secondary">Resumen de ingresos, costos y ganancia para el periodo seleccionado.</p>

        <%@ include file="/Vistas/fragmentos/Mensajes.jsp" %>

        <c:if test="${esAdmin}">
            <div class="card mb-4 d-print-none">
                <div class="card-body">
                    <form method="get" action="${pageContext.request.contextPath}/EstadoResultados" class="row g-2 align-items-end">
                        <div class="col-md-6">
                            <label class="form-label">Configurar/ver estado de resultados de</label>
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

        <div class="card mb-4 d-print-none">
            <div class="card-body">
                <form method="get" action="${pageContext.request.contextPath}/EstadoResultados" class="row g-3 align-items-end">
                    <c:if test="${esAdmin}"><input type="hidden" name="idUsuarioFiltro" value="${idUsuarioFiltro}"></c:if>
                    <div class="col-md-4">
                        <label for="desde" class="form-label">Desde</label>
                        <input type="date" class="form-control" id="desde" name="desde" value="${desde}">
                    </div>
                    <div class="col-md-4">
                        <label for="hasta" class="form-label">Hasta</label>
                        <input type="date" class="form-control" id="hasta" name="hasta" value="${hasta}">
                    </div>
                    <div class="col-md-4">
                        <button type="submit" class="btn btn-primary w-100">Generar</button>
                    </div>
                </form>
            </div>
        </div>

        <div class="card mb-4">
            <div class="card-body">
                <h2 class="h5 text-center mb-4">Periodo: <fmt:formatDate value="${estado.periodoInicio}" pattern="dd/MM/yyyy"/> al <fmt:formatDate value="${estado.periodoFin}" pattern="dd/MM/yyyy"/></h2>
                <div class="row row-cols-2 row-cols-md-3 g-3">
                    <div class="col">
                        <div class="border rounded p-3 h-100">
                            <div class="small text-secondary text-uppercase">Ventas totales</div>
                            <div class="fs-4 fw-bold"><fmt:formatNumber value="${estado.ventasTotal}" type="currency" currencySymbol="$"/></div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="border rounded p-3 h-100">
                            <div class="small text-secondary text-uppercase">Costo de mercaderia vendida</div>
                            <div class="fs-4 fw-bold"><fmt:formatNumber value="${estado.cmv}" type="currency" currencySymbol="$"/></div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="border rounded p-3 h-100">
                            <div class="small text-secondary text-uppercase">Utilidad bruta</div>
                            <div class="fs-4 fw-bold"><fmt:formatNumber value="${estado.utilidadBruta}" type="currency" currencySymbol="$"/></div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="border rounded p-3 h-100">
                            <div class="small text-secondary text-uppercase">Gastos operativos</div>
                            <div class="fs-4 fw-bold"><fmt:formatNumber value="${estado.gastosTotal}" type="currency" currencySymbol="$"/></div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="border rounded p-3 h-100">
                            <div class="small text-secondary text-uppercase">Otros ingresos</div>
                            <div class="fs-4 fw-bold text-success"><fmt:formatNumber value="${estado.oIngresosTotal}" type="currency" currencySymbol="$"/></div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="border rounded p-3 h-100">
                            <div class="small text-secondary text-uppercase">Otros egresos</div>
                            <div class="fs-4 fw-bold text-danger"><fmt:formatNumber value="${estado.oEgresosTotal}" type="currency" currencySymbol="$"/></div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="border rounded p-3 h-100">
                            <div class="small text-secondary text-uppercase">Ganancia del periodo</div>
                            <div class="fs-4 fw-bold ${estado.ganancia >= 0 ? 'text-success' : 'text-danger'}"><fmt:formatNumber value="${estado.ganancia}" type="currency" currencySymbol="$"/></div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="border rounded p-3 h-100">
                            <div class="small text-secondary text-uppercase">Rentabilidad</div>
                            <div class="fs-4 fw-bold ${estado.rentabilidad >= 0 ? 'text-success' : 'text-danger'}"><fmt:formatNumber value="${estado.rentabilidad}" maxFractionDigits="1"/>%</div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="border rounded p-3 h-100">
                            <div class="small text-secondary text-uppercase">Vs. periodo anterior</div>
                            <div class="fs-4 fw-bold ${estado.variacionAnterior >= 0 ? 'text-success' : 'text-danger'}">${estado.variacionAnterior >= 0 ? '+' : ''}<fmt:formatNumber value="${estado.variacionAnterior}" maxFractionDigits="1"/>%</div>
                        </div>
                    </div>
                </div>
                <p class="text-secondary mt-4 mb-0">${resumenNarrativo}</p>
            </div>
        </div>

        <div class="row g-3 mb-4">
            <div class="col-lg-6">
                <div class="card h-100">
                    <div class="card-body">
                        <h2 class="h5">Composicion de gastos</h2>
                        <c:forEach var="barra" items="${composicionGastos}">
                            <div class="row align-items-center mb-2 g-2">
                                <div class="col-5 small">${barra.nombreCategoria}</div>
                                <div class="col-5">
                                    <div class="barra-horizontal-pista">
                                        <div class="barra-horizontal-relleno" style="width:${barra.alturaPorcentaje}%; background:var(${barra.colorCss});"></div>
                                    </div>
                                </div>
                                <div class="col-2 small text-secondary text-end">${barra.valorFormateado}</div>
                            </div>
                        </c:forEach>
                        <c:if test="${empty composicionGastos}">
                            <p class="text-secondary mb-0">No hay gastos registrados en este periodo.</p>
                        </c:if>
                    </div>
                </div>
            </div>
            <div class="col-lg-6">
                <div class="card h-100">
                    <div class="card-body">
                        <h2 class="h5">Composicion de otros ingresos</h2>
                        <c:forEach var="barra" items="${composicionIngresos}">
                            <div class="row align-items-center mb-2 g-2">
                                <div class="col-5 small">${barra.nombreCategoria}</div>
                                <div class="col-5">
                                    <div class="barra-horizontal-pista">
                                        <div class="barra-horizontal-relleno" style="width:${barra.alturaPorcentaje}%; background:var(${barra.colorCss});"></div>
                                    </div>
                                </div>
                                <div class="col-2 small text-secondary text-end">${barra.valorFormateado}</div>
                            </div>
                        </c:forEach>
                        <c:if test="${empty composicionIngresos}">
                            <p class="text-secondary mb-0">No hay otros ingresos registrados en este periodo.</p>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <div class="row g-3 mb-4 d-print-none">
            <div class="col-lg-6">
                <div class="card h-100">
                    <div class="card-header">
                        <button class="btn btn-link text-decoration-none fw-bold p-0" type="button" data-bs-toggle="collapse" data-bs-target="#tablaIncluidos">
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
                                        <td><span class="badge text-bg-secondary">${m.origen}</span></td>
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
                        <button class="btn btn-link text-decoration-none fw-bold p-0" type="button" data-bs-toggle="collapse" data-bs-target="#tablaExcluidos">
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
                                        <td><span class="badge text-bg-secondary">${m.origen}</span></td>
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

        <div class="card d-print-none">
            <div class="card-header">
                <button class="btn btn-link text-decoration-none fw-bold p-0" type="button" data-bs-toggle="collapse" data-bs-target="#formConfiguracion">
                    Configurar estado de resultados
                </button>
            </div>
            <div class="collapse" id="formConfiguracion">
                <div class="card-body">
                    <form method="post" action="${pageContext.request.contextPath}/EstadoResultados">
                        <input type="hidden" name="action" value="guardarConfiguracion">
                        <c:if test="${esAdmin}"><input type="hidden" name="idUsuarioFiltro" value="${idUsuarioFiltro}"></c:if>
                        <div class="row g-3">
                            <div class="col-md-4">
                                <label for="periodoInicioDefault" class="form-label">Periodo por defecto - desde</label>
                                <input type="date" class="form-control" id="periodoInicioDefault" name="periodoInicioDefault" value="<fmt:formatDate value="${configuracion.periodoInicioDefault}" pattern="yyyy-MM-dd"/>" required>
                            </div>
                            <div class="col-md-4">
                                <label for="periodoFinDefault" class="form-label">Periodo por defecto - hasta</label>
                                <input type="date" class="form-control" id="periodoFinDefault" name="periodoFinDefault" value="<fmt:formatDate value="${configuracion.periodoFinDefault}" pattern="yyyy-MM-dd"/>" required>
                            </div>
                            <div class="col-md-4">
                                <label for="margenCMV" class="form-label">Margen de costo de mercaderia vendida (%)</label>
                                <input type="number" class="form-control" id="margenCMV" name="margenCMV" min="0" max="100" step="0.1" value="${configuracion.margenCMV}" required>
                                <div class="form-text">Porcentaje de las ventas que se considera costo de mercaderia vendida.</div>
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
                        <div class="d-flex gap-2 mt-3">
                            <button type="submit" class="btn btn-primary">Guardar configuracion</button>
                            <button type="submit" form="formResetear" class="btn btn-outline-secondary">Restablecer valores por defecto</button>
                        </div>
                    </form>
                    <form id="formResetear" method="post" action="${pageContext.request.contextPath}/EstadoResultados">
                        <input type="hidden" name="action" value="resetear">
                        <c:if test="${esAdmin}"><input type="hidden" name="idUsuarioFiltro" value="${idUsuarioFiltro}"></c:if>
                    </form>
                </div>
            </div>
        </div>
        </c:if>

        <%@ include file="/Vistas/fragmentos/Footer.jsp" %>
    </c:otherwise>
</c:choose>
