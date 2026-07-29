import { useEffect, useMemo, useState } from "react";
import { Boxes, PackagePlus, Power, RefreshCw, Store, Trash2 } from "lucide-react";
import { api } from "../lib/api";
import { EmptyState, ErrorBanner, LoadingBlock } from "../components/StateBlocks";

const businessTypes = ["RETAIL", "INDIVIDUAL"];
const categories = ["T_SHIRTS", "HOODIES", "PANTS", "SHOES", "JACKETS"];
const colors = ["RED", "BLUE", "GREY", "YELLOW", "BLACK", "WHITE"];
const sizes = ["XS", "S", "M", "L", "XL", "XXL"];

const initialStoreForm = {
  storeName: "",
  businessType: "RETAIL",
  latitude: "",
  longitude: ""
};

const initialProductForm = {
  storeId: "",
  name: "",
  price: "",
  description: "",
  imageURL: "",
  quantity: 1,
  category: "T_SHIRTS",
  color: "BLACK",
  size: "M"
};

function label(value = "") {
  return String(value || "").replaceAll("_", " ");
}

function productId(product) {
  return product?.productId ?? product?.id;
}

function productStoreId(product) {
  return product?.storeId ?? product?.store?.storeId ?? product?.storeModel?.storeId;
}

function productStatus(product) {
  return product?.status || product?.productStatus || "ACTIVE";
}

