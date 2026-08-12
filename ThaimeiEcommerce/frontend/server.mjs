import { createServer } from "node:http";
import http from "node:http";
import https from "node:https";
import { createReadStream, existsSync, statSync } from "node:fs";
import path from "node:path";
import { fileURLToPath } from "node:url";

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const distDir = path.join(__dirname, "dist");
const indexPath = path.join(distDir, "index.html");
const port = Number(process.env.PORT || 4173);
const host = process.env.HOST || "0.0.0.0";
const apiProxyTarget = normalizeBase(process.env.API_PROXY_TARGET);
const apiPrefixes = ["/customers", "/Cart", "/payment", "/admin/api", "/sellers"];

const mimeTypes = new Map([
  [".css", "text/css; charset=utf-8"],
  [".gif", "image/gif"],
  [".html", "text/html; charset=utf-8"],
  [".ico", "image/x-icon"],
  [".jpg", "image/jpeg"],
  [".jpeg", "image/jpeg"],
  [".js", "text/javascript; charset=utf-8"],
  [".json", "application/json; charset=utf-8"],
  [".png", "image/png"],
  [".svg", "image/svg+xml"],
  [".txt", "text/plain; charset=utf-8"],
  [".webp", "image/webp"]
]);

if (!existsSync(indexPath)) {
  console.error("Missing frontend/dist. Run `npm run build` before starting the production server.");
  process.exit(1);
}

createServer((request, response) => {
  const requestUrl = new URL(request.url || "/", `http://${request.headers.host || "localhost"}`);

  if (isApiPath(requestUrl.pathname)) {
    if (apiProxyTarget) {
      proxyApiRequest(request, response, requestUrl);
      return;
    }

    sendJson(response, 502, {
      error: "API proxy is not configured. Set API_PROXY_TARGET or build with VITE_API_BASE_URL."
    });
    return;
  }

  if (!["GET", "HEAD"].includes(request.method || "GET")) {
    sendJson(response, 405, { error: "Method not allowed" });
    return;
  }

  const filePath = resolveStaticPath(requestUrl.pathname);
  sendStaticFile(request, response, filePath);
}).listen(port, host, () => {
  console.log(`Serving frontend/dist on http://${host}:${port}`);
  if (apiProxyTarget) {
    console.log(`Proxying API requests to ${apiProxyTarget}`);
  }
});

function resolveStaticPath(urlPathname) {
  const requestedPath = urlPathname === "/" ? "/index.html" : urlPathname;
  const filePath = safeJoin(distDir, requestedPath);

  if (filePath && existsSync(filePath)) {
    const stats = statSync(filePath);
    if (stats.isFile()) return filePath;
    if (stats.isDirectory()) {
      const nestedIndexPath = path.join(filePath, "index.html");
      if (existsSync(nestedIndexPath)) return nestedIndexPath;
    }
  }

  return indexPath;
}

function sendStaticFile(request, response, filePath) {
  const extension = path.extname(filePath).toLowerCase();
  const isAsset = filePath.includes(`${path.sep}assets${path.sep}`);

  response.setHeader("Content-Type", mimeTypes.get(extension) || "application/octet-stream");
  response.setHeader("Cache-Control", isAsset ? "public, max-age=31536000, immutable" : "no-cache");

  if (request.method === "HEAD") {
    response.end();
    return;
  }

  createReadStream(filePath)
    .on("error", () => sendJson(response, 500, { error: "Unable to read static file" }))
    .pipe(response);
}

function proxyApiRequest(clientRequest, clientResponse, requestUrl) {
  const targetUrl = new URL(apiProxyTarget);
  const targetBasePath = targetUrl.pathname.replace(/\/+$/, "");
  const proxyPath = `${targetBasePath}${requestUrl.pathname}${requestUrl.search}`;
  const proxyClient = targetUrl.protocol === "https:" ? https : http;
  const headers = { ...clientRequest.headers, host: targetUrl.host };

  delete headers.connection;

  const proxyRequest = proxyClient.request(
    {
      protocol: targetUrl.protocol,
      hostname: targetUrl.hostname,
      port: targetUrl.port,
      method: clientRequest.method,
      path: proxyPath,
      headers
    },
    (proxyResponse) => {
      clientResponse.writeHead(proxyResponse.statusCode || 502, proxyResponse.headers);
      proxyResponse.pipe(clientResponse);
    }
  );

  proxyRequest.on("error", () => {
    sendJson(clientResponse, 502, { error: "Unable to reach API proxy target" });
  });

  clientRequest.pipe(proxyRequest);
}

function safeJoin(basePath, requestedPath) {
  try {
    const decodedPath = decodeURIComponent(requestedPath);
    const normalizedPath = path.normalize(decodedPath).replace(/^(\.\.[/\\])+/, "");
    const filePath = path.join(basePath, normalizedPath);
    return filePath.startsWith(basePath) ? filePath : "";
  } catch {
    return "";
  }
}

function sendJson(response, status, body) {
  if (response.headersSent) return;
  response.writeHead(status, {
    "Content-Type": "application/json; charset=utf-8",
    "Cache-Control": "no-cache"
  });
  response.end(JSON.stringify(body));
}

function isApiPath(urlPathname) {
  return apiPrefixes.some((prefix) => urlPathname === prefix || urlPathname.startsWith(`${prefix}/`));
}

function normalizeBase(value) {
  return String(value || "").trim().replace(/\/+$/, "");
}
