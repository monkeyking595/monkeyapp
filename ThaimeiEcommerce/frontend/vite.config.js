import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
export default defineConfig({
    plugins: [react()],
    server: {
        port: 5173,
        proxy: {
            "/login": "http://localhost:8080",
            "/signup": "http://localhost:8080",
            "/productsList": "http://localhost:8080",
            "/Product": "http://localhost:8080",
            "/Cart": "http://localhost:8080",
            "/Orders": "http://localhost:8080",
            "/profile": "http://localhost:8080",
            "/profile-info": "http://localhost:8080",
            "/admin": "http://localhost:8080"
        }
    }
});
