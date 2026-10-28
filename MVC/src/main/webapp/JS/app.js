function confirmarAccion(mensaje) {
    return window.confirm(mensaje);
}

function imprimirPagina() {
    window.print();
}

function mostrarCarga() {
    var barra = document.getElementById("barraCarga");
    if (barra) barra.classList.add("activa");
}

function ocultarCarga() {
    var barra = document.getElementById("barraCarga");
    if (barra) barra.classList.remove("activa");
}

// Evita que en un par de campos de fecha "Desde"/"Hasta" se pueda elegir
// una fecha inicial posterior a la final (o viceversa): sincroniza los
// limites min/max del input contrario y corrige el valor si ya quedo
// invertido.
function sincronizarRangoFechas(idDesde, idHasta) {
    var desde = document.getElementById(idDesde);
    var hasta = document.getElementById(idHasta);
    if (!desde || !hasta) return;

    function limitarHasta() {
        hasta.min = desde.value || "";
        if (desde.value && hasta.value && hasta.value < desde.value) {
            hasta.value = desde.value;
        }
    }

    function limitarDesde() {
        desde.max = hasta.value || "";
        if (hasta.value && desde.value && desde.value > hasta.value) {
            desde.value = hasta.value;
        }
    }

    desde.addEventListener("change", limitarHasta);
    hasta.addEventListener("change", limitarDesde);
    limitarHasta();
    limitarDesde();
}

document.addEventListener("DOMContentLoaded", function () {
    document.addEventListener("click", function (evento) {
        var enlace = evento.target.closest("a[href]");
        if (!enlace) return;
        if (enlace.target === "_blank" || enlace.hasAttribute("download")) return;
        if (enlace.dataset.bsToggle || enlace.dataset.bsDismiss) return;
        var href = enlace.getAttribute("href");
        if (!href || href.charAt(0) === "#" || href.indexOf("javascript:") === 0
            || href.indexOf("mailto:") === 0 || href.indexOf("tel:") === 0) return;
        if (evento.defaultPrevented || evento.ctrlKey || evento.metaKey || evento.shiftKey || evento.button !== 0) return;
        mostrarCarga();
    });

    document.addEventListener("submit", function (evento) {
        if (evento.defaultPrevented) return;
        mostrarCarga();
    });
});

window.addEventListener("pageshow", ocultarCarga);
