import { useEffect, useMemo, useState } from "react";
import { Link } from "react-router-dom";
import { Plus, RefreshCw, Search } from "lucide-react";
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
  const [page, setPage] = useState(0);
  const [pageInfo, setPageInfo] = useState({ first: true, last: true, number: 0 });
  const [loading, setLoading] = useState(true);
  const [addingId, setAddingId] = useState(null);
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");

  useEffect(() => {
    loadProducts();
  }, [page]);

  async function loadProducts() {
    setError("");
    setLoading(true);

    try {
      const data = await api.productsSlice(page);
      setProducts(data.content || []);
      setPageInfo({
        first: data.first ?? page === 0,
        last: data.last ?? true,
        number: data.number ?? page
      });
    } catch (err) {
      setError(err instanceof Error ? err.message : "Products could not load");
    } finally {
      setLoading(false);
    }
  }

  const visibleProducts = useMemo(() => {
    const normalized = query.trim().toLowerCase();
    if (!normalized) return products;
    return products.filter((product) =>
      [product.name, product.description].some((value = "") => value.toLowerCase().includes(normalized))
    );
  }, [products, query]);

  async function add(productId) {
    setError("");
    setNotice("");
    setAddingId(productId);

    try {
      await api.addToCart(productId, 1);
      setNotice("Added to cart.");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Could not add item");
    } finally {
      setAddingId(null);
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
        <button className="icon-button" type="button" onClick={loadProducts} title="Refresh products">
          <RefreshCw size={18} />
        </button>
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
                <span className="meta-line">
                  {product.category?.replaceAll("_", " ")} / {product.color} / {product.size}
                </span>
                <h2>{product.name}</h2>
                <p>{product.description}</p>
              </div>
              <div className="product-actions">
                <strong>Rs. {Number(product.price).toFixed(2)}</strong>
                <button
                  className="icon-button dark"
                  type="button"
                  onClick={() => add(product.productId)}
                  title="Add to cart"
                  disabled={addingId === product.productId || Number(product.quantity) < 1}
                >
                  <Plus size={18} />
                </button>
              </div>
            </div>
          </article>
        ))}
      </section>

      {!loading && !!products.length && (
        <div className="pager">
          <button className="button compact" type="button" onClick={() => setPage((value) => Math.max(0, value - 1))} disabled={pageInfo.first || loading}>
            Previous
          </button>
          <span>Page {pageInfo.number + 1}</span>
          <button className="button compact" type="button" onClick={() => setPage((value) => value + 1)} disabled={pageInfo.last || loading}>
            Next
          </button>
        </div>
      )}
    </main>
  );
}
