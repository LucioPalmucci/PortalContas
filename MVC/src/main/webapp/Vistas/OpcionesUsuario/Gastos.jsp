<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:choose>
    <c:when test="${empty sessionScope.idUsuario}">
        <c:redirect url="/Vistas/Principal.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tituloPagina" value="Gastos" scope="request"/>
        <%@ include file="/Vistas/fragmentos/Header.jsp" %>

        <h1>Mis gastos</h1>
        <p class="text-secondary">Registre alquiler, servicios, sueldos y demas gastos operativos.</p>

        <%@ include file="/Vistas/fragmentos/Mensajes.jsp" %>

        <div class="card mb-4">
            <div class="card-header">
                <button class="btn btn-link text-decoration-none fw-bold p-0" type="button" data-bs-toggle="collapse" data-bs-target="#formAgregarGasto">
                    + Registrar nuevo gasto
                </button>
            </div>
            <div class="collapse" id="formAgregarGasto">
                <div class="card-body">
                    <form method="post" action="${pageContext.request.contextPath}/Gasto">
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
                                <label for="idCategoria" class="form-label">Categoria</label>
                                <select class="form-select" id="idCategoria" name="idCategoria" required>
                                    <option value="">Seleccione...</option>
                                    <c:forEach var="categoria" items="${categorias}">
                                        <option value="${categoria.idCategoria}">${categoria.nombre}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <label for="valor" class="form-label">Monto ($)</label>
                                <input type="number" class="form-control" id="valor" name="valor" min="0.01" step="0.01" required>
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
                            <div class="col-md-4">
                                <label for="estado" class="form-label">Estado</label>
                                <select class="form-select" id="estado" name="estado">
                                    <option value="PENDIENTE_DE_PAGO">Pendiente de pago</option>
                                    <option value="PAGADO">Pagado</option>
                                </select>
                            </div>
                            <div class="col-12">
                                <label for="descripcion" class="form-label">Descripcion (opcional)</label>
                                <textarea class="form-control" id="descripcion" name="descripcion"></textarea>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary mt-3">Guardar gasto</button>
                    </form>
                </div>
            </div>
        </div>

        <c:if test="${not empty gastoAEditar}">
            <div class="card mb-4">
                <div class="card-header">Editar gasto #${gastoAEditar.idGasto}</div>
                <div class="card-body">
                    <form method="post" action="${pageContext.request.contextPath}/Gasto">
                        <input type="hidden" name="action" value="editar">
                        <input type="hidden" name="idGasto" value="${gastoAEditar.idGasto}">
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label for="ecategoria" class="form-label">Categoria</label>
                                <select class="form-select" id="ecategoria" name="idCategoria" required>
                                    <c:forEach var="categoria" items="${categorias}">
                                        <option value="${categoria.idCategoria}" ${categoria.idCategoria == gastoAEditar.categoria.idCategoria ? 'selected' : ''}>${categoria.nombre}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label for="evalor" class="form-label">Monto ($)</label>
                                <input type="number" class="form-control" id="evalor" name="valor" min="0.01" step="0.01" value="${gastoAEditar.valor}" required>
                            </div>
                            <div class="col-md-6">
                                <label for="emetodo" class="form-label">Medio de pago</label>
                                <select class="form-select" id="emetodo" name="idMetodo" required>
                                    <c:forEach var="metodo" items="${metodosPago}">
                                        <option value="${metodo.idMetodo}" ${metodo.idMetodo == gastoAEditar.metodo.idMetodo ? 'selected' : ''}>${metodo.nombre}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label for="eestado" class="form-label">Estado</label>
                                <select class="form-select" id="eestado" name="estado">
                                    <option value="PENDIENTE_DE_PAGO" ${gastoAEditar.estado == 'PENDIENTE_DE_PAGO' ? 'selected' : ''}>Pendiente de pago</option>
                                    <option value="PAGADO" ${gastoAEditar.estado == 'PAGADO' ? 'selected' : ''}>Pagado</option>
                                    <option value="ANULADO" ${gastoAEditar.estado == 'ANULADO' ? 'selected' : ''}>Anulado</option>
                                </select>
                            </div>
                            <div class="col-12">
                                <label for="edescripcion" class="form-label">Descripcion (opcional)</label>
                                <textarea class="form-control" id="edescripcion" name="descripcion">${gastoAEditar.descripcion}</textarea>
                            </div>
                        </div>
                        <div class="d-flex gap-2 mt-3">
                            <button type="submit" class="btn btn-primary">Guardar cambios</button>
                            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/Gasto">Cancelar</a>
                        </div>
                    </form>
                </div>
            </div>
        </c:if>

        <c:if test="${esAdmin}">
            <div class="card mb-4 d-print-none">
                <div class="card-body">
                    <form method="get" action="${pageContext.request.contextPath}/Gasto" class="row g-2 align-items-end">
                        <div class="col-md-6">
                            <label class="form-label">Ver gastos de</label>
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

        <c:if test="${not empty gastos}">
            <div class="card mb-4">
                <div class="card-body">
                    <h2 class="h5">Evolucion mensual de gastos (ultimos 12 meses)</h2>
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
                        <input type="search" class="form-control form-control-sm" data-rol="buscador" placeholder="Categoria o descripcion...">
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
                            <option value="PAGADO">Pagado</option>
                            <option value="PENDIENTE_DE_PAGO">Pendiente</option>
                            <option value="ANULADO">Anulado</option>
                        </select>
                    </div>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                        <tr>
                            <c:if test="${esAdmin}"><th data-ordenar="cliente">Cliente<span class="indicador-orden"></span></th></c:if>
                            <th data-ordenar="fecha">Fecha<span class="indicador-orden"></span></th>
                            <th data-ordenar="categoria">Categoria<span class="indicador-orden"></span></th>
                            <th data-ordenar="monto">Monto<span class="indicador-orden"></span></th>
                            <th data-ordenar="medio">Medio<span class="indicador-orden"></span></th>
                            <th data-ordenar="estado">Estado<span class="indicador-orden"></span></th>
                            <th class="d-print-none">Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="gasto" items="${gastos}">
                            <tr data-fila
                                data-texto="${fn:toLowerCase(gasto.categoria.nombre)} ${fn:toLowerCase(gasto.descripcion)} ${fn:toLowerCase(gasto.usuario.nombreCompleto)}"
                                data-fecha="<fmt:formatDate value="${gasto.fecha}" pattern="yyyy-MM-dd"/>"
                                data-cliente="${fn:toLowerCase(gasto.usuario.nombreCompleto)}"
                                data-categoria="${fn:toLowerCase(gasto.categoria.nombre)}"
                                data-monto="${gasto.valor}"
                                data-medio="${fn:toLowerCase(gasto.metodo.nombre)}"
                                data-estado="${gasto.estado}">
                                <c:if test="${esAdmin}"><td>${gasto.usuario.nombreCompleto}</td></c:if>
                                <td><fmt:formatDate value="${gasto.fecha}" pattern="dd/MM/yyyy"/></td>
                                <td>${gasto.categoria.nombre}</td>
                                <td><fmt:formatNumber value="${gasto.valor}" type="currency" currencySymbol="$"/></td>
                                <td>${gasto.metodo.nombre}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${gasto.estado == 'PAGADO'}"><span class="badge text-bg-success">Pagado</span></c:when>
                                        <c:when test="${gasto.estado == 'PENDIENTE_DE_PAGO'}"><span class="badge text-bg-warning">Pendiente</span></c:when>
                                        <c:otherwise><span class="badge text-bg-secondary">Anulado</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="d-print-none">
                                    <div class="d-flex gap-1 flex-wrap">
                                        <a class="btn btn-sm btn-outline-secondary" href="${pageContext.request.contextPath}/Gasto?editarId=${gasto.idGasto}">Editar</a>
                                        <c:if test="${gasto.estado == 'PENDIENTE_DE_PAGO'}">
                                            <form method="post" action="${pageContext.request.contextPath}/Gasto" class="d-inline">
                                                <input type="hidden" name="action" value="marcarPagado">
                                                <input type="hidden" name="idGasto" value="${gasto.idGasto}">
                                                <button type="submit" class="btn btn-sm btn-success">Marcar pagado</button>
                                            </form>
                                        </c:if>
                                        <c:if test="${gasto.estado != 'ANULADO'}">
                                            <form method="post" action="${pageContext.request.contextPath}/Gasto" class="d-inline" onsubmit="return confirmarAccion('¿Anular este gasto?');">
                                                <input type="hidden" name="action" value="anular">
                                                <input type="hidden" name="idGasto" value="${gasto.idGasto}">
                                                <button type="submit" class="btn btn-sm btn-danger">Anular</button>
                                            </form>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr data-fila-vacia style="display:none;">
                            <td colspan="${esAdmin ? 7 : 6}" class="text-center text-secondary py-4">No se encontraron gastos con esos filtros.</td>
                        </tr>
                        <c:if test="${empty gastos}">
                            <tr><td colspan="${esAdmin ? 7 : 6}" class="text-center text-secondary py-4">Todavia no registro ningun gasto.</td></tr>
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
