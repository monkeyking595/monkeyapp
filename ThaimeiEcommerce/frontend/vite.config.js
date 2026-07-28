import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
export default defineConfig({
    plugins: [react()],
    server: {
        port: 5173,
        allowedHosts: ["recluse-scion-wildcard.ngrok-free.dev"],
        proxy: {
            "/customers": "http://localhost:8080",
            "/Cart": "http://localhost:8080",
            "/cutomers": "http://localhost:8080",
            "/admin/api": "http://localhost:8080",
            "/sellers": "http://localhost:8080"
        }
    }
});
