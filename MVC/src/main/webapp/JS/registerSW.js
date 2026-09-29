if ('serviceWorker' in navigator) {
    window.addEventListener('load', () => {
        var contextPath = window.APP_CONTEXT_PATH || '';
        navigator.serviceWorker.register(contextPath + '/sw.js')
            .then((reg) => console.log('Service Worker registrado:', reg.scope))
            .catch((err) => console.error('Error registrando Service Worker:', err));
    });
}