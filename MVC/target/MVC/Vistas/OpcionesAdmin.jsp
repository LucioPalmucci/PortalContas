<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="edu.usal.jdbc.servicio.UsuarioServicio" %>
<%@ page import="edu.usal.jdbc.servicio.VencimientoServicio" %>
<%@ page import="edu.usal.jdbc.dominio.Usuario" %>
<%@ page import="edu.usal.jdbc.dominio.Vencimiento" %>
<%@ page import="java.util.List" %>
<c:choose>
    <c:when test="${empty sessionScope.idUsuario or sessionScope.rolUsuario != 'ADMINISTRADOR'}">
        <c:redirect url="/Vistas/Principal.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tituloPagina" value="Panel de administracion" scope="request"/>
        <%@ include file="/Vistas/fragmentos/Header.jsp" %>

        <%
            List<Usuario> usuarios = new UsuarioServicio().obtenerTodosLosUsuarios();
            long activos = usuarios.stream().filter(Usuario::isEstaActivo).count();
            VencimientoServicio vencimientoServicio = new VencimientoServicio();
            List<Vencimiento> vencimientos = vencimientoServicio.obtenerTodosLosVencimientos();
            long pendientes = vencimientos.stream().filter(v -> v.getEstado().name().equals("PENDIENTE")).count();
            long proximos = vencimientos.stream().filter(vencimientoServicio::esProximo).count();
            request.setAttribute("totalUsuarios", usuarios.size());
            request.setAttribute("usuariosActivos", activos);
            request.setAttribute("vencimientosPendientes", pendientes);
            request.setAttribute("vencimientosProximos", proximos);
        %>

        <div class="panel-bienvenida d-flex align-items-center gap-3">
            <div class="icono-circulo text-secondary" style="width:52px;height:52px;font-size:1.5rem;">
                <i class="bi bi-speedometer2" aria-hidden="true"></i>
            </div>
            <div>
                <h1 class="h3 mb-1">Bienvenido, ${sessionScope.nombreUsuarioSesion}</h1>
                <p class="mb-0">Panel de administracion del estudio.</p>
            </div>
        </div>

        <div class="row row-cols-1 row-cols-md-3 g-3 mb-4">
            <div class="col">
                <div class="card card-body h-100 border-start border-4 border-primary d-flex flex-row align-items-center gap-3">
                    <div class="icono-circulo bg-primary-subtle text-primary">
                        <i class="bi bi-people" aria-hidden="true"></i>
                    </div>
                    <div>
                        <div class="small text-secondary text-uppercase">Usuarios activos</div>
                        <div class="fs-3 fw-bold">${usuariosActivos} <span class="fs-6 text-secondary">/ ${totalUsuarios}</span></div>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card card-body h-100 border-start border-4 ${vencimientosPendientes > 0 ? 'border-danger' : 'border-secondary-subtle'} d-flex flex-row align-items-center gap-3">
                    <div class="icono-circulo ${vencimientosPendientes > 0 ? 'bg-danger-subtle text-danger' : 'bg-secondary-subtle text-secondary'}">
                        <i class="bi bi-exclamation-circle" aria-hidden="true"></i>
                    </div>
                    <div>
                        <div class="small text-secondary text-uppercase">Vencimientos pendientes</div>
                        <div class="fs-3 fw-bold ${vencimientosPendientes > 0 ? 'text-danger' : ''}">${vencimientosPendientes}</div>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card card-body h-100 border-start border-4 ${vencimientosProximos > 0 ? 'border-danger' : 'border-success'} d-flex flex-row align-items-center gap-3">
                    <div class="icono-circulo ${vencimientosProximos > 0 ? 'bg-danger-subtle text-danger' : 'bg-success-subtle text-success'}">
                        <i class="bi bi-calendar2-week" aria-hidden="true"></i>
                    </div>
                    <div>
                        <div class="small text-secondary text-uppercase">Vencen en 7 dias o menos</div>
                        <div class="fs-3 fw-bold ${vencimientosProximos > 0 ? 'text-danger' : 'text-success'}">${vencimientosProximos}</div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row row-cols-1 row-cols-md-3 g-3">
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/Usuario">
                    <div class="card-body d-flex align-items-start gap-3">
                        <div class="icono-circulo icono-serie-1"><i class="bi bi-people" aria-hidden="true"></i></div>
                        <div>
                            <h3 class="h5 mb-1">Usuarios</h3>
                            <p class="text-secondary mb-0">Crear, editar y activar o desactivar cuentas de clientes.</p>
                        </div>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/Vencimiento">
                    <div class="card-body d-flex align-items-start gap-3">
                        <div class="icono-circulo icono-serie-2"><i class="bi bi-calendar-event" aria-hidden="true"></i></div>
                        <div>
                            <h3 class="h5 mb-1">Calendario de vencimientos</h3>
                            <p class="text-secondary mb-0">Administrar fechas impositivas, importar y exportar el calendario.</p>
                        </div>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/Catalogo">
                    <div class="card-body d-flex align-items-start gap-3">
                        <div class="icono-circulo icono-serie-7"><i class="bi bi-tags" aria-hidden="true"></i></div>
                        <div>
                            <h3 class="h5 mb-1">Categorias y medios</h3>
                            <p class="text-secondary mb-0">Configurar los medios de pago/cobro y las categorias de conceptos.</p>
                        </div>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
        </div>

        <h2 class="h5 mt-4 border-start border-4 border-primary ps-2">Movimientos de usuarios</h2>
        <p class="text-secondary">Seleccione un tipo de movimiento y luego el usuario para ver su informacion.</p>
        <div class="row row-cols-1 row-cols-md-3 g-3 mb-4">
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/Venta">
                    <div class="card-body d-flex align-items-start gap-3">
                        <div class="icono-circulo icono-serie-5"><i class="bi bi-cart-check" aria-hidden="true"></i></div>
                        <div>
                            <h3 class="h5 mb-1">Ventas</h3>
                            <p class="text-secondary mb-0">Ver y editar las ventas de un usuario.</p>
                        </div>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/Compra">
                    <div class="card-body d-flex align-items-start gap-3">
                        <div class="icono-circulo icono-serie-1"><i class="bi bi-bag" aria-hidden="true"></i></div>
                        <div>
                            <h3 class="h5 mb-1">Compras</h3>
                            <p class="text-secondary mb-0">Ver y editar las compras de un usuario.</p>
                        </div>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/Gasto">
                    <div class="card-body d-flex align-items-start gap-3">
                        <div class="icono-circulo icono-serie-3"><i class="bi bi-receipt" aria-hidden="true"></i></div>
                        <div>
                            <h3 class="h5 mb-1">Gastos</h3>
                            <p class="text-secondary mb-0">Ver y editar los gastos de un usuario.</p>
                        </div>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/OtroIngreso">
                    <div class="card-body d-flex align-items-start gap-3">
                        <div class="icono-circulo icono-serie-4"><i class="bi bi-plus-circle" aria-hidden="true"></i></div>
                        <div>
                            <h3 class="h5 mb-1">Otros ingresos</h3>
                            <p class="text-secondary mb-0">Ver y editar otros ingresos de un usuario.</p>
                        </div>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/OtroEgreso">
                    <div class="card-body d-flex align-items-start gap-3">
                        <div class="icono-circulo icono-serie-2"><i class="bi bi-dash-circle" aria-hidden="true"></i></div>
                        <div>
                            <h3 class="h5 mb-1">Otros egresos</h3>
                            <p class="text-secondary mb-0">Ver y editar otros egresos de un usuario.</p>
                        </div>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
        </div>

        <h2 class="h5 mt-4 border-start border-4 border-primary ps-2">Reportes de usuarios</h2>
        <p class="text-secondary">Consulte la tesoreria, el estado de resultados o el estado financiero de un usuario.</p>
        <div class="row row-cols-1 row-cols-md-3 g-3 mb-4">
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/Tesoreria">
                    <div class="card-body d-flex align-items-start gap-3">
                        <div class="icono-circulo icono-serie-7"><i class="bi bi-wallet2" aria-hidden="true"></i></div>
                        <div>
                            <h3 class="h5 mb-1">Tesoreria</h3>
                            <p class="text-secondary mb-0">Cuentas por cobrar y por pagar de un usuario, o consolidado.</p>
                        </div>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/EstadoResultados">
                    <div class="card-body d-flex align-items-start gap-3">
                        <div class="icono-circulo icono-serie-6"><i class="bi bi-bar-chart" aria-hidden="true"></i></div>
                        <div>
                            <h3 class="h5 mb-1">Estado de resultados</h3>
                            <p class="text-secondary mb-0">Calcule la ganancia de un periodo para un usuario.</p>
                        </div>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
            <div class="col">
                <a class="card h-100 position-relative card-enlace" href="${pageContext.request.contextPath}/EstadoFinanciero">
                    <div class="card-body d-flex align-items-start gap-3">
                        <div class="icono-circulo icono-serie-8"><i class="bi bi-graph-up" aria-hidden="true"></i></div>
                        <div>
                            <h3 class="h5 mb-1">Estado financiero</h3>
                            <p class="text-secondary mb-0">Tendencias, alertas y comparaciones de un usuario.</p>
                        </div>
                    </div>
                    <span class="stretched-link"></span>
                </a>
            </div>
        </div>

        <%@ include file="/Vistas/fragmentos/Footer.jsp" %>
    </c:otherwise>
</c:choose>
