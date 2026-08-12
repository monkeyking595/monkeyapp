import { defineConfig, loadEnv } from "vite";
import react from "@vitejs/plugin-react";

const apiProxyPaths = ["/customers", "/Cart", "^/payment(/|$)", "/admin/api", "/sellers"];

export default defineConfig(({ mode }) => {
    const env = loadEnv(mode, "..", "");
    const backendProxyTarget = env.VITE_DEV_API_PROXY_TARGET || env.DEV_API_PROXY_TARGET || "http://127.0.0.1:8080";
    const allowedHosts = parseList(env.VITE_DEV_ALLOWED_HOSTS || env.DEV_ALLOWED_HOSTS);
    const allowAllHosts = allowedHosts.includes("*");

    return {
        envDir: "..",
        envPrefix: ["VITE_", "STRIPE_PUBLISHABLE_KEY"],
        plugins: [react()],
        server: {
            host: env.VITE_DEV_HOST || "127.0.0.1",
            port: Number(env.VITE_DEV_PORT || 5173),
            ...(allowAllHosts ? { allowedHosts: true } : allowedHosts.length ? { allowedHosts } : {}),
            proxy: Object.fromEntries(
                apiProxyPaths.map((apiPath) => [
                    apiPath,
                    {
                        target: backendProxyTarget,
                        changeOrigin: true,
                        secure: false
                    }
                ])
            )
        }
    };
});

function parseList(value) {
    return String(value || "")
        .split(",")
        .map((item) => item.trim())
        .filter(Boolean);
}
