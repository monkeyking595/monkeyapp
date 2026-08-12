# Thaimei Frontend

## Production hosting

- Build command: `npm run build`
- Static publish directory: `dist`
- Node start command: `npm start`

`npm start` builds the Vite app and serves `dist` with a small production static server. It also supports React Router refreshes on routes like `/products/12`.

Set `VITE_API_BASE_URL` when the backend API is hosted on a different origin. Leave it empty only when your host or reverse proxy sends `/customers`, `/Cart`, `/payment`, `/admin/api`, and `/sellers` to the backend.

If you use the included Node server and want the frontend domain to proxy API requests to the backend, set `API_PROXY_TARGET` to the backend origin instead of setting `VITE_API_BASE_URL`.

Stripe payments need `VITE_STRIPE_PUBLISHABLE_KEY` or `STRIPE_PUBLISHABLE_KEY`. Never expose the Stripe secret key in frontend env vars.
