# StudySathi — PWA Install Fix

## Why Install was not appearing
`manifest.json` alone does not make a GitHub Pages site installable as a PWA.
StudySathi now includes:
- `manifest.json`
- `sw.js` service worker
- 192px and 512px app icons
- service-worker registration in `app.js`

## GitHub upload
At the repository ROOT:

REPLACE:
- `manifest.json`
- `app.js`

ADD:
- `sw.js`
- `icons/icon-192.png`
- `icons/icon-512.png`

KEEP:
- `index.html`
- `style.css`

Do not upload the ZIP itself into the repository.

## After upload
1. Wait for GitHub Pages deployment to finish.
2. Open the website in Chrome.
3. Refresh once or twice.
4. Open Chrome ⋮ menu.
5. Look for `Install app` / `Add to home screen` (wording varies by Chrome version).
6. If it still shows the old version, clear the site's cached data or open the site in an Incognito tab once, then return to normal Chrome.

## Important
This installs the web/PWA version. It does NOT install the native Android APK.
The real cross-app Focus Protection feature remains part of the Android build.
