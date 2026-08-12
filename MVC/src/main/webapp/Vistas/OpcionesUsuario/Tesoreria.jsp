<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:choose>
    <c:when test="${empty sessionScope.idUsuario}">
        <c:redirect url="/Vistas/Principal.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tituloPagina" value="Tesoreria" scope="request"/>
        <%@ include file="/Vistas/fragmentos/Header.jsp" %>

        <h1>Tesoreria</h1>
        <p class="text-secondary">Resumen automatico de todo lo que tiene pendiente de cobro y de pago en este momento.</p>

        <%@ include file="/Vistas/fragmentos/Mensajes.jsp" %>

        <c:if test="${esAdmin}">
            <div class="card mb-4 d-print-none">
                <div class="card-body">
                    <form method="get" action="${pageContext.request.contextPath}/Tesoreria" class="row g-2 align-items-end">
                        <div class="col-md-6">
                            <label class="form-label">Ver tesoreria de</label>
                            <select class="form-select" name="idUsuarioFiltro" onchange="this.form.submit()">
                                <option value="">Consolidado de todos los clientes</option>
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

        <div class="row row-cols-1 row-cols-md-3 g-3 mb-4">
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
            <div class="col">
                <div class="card card-body h-100">
                    <div class="small text-secondary text-uppercase">Saldo neto proyectado</div>
                    <div class="fs-3 fw-bold ${tesoreria.saldoNeto >= 0 ? 'text-success' : 'text-danger'}"><fmt:formatNumber value="${tesoreria.saldoNeto}" type="currency" currencySymbol="$"/></div>
                </div>
            </div>
        </div>

        <div class="card mb-4">
            <div class="card-header fw-bold">Pendientes de cobro</div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light"><tr><th>Origen</th><c:if test="${esAdmin}"><th>Cliente</th></c:if><th>Fecha</th><th>Concepto</th><th>Monto</th><th></th></tr></thead>
                    <tbody>
                    <c:forEach var="v" items="${tesoreria.ventasPendientes}">
                        <tr>
                            <td><span class="badge text-bg-secondary">Venta</span></td>
                            <c:if test="${esAdmin}"><td>${v.usuario.nombreCompleto}</td></c:if>
                            <td><fmt:formatDate value="${v.fecha}" pattern="dd/MM/yyyy"/></td>
                            <td>${v.conceptoVenta}</td>
                            <td><fmt:formatNumber value="${v.subtotal}" type="currency" currencySymbol="$"/></td>
                            <td><a class="btn btn-sm btn-outline-secondary" href="${pageContext.request.contextPath}/Venta">Ver</a></td>
                        </tr>
                    </c:forEach>
                    <c:forEach var="o" items="${tesoreria.otrosIngresosPendientes}">
                        <tr>
                            <td><span class="badge text-bg-secondary">Otro ingreso</span></td>
                            <c:if test="${esAdmin}"><td>${o.usuario.nombreCompleto}</td></c:if>
                            <td><fmt:formatDate value="${o.fecha}" pattern="dd/MM/yyyy"/></td>
                            <td>${o.categoria.nombre}</td>
                            <td><fmt:formatNumber value="${o.valor}" type="currency" currencySymbol="$"/></td>
                            <td><a class="btn btn-sm btn-outline-secondary" href="${pageContext.request.contextPath}/OtroIngreso">Ver</a></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty tesoreria.ventasPendientes and empty tesoreria.otrosIngresosPendientes}">
                        <tr><td colspan="${esAdmin ? 6 : 5}" class="text-center text-secondary py-4">No hay cobros pendientes.</td></tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="card mb-4">
            <div class="card-header fw-bold">Pendientes de pago</div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light"><tr><th>Origen</th><c:if test="${esAdmin}"><th>Cliente</th></c:if><th>Fecha</th><th>Concepto</th><th>Monto</th><th></th></tr></thead>
                    <tbody>
                    <c:forEach var="c" items="${tesoreria.comprasPendientes}">
                        <tr>
                            <td><span class="badge text-bg-secondary">Compra</span></td>
                            <c:if test="${esAdmin}"><td>${c.usuario.nombreCompleto}</td></c:if>
                            <td><fmt:formatDate value="${c.fecha}" pattern="dd/MM/yyyy"/></td>
                            <td>${c.conceptoCompra}</td>
                            <td><fmt:formatNumber value="${c.valorTotal}" type="currency" currencySymbol="$"/></td>
                            <td><a class="btn btn-sm btn-outline-secondary" href="${pageContext.request.contextPath}/Compra">Ver</a></td>
                        </tr>
                    </c:forEach>
                    <c:forEach var="g" items="${tesoreria.gastosPendientes}">
                        <tr>
                            <td><span class="badge text-bg-secondary">Gasto</span></td>
                            <c:if test="${esAdmin}"><td>${g.usuario.nombreCompleto}</td></c:if>
                            <td><fmt:formatDate value="${g.fecha}" pattern="dd/MM/yyyy"/></td>
                            <td>${g.categoria.nombre}</td>
                            <td><fmt:formatNumber value="${g.valor}" type="currency" currencySymbol="$"/></td>
                            <td><a class="btn btn-sm btn-outline-secondary" href="${pageContext.request.contextPath}/Gasto">Ver</a></td>
                        </tr>
                    </c:forEach>
                    <c:forEach var="e" items="${tesoreria.otrosEgresosPendientes}">
                        <tr>
                            <td><span class="badge text-bg-secondary">Otro egreso</span></td>
                            <c:if test="${esAdmin}"><td>${e.usuario.nombreCompleto}</td></c:if>
                            <td><fmt:formatDate value="${e.fecha}" pattern="dd/MM/yyyy"/></td>
                            <td>${e.categoria.nombre}</td>
                            <td><fmt:formatNumber value="${e.valor}" type="currency" currencySymbol="$"/></td>
                            <td><a class="btn btn-sm btn-outline-secondary" href="${pageContext.request.contextPath}/OtroEgreso">Ver</a></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty tesoreria.comprasPendientes and empty tesoreria.gastosPendientes and empty tesoreria.otrosEgresosPendientes}">
                        <tr><td colspan="${esAdmin ? 6 : 5}" class="text-center text-secondary py-4">No hay pagos pendientes.</td></tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <%@ include file="/Vistas/fragmentos/Footer.jsp" %>
    </c:otherwise>
</c:choose>
