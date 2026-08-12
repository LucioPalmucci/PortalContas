// Buscador, filtros, ordenamiento por columna y paginado en tiempo real (sin recargar la pagina),
// operando sobre las filas ya renderizadas por el servidor. No requiere ida y vuelta al servidor.
(function () {
    function inicializarTablaFiltros(contenedor) {
        var tbody = contenedor.querySelector("tbody");
        if (!tbody) return;
        var filasOriginales = Array.prototype.slice.call(tbody.querySelectorAll("tr[data-fila]"));
        if (filasOriginales.length === 0) return;

        var buscador = contenedor.querySelector("[data-rol='buscador']");
        var desde = contenedor.querySelector("[data-rol='desde']");
        var hasta = contenedor.querySelector("[data-rol='hasta']");
        var estadoFiltro = contenedor.querySelector("[data-rol='estado']");
        var clienteFiltro = contenedor.querySelector("[data-rol='cliente']");
        var paginador = contenedor.querySelector("[data-rol='paginador']");
        var contador = contenedor.querySelector("[data-rol='contador']");
        var filasPorPagina = 10;
        var paginaActual = 1;
        var ordenCampo = null;
        var ordenAsc = true;

        function coincide(fila) {
            var texto = (fila.getAttribute("data-texto") || "").toLowerCase();
            var fecha = fila.getAttribute("data-fecha") || "";
            var estado = fila.getAttribute("data-estado") || "";
            var cliente = fila.getAttribute("data-cliente") || "";

            if (buscador && buscador.value.trim() !== "" && texto.indexOf(buscador.value.trim().toLowerCase()) === -1) {
                return false;
            }
            if (desde && desde.value !== "" && fecha !== "" && fecha < desde.value) return false;
            if (hasta && hasta.value !== "" && fecha !== "" && fecha > hasta.value) return false;
            if (estadoFiltro && estadoFiltro.value !== "" && estado !== estadoFiltro.value) return false;
            if (clienteFiltro && clienteFiltro.value !== "" && cliente !== clienteFiltro.value) return false;
            return true;
        }

        function autopoblarSelectCliente() {
            if (!clienteFiltro || clienteFiltro.getAttribute("data-auto-opciones") !== "cliente") return;
            var vistos = {};
            filasOriginales.forEach(function (f) {
                var valor = f.getAttribute("data-cliente");
                var etiqueta = f.getAttribute("data-cliente-label") || valor;
                if (valor && !(valor in vistos)) vistos[valor] = etiqueta;
            });
            Object.keys(vistos).sort(function (a, b) {
                return vistos[a].localeCompare(vistos[b]);
            }).forEach(function (valor) {
                var opcion = document.createElement("option");
                opcion.value = valor;
                opcion.textContent = vistos[valor];
                clienteFiltro.appendChild(opcion);
            });
        }

        function actualizarIndicadoresOrden() {
            contenedor.querySelectorAll("[data-ordenar]").forEach(function (th) {
                th.classList.remove("text-primary");
                var indicador = th.querySelector(".indicador-orden");
                if (indicador) indicador.textContent = "";
            });
            if (!ordenCampo) return;
            var thActivo = contenedor.querySelector("[data-ordenar='" + ordenCampo + "']");
            if (thActivo) {
                thActivo.classList.add("text-primary");
                var indicador = thActivo.querySelector(".indicador-orden");
                if (indicador) indicador.textContent = ordenAsc ? " ▲" : " ▼";
            }
        }

        function render() {
            var filtradas = filasOriginales.filter(coincide);

            if (ordenCampo) {
                filtradas.sort(function (a, b) {
                    var va = a.getAttribute("data-" + ordenCampo) || "";
                    var vb = b.getAttribute("data-" + ordenCampo) || "";
                    var na = parseFloat(va);
                    var nb = parseFloat(vb);
                    var cmp;
                    if (!isNaN(na) && !isNaN(nb) && va !== "" && vb !== "") {
                        cmp = na - nb;
                    } else {
                        cmp = va.localeCompare(vb);
                    }
                    return ordenAsc ? cmp : -cmp;
                });
            }

            filasOriginales.forEach(function (f) { f.style.display = "none"; });

            var totalPaginas = Math.max(1, Math.ceil(filtradas.length / filasPorPagina));
            if (paginaActual > totalPaginas) paginaActual = totalPaginas;
            var inicio = (paginaActual - 1) * filasPorPagina;
            var visibles = filtradas.slice(inicio, inicio + filasPorPagina);

            visibles.forEach(function (f) {
                f.style.display = "";
                tbody.appendChild(f);
            });

            var filaVacia = contenedor.querySelector("[data-fila-vacia]");
            if (filaVacia) filaVacia.style.display = filtradas.length === 0 ? "" : "none";

            if (contador) {
                contador.textContent = filtradas.length === 0
                    ? ""
                    : "Mostrando " + (inicio + 1) + "-" + (inicio + visibles.length) + " de " + filtradas.length;
            }

            if (paginador) {
                paginador.innerHTML = "";
                if (totalPaginas > 1) {
                    for (var p = 1; p <= totalPaginas; p++) {
                        var boton = document.createElement("button");
                        boton.type = "button";
                        boton.className = "btn btn-sm " + (p === paginaActual ? "btn-primary" : "btn-outline-secondary") + " me-1";
                        boton.textContent = String(p);
                        boton.addEventListener("click", (function (pagina) {
                            return function () { paginaActual = pagina; render(); };
                        })(p));
                        paginador.appendChild(boton);
                    }
                }
            }

            actualizarIndicadoresOrden();
        }

        if (buscador) buscador.addEventListener("input", function () { paginaActual = 1; render(); });
        if (desde) desde.addEventListener("change", function () { paginaActual = 1; render(); });
        if (hasta) hasta.addEventListener("change", function () { paginaActual = 1; render(); });
        if (estadoFiltro) estadoFiltro.addEventListener("change", function () { paginaActual = 1; render(); });
        if (clienteFiltro) clienteFiltro.addEventListener("change", function () { paginaActual = 1; render(); });

        contenedor.querySelectorAll("[data-ordenar]").forEach(function (th) {
            th.style.cursor = "pointer";
            th.addEventListener("click", function () {
                var campo = th.getAttribute("data-ordenar");
                if (ordenCampo === campo) {
                    ordenAsc = !ordenAsc;
                } else {
                    ordenCampo = campo;
                    ordenAsc = true;
                }
                render();
            });
        });

        autopoblarSelectCliente();
        render();
    }

    document.addEventListener("DOMContentLoaded", function () {
        document.querySelectorAll("[data-tabla-filtros]").forEach(inicializarTablaFiltros);
    });
})();
