const SESSION_KEY = "thaimei.session";

export const ROLES = {
  CUSTOMER: "CUSTOMER",
  SELLER: "SELLER",
  ADMIN: "ADMIN"
};

export function loadSession() {
  const raw = localStorage.getItem(SESSION_KEY);
  if (!raw) return null;
  try {
    return normalizeSession(JSON.parse(raw));
  } catch {
    localStorage.removeItem(SESSION_KEY);
    return null;
  }
}

export function saveSession(session) {
  localStorage.setItem(SESSION_KEY, JSON.stringify(normalizeSession(session)));
}

export function clearSession() {
  localStorage.removeItem(SESSION_KEY);
}

export function sessionRole(session) {
  return normalizeRole(session?.role || session?.userRole || session?.authorities);
}

export function hasRole(session, role) {
  return sessionRole(session) === role;
}

export function landingPath(session) {
  const role = sessionRole(session);
  if (role === ROLES.ADMIN) return "/admin";
  if (role === ROLES.SELLER) return "/seller";
  return "/products";
}

function normalizeSession(session) {
  if (!session) return null;

  const role =
    normalizeRole(session.role || session.userRole || session.authorities) ||
    (session.isAdmin ? ROLES.ADMIN : session.isSeller ? ROLES.SELLER : ROLES.CUSTOMER);

  return {
    ...session,
    role,
    isAdmin: role === ROLES.ADMIN,
    isSeller: role === ROLES.SELLER
  };
}

function normalizeRole(value) {
  if (Array.isArray(value)) {
    return value.map(normalizeRole).find(Boolean) || "";
  }

  if (!value) return "";

  const normalized = String(value).replace(/^ROLE_/, "").toUpperCase();
  return Object.values(ROLES).includes(normalized) ? normalized : "";
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
    throw new Error(extractErrorMessage(body, text, response.status));
  }

  return body;
}

function extractErrorMessage(body, text, status) {
  if (typeof body === "string" && body.trim()) {
    return body;
  }

  if (body && typeof body === "object") {
    const validationMessage = extractValidationMessage(body);
    if (validationMessage) {
      return validationMessage;
    }

    const error = stringValue(body.error);
    const message = stringValue(body.message);
    const detail = stringValue(body.detail);

    if (message && (!error || isGenericHttpError(error))) {
      return message;
    }

    if (error) {
      return error;
    }

    if (message) {
      return message;
    }

    if (detail) {
      return detail;
    }
  }

  return text || `Request failed with ${status}`;
}

function extractValidationMessage(body) {
  const errors = body.errors || body.fieldErrors || body.validationErrors;

  if (Array.isArray(errors)) {
    return errors
      .map((error) => {
        if (typeof error === "string") return error;
        if (!error || typeof error !== "object") return "";

        const field = stringValue(error.field || error.property || error.name);
        const message = stringValue(error.defaultMessage || error.message || error.error || error.reason);
        return field && message ? `${field}: ${message}` : message;
      })
      .filter(Boolean)
      .join("; ");
  }

  if (errors && typeof errors === "object") {
    return Object.entries(errors)
      .map(([field, value]) => {
        const message = Array.isArray(value) ? value.filter(Boolean).join(", ") : stringValue(value);
        return message ? `${field}: ${message}` : "";
      })
      .filter(Boolean)
      .join("; ");
  }

  return "";
}

function stringValue(value) {
  return typeof value === "string" && value.trim() ? value : "";
}

function isGenericHttpError(error) {
  return [
    "Bad Request",
    "Unauthorized",
    "Forbidden",
    "Not Found",
    "Conflict",
    "Internal Server Error",
    "Method Not Allowed"
  ].includes(error);
}

function tryParseJson(text) {
  try {
    return JSON.parse(text);
  } catch {
    return text;
  }
}

function slicePayload(data) {
  if (Array.isArray(data)) {
    return {
      content: data,
      first: true,
      last: true,
      number: 0,
      size: data.length
    };
  }

  return {
    content: Array.isArray(data?.content) ? data.content : [],
    first: data?.first ?? true,
    last: data?.last ?? true,
    number: data?.number ?? 0,
    size: data?.size ?? 20
  };
}

function contentPayload(data) {
  return slicePayload(data).content;
}

function pagedPath(path, page = 0, size = 20, extra = {}) {
  const query = new URLSearchParams({
    page: String(page),
    size: String(size),
    ...extra
  });

  return `${path}?${query}`;
}

function adminList(path, page = 0, size = 20, role) {
  const query = new URLSearchParams({
    page: String(page),
    size: String(size)
  });

  if (role) {
    query.set("role", role);
  }

  return request(`${path}?${query}`).then(slicePayload);
}

