<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:choose>
    <c:when test="${empty sessionScope.idUsuario}">
        <c:redirect url="/Vistas/Principal.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tituloPagina" value="Ventas" scope="request"/>
        <%@ include file="/Vistas/fragmentos/Header.jsp" %>

        <c:choose>
            <c:when test="${esAdmin}">
                <h1>Ventas de usuarios</h1>
                <p class="text-secondary">Seleccione un usuario para ver sus ventas. El subtotal se calcula automaticamente.</p>
            </c:when>
            <c:otherwise>
                <h1>Mis ventas</h1>
                <p class="text-secondary">Registre cada venta que realiza. El subtotal se calcula automaticamente.</p>
            </c:otherwise>
        </c:choose>

        <%@ include file="/Vistas/fragmentos/Mensajes.jsp" %>

        <div class="card mb-4">
            <div class="card-header">
                <button class="btn btn-link text-decoration-none fw-bold p-0" type="button" data-bs-toggle="collapse" data-bs-target="#formAgregarVenta">
                    + Registrar nueva venta
                </button>
            </div>
            <div class="collapse" id="formAgregarVenta">
                <div class="card-body">
                    <form method="post" action="${pageContext.request.contextPath}/Venta">
                        <input type="hidden" name="action" value="agregar">
                        <div class="row g-3">
                            <c:if test="${esAdmin}">
                                <div class="col-12">
                                    <label for="idUsuarioDestino" class="form-label">Cliente</label>
                                    <select class="form-select" id="idUsuarioDestino" name="idUsuarioDestino" required>
                                        <option value="">Seleccione...</option>
                                        <c:forEach var="cliente" items="${clientes}">
                                            <c:if test="${cliente.rol == 'USUARIO'}">
                                                <option value="${cliente.idUsuario}">${cliente.nombreCompleto}</option>
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                </div>
                            </c:if>
                            <div class="col-md-4">
                                <label for="fecha" class="form-label">Fecha</label>
                                <input type="date" class="form-control" id="fecha" name="fecha" required>
                            </div>
                            <div class="col-md-8">
                                <label for="conceptoVenta" class="form-label">Concepto</label>
                                <input type="text" class="form-control" id="conceptoVenta" name="conceptoVenta" placeholder="Ej: Venta de mercaderia" required>
                            </div>
                            <div class="col-md-4">
                                <label for="precioUnitario" class="form-label">Precio unitario ($)</label>
                                <input type="number" class="form-control" id="precioUnitario" name="precioUnitario" min="0.01" step="0.01" required>
                            </div>
                            <div class="col-md-4">
                                <label for="cantidad" class="form-label">Cantidad</label>
                                <input type="number" class="form-control" id="cantidad" name="cantidad" min="1" step="1" required>
                            </div>
                            <div class="col-md-4">
                                <label for="idMetodo" class="form-label">Medio de cobro</label>
                                <select class="form-select" id="idMetodo" name="idMetodo" required>
                                    <option value="">Seleccione...</option>
                                    <c:forEach var="metodo" items="${metodosCobro}">
                                        <option value="${metodo.idMetodo}">${metodo.nombre}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label for="estado" class="form-label">Estado</label>
                                <select class="form-select" id="estado" name="estado">
                                    <option value="PENDIENTE_DE_COBRO">Pendiente de cobro</option>
                                    <option value="COBRADO">Cobrada</option>
                                </select>
                            </div>
                            <div class="col-12">
                                <label for="descripcion" class="form-label">Descripcion (opcional)</label>
                                <textarea class="form-control" id="descripcion" name="descripcion"></textarea>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary mt-3">Guardar venta</button>
                    </form>
                </div>
            </div>
        </div>

        <c:if test="${not empty ventaAEditar}">
            <div class="modal fade" id="modalEditarVenta" tabindex="-1" aria-labelledby="modalEditarVentaLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <form method="post" action="${pageContext.request.contextPath}/Venta">
                            <div class="modal-header">
                                <h5 class="modal-title" id="modalEditarVentaLabel">Editar venta #${ventaAEditar.idVenta}</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar" onclick="window.location='${pageContext.request.contextPath}/Venta'"></button>
                            </div>
                            <div class="modal-body">
                                <input type="hidden" name="action" value="editar">
                                <input type="hidden" name="idVenta" value="${ventaAEditar.idVenta}">
                                <div class="row g-3">
                                    <div class="col-md-4">
                                        <label for="efecha" class="form-label">Fecha</label>
                                        <input type="date" class="form-control" id="efecha" name="fecha" value="<fmt:formatDate value="${ventaAEditar.fecha}" pattern="yyyy-MM-dd"/>" required>
                                    </div>
                                    <div class="col-md-8">
                                        <label for="econcepto" class="form-label">Concepto</label>
                                        <input type="text" class="form-control" id="econcepto" name="conceptoVenta" value="${ventaAEditar.conceptoVenta}" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="eprecio" class="form-label">Precio unitario ($)</label>
                                        <input type="number" class="form-control" id="eprecio" name="precioUnitario" min="0.01" step="0.01" value="${ventaAEditar.precioUnitario}" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="ecantidad" class="form-label">Cantidad</label>
                                        <input type="number" class="form-control" id="ecantidad" name="cantidad" min="1" step="1" value="${ventaAEditar.cantidad}" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="emetodo" class="form-label">Medio de cobro</label>
                                        <select class="form-select" id="emetodo" name="idMetodo" required>
                                            <c:forEach var="metodo" items="${metodosCobro}">
                                                <option value="${metodo.idMetodo}" ${metodo.idMetodo == ventaAEditar.metodo.idMetodo ? 'selected' : ''}>${metodo.nombre}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="eestado" class="form-label">Estado</label>
                                        <select class="form-select" id="eestado" name="estado">
                                            <option value="PENDIENTE_DE_COBRO" ${ventaAEditar.estado == 'PENDIENTE_DE_COBRO' ? 'selected' : ''}>Pendiente de cobro</option>
                                            <option value="COBRADO" ${ventaAEditar.estado == 'COBRADO' ? 'selected' : ''}>Cobrada</option>
                                            <option value="ANULADO" ${ventaAEditar.estado == 'ANULADO' ? 'selected' : ''}>Anulada</option>
                                        </select>
                                    </div>
                                    <div class="col-12">
                                        <label for="edescripcion" class="form-label">Descripcion (opcional)</label>
                                        <textarea class="form-control" id="edescripcion" name="descripcion">${ventaAEditar.descripcion}</textarea>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/Venta">Cancelar</a>
                                <button type="submit" class="btn btn-primary">Guardar cambios</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <script>
                document.addEventListener("DOMContentLoaded", function () {
                    new bootstrap.Modal(document.getElementById("modalEditarVenta")).show();
                });
            </script>
        </c:if>

        <c:if test="${esAdmin}">
            <div class="card mb-4 d-print-none">
                <div class="card-body">
                    <form method="get" action="${pageContext.request.contextPath}/Venta" class="row g-2 align-items-end">
                        <div class="col-md-6">
                            <label class="form-label">Ver ventas de</label>
                            <select class="form-select" name="idUsuarioFiltro" onchange="this.form.submit()">
                                <option value="">Todos los clientes</option>
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

        <c:if test="${not empty ventas}">
            <div class="card mb-4">
                <div class="card-body">
                    <h2 class="h5">Evolucion mensual de ventas (ultimos 12 meses)</h2>
                    <div class="grafico-barras">
                        <c:forEach var="barra" items="${evolucionMensual}">
                            <div class="barra-col" title="${barra.etiqueta}: ${barra.valorFormateado}">
                                <div class="barra-valor">${barra.valorFormateado}</div>
                                <div class="barra" style="height:${barra.alturaPorcentaje}%;"></div>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="grafico-etiquetas">
                        <c:forEach var="barra" items="${evolucionMensual}"><span>${barra.etiqueta}</span></c:forEach>
                    </div>
                </div>
            </div>
        </c:if>

        <div class="card" data-tabla-filtros>
            <div class="card-body">
                <div class="row g-2 align-items-end mb-3 d-print-none">
                    <div class="col-md-4">
                        <label class="form-label small mb-1">Buscar</label>
                        <input type="search" class="form-control form-control-sm" data-rol="buscador" placeholder="Concepto o descripcion...">
                    </div>
                    <div class="col-md-2">
                        <label class="form-label small mb-1">Desde</label>
                        <input type="date" class="form-control form-control-sm" data-rol="desde">
                    </div>
                    <div class="col-md-2">
                        <label class="form-label small mb-1">Hasta</label>
                        <input type="date" class="form-control form-control-sm" data-rol="hasta">
                    </div>
                    <div class="col-md-4">
                        <label class="form-label small mb-1">Estado</label>
                        <select class="form-select form-select-sm" data-rol="estado">
                            <option value="">Todos</option>
                            <option value="COBRADO">Cobrada</option>
                            <option value="PENDIENTE_DE_COBRO">Pendiente</option>
                            <option value="ANULADO">Anulada</option>
                        </select>
                    </div>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                        <tr>
                            <c:if test="${esAdmin}"><th data-ordenar="cliente">Cliente<span class="indicador-orden"></span></th></c:if>
                            <th data-ordenar="fecha">Fecha<span class="indicador-orden"></span></th>
                            <th data-ordenar="concepto">Concepto<span class="indicador-orden"></span></th>
                            <th data-ordenar="precio">Precio unit.<span class="indicador-orden"></span></th>
                            <th data-ordenar="cantidad">Cant.<span class="indicador-orden"></span></th>
                            <th data-ordenar="subtotal">Subtotal<span class="indicador-orden"></span></th>
                            <th data-ordenar="medio">Medio<span class="indicador-orden"></span></th>
                            <th data-ordenar="estado">Estado<span class="indicador-orden"></span></th>
                            <th class="d-print-none">Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="venta" items="${ventas}">
                            <tr data-fila
                                data-texto="${fn:toLowerCase(venta.conceptoVenta)} ${fn:toLowerCase(venta.descripcion)} ${fn:toLowerCase(venta.usuario.nombreCompleto)}"
                                data-fecha="<fmt:formatDate value="${venta.fecha}" pattern="yyyy-MM-dd"/>"
                                data-cliente="${fn:toLowerCase(venta.usuario.nombreCompleto)}"
                                data-concepto="${fn:toLowerCase(venta.conceptoVenta)}"
                                data-precio="${venta.precioUnitario}"
                                data-cantidad="${venta.cantidad}"
                                data-subtotal="${venta.subtotal}"
                                data-medio="${fn:toLowerCase(venta.metodo.nombre)}"
                                data-estado="${venta.estado}">
                                <c:if test="${esAdmin}"><td>${venta.usuario.nombreCompleto}</td></c:if>
                                <td><fmt:formatDate value="${venta.fecha}" pattern="dd/MM/yyyy"/></td>
                                <td>${venta.conceptoVenta}</td>
                                <td><fmt:formatNumber value="${venta.precioUnitario}" type="currency" currencySymbol="$"/></td>
                                <td>${venta.cantidad}</td>
                                <td><fmt:formatNumber value="${venta.subtotal}" type="currency" currencySymbol="$"/></td>
                                <td>${venta.metodo.nombre}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${venta.estado == 'COBRADO'}"><span class="badge text-bg-success">Cobrada</span></c:when>
                                        <c:when test="${venta.estado == 'PENDIENTE_DE_COBRO'}"><span class="badge text-bg-warning">Pendiente</span></c:when>
                                        <c:otherwise><span class="badge text-bg-secondary">Anulada</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="d-print-none">
                                    <div class="d-flex gap-1 flex-wrap">
                                        <a class="btn btn-sm btn-outline-secondary" href="${pageContext.request.contextPath}/Venta?editarId=${venta.idVenta}">Editar</a>
                                        <c:if test="${venta.estado == 'PENDIENTE_DE_COBRO'}">
                                            <form method="post" action="${pageContext.request.contextPath}/Venta" class="d-inline">
                                                <input type="hidden" name="action" value="marcarCobrada">
                                                <input type="hidden" name="idVenta" value="${venta.idVenta}">
                                                <button type="submit" class="btn btn-sm btn-success">Marcar cobrada</button>
                                            </form>
                                        </c:if>
                                        <c:if test="${venta.estado != 'ANULADO'}">
                                            <form method="post" action="${pageContext.request.contextPath}/Venta" class="d-inline" onsubmit="return confirmarAccion('¿Anular esta venta?');">
                                                <input type="hidden" name="action" value="anular">
                                                <input type="hidden" name="idVenta" value="${venta.idVenta}">
                                                <button type="submit" class="btn btn-sm btn-danger">Anular</button>
                                            </form>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr data-fila-vacia style="display:none;">
                            <td colspan="${esAdmin ? 9 : 8}" class="text-center text-secondary py-4">No se encontraron ventas con esos filtros.</td>
                        </tr>
                        <c:if test="${empty ventas}">
                            <tr><td colspan="${esAdmin ? 9 : 8}" class="text-center text-secondary py-4">Todavia no registro ninguna venta.</td></tr>
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
