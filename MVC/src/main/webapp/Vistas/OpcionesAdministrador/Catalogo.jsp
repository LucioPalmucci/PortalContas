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
                                <thead class="table-light"><tr><th>Nombre</th><th>Tipo</th><th>Estado</th><th></th></tr></thead>
                                <tbody>
                                <c:forEach var="metodo" items="${metodos}">
                                    <tr>
                                        <td>${metodo.nombre}</td>
                                        <td>${metodo.cobroOPago == 'COBRO' ? 'Cobro' : 'Pago'}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${metodo.estaActivo}"><span class="badge text-bg-success">Activo</span></c:when>
                                                <c:otherwise><span class="badge text-bg-secondary">Inactivo</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${metodo.estaActivo}">
                                                    <form method="post" action="${pageContext.request.contextPath}/Catalogo" onsubmit="return confirmarAccion('¿Desactivar este medio? Dejará de ofrecerse en los formularios; los movimientos ya cargados lo conservan.');">
                                                        <input type="hidden" name="action" value="desactivarMetodo">
                                                        <input type="hidden" name="idMetodo" value="${metodo.idMetodo}">
                                                        <button type="submit" class="btn btn-sm btn-danger">Desactivar</button>
                                                    </form>
                                                </c:when>
                                                <c:otherwise>
                                                    <form method="post" action="${pageContext.request.contextPath}/Catalogo">
                                                        <input type="hidden" name="action" value="activarMetodo">
                                                        <input type="hidden" name="idMetodo" value="${metodo.idMetodo}">
                                                        <button type="submit" class="btn btn-sm btn-success">Activar</button>
                                                    </form>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty metodos}"><tr><td colspan="4" class="text-center text-secondary py-3">Sin medios cargados.</td></tr></c:if>
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
                                <label for="categoriaAplicaA" class="form-label">Se usa en</label>
                                <select class="form-select" id="categoriaAplicaA" name="categoriaAplicaA" required>
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
                                <thead class="table-light"><tr><th>Nombre</th><th>Se usa en</th><th>Estado</th><th></th></tr></thead>
                                <tbody>
                                <c:forEach var="categoria" items="${categorias}">
                                    <tr>
                                        <td>${categoria.nombre}</td>
                                        <td>
                                            <c:if test="${categoria.categoriaAplicaA == 'GASTO'}">Gastos</c:if>
                                            <c:if test="${categoria.categoriaAplicaA == 'INGRESO'}">Otros ingresos</c:if>
                                            <c:if test="${categoria.categoriaAplicaA == 'EGRESO'}">Otros egresos</c:if>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${categoria.estaActivo}"><span class="badge text-bg-success">Activo</span></c:when>
                                                <c:otherwise><span class="badge text-bg-secondary">Inactivo</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${categoria.estaActivo}">
                                                    <form method="post" action="${pageContext.request.contextPath}/Catalogo" onsubmit="return confirmarAccion('¿Desactivar esta categoría? Dejará de ofrecerse en los formularios; los movimientos ya cargados la conservan.');">
                                                        <input type="hidden" name="action" value="desactivarCategoria">
                                                        <input type="hidden" name="idCategoria" value="${categoria.idCategoria}">
                                                        <button type="submit" class="btn btn-sm btn-danger">Desactivar</button>
                                                    </form>
                                                </c:when>
                                                <c:otherwise>
                                                    <form method="post" action="${pageContext.request.contextPath}/Catalogo">
                                                        <input type="hidden" name="action" value="activarCategoria">
                                                        <input type="hidden" name="idCategoria" value="${categoria.idCategoria}">
                                                        <button type="submit" class="btn btn-sm btn-success">Activar</button>
                                                    </form>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty categorias}"><tr><td colspan="4" class="text-center text-secondary py-3">Sin categorias cargadas.</td></tr></c:if>
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
