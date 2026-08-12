<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
    <c:when test="${empty sessionScope.idUsuario}">
        <c:redirect url="/Vistas/Principal.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tituloPagina" value="Manual de uso" scope="request"/>
        <%@ include file="/Vistas/fragmentos/Header.jsp" %>

        <div class="d-flex align-items-center justify-content-between flex-wrap gap-2 mb-3">
            <h1 class="mb-0">Manual de uso</h1>
            <button type="button" class="btn btn-outline-secondary d-print-none" onclick="imprimirPagina()">Imprimir / guardar como PDF</button>
        </div>

        <div class="card mb-3">
            <div class="card-body">
                <h2 class="h5">1. Ingresar al sistema</h2>
                <p class="mb-0">Ingrese su usuario y contraseña en la pantalla de inicio. Si marca "Recordarme", su sesion se mantendra activa por mas tiempo en ese dispositivo.</p>
            </div>
        </div>

        <div class="card mb-3">
            <div class="card-body">
                <h2 class="h5">2. Registrar movimientos</h2>
                <p>Desde el menu superior puede acceder a Ventas, Compras, Gastos, Otros ingresos y Otros egresos. En cada seccion:</p>
                <ul class="mb-0">
                    <li>Abra "Registrar nuevo" para cargar un movimiento.</li>
                    <li>Complete fecha, monto y medio de pago o cobro.</li>
                    <li>Puede editar un movimiento con el boton "Editar", o anularlo si fue cargado por error.</li>
                </ul>
            </div>
        </div>

        <div class="card mb-3">
            <div class="card-body">
                <h2 class="h5">3. Tesoreria</h2>
                <p class="mb-0">Muestra automaticamente cuanto tiene pendiente de cobrar y de pagar, sumando todos los movimientos marcados como "pendiente".</p>
            </div>
        </div>

        <div class="card mb-3">
            <div class="card-body">
                <h2 class="h5">4. Estado de resultados</h2>
                <p class="mb-0">Calcula sus ganancias para un periodo determinado. Puede ajustar el periodo por defecto y las categorias excluidas en "Configurar estado de resultados", y descargar el resultado como PDF.</p>
            </div>
        </div>

        <div class="card mb-3">
            <div class="card-body">
                <h2 class="h5">5. Estado financiero</h2>
                <p class="mb-0">Panel con la evolucion mensual de ventas y ganancias, la composicion de sus gastos por categoria, alertas de meses con perdida, y una herramienta para comparar dos periodos entre si.</p>
            </div>
        </div>

        <div class="card mb-3">
            <div class="card-body">
                <h2 class="h5">6. Solo administradores</h2>
                <p class="mb-0">Los administradores pueden crear y desactivar usuarios, administrar el calendario de vencimientos impositivos, e importar o exportar ese calendario en formato CSV/Excel.</p>
            </div>
        </div>

        <%@ include file="/Vistas/fragmentos/Footer.jsp" %>
    </c:otherwise>
</c:choose>
