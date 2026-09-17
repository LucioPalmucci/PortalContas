const CACHE_NAME = 'contas-v1';

const BASE = self.registration.scope;

const urlsToCache = [
    BASE,
    BASE + 'manifest/manifest.json',
    BASE + 'icons/icon-192.png',
    BASE + 'icons/icon-512.png',
    BASE + 'CSS/bootstrap.min.css',
    BASE + 'CSS/custom.css',
    BASE + 'JS/app.js',
    BASE + 'JS/bootstrap.bundle.min.js'
];

self.addEventListener('install', (event) => {
    event.waitUntil(
        caches.open(CACHE_NAME)
            .then((cache) => cache.addAll(urlsToCache))
            .then(() => self.skipWaiting())
    );
});

self.addEventListener('activate', (event) => {
    event.waitUntil(
        caches.keys().then((cacheNames) => {
            return Promise.all(
                cacheNames
                    .filter((name) => name !== CACHE_NAME)
                    .map((name) => caches.delete(name))
            );
        }).then(() => self.clients.claim())
    );
});

self.addEventListener('fetch', (event) => {
    event.respondWith(
        caches.match(event.request).then((cachedResponse) => {
            return cachedResponse || fetch(event.request);
        })
    );
});