export default function SellerDashboardPage() {
  const [stores, setStores] = useState([]);
  const [products, setProducts] = useState([]);
  const [storeForm, setStoreForm] = useState(initialStoreForm);
  const [productForm, setProductForm] = useState(initialProductForm);
  const [manageStoreId, setManageStoreId] = useState("");
  const [selectedProductIds, setSelectedProductIds] = useState([]);
  const [loading, setLoading] = useState(true);
  const [busyStore, setBusyStore] = useState(false);
  const [busyProduct, setBusyProduct] = useState(false);
  const [busyDelete, setBusyDelete] = useState(false);
  const [busyStoreStatusId, setBusyStoreStatusId] = useState(null);
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");

  useEffect(() => {
    loadSellerData();
  }, []);

  const selectedStore = useMemo(
    () => stores.find((store) => String(store.storeId) === String(productForm.storeId)),
    [productForm.storeId, stores]
  );

  const visibleProducts = useMemo(() => {
    if (!manageStoreId) return products;

    const productsWithStore = products.filter((product) => productStoreId(product));
    if (!productsWithStore.length) return products;

    return products.filter((product) => String(productStoreId(product)) === String(manageStoreId));
  }, [manageStoreId, products]);

  useEffect(() => {
    const firstStoreId = stores[0]?.storeId ? String(stores[0].storeId) : "";

    if (!stores.length) {
      setProductForm((current) => ({ ...current, storeId: "" }));
      setManageStoreId("");
      setSelectedProductIds([]);
      return;
    }

    setProductForm((current) => (current.storeId ? current : { ...current, storeId: firstStoreId }));
    setManageStoreId((current) => current || firstStoreId);
  }, [stores]);

  async function loadSellerData() {
    setError("");
    setLoading(true);
    try {
      const [nextStores, nextProducts] = await Promise.all([api.sellerStores(), api.sellerProducts()]);
      setStores(nextStores || []);
      setProducts(nextProducts || []);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Seller data could not load");
    } finally {
      setLoading(false);
    }
  }

  function updateStoreForm(key, value) {
    setStoreForm((current) => ({ ...current, [key]: value }));
  }

  function updateProductForm(key, value) {
    setProductForm((current) => ({ ...current, [key]: value }));
  }

  async function createStore(event) {
    event.preventDefault();
    setError("");
    setNotice("");
    setBusyStore(true);

    try {
      await api.createSellerStore({
        ...storeForm,
        latitude: Number(storeForm.latitude),
        longitude: Number(storeForm.longitude)
      });
      setStoreForm(initialStoreForm);
      setNotice("Store registered.");
      await loadSellerData();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Store could not be created");
    } finally {
      setBusyStore(false);
    }
  }

  async function addProduct(event) {
    event.preventDefault();
    setError("");
    setNotice("");
    setBusyProduct(true);

    try {
      await api.addSellerProduct(productForm);
      setProductForm((current) => ({
        ...initialProductForm,
        storeId: current.storeId
      }));
      setNotice("Product saved.");
      await loadSellerData();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Product could not be saved");
    } finally {
      setBusyProduct(false);
    }
  }

  async function toggleStoreOpenState(store) {
    const nextStatus = store.openCloseStore === "OPEN" ? "CLOSED" : "OPEN";

    setError("");
    setNotice("");
    setBusyStoreStatusId(store.storeId);

    try {
      await api.openSellerStore(store.storeId, nextStatus);
      setStores((current) =>
        current.map((item) => (item.storeId === store.storeId ? { ...item, openCloseStore: nextStatus } : item))
      );
      setNotice(`${store.storeName} is now ${label(nextStatus).toLowerCase()}.`);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Store status could not be updated");
    } finally {
      setBusyStoreStatusId(null);
    }
  }

  function toggleProductSelection(id) {
    if (id === null || id === undefined) return;

    setSelectedProductIds((current) =>
      current.includes(id) ? current.filter((item) => item !== id) : [...current, id]
    );
  }

  async function deleteSelectedProducts() {
    if (!manageStoreId) {
      setError("Select a store before deleting products.");
      return;
    }

    if (!selectedProductIds.length) {
      setError("Select at least one product to delete.");
      return;
    }

    setError("");
    setNotice("");
    setBusyDelete(true);

    try {
      await api.deleteSellerProducts(manageStoreId, selectedProductIds);
      const count = selectedProductIds.length;
      setSelectedProductIds([]);
      setNotice(`Deleted ${count} product${count === 1 ? "" : "s"}.`);
      await loadSellerData();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Products could not be deleted");
    } finally {
      setBusyDelete(false);
    }
  }

  return (
    <main className="page">
      <div className="page-heading">
        <div>
          <span className="pill">Seller</span>
          <h1>Store Manager</h1>
        </div>
        <button className="icon-button" type="button" onClick={loadSellerData} title="Refresh seller data">
          <RefreshCw size={18} />
        </button>
      </div>

      {error && <ErrorBanner message={error} />}
      {notice && <div className="banner success">{notice}</div>}
      {loading && <LoadingBlock label="Loading seller workspace" />}

      {!loading && (
        <>
          <section className="seller-grid">
            <form className="profile-form seller-form" onSubmit={createStore}>
              <div className="form-heading span-two">
                <Store size={22} />
                <h2>Register Store</h2>
              </div>
              <label>
                Store name
                <input
                  value={storeForm.storeName}
                  onChange={(event) => updateStoreForm("storeName", event.target.value)}
                  required
                />
              </label>
              <label>
                Business type
                <select
                  value={storeForm.businessType}
                  onChange={(event) => updateStoreForm("businessType", event.target.value)}
                  required
                >
                  {businessTypes.map((type) => (
                    <option key={type} value={type}>
                      {label(type)}
                    </option>
                  ))}
                </select>
              </label>
              <label>
                Latitude
                <input
                  type="number"
                  step="any"
                  value={storeForm.latitude}
                  onChange={(event) => updateStoreForm("latitude", event.target.value)}
                  required
                />
              </label>
              <label>
                Longitude
                <input
                  type="number"
                  step="any"
                  value={storeForm.longitude}
                  onChange={(event) => updateStoreForm("longitude", event.target.value)}
                  required
                />
              </label>
              <button className="button span-two" type="submit" disabled={busyStore}>
                <Store size={18} />
                {busyStore ? "Saving..." : "Save store"}
              </button>
            </form>

            <form className="profile-form seller-form" onSubmit={addProduct}>
              <div className="form-heading span-two">
                <PackagePlus size={22} />
                <h2>Add Product</h2>
              </div>
              <label className="span-two">
                Store
                <select
                  value={productForm.storeId}
                  onChange={(event) => updateProductForm("storeId", event.target.value)}
                  disabled={!stores.length}
                  required
                >
                  {!stores.length && <option value="">No stores</option>}
                  {stores.map((store) => (
                    <option key={store.storeId} value={store.storeId}>
                      {store.storeName}
                    </option>
                  ))}
                </select>
              </label>
              <label>
                Product name
                <input
                  value={productForm.name}
                  onChange={(event) => updateProductForm("name", event.target.value)}
                  required
                />
              </label>
              <label>
                Price
                <input
                  type="number"
                  step="0.01"
                  min="0.01"
                  value={productForm.price}
                  onChange={(event) => updateProductForm("price", event.target.value)}
                  required
                />
              </label>
              <label>
                Quantity
                <input
                  type="number"
                  min="1"
                  value={productForm.quantity}
                  onChange={(event) => updateProductForm("quantity", event.target.value)}
                  required
                />
              </label>
              <label>
                Image URL
                <input
                  value={productForm.imageURL}
                  onChange={(event) => updateProductForm("imageURL", event.target.value)}
                  required
                />
              </label>
              <label>
                Category
                <select value={productForm.category} onChange={(event) => updateProductForm("category", event.target.value)}>
                  {categories.map((category) => (
                    <option key={category} value={category}>
                      {label(category)}
                    </option>
                  ))}
                </select>
              </label>
              <label>
                Color
                <select value={productForm.color} onChange={(event) => updateProductForm("color", event.target.value)}>
                  {colors.map((color) => (
                    <option key={color} value={color}>
                      {label(color)}
                    </option>
                  ))}
                </select>
              </label>
              <label>
                Size
                <select value={productForm.size} onChange={(event) => updateProductForm("size", event.target.value)}>
                  {sizes.map((size) => (
                    <option key={size} value={size}>
                      {size}
                    </option>
                  ))}
                </select>
              </label>
              <label className="span-two">
                Description
                <textarea
                  value={productForm.description}
                  onChange={(event) => updateProductForm("description", event.target.value)}
                  required
                />
              </label>
              <button className="button span-two" type="submit" disabled={busyProduct || !selectedStore}>
                <PackagePlus size={18} />
                {busyProduct ? "Saving..." : "Save product"}
              </button>
            </form>
          </section>

          <section className="seller-summary">
            <div className="summary-panel">
              <div className="form-heading">
                <Store size={22} />
                <h2>Stores</h2>
              </div>
              {!stores.length ? (
                <EmptyState title="No stores yet" text="Registered stores will appear here." />
              ) : (
                <div className="store-manage-list">
                  {stores.map((store) => {
                    const openCloseStore = store.openCloseStore || "CLOSED";
                    const nextStatus = openCloseStore === "OPEN" ? "CLOSED" : "OPEN";

                    return (
                      <article className="store-manage-row" key={store.storeId}>
                        <div>
                          <strong>{store.storeName}</strong>
                          <small>#{store.storeId}</small>
                        </div>
                        <button
                          className="button compact"
                          type="button"
                          disabled={busyStoreStatusId === store.storeId}
                          onClick={() => toggleStoreOpenState(store)}
                          title={`Mark store ${nextStatus.toLowerCase()}`}
                        >
                          <Power size={17} />
                          {label(openCloseStore)}
                        </button>
                      </article>
                    );
                  })}
                </div>
              )}
            </div>

            <div className="table-wrap">
              <div className="table-title">
                <div className="form-heading">
                  <Boxes size={22} />
                  <h2>Products</h2>
                </div>
              </div>
              <div className="seller-product-tools">
                <label>
                  Store
                  <select
                    value={manageStoreId}
                    onChange={(event) => {
                      setManageStoreId(event.target.value);
                      setSelectedProductIds([]);
                    }}
                    disabled={!stores.length}
                  >
                    {!stores.length && <option value="">No stores</option>}
                    {stores.map((store) => (
                      <option key={store.storeId} value={store.storeId}>
                        {store.storeName}
                      </option>
                    ))}
                  </select>
                </label>
                <button
                  className="button compact danger"
                  type="button"
                  onClick={deleteSelectedProducts}
                  disabled={busyDelete || !selectedProductIds.length}
                >
                  <Trash2 size={17} />
                  Delete selected
                </button>
              </div>
              {!visibleProducts.length ? (
                <EmptyState title="No products yet" text="Saved seller products will appear here." />
              ) : (
                <table>
                  <thead>
                    <tr>
                      <th></th>
                      <th>ID</th>
                      <th>Name</th>
                      <th>Variant</th>
                      <th>Quantity</th>
                      <th>Price</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    {visibleProducts.map((product) => {
                      const id = productId(product);
                      const hasId = id !== null && id !== undefined;
                      const status = productStatus(product);

                      return (
                        <tr key={hasId ? id : product.name}>
                          <td>
                            <input
                              className="row-check"
                              type="checkbox"
                              checked={selectedProductIds.includes(id)}
                              disabled={!hasId}
                              onChange={() => toggleProductSelection(id)}
                              aria-label={`Select ${product.name || `product ${id}`}`}
                            />
                          </td>
                          <td>{id || "-"}</td>
                          <td>{product.name}</td>
                          <td>
                            {label(product.category)} / {label(product.color)} / {product.size}
                          </td>
                          <td>{product.quantity}</td>
                          <td>Rs. {Number(product.price).toFixed(2)}</td>
                          <td>
                            <span className={`status status-${String(status).toLowerCase()}`}>{label(status)}</span>
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              )}
            </div>
          </section>
        </>
      )}
    </main>
  );
}