export const api = {
  async login(username, password) {
    const data = await request("/customers/login", {
      method: "POST",
      auth: false,
      body: JSON.stringify({ username, password })
    });
    const session = { ...data, role: ROLES.CUSTOMER };
    saveSession(session);
    return session;
  },

  async signup(username, email, password, confirmpassword) {
    const data = await request("/customers/signup", {
      method: "POST",
      auth: false,
      body: JSON.stringify({ username, email, password, confirmpassword })
    });
    const session = { ...data, role: ROLES.CUSTOMER };
    saveSession(session);
    return session;
  },

  async adminLogin(adminUsername, adminPassword) {
    const data = await request("/admin/api/adminlogin", {
      method: "POST",
      auth: false,
      body: JSON.stringify({ adminUsername, adminPassword })
    });
    const session = { ...data, role: ROLES.ADMIN };
    saveSession(session);
    return session;
  },

  async sellerLogin(sellersName, sellersPassword) {
    const data = await request("/sellers/sellerLogin", {
      method: "POST",
      auth: false,
      body: JSON.stringify({ sellersName, sellersPassword })
    });
    const session = { ...data, role: ROLES.SELLER };
    saveSession(session);
    return session;
  },

  async sellerSignup(username, email, password, confirmpassword) {
    const data = await request("/sellers/registration", {
      method: "POST",
      auth: false,
      body: JSON.stringify({ username, email, password, confirmpassword })
    });
    const session = { ...data, role: ROLES.SELLER };
    saveSession(session);
    return session;
  },

  adminRegister: (adminname, adminemail, adminpassword, adminconfirmpassword) =>
    request("/admin/api/register", {
      method: "POST",
      body: JSON.stringify({
        username: adminname,
        email: adminemail,
        password: adminpassword,
        confirmpassword: adminconfirmpassword
      })
    }),

  productsSlice: (page = 0, size = 20) => request(pagedPath("/customers/productlist", page, size)),
  products: (page = 0, size = 20) => api.productsSlice(page, size).then(contentPayload),
  product: (id) => request(`/customers/details/${id}`),
  cart: () => request("/Cart/getItems"),
  addToCart: (productId, quantity) =>
    request("/Cart/AddItems", {
      method: "POST",
      body: JSON.stringify({ productId, quantity })
    }),
  orders: () => request("/cutomers/GetOrder"),
  checkoutCart: (items) =>
    request("/cutomers/CartCheckout", {
      method: "POST",
      body: JSON.stringify({
        orderItems: items.map(({ productId, quantity }) => ({
          productId: Number(productId),
          quantity: Number(quantity)
        }))
      })
    }),
  buyNow: (productId, quantity = 1) =>
    request("/cutomers/buyNowCheckout", {
      method: "POST",
      body: JSON.stringify({
        orderItems: [{ productId: Number(productId), quantity: Number(quantity) }]
      })
    }),
  profile: () => request("/customers/profile-info"),
  saveProfile: (profile) =>
    request("/customers/profile", {
      method: "POST",
      body: JSON.stringify(profile)
    }),
  adminUsers: (page = 0, size = 20) => adminList("/admin/api/customers/sellers", page, size, ROLES.CUSTOMER),
  adminSellers: (page = 0, size = 20) => adminList("/admin/api/customers/sellers", page, size, ROLES.SELLER),
  adminOrders: (page = 0, size = 20) => request(pagedPath("/admin/api/adminOrders", page, size)).then(slicePayload),
  adminSellerOrders: (sellerId, page = 0, size = 20) =>
    request(pagedPath(`/admin/api/sellerOrdersForAdmin/${sellerId}`, page, size)).then(slicePayload),
  adminSellerStores: (sellerId) => request(`/admin/api/getAllStoresBySeller/${sellerId}`),
  adminSellerProducts: (sellerId, page = 0, size = 20) =>
    request(pagedPath(`/admin/api/seller/${sellerId}`, page, size)).then(slicePayload),
  approveStore: (storeId, status) =>
    request(`/admin/api/approveStores/${storeId}`, {
      method: "PATCH",
      body: JSON.stringify({ status })
    }),
  updateProductStatus: (storeId, productIds, status) =>
    request(`/admin/api/store/${storeId}/products?${new URLSearchParams({ status })}`, {
      method: "PATCH",
      body: JSON.stringify(productIds.map(Number))
    }),
  updateUserStatus: (userId, userStatus) =>
    request(`/admin/api/updateUserStatus/${userId}`, {
      method: "PATCH",
      body: JSON.stringify({ userStatus })
    }),
  sellerStores: () => request("/sellers/getStoresForSeller"),
  createSellerStore: (store) =>
    request("/sellers/addBusiness", {
      method: "POST",
      body: JSON.stringify(store)
    }),
  openSellerStore: (storeId, openCloseStore) =>
    request(`/sellers/openStore/${storeId}`, {
      method: "PATCH",
      body: JSON.stringify({ openCloseStore })
    }),
  sellerProductsSlice: (page = 0, size = 20) => request(pagedPath("/sellers/getProducts", page, size)),
  sellerProducts: (page = 0, size = 20) => api.sellerProductsSlice(page, size).then(contentPayload),
  addSellerProduct: (product) =>
    request("/sellers/addProducts", {
      method: "POST",
      body: JSON.stringify({
        ...product,
        storeId: Number(product.storeId),
        price: Number(product.price),
        quantity: Number(product.quantity)
      })
    })
};
