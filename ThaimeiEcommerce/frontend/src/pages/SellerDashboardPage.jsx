import { useEffect, useMemo, useState } from "react";
import { Boxes, PackagePlus, RefreshCw, Store } from "lucide-react";
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

function label(value) {
  return value.replaceAll("_", " ");
}

export default function SellerDashboardPage() {
  const [stores, setStores] = useState([]);
  const [products, setProducts] = useState([]);
  const [storeForm, setStoreForm] = useState(initialStoreForm);
  const [productForm, setProductForm] = useState(initialProductForm);
  const [loading, setLoading] = useState(true);
  const [busyStore, setBusyStore] = useState(false);
  const [busyProduct, setBusyProduct] = useState(false);
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");

  useEffect(() => {
    loadSellerData();
  }, []);

  useEffect(() => {
    if (!productForm.storeId && stores.length) {
      setProductForm((current) => ({ ...current, storeId: String(stores[0].storeId) }));
    }
  }, [productForm.storeId, stores]);

  const selectedStore = useMemo(
    () => stores.find((store) => String(store.storeId) === String(productForm.storeId)),
    [productForm.storeId, stores]
  );

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
                <div className="store-list">
                  {stores.map((store) => (
                    <span className="store-chip" key={store.storeId}>
                      {store.storeName}
                      <small>#{store.storeId}</small>
                    </span>
                  ))}
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
              {!products.length ? (
                <EmptyState title="No products yet" text="Saved seller products will appear here." />
              ) : (
                <table>
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Name</th>
                      <th>Variant</th>
                      <th>Quantity</th>
                      <th>Price</th>
                    </tr>
                  </thead>
                  <tbody>
                    {products.map((product) => (
                      <tr key={product.productId}>
                        <td>{product.productId}</td>
                        <td>{product.name}</td>
                        <td>
                          {label(product.category)} / {label(product.color)} / {product.size}
                        </td>
                        <td>{product.quantity}</td>
                        <td>Rs. {Number(product.price).toFixed(2)}</td>
                      </tr>
                    ))}
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
