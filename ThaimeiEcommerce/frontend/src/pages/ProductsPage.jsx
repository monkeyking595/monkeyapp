import { useEffect, useMemo, useState } from "react";
import { Link } from "react-router-dom";
import { Plus, Search } from "lucide-react";
import { api } from "../lib/api";
import { EmptyState, ErrorBanner, LoadingBlock } from "../components/StateBlocks";

const fallbackImages = [
  "https://images.unsplash.com/photo-1523381294911-8d3cead13475?auto=format&fit=crop&w=900&q=80",
  "https://images.unsplash.com/photo-1489987707025-afc232f7ea0f?auto=format&fit=crop&w=900&q=80",
  "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?auto=format&fit=crop&w=900&q=80"
];

export function productImage(product, index = 0) {
  return product.imageURL || fallbackImages[index % fallbackImages.length];
}

export default function ProductsPage() {
  const [products, setProducts] = useState([]);
  const [query, setQuery] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");

  useEffect(() => {
    api
      .products()
      .then(setProducts)
      .catch((err) => setError(err instanceof Error ? err.message : "Products could not load"))
      .finally(() => setLoading(false));
  }, []);

  const visibleProducts = useMemo(() => {
    const normalized = query.trim().toLowerCase();
    if (!normalized) return products;
    return products.filter((product) =>
      [product.name, product.description].some((value) => value.toLowerCase().includes(normalized))
    );
  }, [products, query]);

  async function add(productId) {
    setNotice("");
    try {
      await api.addToCart(productId, 1);
      setNotice("Added to cart.");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Could not add item");
    }
  }

  return (
    <main className="page">
      <div className="page-heading">
        <div>
          <span className="pill">Storefront</span>
          <h1>Products</h1>
        </div>
        <label className="field-inline">
          <Search size={18} />
          <input value={query} onChange={(event) => setQuery(event.target.value)} placeholder="Filter products" />
        </label>
      </div>

      {error && <ErrorBanner message={error} />}
      {notice && <div className="banner success">{notice}</div>}
      {loading && <LoadingBlock label="Loading products" />}
      {!loading && !visibleProducts.length && <EmptyState title="No products yet" text="Add products in the backend database and they will appear here." />}

      <section className="product-grid">
        {visibleProducts.map((product, index) => (
          <article className="product-card" key={product.productId}>
            <Link to={`/products/${product.productId}`} className="product-image-link">
              <img src={productImage(product, index)} alt={product.name} />
            </Link>
            <div className="product-info">
              <div>
                <h2>{product.name}</h2>
                <p>{product.description}</p>
              </div>
              <div className="product-actions">
                <strong>Rs. {Number(product.price).toFixed(2)}</strong>
                <button className="icon-button dark" type="button" onClick={() => add(product.productId)} title="Add to cart">
                  <Plus size={18} />
                </button>
              </div>
            </div>
          </article>
        ))}
      </section>
    </main>
  );
}
