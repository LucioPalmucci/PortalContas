<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:choose>
    <c:when test="${empty sessionScope.idUsuario or sessionScope.rolUsuario != 'ADMINISTRADOR'}">
        <c:redirect url="/Vistas/Principal.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tituloPagina" value="Usuarios" scope="request"/>
        <%@ include file="/Vistas/fragmentos/Header.jsp" %>

        <h1>Gestion de usuarios</h1>
        <p class="text-secondary">Cree, edite y active o desactive las cuentas de los clientes del estudio.</p>

        <%@ include file="/Vistas/fragmentos/Mensajes.jsp" %>

        <div class="card mb-4">
            <div class="card-header">
                <button class="btn btn-link text-decoration-none fw-bold p-0" type="button" data-bs-toggle="collapse" data-bs-target="#formCrearUsuario">
                    + Crear nuevo usuario
                </button>
            </div>
            <div class="collapse" id="formCrearUsuario">
                <div class="card-body">
                    <form method="post" action="${pageContext.request.contextPath}/Usuario">
                        <input type="hidden" name="action" value="agregar">
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label for="nombreCompleto" class="form-label">Nombre completo</label>
                                <input type="text" class="form-control" id="nombreCompleto" name="nombreCompleto" required>
                            </div>
                            <div class="col-md-6">
                                <label for="telefono" class="form-label">Telefono</label>
                                <input type="tel" class="form-control" id="telefono" name="telefono">
                            </div>
                            <div class="col-md-6">
                                <label for="correoElectronico" class="form-label">Correo electronico</label>
                                <input type="text" class="form-control" id="correoElectronico" name="correoElectronico" required>
                            </div>
                            <div class="col-md-6">
                                <label for="nombreUsuario" class="form-label">Nombre de usuario</label>
                                <input type="text" class="form-control" id="nombreUsuario" name="nombreUsuario" required>
                            </div>
                            <div class="col-md-6">
                                <label for="contrasena" class="form-label">Contraseña</label>
                                <input type="password" class="form-control" id="contrasena" name="contrasena" minlength="8" required>
                                <div class="form-text">Minimo 8 caracteres, con al menos una mayuscula y un numero.</div>
                            </div>
                            <div class="col-md-6">
                                <label for="rol" class="form-label">Rol</label>
                                <select class="form-select" id="rol" name="rol" required>
                                    <option value="USUARIO">Usuario (cliente)</option>
                                    <option value="ADMINISTRADOR">Administrador</option>
                                </select>
                            </div>
                            <div class="col-12">
                                <label for="descripcion" class="form-label">Notas internas (opcional, solo visible para administradores)</label>
                                <textarea class="form-control" id="descripcion" name="descripcion"></textarea>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary mt-3">Crear usuario</button>
                    </form>
                </div>
            </div>
        </div>

        <c:if test="${not empty usuarioAEditar}">
            <div class="card mb-4">
                <div class="card-header">Editar usuario: ${usuarioAEditar.nombreCompleto}</div>
                <div class="card-body">
                    <form method="post" action="${pageContext.request.contextPath}/Usuario">
                        <input type="hidden" name="action" value="editar">
                        <input type="hidden" name="idUsuario" value="${usuarioAEditar.idUsuario}">
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label for="enombre" class="form-label">Nombre completo</label>
                                <input type="text" class="form-control" id="enombre" name="nombreCompleto" value="${usuarioAEditar.nombreCompleto}" required>
                            </div>
                            <div class="col-md-6">
                                <label for="etelefono" class="form-label">Telefono</label>
                                <input type="tel" class="form-control" id="etelefono" name="telefono" value="${usuarioAEditar.telefono}">
                            </div>
                            <div class="col-md-6">
                                <label for="ecorreo" class="form-label">Correo electronico</label>
                                <input type="text" class="form-control" id="ecorreo" name="correoElectronico" value="${usuarioAEditar.correoElectronico}" required>
                            </div>
                            <div class="col-md-6">
                                <label for="erol" class="form-label">Rol</label>
                                <select class="form-select" id="erol" name="rol" required>
                                    <option value="USUARIO" ${usuarioAEditar.rol == 'USUARIO' ? 'selected' : ''}>Usuario (cliente)</option>
                                    <option value="ADMINISTRADOR" ${usuarioAEditar.rol == 'ADMINISTRADOR' ? 'selected' : ''}>Administrador</option>
                                </select>
                            </div>
                            <div class="col-12">
                                <label for="edescripcion" class="form-label">Notas internas</label>
                                <textarea class="form-control" id="edescripcion" name="descripcion">${usuarioAEditar.descripcion}</textarea>
                            </div>
                        </div>
                        <div class="d-flex gap-2 mt-3">
                            <button type="submit" class="btn btn-primary">Guardar cambios</button>
                            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/Usuario">Cancelar</a>
                        </div>
                    </form>
                    <hr>
                    <form method="post" action="${pageContext.request.contextPath}/Usuario" class="row g-3 align-items-end">
                        <input type="hidden" name="action" value="cambiarContrasena">
                        <input type="hidden" name="idUsuario" value="${usuarioAEditar.idUsuario}">
                        <div class="col-md-6">
                            <label for="nuevaContrasena" class="form-label">Restablecer contraseña</label>
                            <input type="password" class="form-control" id="nuevaContrasena" name="nuevaContrasena" minlength="8" required>
                        </div>
                        <div class="col-auto">
                            <button type="submit" class="btn btn-outline-secondary">Actualizar contraseña</button>
                        </div>
                    </form>
                </div>
            </div>
        </c:if>

        <div class="card" data-tabla-filtros>
            <div class="card-body">
                <div class="row g-2 align-items-end mb-3">
                    <div class="col-md-6">
                        <label class="form-label small mb-1">Buscar</label>
                        <input type="search" class="form-control form-control-sm" data-rol="buscador" placeholder="Nombre, correo o usuario...">
                    </div>
                    <div class="col-md-3">
                        <label class="form-label small mb-1">Estado</label>
                        <select class="form-select form-select-sm" data-rol="estado">
                            <option value="">Todos</option>
                            <option value="ACTIVO">Activo</option>
                            <option value="INACTIVO">Inactivo</option>
                        </select>
                    </div>
                </div>
                <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light">
                    <tr>
                        <th data-ordenar="nombre">Nombre<span class="indicador-orden"></span></th>
                        <th data-ordenar="correo">Correo<span class="indicador-orden"></span></th>
                        <th data-ordenar="usuario">Usuario<span class="indicador-orden"></span></th>
                        <th data-ordenar="tipocuenta">Rol<span class="indicador-orden"></span></th>
                        <th data-ordenar="estado">Estado<span class="indicador-orden"></span></th>
                        <th data-ordenar="ultimoacceso">Ultimo acceso<span class="indicador-orden"></span></th>
                        <th>Ultima modificacion</th>
                        <th>Acciones</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="usuario" items="${usuarios}">
                        <tr data-fila
                            data-texto="${fn:toLowerCase(usuario.nombreCompleto)} ${fn:toLowerCase(usuario.correoElectronico)} ${fn:toLowerCase(usuario.nombreUsuario)}"
                            data-nombre="${fn:toLowerCase(usuario.nombreCompleto)}"
                            data-correo="${fn:toLowerCase(usuario.correoElectronico)}"
                            data-usuario="${fn:toLowerCase(usuario.nombreUsuario)}"
                            data-tipocuenta="${usuario.rol}"
                            data-estado="${usuario.estaActivo ? 'ACTIVO' : 'INACTIVO'}"
                            data-ultimoacceso="<c:if test="${not empty usuario.ultimoAcceso}"><fmt:formatDate value="${usuario.ultimoAcceso}" pattern="yyyy-MM-dd'T'HH:mm:ss"/></c:if>">
                            <td>${usuario.nombreCompleto}</td>
                            <td>${usuario.correoElectronico}</td>
                            <td>${usuario.nombreUsuario}</td>
                            <td>${usuario.rol == 'ADMINISTRADOR' ? 'Administrador' : 'Usuario'}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${usuario.estaActivo}"><span class="badge text-bg-success">Activo</span></c:when>
                                    <c:otherwise><span class="badge text-bg-secondary">Inactivo</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:if test="${not empty usuario.ultimoAcceso}"><fmt:formatDate value="${usuario.ultimoAcceso}" pattern="dd/MM/yyyy HH:mm"/></c:if>
                                <c:if test="${empty usuario.ultimoAcceso}"><span class="text-secondary">Nunca</span></c:if>
                            </td>
                            <td>
                                <c:if test="${not empty usuario.fechaModificacion}">
                                    <fmt:formatDate value="${usuario.fechaModificacion}" pattern="dd/MM/yyyy HH:mm"/>
                                    <c:if test="${not empty usuario.modificadoPor}"><br><span class="text-secondary small">por ${usuario.modificadoPor.nombreCompleto}</span></c:if>
                                </c:if>
                                <c:if test="${empty usuario.fechaModificacion}"><span class="text-secondary">-</span></c:if>
                            </td>
                            <td>
                                <div class="d-flex gap-1 flex-wrap">
                                    <a class="btn btn-sm btn-outline-secondary" href="${pageContext.request.contextPath}/Usuario?editarId=${usuario.idUsuario}">Editar</a>
                                    <c:choose>
                                        <c:when test="${usuario.estaActivo}">
                                            <button type="button" class="btn btn-sm btn-danger" data-bs-toggle="modal" data-bs-target="#modalDesactivar${usuario.idUsuario}">Desactivar</button>
                                            <div class="modal fade" id="modalDesactivar${usuario.idUsuario}" tabindex="-1" aria-hidden="true">
                                                <div class="modal-dialog">
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title">Confirmar desactivacion</h5>
                                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                                                        </div>
                                                        <div class="modal-body">
                                                            ¿Desactivar a <strong>${usuario.nombreCompleto}</strong>? Perdera acceso al sistema hasta que un administrador lo reactive.
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancelar</button>
                                                            <form method="post" action="${pageContext.request.contextPath}/Usuario" class="d-inline">
                                                                <input type="hidden" name="action" value="desactivar">
                                                                <input type="hidden" name="idUsuario" value="${usuario.idUsuario}">
                                                                <button type="submit" class="btn btn-danger">Desactivar</button>
                                                            </form>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <form method="post" action="${pageContext.request.contextPath}/Usuario" class="d-inline">
                                                <input type="hidden" name="action" value="activar">
                                                <input type="hidden" name="idUsuario" value="${usuario.idUsuario}">
                                                <button type="submit" class="btn btn-sm btn-success">Activar</button>
                                            </form>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr data-fila-vacia style="display:none;">
                        <td colspan="8" class="text-center text-secondary py-4">No se encontraron usuarios con esos filtros.</td>
                    </tr>
                    <c:if test="${empty usuarios}">
                        <tr><td colspan="8" class="text-center text-secondary py-4">No se encontraron usuarios.</td></tr>
                    </c:if>
                    </tbody>
                </table>
                </div>
                <div class="d-flex justify-content-between align-items-center mt-3">
                    <small class="text-secondary" data-rol="contador"></small>
                    <div data-rol="paginador"></div>
                </div>
            </div>
        </div>

        <%@ include file="/Vistas/fragmentos/Footer.jsp" %>
    </c:otherwise>
</c:choose>
