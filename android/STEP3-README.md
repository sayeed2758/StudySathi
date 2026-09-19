# StudySathi Step 3

## What changed
1. Premium light/white web theme.
2. Native Android app can list launchable apps.
3. User can select which apps to protect.
4. Selection is stored locally.
5. Accessibility service now checks the selected package set instead of a hardcoded list.

## File flow
### Web
REPLACE only:
- `style.css`

KEEP:
- `index.html`
- `app.js`
- `manifest.json`
- `README.md`

### Android
For this phase, replace the entire previous `android` folder with the new `android` folder from this package.

## Testing
Open the `android` folder in Android Studio, build and install it.
Open StudySathi → select apps → enable Accessibility access → Start Focus Mode → open one selected app.

## Important limitation
The service is a focus-protection prototype. Android accessibility services are user-enabled system services and have platform/policy constraints; review current Android/Google Play requirements before any public release.
