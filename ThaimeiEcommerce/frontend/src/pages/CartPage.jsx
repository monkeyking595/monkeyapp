import { useEffect, useMemo, useState } from "react";
import { CreditCard, RefreshCw } from "lucide-react";
import PaymentCheckout from "../components/PaymentCheckout";
import { api, paymentIntentIdFromClientSecret } from "../lib/api";
import { EmptyState, ErrorBanner, LoadingBlock } from "../components/StateBlocks";

export default function CartPage() {
  const [cart, setCart] = useState(null);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [checkoutBusy, setCheckoutBusy] = useState(false);
  const [checkoutSession, setCheckoutSession] = useState(null);
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");
  const items = useMemo(() => {
    const productMatches = products.reduce((matches, product) => {
      const keys = [
        product.name,
        `${product.name}|${product.price}`,
        `${product.name}|${product.price}|${product.imageURL || ""}`
      ];

      keys.forEach((key) => {
        if (key && !matches.has(key)) {
          matches.set(key, product);
        }
      });

      return matches;
    }, new Map());

    return (cart?.items ?? []).map((item) => {
      const product =
        productMatches.get(`${item.productName}|${item.price}|${item.imageURL || ""}`) ||
        productMatches.get(`${item.productName}|${item.price}`) ||
        productMatches.get(item.productName);

      return { ...item, productId: item.productId || product?.productId };
    });
  }, [cart, products]);
  const total = useMemo(() => items.reduce((sum, item) => sum + Number(item.totalPrice || 0), 0), [items]);

  useEffect(() => {
    loadCart();
  }, []);

  async function loadCart() {
    setError("");
    setLoading(true);

    Promise.all([api.cart(), api.products(0, 100)])
      .then(([nextCart, nextProducts]) => {
        setCart(nextCart);
        setProducts(nextProducts);
      })
      .catch((err) => setError(err instanceof Error ? err.message : "Cart could not load"))
      .finally(() => setLoading(false));
  }

  async function placeOrders() {
    if (!items.length) return;
    setError("");
    setNotice("");
    setCheckoutBusy(true);

    const orderItems = items.filter((item) => item.productId);

    if (orderItems.length !== items.length) {
      setError("Some cart products could not be matched for checkout.");
      setCheckoutBusy(false);
      return;
    }

    try {
      const checkout = await api.checkoutCart(orderItems);
      const clientSecret = checkout?.clientSecret;

      if (!clientSecret) {
        throw new Error("Checkout did not return a payment client secret.");
      }

      setCheckoutSession({
        clientSecret,
        paymentId: paymentIntentIdFromClientSecret(clientSecret)
      });
      setNotice("Order created. Complete the payment to confirm it.");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Order could not be placed");
    } finally {
      setCheckoutBusy(false);
    }
  }

  function handlePaymentComplete({ payment }) {
    setNotice(payment ? "Payment confirmed and recorded." : "Payment confirmed. Payment status will update after the webhook.");
    loadCart();
  }

  return (
    <main className="page">
      <div className="page-heading">
        <div>
          <span className="pill">Checkout</span>
          <h1>Your cart</h1>
        </div>
        <div className="heading-actions">
          <strong className="price">Rs. {total.toFixed(2)}</strong>
          <button className="icon-button" type="button" onClick={loadCart} title="Refresh cart">
            <RefreshCw size={18} />
          </button>
        </div>
      </div>

      {error && <ErrorBanner message={error} />}
      {notice && <div className="banner success">{notice}</div>}
      {loading && <LoadingBlock label="Loading cart" />}
      {!loading && !items.length && <EmptyState title="Cart is empty" text="Add a product and it will show up here." />}

      {!!items.length && (
        <section className="split-layout">
          <div className="line-items">
            {items.map((item, index) => (
              <article className="line-item" key={item.itemId || `${item.productName}-${index}`}>
                <div className="item-copy">
                  {item.imageURL && <img className="item-thumb" src={item.imageURL} alt={item.productName} />}
                  <div>
                    <h2>{item.productName}</h2>
                    <p>Qty {item.quantity} x Rs. {Number(item.price).toFixed(2)}</p>
                  </div>
                </div>
                <strong>Rs. {Number(item.totalPrice).toFixed(2)}</strong>
              </article>
            ))}
          </div>
          <aside className="summary-panel">
            <h2>Summary</h2>
            <p>{items.length} items ready for checkout.</p>
            <div className="summary-row">
              <span>Total</span>
              <strong>Rs. {total.toFixed(2)}</strong>
            </div>
            {checkoutSession ? (
              <PaymentCheckout
                clientSecret={checkoutSession.clientSecret}
                intentId={checkoutSession.paymentId}
                amountLabel={`Rs. ${total.toFixed(2)}`}
                onPaid={handlePaymentComplete}
                onCancel={() => setCheckoutSession(null)}
              />
            ) : (
              <button className="button" type="button" onClick={placeOrders} disabled={checkoutBusy}>
                <CreditCard size={18} />
                {checkoutBusy ? "Placing..." : "Place order"}
              </button>
            )}
          </aside>
        </section>
      )}
    </main>
  );
}
