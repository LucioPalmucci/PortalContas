<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
    <c:when test="${empty sessionScope.idUsuario}">
        <c:redirect url="/Vistas/Principal.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tituloPagina" value="Soporte" scope="request"/>
        <%@ include file="/Vistas/fragmentos/Header.jsp" %>

        <h1>Soporte</h1>
        <p class="text-secondary">Encuentre respuestas rapidas o comuniquese directamente con el estudio.</p>

        <div class="card mb-4">
            <div class="card-header fw-bold">Preguntas frecuentes</div>
            <div class="card-body">
                <input type="text" class="form-control mb-3" id="buscadorFaq" placeholder="Escriba su consulta..." autocomplete="off">

                <div class="accordion" id="acordeonFaq">
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq1">
                                ¿Cuando debo registrar una venta?
                            </button>
                        </h2>
                        <div id="faq1" class="accordion-collapse collapse" data-bs-parent="#acordeonFaq">
                            <div class="accordion-body">
                                Apenas concreta la operacion, sin importar si ya cobro o no. Puede cargarla con estado "Pendiente de cobro" y marcarla como cobrada mas adelante.
                            </div>
                        </div>
                    </div>
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq2">
                                ¿Que diferencia hay entre una compra y un gasto?
                            </button>
                        </h2>
                        <div id="faq2" class="accordion-collapse collapse" data-bs-parent="#acordeonFaq">
                            <div class="accordion-body">
                                Las compras son mercaderia que adquiere para revender, y forman parte del costo de mercaderia vendida. Los gastos son costos operativos del negocio (alquiler, servicios, sueldos, etc.) que no se revenden.
                            </div>
                        </div>
                    </div>
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq3">
                                ¿Como registro un producto vencido o un consumo interno?
                            </button>
                        </h2>
                        <div id="faq3" class="accordion-collapse collapse" data-bs-parent="#acordeonFaq">
                            <div class="accordion-body">
                                Cargelo como un Gasto en la categoria "Otros", indicando en la descripcion el motivo (producto vencido, consumo interno, etc.).
                            </div>
                        </div>
                    </div>
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq4">
                                ¿Puedo cargar una venta que todavia no cobre?
                            </button>
                        </h2>
                        <div id="faq4" class="accordion-collapse collapse" data-bs-parent="#acordeonFaq">
                            <div class="accordion-body">
                                Si. Al registrarla, seleccione el estado "Pendiente de cobro". El monto se reflejara automaticamente en Tesoreria como cuenta por cobrar.
                            </div>
                        </div>
                    </div>
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq5">
                                ¿Como se calcula el costo de la mercaderia vendida?
                            </button>
                        </h2>
                        <div id="faq5" class="accordion-collapse collapse" data-bs-parent="#acordeonFaq">
                            <div class="accordion-body">
                                Se calcula aplicando el margen de CMV que configuro en "Estado de resultados &gt; Configurar estado de resultados" sobre el total de ventas del periodo.
                            </div>
                        </div>
                    </div>
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq6">
                                ¿Por que un gasto que pague en junio aparece en mayo?
                            </button>
                        </h2>
                        <div id="faq6" class="accordion-collapse collapse" data-bs-parent="#acordeonFaq">
                            <div class="accordion-body">
                                El sistema ordena cada movimiento por su fecha, no por cuando lo pago. Un gasto de mayo pagado en junio se contabiliza en mayo.
                            </div>
                        </div>
                    </div>
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq7">
                                ¿Que significa el porcentaje de rentabilidad?
                            </button>
                        </h2>
                        <div id="faq7" class="accordion-collapse collapse" data-bs-parent="#acordeonFaq">
                            <div class="accordion-body">
                                Es la ganancia neta del periodo expresada como porcentaje de las ventas totales: indica cuanto de cada peso vendido termina siendo ganancia.
                            </div>
                        </div>
                    </div>
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq8">
                                ¿Como marco una venta como cobrada?
                            </button>
                        </h2>
                        <div id="faq8" class="accordion-collapse collapse" data-bs-parent="#acordeonFaq">
                            <div class="accordion-body">
                                En la tabla de Ventas, use el boton "Marcar cobrada" en la fila correspondiente.
                            </div>
                        </div>
                    </div>
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq9">
                                ¿Donde veo lo que tengo pendiente de cobro?
                            </button>
                        </h2>
                        <div id="faq9" class="accordion-collapse collapse" data-bs-parent="#acordeonFaq">
                            <div class="accordion-body">
                                En el modulo de Tesoreria, en la seccion de cuentas por cobrar.
                            </div>
                        </div>
                    </div>
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq10">
                                ¿Como registro el medio de pago de un gasto?
                            </button>
                        </h2>
                        <div id="faq10" class="accordion-collapse collapse" data-bs-parent="#acordeonFaq">
                            <div class="accordion-body">
                                Al cargar o editar el gasto, seleccione el medio de pago (Efectivo, Transferencia, Tarjeta, Cheque u Otro) en el formulario.
                            </div>
                        </div>
                    </div>
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq11">
                                ¿Como cambio mi contraseña?
                            </button>
                        </h2>
                        <div id="faq11" class="accordion-collapse collapse" data-bs-parent="#acordeonFaq">
                            <div class="accordion-body">
                                Por seguridad, solo un administrador puede restablecer su contraseña. Comuniquese con el estudio por WhatsApp o correo electronico.
                            </div>
                        </div>
                    </div>
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq12">
                                ¿Que hago si no puedo ingresar al sistema?
                            </button>
                        </h2>
                        <div id="faq12" class="accordion-collapse collapse" data-bs-parent="#acordeonFaq">
                            <div class="accordion-body">
                                Verifique que su usuario y contraseña sean correctos. Si el problema persiste, comuniquese con el estudio por WhatsApp para que un administrador revise su cuenta.
                            </div>
                        </div>
                    </div>
                </div>

                <p id="faqSinResultados" class="text-secondary text-center mt-3 mb-0 d-none">
                    No encontramos preguntas relacionadas. Pruebe con otras palabras o use los accesos de abajo.
                </p>
            </div>
        </div>

        <div class="row g-3">
            <div class="col-md-6">
                <div class="card h-100">
                    <div class="card-body">
                        <h3 class="h5">Hablar con el estudio</h3>
                        <p class="text-secondary">Escribanos por WhatsApp y le responderemos a la brevedad.</p>
                        <a class="btn btn-primary w-100" href="https://wa.me/5490000000000" target="_blank" rel="noopener">Escribir por WhatsApp</a>
                        <p class="small text-secondary mt-2 mb-0">Numero de ejemplo: reemplazar por el numero real del estudio.</p>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card h-100">
                    <div class="card-body">
                        <h3 class="h5">Manual de uso</h3>
                        <p class="text-secondary">Guia paso a paso de todas las funciones del sistema.</p>
                        <a class="btn btn-outline-secondary w-100" href="${pageContext.request.contextPath}/Vistas/Manual.jsp" target="_blank">Abrir manual de uso</a>
                    </div>
                </div>
            </div>
        </div>

        <script>
            (function () {
                var buscador = document.getElementById("buscadorFaq");
                var items = document.querySelectorAll("#acordeonFaq .accordion-item");
                var sinResultados = document.getElementById("faqSinResultados");

                buscador.addEventListener("input", function () {
                    var termino = buscador.value.trim().toLowerCase();
                    var visibles = 0;
                    items.forEach(function (item) {
                        var texto = item.textContent.toLowerCase();
                        var coincide = termino === "" || texto.indexOf(termino) !== -1;
                        item.classList.toggle("d-none", !coincide);
                        if (coincide) visibles++;
                    });
                    sinResultados.classList.toggle("d-none", visibles !== 0);
                });
            })();
        </script>

        <%@ include file="/Vistas/fragmentos/Footer.jsp" %>
    </c:otherwise>
</c:choose>
