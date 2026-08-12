<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
    <c:when test="${empty sessionScope.idUsuario or sessionScope.rolUsuario != 'ADMINISTRADOR'}">
        <c:redirect url="/Vistas/Principal.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tituloPagina" value="Categorias y medios" scope="request"/>
        <%@ include file="/Vistas/fragmentos/Header.jsp" %>

        <h1>Categorias y medios de pago/cobro</h1>
        <p class="text-secondary">Estas listas aparecen como opciones al cargar ventas, compras, gastos y otros movimientos.</p>

        <%@ include file="/Vistas/fragmentos/Mensajes.jsp" %>

        <div class="row g-4">
            <div class="col-lg-6">
                <div class="card h-100">
                    <div class="card-body">
                        <h2 class="h5">Medios de pago/cobro</h2>
                        <form method="post" action="${pageContext.request.contextPath}/Catalogo" class="row g-2 align-items-end mb-3">
                            <input type="hidden" name="action" value="agregarMetodo">
                            <div class="col-sm-5">
                                <label for="nombreMetodo" class="form-label">Nombre</label>
                                <input type="text" class="form-control" id="nombreMetodo" name="nombreMetodo" placeholder="Ej: Mercado Pago" required>
                            </div>
                            <div class="col-sm-5">
                                <label for="cobroOPago" class="form-label">Se usa para</label>
                                <select class="form-select" id="cobroOPago" name="cobroOPago" required>
                                    <option value="COBRO">Cobros (ventas, otros ingresos)</option>
                                    <option value="PAGO">Pagos (compras, gastos, otros egresos)</option>
                                </select>
                            </div>
                            <div class="col-sm-2">
                                <button type="submit" class="btn btn-primary w-100">Agregar</button>
                            </div>
                        </form>
                        <div class="table-responsive">
                            <table class="table table-sm table-hover align-middle mb-0">
                                <thead class="table-light"><tr><th>Nombre</th><th>Tipo</th><th></th></tr></thead>
                                <tbody>
                                <c:forEach var="metodo" items="${metodos}">
                                    <tr>
                                        <td>${metodo.nombre}</td>
                                        <td>${metodo.cobroOPago == 'COBRO' ? 'Cobro' : 'Pago'}</td>
                                        <td>
                                            <form method="post" action="${pageContext.request.contextPath}/Catalogo" onsubmit="return confirmarAccion('¿Eliminar este medio?');">
                                                <input type="hidden" name="action" value="eliminarMetodo">
                                                <input type="hidden" name="idMetodo" value="${metodo.idMetodo}">
                                                <button type="submit" class="btn btn-sm btn-danger">Eliminar</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty metodos}"><tr><td colspan="3" class="text-center text-secondary py-3">Sin medios cargados.</td></tr></c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-6">
                <div class="card h-100">
                    <div class="card-body">
                        <h2 class="h5">Categorias</h2>
                        <form method="post" action="${pageContext.request.contextPath}/Catalogo" class="row g-2 align-items-end mb-3">
                            <input type="hidden" name="action" value="agregarCategoria">
                            <div class="col-sm-5">
                                <label for="nombreCategoria" class="form-label">Nombre</label>
                                <input type="text" class="form-control" id="nombreCategoria" name="nombreCategoria" placeholder="Ej: Papeleria" required>
                            </div>
                            <div class="col-sm-5">
                                <label for="aplicaA" class="form-label">Se usa en</label>
                                <select class="form-select" id="aplicaA" name="aplicaA" required>
                                    <option value="GASTO">Gastos</option>
                                    <option value="INGRESO">Otros ingresos</option>
                                    <option value="EGRESO">Otros egresos</option>
                                </select>
                            </div>
                            <div class="col-sm-2">
                                <button type="submit" class="btn btn-primary w-100">Agregar</button>
                            </div>
                        </form>
                        <div class="table-responsive">
                            <table class="table table-sm table-hover align-middle mb-0">
                                <thead class="table-light"><tr><th>Nombre</th><th>Se usa en</th><th></th></tr></thead>
                                <tbody>
                                <c:forEach var="categoria" items="${categorias}">
                                    <tr>
                                        <td>${categoria.nombre}</td>
                                        <td>
                                            <c:if test="${categoria.aplicaA == 'GASTO'}">Gastos</c:if>
                                            <c:if test="${categoria.aplicaA == 'INGRESO'}">Otros ingresos</c:if>
                                            <c:if test="${categoria.aplicaA == 'EGRESO'}">Otros egresos</c:if>
                                        </td>
                                        <td>
                                            <form method="post" action="${pageContext.request.contextPath}/Catalogo" onsubmit="return confirmarAccion('¿Eliminar esta categoria?');">
                                                <input type="hidden" name="action" value="eliminarCategoria">
                                                <input type="hidden" name="idCategoria" value="${categoria.idCategoria}">
                                                <button type="submit" class="btn btn-sm btn-danger">Eliminar</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty categorias}"><tr><td colspan="3" class="text-center text-secondary py-3">Sin categorias cargadas.</td></tr></c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <%@ include file="/Vistas/fragmentos/Footer.jsp" %>
    </c:otherwise>
</c:choose>
