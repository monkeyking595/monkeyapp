const SESSION_KEY = "thaimei.session";

export function loadSession() {
  const raw = localStorage.getItem(SESSION_KEY);
  if (!raw) return null;
  try {
    return JSON.parse(raw);
  } catch {
    localStorage.removeItem(SESSION_KEY);
    return null;
  }
}

export function saveSession(session) {
  localStorage.setItem(SESSION_KEY, JSON.stringify(session));
}

export function clearSession() {
  localStorage.removeItem(SESSION_KEY);
}

async function request(path, options = {}) {
  const headers = new Headers(options.headers);
  const session = loadSession();

  if (options.body && !headers.has("Content-Type")) {
    headers.set("Content-Type", "application/json");
  }

  if (options.auth !== false && session?.token) {
    headers.set("Authorization", `Bearer ${session.token}`);
  }

  const response = await fetch(path, { ...options, headers });
  const text = await response.text();
  const body = text ? tryParseJson(text) : null;

  if (!response.ok) {
    const message =
      typeof body === "object" && body && "error" in body
        ? String(body.error)
        : text || `Request failed with ${response.status}`;
    throw new Error(message);
  }

  return body;
}

function tryParseJson(text) {
  try {
    return JSON.parse(text);
  } catch {
    return text;
  }
}

export const api = {
  async login(username, password) {
    const data = await request("/login", {
      method: "POST",
      auth: false,
      body: JSON.stringify({ username, password })
    });
    const session = { ...data, isAdmin: false };
    saveSession(session);
    return session;
  },

  async signup(username, email, password, confirmpassword) {
    const data = await request("/signup", {
      method: "POST",
      auth: false,
      body: JSON.stringify({ username, email, password, confirmpassword })
    });
    const session = { ...data, isAdmin: false };
    saveSession(session);
    return session;
  },

  async adminLogin(adminUsername, adminPassword) {
    const data = await request("/admin/api/adminlogin", {
      method: "POST",
      auth: false,
      body: JSON.stringify({ adminUsername, adminPassword })
    });
    const session = { ...data, isAdmin: true };
    saveSession(session);
    return session;
  },

  adminRegister: (adminname, adminemail, adminpassword, adminconfirmpassword) =>
    request("/admin/api/register", {
      method: "POST",
      body: JSON.stringify({ adminname, adminemail, adminpassword, adminconfirmpassword })
    }),

  products: () => request("/productsList"),
  product: (id) => request(`/Product/productDetails/${id}`),
  cart: () => request("/Cart/getItems"),
  addToCart: (productId, quantity) =>
    request("/Cart/AddItems", {
      method: "POST",
      body: JSON.stringify({ productId, quantity })
    }),
  orders: () => request("/Orders/GetOrder"),
  placeOrder: (order) =>
    request("/Orders/PlaceOrder", {
      method: "POST",
      body: JSON.stringify(order)
    }),
  profile: () => request("/profile-info"),
  saveProfile: (profile) =>
    request("/profile", {
      method: "POST",
      body: JSON.stringify(profile)
    }),
  adminUsers: () => request("/admin/api/AllUsers")
};
