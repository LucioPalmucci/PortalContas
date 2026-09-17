if ('serviceWorker' in navigator) {
    window.addEventListener('load', () => {
        // window.APP_CONTEXT_PATH lo define cada pagina JSP antes de cargar este script
        // (Header.jsp/Footer.jsp o Principal.jsp), asi este archivo estatico no necesita
        // hardcodear el context path del despliegue.
        var contextPath = window.APP_CONTEXT_PATH || '';
        navigator.serviceWorker.register(contextPath + '/sw.js')
            .then((reg) => console.log('Service Worker registrado:', reg.scope))
            .catch((err) => console.error('Error registrando Service Worker:', err));
    });
}