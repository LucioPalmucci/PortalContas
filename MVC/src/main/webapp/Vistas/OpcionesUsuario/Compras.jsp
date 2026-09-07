<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:choose>
    <c:when test="${empty sessionScope.idUsuario}">
        <c:redirect url="/Vistas/Principal.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tituloPagina" value="Compras" scope="request"/>
        <%@ include file="/Vistas/fragmentos/Header.jsp" %>

        <div class="d-flex align-items-center gap-3 mb-4">
            <div class="icono-circulo icono-serie-1" style="width:52px;height:52px;font-size:1.5rem;">
                <i class="bi bi-bag" aria-hidden="true"></i>
            </div>
            <div>
                <c:choose>
                    <c:when test="${esAdmin}">
                        <h1 class="h3 mb-1">Compras de usuarios</h1>
                        <p class="text-secondary mb-0">Seleccione un usuario para ver sus compras. El total se calcula automaticamente.</p>
                    </c:when>
                    <c:otherwise>
                        <h1 class="h3 mb-1">Mis compras</h1>
                        <p class="text-secondary mb-0">Registre cada compra de mercaderia que realiza. El total se calcula automaticamente.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <%@ include file="/Vistas/fragmentos/Mensajes.jsp" %>

        <div class="card mb-4">
            <div class="card-header">
                <button class="btn btn-link text-decoration-none fw-bold p-0 d-flex align-items-center gap-2 texto-serie-1" type="button" data-bs-toggle="collapse" data-bs-target="#formAgregarCompra">
                    <i class="bi bi-plus-circle" aria-hidden="true"></i>
                    Registrar nueva compra
                </button>
            </div>
            <div class="collapse" id="formAgregarCompra">
                <div class="card-body">
                    <form method="post" action="${pageContext.request.contextPath}/Compra">
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
                                <label for="conceptoCompra" class="form-label">Concepto</label>
                                <input type="text" class="form-control" id="conceptoCompra" name="conceptoCompra" placeholder="Ej: Compra de mercaderia" required>
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
                                <label for="idMetodo" class="form-label">Medio de pago</label>
                                <select class="form-select" id="idMetodo" name="idMetodo" required>
                                    <option value="">Seleccione...</option>
                                    <c:forEach var="metodo" items="${metodosPago}">
                                        <option value="${metodo.idMetodo}">${metodo.nombre}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label for="estado" class="form-label">Estado</label>
                                <select class="form-select" id="estado" name="estado">
                                    <option value="PENDIENTE_DE_PAGO">Pendiente de pago</option>
                                    <option value="PAGADO">Pagada</option>
                                </select>
                            </div>
                            <div class="col-12">
                                <label for="descripcion" class="form-label">Descripcion (opcional)</label>
                                <textarea class="form-control" id="descripcion" name="descripcion"></textarea>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary mt-3"><i class="bi bi-check-lg me-1" aria-hidden="true"></i>Guardar compra</button>
                    </form>
                </div>
            </div>
        </div>

        <c:if test="${not empty compraAEditar}">
            <div class="modal fade" id="modalEditarCompra" tabindex="-1" aria-labelledby="modalEditarCompraLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <form method="post" action="${pageContext.request.contextPath}/Compra">
                            <div class="modal-header">
                                <h5 class="modal-title d-flex align-items-center gap-2" id="modalEditarCompraLabel"><i class="bi bi-pencil-square texto-serie-1" aria-hidden="true"></i>Editar compra #${compraAEditar.idCompra}</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar" onclick="window.location='${pageContext.request.contextPath}/Compra'"></button>
                            </div>
                            <div class="modal-body">
                                <input type="hidden" name="action" value="editar">
                                <input type="hidden" name="idCompra" value="${compraAEditar.idCompra}">
                                <div class="row g-3">
                                    <div class="col-md-4">
                                        <label for="efecha" class="form-label">Fecha</label>
                                        <input type="date" class="form-control" id="efecha" name="fecha" value="<fmt:formatDate value="${compraAEditar.fecha}" pattern="yyyy-MM-dd"/>" required>
                                    </div>
                                    <div class="col-md-8">
                                        <label for="econcepto" class="form-label">Concepto</label>
                                        <input type="text" class="form-control" id="econcepto" name="conceptoCompra" value="${compraAEditar.conceptoCompra}" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="eprecio" class="form-label">Precio unitario ($)</label>
                                        <input type="number" class="form-control" id="eprecio" name="precioUnitario" min="0.01" step="0.01" value="${compraAEditar.precioUnitario}" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="ecantidad" class="form-label">Cantidad</label>
                                        <input type="number" class="form-control" id="ecantidad" name="cantidad" min="1" step="1" value="${compraAEditar.cantidad}" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="emetodo" class="form-label">Medio de pago</label>
                                        <select class="form-select" id="emetodo" name="idMetodo" required>
                                            <c:forEach var="metodo" items="${metodosPago}">
                                                <option value="${metodo.idMetodo}" ${metodo.idMetodo == compraAEditar.metodo.idMetodo ? 'selected' : ''}>${metodo.nombre}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="eestado" class="form-label">Estado</label>
                                        <select class="form-select" id="eestado" name="estado">
                                            <option value="PENDIENTE_DE_PAGO" ${compraAEditar.estado == 'PENDIENTE_DE_PAGO' ? 'selected' : ''}>Pendiente de pago</option>
                                            <option value="PAGADO" ${compraAEditar.estado == 'PAGADO' ? 'selected' : ''}>Pagada</option>
                                            <option value="ANULADO" ${compraAEditar.estado == 'ANULADO' ? 'selected' : ''}>Anulada</option>
                                        </select>
                                    </div>
                                    <div class="col-12">
                                        <label for="edescripcion" class="form-label">Descripcion (opcional)</label>
                                        <textarea class="form-control" id="edescripcion" name="descripcion">${compraAEditar.descripcion}</textarea>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/Compra">Cancelar</a>
                                <button type="submit" class="btn btn-primary">Guardar cambios</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <script>
                document.addEventListener("DOMContentLoaded", function () {
                    new bootstrap.Modal(document.getElementById("modalEditarCompra")).show();
                });
            </script>
        </c:if>

        <c:if test="${esAdmin}">
            <div class="card mb-4 d-print-none">
                <div class="card-body">
                    <form method="get" action="${pageContext.request.contextPath}/Compra" class="row g-2 align-items-end">
                        <div class="col-md-6">
                            <label class="form-label d-flex align-items-center gap-2"><i class="bi bi-funnel texto-serie-1" aria-hidden="true"></i>Ver compras de</label>
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

        <c:if test="${not empty compras}">
            <div class="card mb-4">
                <div class="card-body">
                    <h2 class="h5 border-start border-4 borde-serie-1 ps-2 d-flex align-items-center gap-2"><i class="bi bi-graph-up texto-serie-1" aria-hidden="true"></i>Evolucion mensual de compras (ultimos 12 meses)</h2>
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
                <h2 class="h5 border-start border-4 borde-serie-1 ps-2 d-flex align-items-center gap-2"><i class="bi bi-list-ul texto-serie-1" aria-hidden="true"></i>Historial de compras</h2>
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
                            <option value="PAGADO">Pagada</option>
                            <option value="PENDIENTE_DE_PAGO">Pendiente</option>
                            <option value="ANULADO">Anulada</option>
                        </select>
                    </div>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0 filas-espaciadas">
                        <thead class="table-light">
                        <tr>
                            <c:if test="${esAdmin}"><th data-ordenar="cliente">Cliente<span class="indicador-orden"></span></th></c:if>
                            <th data-ordenar="fecha">Fecha<span class="indicador-orden"></span></th>
                            <th data-ordenar="concepto">Concepto<span class="indicador-orden"></span></th>
                            <th data-ordenar="precio">Precio unit.<span class="indicador-orden"></span></th>
                            <th data-ordenar="cantidad">Cant.<span class="indicador-orden"></span></th>
                            <th data-ordenar="total">Total<span class="indicador-orden"></span></th>
                            <th data-ordenar="medio">Medio<span class="indicador-orden"></span></th>
                            <th data-ordenar="estado">Estado<span class="indicador-orden"></span></th>
                            <th class="d-print-none">Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="compra" items="${compras}">
                            <tr data-fila
                                data-texto="${fn:toLowerCase(compra.conceptoCompra)} ${fn:toLowerCase(compra.descripcion)} ${fn:toLowerCase(compra.usuario.nombreCompleto)}"
                                data-fecha="<fmt:formatDate value="${compra.fecha}" pattern="yyyy-MM-dd"/>"
                                data-cliente="${fn:toLowerCase(compra.usuario.nombreCompleto)}"
                                data-concepto="${fn:toLowerCase(compra.conceptoCompra)}"
                                data-precio="${compra.precioUnitario}"
                                data-cantidad="${compra.cantidad}"
                                data-total="${compra.valorTotal}"
                                data-medio="${fn:toLowerCase(compra.metodo.nombre)}"
                                data-estado="${compra.estado}">
                                <c:if test="${esAdmin}"><td>${compra.usuario.nombreCompleto}</td></c:if>
                                <td><fmt:formatDate value="${compra.fecha}" pattern="dd/MM/yyyy"/></td>
                                <td class="fw-semibold">${compra.conceptoCompra}</td>
                                <td><fmt:formatNumber value="${compra.precioUnitario}" type="currency" currencySymbol="$"/></td>
                                <td>${compra.cantidad}</td>
                                <td class="fw-semibold"><fmt:formatNumber value="${compra.valorTotal}" type="currency" currencySymbol="$"/></td>
                                <td>${compra.metodo.nombre}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${compra.estado == 'PAGADO'}"><span class="badge rounded-pill bg-success-subtle text-success-emphasis">Pagada</span></c:when>
                                        <c:when test="${compra.estado == 'PENDIENTE_DE_PAGO'}"><span class="badge rounded-pill bg-warning-subtle text-warning-emphasis">Pendiente</span></c:when>
                                        <c:otherwise><span class="badge rounded-pill bg-secondary-subtle text-secondary-emphasis">Anulada</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="d-print-none">
                                    <div class="d-flex gap-1 flex-wrap">
                                        <a class="btn btn-sm btn-outline-secondary rounded-pill" href="${pageContext.request.contextPath}/Compra?editarId=${compra.idCompra}"><i class="bi bi-pencil" aria-hidden="true"></i> Editar</a>
                                        <c:if test="${compra.estado == 'PENDIENTE_DE_PAGO'}">
                                            <form method="post" action="${pageContext.request.contextPath}/Compra" class="d-inline">
                                                <input type="hidden" name="action" value="marcarPagada">
                                                <input type="hidden" name="idCompra" value="${compra.idCompra}">
                                                <button type="submit" class="btn btn-sm btn-success rounded-pill"><i class="bi bi-check2" aria-hidden="true"></i> Marcar pagada</button>
                                            </form>
                                        </c:if>
                                        <c:if test="${compra.estado != 'ANULADO'}">
                                            <form method="post" action="${pageContext.request.contextPath}/Compra" class="d-inline" onsubmit="return confirmarAccion('¿Anular esta compra?');">
                                                <input type="hidden" name="action" value="anular">
                                                <input type="hidden" name="idCompra" value="${compra.idCompra}">
                                                <button type="submit" class="btn btn-sm btn-danger rounded-pill"><i class="bi bi-x-circle" aria-hidden="true"></i> Anular</button>
                                            </form>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr data-fila-vacia style="display:none;">
                            <td colspan="${esAdmin ? 9 : 8}" class="text-center text-secondary py-4">No se encontraron compras con esos filtros.</td>
                        </tr>
                        <c:if test="${empty compras}">
                            <tr><td colspan="${esAdmin ? 9 : 8}" class="text-center text-secondary py-4">Todavia no registro ninguna compra.</td></tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>
                <div class="d-flex justify-content-end align-items-center mt-3 d-print-none">
                    <div data-rol="paginador"></div>
                </div>
            </div>
        </div>

        <%@ include file="/Vistas/fragmentos/Footer.jsp" %>
    </c:otherwise>
</c:choose>
