import { useEffect, useMemo, useState } from "react";
import { CreditCard } from "lucide-react";
import { api } from "../lib/api";
import { EmptyState, ErrorBanner, LoadingBlock } from "../components/StateBlocks";

export default function CartPage() {
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");
  const total = useMemo(() => cart?.items.reduce((sum, item) => sum + Number(item.totalPrice || 0), 0) ?? 0, [cart]);

  useEffect(() => {
    api
      .cart()
      .then(setCart)
      .catch((err) => setError(err instanceof Error ? err.message : "Cart could not load"))
      .finally(() => setLoading(false));
  }, []);

  async function placeOrders() {
    if (!cart?.items.length) return;
    setError("");
    setNotice("");
    try {
      for (const item of cart.items) {
        await api.placeOrder({
          id: item.productId,
          productName: item.productName,
          quantity: item.quantity,
          status: "PLACED",
          totalPrice: Number(item.totalPrice),
          imageURL: item.imageURL || ""
        });
      }
      setNotice("Order request sent.");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Order could not be placed");
    }
  }

  return (
    <main className="page">
      <div className="page-heading">
        <div>
          <span className="pill">Checkout</span>
          <h1>Your cart</h1>
        </div>
        <strong className="price">Rs. {total.toFixed(2)}</strong>
      </div>

      {error && <ErrorBanner message={error} />}
      {notice && <div className="banner success">{notice}</div>}
      {loading && <LoadingBlock label="Loading cart" />}
      {!loading && !cart?.items?.length && <EmptyState title="Cart is empty" text="Add a product and it will show up here." />}

      {!!cart?.items?.length && (
        <section className="split-layout">
          <div className="line-items">
            {cart.items.map((item) => (
              <article className="line-item" key={item.productId}>
                <div>
                  <h2>{item.productName}</h2>
                  <p>Qty {item.quantity} x Rs. {Number(item.price).toFixed(2)}</p>
                </div>
                <strong>Rs. {Number(item.totalPrice).toFixed(2)}</strong>
              </article>
            ))}
          </div>
          <aside className="summary-panel">
            <h2>Summary</h2>
            <p>{cart.items.length} items ready for checkout.</p>
            <div className="summary-row">
              <span>Total</span>
              <strong>Rs. {total.toFixed(2)}</strong>
            </div>
            <button className="button" type="button" onClick={placeOrders}>
              <CreditCard size={18} />
              Place order
            </button>
          </aside>
        </section>
      )}
    </main>
  );
}
