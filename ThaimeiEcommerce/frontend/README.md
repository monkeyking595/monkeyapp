# Thaimei Frontend

## Local development

The frontend and Spring backend share the repository-root `.env` file. A local development version is included for convenience; it uses a local MySQL database and proxies browser API calls to `http://127.0.0.1:8080`.

1. Start the backend and MySQL from the repository root: `docker compose up --build`
2. In another terminal, run `cd frontend && npm install && npm run dev`
3. Open `http://127.0.0.1:5173`

For a production-style frontend server, build and serve it with `cd frontend && npm start`. It listens on `http://127.0.0.1:4173` and uses `API_PROXY_TARGET` from the root `.env` file.

Before using checkout, put Stripe **test** keys in `.env`:

- `STRIPE_SECRET_KEY`
- `STRIPE_PUBLISHABLE_KEY`
- `VITE_STRIPE_PUBLISHABLE_KEY`
- `STRIPE_WEBHOOK_SECRET` (only needed for webhook verification)

Do not commit real secrets. Copy `.env.example` if you need to recreate the configuration.

## Production hosting

- Build command: `npm run build`
- Static publish directory: `dist`
- Node start command: `npm start`

`npm start` builds the Vite app and serves `dist` with a small production static server. It also supports React Router refreshes on routes like `/products/12`.

Set `VITE_API_BASE_URL` when the backend API is hosted on a different origin. Leave it empty only when your host or reverse proxy sends `/customers`, `/Cart`, `/payment`, `/admin/api`, and `/sellers` to the backend.

If you use the included Node server and want the frontend domain to proxy API requests to the backend, set `API_PROXY_TARGET` to the backend origin instead of setting `VITE_API_BASE_URL`.

Stripe payments need `VITE_STRIPE_PUBLISHABLE_KEY` or `STRIPE_PUBLISHABLE_KEY`. Never expose the Stripe secret key in frontend env vars.
