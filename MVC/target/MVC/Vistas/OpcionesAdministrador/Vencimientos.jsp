<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:choose>
    <c:when test="${empty sessionScope.idUsuario or sessionScope.rolUsuario != 'ADMINISTRADOR'}">
        <c:redirect url="/Vistas/Principal.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tituloPagina" value="Vencimientos" scope="request"/>
        <%@ include file="/Vistas/fragmentos/Header.jsp" %>

        <h1>Calendario de vencimientos</h1>
        <p class="text-secondary">
            <span class="badge text-bg-danger">Pendiente</span>
            <span class="badge text-bg-warning">Vence en 7 dias o menos</span>
            <span class="badge text-bg-success">Realizado</span>
        </p>

        <%@ include file="/Vistas/fragmentos/Mensajes.jsp" %>

        <div class="card mb-4">
            <div class="card-body">
                <div class="d-flex align-items-center justify-content-between mb-3">
                    <a class="btn btn-outline-secondary btn-sm" href="${pageContext.request.contextPath}/Vencimiento?anio=${mesAnterior.year}&mes=${mesAnterior.monthValue}">&larr; Anterior</a>
                    <h2 class="h5 mb-0 text-capitalize">${nombreMesActual}</h2>
                    <a class="btn btn-outline-secondary btn-sm" href="${pageContext.request.contextPath}/Vencimiento?anio=${mesSiguiente.year}&mes=${mesSiguiente.monthValue}">Siguiente &rarr;</a>
                </div>
                <div class="calendario">
                    <div class="small text-secondary text-center">Lun</div>
                    <div class="small text-secondary text-center">Mar</div>
                    <div class="small text-secondary text-center">Mie</div>
                    <div class="small text-secondary text-center">Jue</div>
                    <div class="small text-secondary text-center">Vie</div>
                    <div class="small text-secondary text-center">Sab</div>
                    <div class="small text-secondary text-center">Dom</div>
                    <c:forEach var="celda" items="${celdas}">
                        <c:choose>
                            <c:when test="${celda.vacia}"><div class="calendario-celda vacia"></div></c:when>
                            <c:otherwise>
                                <div class="calendario-celda">
                                    <div class="calendario-dia">${celda.dia}</div>
                                    <c:forEach var="v" items="${celda.vencimientos}">
                                        <span class="calendario-punto ${claseEstado[v.idVencimiento]}" title="${v.impuestoPagar} - ${v.nombreCliente}">${v.impuestoPagar}</span>
                                    </c:forEach>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </div>
            </div>
        </div>

        <div class="mb-3">
            <button class="btn btn-primary" type="button" data-bs-toggle="modal" data-bs-target="#modalAgregarVencimiento">
                + Agregar vencimiento
            </button>
        </div>

        <div class="modal fade" id="modalAgregarVencimiento" tabindex="-1" aria-labelledby="modalAgregarVencimientoLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form method="post" action="${pageContext.request.contextPath}/Vencimiento">
                        <div class="modal-header">
                            <h5 class="modal-title" id="modalAgregarVencimientoLabel">Agregar vencimiento</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                        </div>
                        <div class="modal-body">
                            <input type="hidden" name="action" value="agregar">
                            <div class="mb-3">
                                <label for="fecha" class="form-label">Fecha</label>
                                <input type="date" class="form-control" id="fecha" name="fecha" required>
                            </div>
                            <div class="mb-3">
                                <label for="impuestoPagar" class="form-label">Impuesto a pagar</label>
                                <input type="text" class="form-control" id="impuestoPagar" name="impuestoPagar" placeholder="Ej: IVA, Ganancias, Ingresos Brutos" required>
                            </div>
                            <div class="mb-3">
                                <label for="nombreCliente" class="form-label">Cliente</label>
                                <input type="text" class="form-control" id="nombreCliente" name="nombreCliente" required>
                            </div>
                            <div class="mb-3">
                                <label for="ultimosDigitosCuit" class="form-label">Ultimos 4 digitos de CUIT</label>
                                <input type="text" class="form-control" id="ultimosDigitosCuit" name="ultimosDigitosCuit" maxlength="4" required>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancelar</button>
                            <button type="submit" class="btn btn-primary">Agregar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="card mb-4">
            <div class="card-header fw-bold">Importar / exportar</div>
            <div class="card-body">
                <p class="small text-secondary">El archivo Excel (.xlsx) debe tener las columnas, en este orden: Fecha (aaaa-mm-dd), UltimosDigitosCuit, NombreCliente, ImpuestoPagar.</p>
                <a class="btn btn-outline-secondary mb-3" href="${pageContext.request.contextPath}/Vencimiento?action=exportarExcel">Exportar a Excel</a>
                <form method="post" action="${pageContext.request.contextPath}/Vencimiento" enctype="multipart/form-data" class="row g-2 align-items-end">
                    <input type="hidden" name="action" value="importarExcel">
                    <div class="col-md-6">
                        <label for="archivoExcel" class="form-label">Importar desde Excel</label>
                        <input type="file" class="form-control" id="archivoExcel" name="archivoExcel" accept=".xlsx">
                    </div>
                    <div class="col-md-3">
                        <button type="submit" class="btn btn-outline-primary w-100">Importar</button>
                    </div>
                </form>
            </div>
        </div>

        <c:if test="${not empty vencimientoAEditar}">
            <div class="modal fade" id="modalEditarVencimiento" tabindex="-1" aria-labelledby="modalEditarVencimientoLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <form method="post" action="${pageContext.request.contextPath}/Vencimiento">
                            <div class="modal-header">
                                <h5 class="modal-title" id="modalEditarVencimientoLabel">Editar vencimiento</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar" onclick="window.location='${pageContext.request.contextPath}/Vencimiento'"></button>
                            </div>
                            <div class="modal-body">
                                <input type="hidden" name="action" value="editar">
                                <input type="hidden" name="idVencimiento" value="${vencimientoAEditar.idVencimiento}">
                                <div class="row g-3">
                                    <div class="col-md-6">
                                        <label for="efecha" class="form-label">Fecha</label>
                                        <input type="date" class="form-control" id="efecha" name="fecha" value="<fmt:formatDate value="${vencimientoAEditar.fecha}" pattern="yyyy-MM-dd"/>" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="eimpuesto" class="form-label">Impuesto a pagar</label>
                                        <input type="text" class="form-control" id="eimpuesto" name="impuestoPagar" value="${vencimientoAEditar.impuestoPagar}" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="ecliente" class="form-label">Cliente</label>
                                        <input type="text" class="form-control" id="ecliente" name="nombreCliente" value="${vencimientoAEditar.nombreCliente}" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="ecuit" class="form-label">Ultimos 4 digitos de CUIT</label>
                                        <input type="text" class="form-control" id="ecuit" name="ultimosDigitosCuit" maxlength="4" value="${vencimientoAEditar.ultimosDigitosCuit}" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="eestado" class="form-label">Estado</label>
                                        <select class="form-select" id="eestado" name="estado">
                                            <option value="PENDIENTE" ${vencimientoAEditar.estado == 'PENDIENTE' ? 'selected' : ''}>Pendiente</option>
                                            <option value="REALIZADO" ${vencimientoAEditar.estado == 'REALIZADO' ? 'selected' : ''}>Realizado</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/Vencimiento">Cancelar</a>
                                <button type="submit" class="btn btn-primary">Guardar cambios</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <script>
                document.addEventListener("DOMContentLoaded", function () {
                    new bootstrap.Modal(document.getElementById("modalEditarVencimiento")).show();
                });
            </script>
        </c:if>

        <div class="card" data-tabla-filtros>
            <div class="card-body">
                <div class="row g-2 align-items-end mb-3 d-print-none">
                    <div class="col-md-6">
                        <label class="form-label small mb-1">Buscar</label>
                        <input type="search" class="form-control form-control-sm" data-rol="buscador" placeholder="Cliente, CUIT o impuesto...">
                    </div>
                    <div class="col-md-4">
                        <label class="form-label small mb-1">Estado</label>
                        <select class="form-select form-select-sm" data-rol="estado">
                            <option value="">Todos</option>
                            <option value="critico">Pendiente</option>
                            <option value="advertencia">Proximo</option>
                            <option value="bueno">Realizado</option>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <label class="form-label small mb-1">Cliente</label>
                        <select class="form-select form-select-sm" data-rol="cliente" data-auto-opciones="cliente">
                            <option value="">Todos los clientes</option>
                        </select>
                    </div>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                        <tr>
                            <th data-ordenar="fecha">Fecha<span class="indicador-orden"></span></th>
                            <th data-ordenar="impuesto">Impuesto<span class="indicador-orden"></span></th>
                            <th data-ordenar="cliente">Cliente<span class="indicador-orden"></span></th>
                            <th data-ordenar="cuit">CUIT<span class="indicador-orden"></span></th>
                            <th data-ordenar="dias">Dias restantes<span class="indicador-orden"></span></th>
                            <th data-ordenar="estado">Estado<span class="indicador-orden"></span></th>
                            <th class="d-print-none">Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="v" items="${vencimientos}">
                            <tr data-fila
                                data-texto="${fn:toLowerCase(v.impuestoPagar)} ${fn:toLowerCase(v.nombreCliente)} ${fn:toLowerCase(v.ultimosDigitosCuit)}"
                                data-fecha="<fmt:formatDate value="${v.fecha}" pattern="yyyy-MM-dd"/>"
                                data-impuesto="${fn:toLowerCase(v.impuestoPagar)}"
                                data-cliente="${fn:toLowerCase(v.nombreCliente)}"
                                data-cliente-label="${v.nombreCliente}"
                                data-cuit="${v.ultimosDigitosCuit}"
                                data-dias="${diasRestantes[v.idVencimiento]}"
                                data-estado="${claseEstado[v.idVencimiento]}">
                                <td><fmt:formatDate value="${v.fecha}" pattern="dd/MM/yyyy"/></td>
                                <td>${v.impuestoPagar}</td>
                                <td>${v.nombreCliente}</td>
                                <td>${v.ultimosDigitosCuit}</td>
                                <td>${diasRestantes[v.idVencimiento]}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${claseEstado[v.idVencimiento] == 'bueno'}"><span class="badge text-bg-success">Realizado</span></c:when>
                                        <c:when test="${claseEstado[v.idVencimiento] == 'advertencia'}"><span class="badge text-bg-warning">Proximo</span></c:when>
                                        <c:otherwise><span class="badge text-bg-danger">Pendiente</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="d-print-none">
                                    <div class="d-flex gap-1 flex-wrap">
                                        <a class="btn btn-sm btn-outline-secondary" href="${pageContext.request.contextPath}/Vencimiento?editarId=${v.idVencimiento}">Editar</a>
                                        <c:if test="${v.estado == 'PENDIENTE'}">
                                            <form method="post" action="${pageContext.request.contextPath}/Vencimiento" class="d-inline">
                                                <input type="hidden" name="action" value="marcarRealizado">
                                                <input type="hidden" name="idVencimiento" value="${v.idVencimiento}">
                                                <button type="submit" class="btn btn-sm btn-success">Marcar realizado</button>
                                            </form>
                                        </c:if>
                                        <form method="post" action="${pageContext.request.contextPath}/Vencimiento" class="d-inline" onsubmit="return confirmarAccion('Esta accion elimina el vencimiento de forma permanente. ¿Continuar?');">
                                            <input type="hidden" name="action" value="eliminar">
                                            <input type="hidden" name="idVencimiento" value="${v.idVencimiento}">
                                            <button type="submit" class="btn btn-sm btn-danger">Eliminar</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr data-fila-vacia style="display:none;">
                            <td colspan="7" class="text-center text-secondary py-4">No se encontraron vencimientos con esos filtros.</td>
                        </tr>
                        <c:if test="${empty vencimientos}">
                            <tr><td colspan="7" class="text-center text-secondary py-4">No hay vencimientos cargados.</td></tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>
                <div class="d-flex justify-content-between align-items-center mt-3 d-print-none">
                    <small class="text-secondary" data-rol="contador"></small>
                    <div data-rol="paginador"></div>
                </div>
            </div>
        </div>

        <%@ include file="/Vistas/fragmentos/Footer.jsp" %>
    </c:otherwise>
</c:choose>
