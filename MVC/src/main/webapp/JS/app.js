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
