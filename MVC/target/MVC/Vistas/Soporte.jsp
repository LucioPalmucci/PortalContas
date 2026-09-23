<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
    <c:when test="${empty sessionScope.idUsuario}">
        <c:redirect url="/Vistas/Principal.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tituloPagina" value="Soporte" scope="request"/>
        <%@ include file="/Vistas/fragmentos/Header.jsp" %>

<div class="soporte-compacto">
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

        <div class="card mb-4">
            <div class="card-header fw-bold">Instalar la app en tu celular</div>
            <div class="card-body">
                <p class="text-secondary">Podes instalar Contas Portal en tu celular como cualquier otra app: te queda un icono en la pantalla de inicio y se abre sin la barra del navegador.</p>

                <ul class="nav nav-tabs mb-3 tabs-scroll" id="tabsInstalacion" role="tablist">
                    <li class="nav-item" role="presentation">
                        <button class="nav-link active" data-bs-toggle="tab" data-bs-target="#panel-android" type="button" role="tab">Android</button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link" data-bs-toggle="tab" data-bs-target="#panel-ios" type="button" role="tab">iPhone / Safari</button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link" data-bs-toggle="tab" data-bs-target="#panel-sin-opcion" type="button" role="tab">Si no aparece la opcion</button>
                    </li>
                </ul>

                <div class="tab-content">
                    <div class="tab-pane fade show active" id="panel-android" role="tabpanel">
                        <p class="mb-2">Los pasos cambian un poco segun el navegador que use su celular:</p>
                        <p class="mb-1"><strong>Con Chrome:</strong></p>
                        <ol class="mb-3">
                            <li>Abra Contas Portal en Chrome.</li>
                            <li>Toque los tres puntos (&#8942;) arriba a la derecha.</li>
                            <li>Toque <strong>"Instalar y crear acceso directo"</strong> (o "Instalar aplicacion").</li>
                            <li>Toque <strong>"Crear acceso directo"</strong>.</li>
                            <li>Toque <strong>"Agregar"</strong> y confirme con <strong>"Añadir"</strong>.</li>
                        </ol>
                        <p class="mb-1"><strong>Con el navegador Samsung Internet:</strong></p>
                        <ol class="mb-0">
                            <li>Abra Contas Portal en Samsung Internet.</li>
                            <li>Toque los tres puntos (&#8942;) abajo a la derecha.</li>
                            <li>Toque <strong>"Añadir pagina a"</strong>.</li>
                            <li>Elija <strong>"Pantalla de inicio"</strong>.</li>
                            <li>Toque <strong>"Añadir"</strong>.</li>
                        </ol>
                    </div>
                    <div class="tab-pane fade" id="panel-ios" role="tabpanel">
                        <p class="mb-2">En iPhone o iPad hay que instalarla a mano, y tiene que ser desde <strong>Safari</strong> (no Chrome: en iOS todos los navegadores usan el motor de Safari por dentro, pero solo Safari puede instalar apps).</p>
                        <ol class="mb-0">
                            <li>Abra Contas Portal en Safari.</li>
                            <li>Si no ve el boton de <strong>Compartir</strong> a la vista, toque primero los tres puntos (<strong>&bull;&bull;&bull;</strong>) y despues <strong>Compartir</strong> dentro de ese menu.</li>
                            <li>En el listado que se abre, si no ve "Agregar a pantalla de inicio" de entrada, toque la <strong>flechita</strong> (o el texto "Mas"/"Ver mas", segun su iPhone) para desplegar todas las opciones.</li>
                            <li>Elija <strong>"Agregar a pantalla de inicio"</strong>.</li>
                            <li>Confirme tocando <strong>Agregar</strong> arriba a la derecha.</li>
                        </ol>
                    </div>
                    <div class="tab-pane fade" id="panel-sin-opcion" role="tabpanel">
                        <p class="mb-2">Algunos navegadores no permiten instalar apps &mdash; por ejemplo, el navegador que se abre <em>dentro</em> de Instagram o WhatsApp cuando toca un link.</p>
                        <p class="mb-0">Si no ve la opcion de instalar:</p>
                        <ol class="mb-0">
                            <li>Confirme que este usando Chrome (Android) o Safari (iPhone), no otro navegador.</li>
                            <li>Si toco el link desde otra app, copielo y abralo directo en Chrome o Safari.</li>
                            <li>Con Firefox, Edge u otro navegador en Android, busque en su menu algo como "Instalar aplicacion" &mdash; casi todos lo tienen, solo cambia el nombre.</li>
                            <li>Si aun asi no puede instalarla, no hay problema: la app funciona igual desde el navegador, solo que sin el icono en la pantalla de inicio &mdash; guardela como favorito.</li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>

        <div class="row g-3">
            <div class="col-md-6">
                <div class="card h-100">
                    <div class="card-body">
                        <h3 class="h5">Hablar con el estudio</h3>
                        <p class="text-secondary">Escribanos por WhatsApp y le responderemos a la brevedad.</p>
                        <a class="btn btn-primary w-100" href="https://wa.me/+5491122538164" target="_blank" rel="noopener">Escribir por WhatsApp</a>
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
</div>

        <%@ include file="/Vistas/fragmentos/Footer.jsp" %>
    </c:otherwise>
</c:choose>
