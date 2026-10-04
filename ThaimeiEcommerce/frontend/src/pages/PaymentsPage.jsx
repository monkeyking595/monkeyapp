import { useEffect, useMemo, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { ArrowLeft, CreditCard, RefreshCw, Search } from "lucide-react";
import { api } from "../lib/api";
import { EmptyState, ErrorBanner, LoadingBlock } from "../components/StateBlocks";

export default function PaymentsPage() {
  const [searchParams] = useSearchParams();
  const initialPaymentId = searchParams.get("paymentId") || searchParams.get("payment_intent") || "";
  const redirectStatus = searchParams.get("redirect_status") || "";
  const [paymentId, setPaymentId] = useState(initialPaymentId);
  const [payment, setPayment] = useState(null);
  const [loading, setLoading] = useState(Boolean(initialPaymentId));
  const [error, setError] = useState("");
  const [awaitingRecord, setAwaitingRecord] = useState(false);

  useEffect(() => {
    if (initialPaymentId) {
      loadPayment(initialPaymentId, { waitForWebhook: redirectStatus === "succeeded" });
    }
  }, [initialPaymentId]);

  async function loadPayment(nextPaymentId = paymentId, { waitForWebhook = false } = {}) {
    const trimmedPaymentId = nextPaymentId.trim();
    if (!trimmedPaymentId) return;

    setError("");
    setAwaitingRecord(false);
    setLoading(true);
    setPayment(null);

    for (let attempt = 0; attempt < (waitForWebhook ? 10 : 1); attempt += 1) {
      if (attempt > 0) {
        await new Promise((resolve) => window.setTimeout(resolve, 1000));
      }

      try {
        const nextPayment = await api.paymentDetails(trimmedPaymentId);
        setPayment(nextPayment);
        setLoading(false);
        return;
      } catch (err) {
        if (attempt === 0 && !waitForWebhook) {
          setError(err instanceof Error ? err.message : "Payment could not load");
          setLoading(false);
          return;
        }
      }
    }

    setAwaitingRecord(true);
    setLoading(false);
  }

  function submit(event) {
    event.preventDefault();
    loadPayment(paymentId, { waitForWebhook: redirectStatus === "succeeded" });
  }

  const status = payment?.paymentStatus || payment?.status || "";
  const amount = useMemo(() => formatMoney(payment?.totalAmount, payment?.currency), [payment]);
  const orders = useMemo(() => normalizeOrders(payment), [payment]);
  const orderIds = useMemo(() => normalizeOrderIds(payment, orders), [payment, orders]);
  const failedOrders = orders.filter((order) => String(order.status || "").toUpperCase() === "FAILED");
  const orderLabel = formatOrderIds(orderIds);
  const orderSummary = formatOrderSummary(orderIds);

  return (
    <main className="page narrow">
      <Link className="back-link" to="/orders">
        <ArrowLeft size={18} />
        Orders
      </Link>

      <div className="page-heading">
        <div>
          <span className="pill">Payments</span>
          <h1>Payment status</h1>
        </div>
        <CreditCard size={30} />
      </div>

      {redirectStatus === "succeeded" && (
        <div className="banner success">Stripe confirmed the payment. The backend record may take a moment to appear.</div>
      )}
      {error && <ErrorBanner message={error} />}
      {awaitingRecord && (
        <div className="banner">
          Stripe confirmed the payment, but the backend has not recorded it yet. Check again shortly before relying on the updated inventory.
        </div>
      )}
      {!!failedOrders.length && (
        <div className="banner warning">
          Payment was successful, but {failedOrders.length} order{failedOrders.length === 1 ? "" : "s"} could not be fulfilled. Review the order statuses below.
        </div>
      )}

      <form className="lookup-form" onSubmit={submit}>
        <label>
          Payment intent ID
          <input
            type="text"
            value={paymentId}
            onChange={(event) => setPaymentId(event.target.value)}
            placeholder="pi_..."
            autoComplete="off"
          />
        </label>
        <button className="button" type="submit" disabled={loading || !paymentId.trim()}>
          {loading ? <RefreshCw size={18} /> : <Search size={18} />}
          {loading ? "Checking..." : awaitingRecord ? "Check again" : "Check payment"}
        </button>
      </form>

      {loading && <LoadingBlock label="Loading payment" />}
      {!loading && !payment && !error && (
        <EmptyState
          title={awaitingRecord ? "Payment record is pending" : "No payment selected"}
          text={awaitingRecord ? "The webhook may still be processing. Use Check again to refresh its status." : "Enter the Stripe PaymentIntent id returned during checkout."}
        />
      )}

      {payment && (
        <section className="payment-details">
          <div className="payment-status-card">
            <span className={`status status-${String(status || "unknown").toLowerCase()}`}>{status || "UNKNOWN"}</span>
            <h2>{amount}</h2>
            <p>{payment.currency || "INR"} payment for {orderSummary}</p>
          </div>
          <dl className="payment-meta">
            <div>
              <dt>Payment ID</dt>
              <dd>{payment.paymentId || paymentId}</dd>
            </div>
            <div>
              <dt>{orderIds.length === 1 ? "Order ID" : "Order IDs"}</dt>
              <dd>{orderLabel}</dd>
            </div>
            <div>
              <dt>Method</dt>
              <dd>{payment.paymentMethod || "Not recorded"}</dd>
            </div>
            <div>
              <dt>Currency</dt>
              <dd>{payment.currency || "INR"}</dd>
            </div>
          </dl>
        </section>
      )}
      {!!orders.length && (
        <section className="payment-orders">
          <h2>Order outcomes</h2>
          <ul className="payment-order-outcomes">
            {orders.map((order) => (
              <li key={order.orderId}>
                <span>Order {order.orderId ?? "—"}</span>
                <span className={`status status-${String(order.status || "unknown").toLowerCase()}`}>
                  {order.status || "UNKNOWN"}
                </span>
              </li>
            ))}
          </ul>
        </section>
      )}
    </main>
  );
}

function normalizeOrders(payment) {
  return Array.isArray(payment?.orders) ? payment.orders : [];
}

function normalizeOrderIds(payment, orders) {
  if (!payment) return [];

  const rawOrderIds = orders.length
    ? orders.map((order) => order.orderId)
    : Array.isArray(payment.orderIds)
      ? payment.orderIds
      : payment.orderId
        ? [payment.orderId]
        : [];
  return rawOrderIds.map((orderId) => String(orderId).trim()).filter(Boolean);
}

function formatOrderIds(orderIds) {
  return orderIds.length ? orderIds.join(", ") : "Not recorded";
}

function formatOrderSummary(orderIds) {
  if (!orderIds.length) return "orders not recorded";
  return `${orderIds.length === 1 ? "order" : "orders"} ${formatOrderIds(orderIds)}`;
}

function formatMoney(amount, currency = "INR") {
  const value = Number(amount || 0);
  const code = currency || "INR";

  try {
    return new Intl.NumberFormat("en-IN", {
      style: "currency",
      currency: code
    }).format(value);
  } catch {
    return `${code} ${value.toFixed(2)}`;
  }
}
