import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { ArrowLeft, Minus, Plus, ShoppingCart } from "lucide-react";
import { api } from "../lib/api";
import { ErrorBanner, LoadingBlock } from "../components/StateBlocks";
import { productImage } from "./ProductsPage";

export default function ProductDetailPage() {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [quantity, setQuantity] = useState(1);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");

  useEffect(() => {
    const productId = Number(id);
    if (!productId) return;
    api
      .product(productId)
      .then(setProduct)
      .catch((err) => setError(err instanceof Error ? err.message : "Product could not load"))
      .finally(() => setLoading(false));
  }, [id]);

  async function add() {
    if (!product) return;
    setNotice("");
    try {
      await api.addToCart(product.productId, quantity);
      setNotice("Added to cart.");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Could not add item");
    }
  }

  return (
    <main className="page detail-page">
      <Link className="back-link" to="/products">
        <ArrowLeft size={18} />
        Products
      </Link>
      {error && <ErrorBanner message={error} />}
      {notice && <div className="banner success">{notice}</div>}
      {loading && <LoadingBlock label="Loading product" />}
      {product && (
        <section className="detail-layout">
          <img className="detail-image" src={productImage(product)} alt={product.name} />
          <div className="detail-copy">
            <span className="pill">{product.quantity > 0 ? `${product.quantity} in stock` : "Out of stock"}</span>
            <h1>{product.name}</h1>
            <p>{product.description}</p>
            <strong className="price">Rs. {Number(product.price).toFixed(2)}</strong>
            <div className="quantity-control">
              <button type="button" onClick={() => setQuantity(Math.max(1, quantity - 1))} title="Decrease">
                <Minus size={17} />
              </button>
              <span>{quantity}</span>
              <button type="button" onClick={() => setQuantity(quantity + 1)} title="Increase">
                <Plus size={17} />
              </button>
            </div>
            <button className="button" type="button" onClick={add}>
              <ShoppingCart size={18} />
              Add to cart
            </button>
          </div>
        </section>
      )}
    </main>
  );
}
