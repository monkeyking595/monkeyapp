import { useRef, useState } from "react";
import { Elements, PaymentElement, useElements, useStripe } from "@stripe/react-stripe-js";
import { loadStripe } from "@stripe/stripe-js";
import { AlertTriangle, CheckCircle2, CreditCard, ExternalLink, RefreshCw, ShieldCheck, X } from "lucide-react";
import { Link } from "react-router-dom";
import { api, paymentIntentIdFromClientSecret } from "../lib/api";

const publishableKey = import.meta.env.VITE_STRIPE_PUBLISHABLE_KEY || import.meta.env.STRIPE_PUBLISHABLE_KEY || "";
const stripePromise = publishableKey ? loadStripe(publishableKey) : null;

const stripeAppearance = {
  theme: "stripe",
  variables: {
    colorPrimary: "#161616",
    colorText: "#22211f",
    colorDanger: "#9a291d",
    borderRadius: "8px",
    fontFamily: "Inter, system-ui, sans-serif"
  }
};

export default function PaymentCheckout({ clientSecret, intentId, amountLabel, onPaid, onCancel }) {
  const paymentId = intentId || paymentIntentIdFromClientSecret(clientSecret);

  if (!clientSecret) return null;

  if (!stripePromise) {
    return (
      <div className="payment-panel">
        <div className="banner error">
          Stripe publishable key is missing. Add `STRIPE_PUBLISHABLE_KEY` or `VITE_STRIPE_PUBLISHABLE_KEY` before paying.
        </div>
      </div>
    );
  }

  return (
    <section className="payment-panel" aria-label="Payment">
      <div className="payment-heading">
        <div>
          <span className="pill">Payment</span>
          <h2>Secure checkout</h2>
        </div>
        <ShieldCheck size={24} />
      </div>
      {amountLabel && (
        <div className="summary-row compact-row">
          <span>Amount</span>
          <strong>{amountLabel}</strong>
        </div>
      )}
      <Elements stripe={stripePromise} options={{ clientSecret, appearance: stripeAppearance }} key={clientSecret}>
        <CheckoutForm paymentId={paymentId} onPaid={onPaid} onCancel={onCancel} />
      </Elements>
    </section>
  );
}

function CheckoutForm({ paymentId, onPaid, onCancel }) {
  const stripe = useStripe();
  const elements = useElements();
  const completedPaymentId = useRef("");
  const [busy, setBusy] = useState(false);
  const [refreshing, setRefreshing] = useState(false);
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");
  const [paymentRecord, setPaymentRecord] = useState(null);

  async function fetchPayment(nextPaymentId) {
    if (!nextPaymentId) return null;

    try {
      return await api.paymentDetails(nextPaymentId);
    } catch {
      return null;
    }
  }

  function recordSuccessfulPayment(record, nextPaymentId) {
    const status = String(record?.paymentStatus || record?.status || "").toUpperCase();
    if (status !== "SUCCESSFUL" || completedPaymentId.current === nextPaymentId) return false;

    completedPaymentId.current = nextPaymentId;
    const failedOrders = (Array.isArray(record.orders) ? record.orders : []).filter(
      (order) => String(order.status || "").toUpperCase() === "FAILED"
    );
    onPaid?.({ payment: record, paymentId: nextPaymentId, hasFailedOrders: failedOrders.length > 0 });
    return true;
  }

  async function refreshPayment(nextPaymentId = paymentId, { quiet = false } = {}) {
    if (!nextPaymentId) return null;
    setRefreshing(true);
    if (!quiet) {
      setError("");
      setNotice("");
    }

    try {
      const record = await fetchPayment(nextPaymentId);
      if (!record) {
        if (!quiet) {
          setError("The backend has not recorded this payment yet. Check again shortly.");
        }
        return null;
      }

      setPaymentRecord(record);
      if (recordSuccessfulPayment(record, nextPaymentId)) {
        setNotice(paymentRecordNotice(record));
      }
      return record;
    } catch (err) {
      if (!quiet) {
        setError(err instanceof Error ? err.message : "Payment status could not load");
      }
      return null;
    } finally {
      setRefreshing(false);
    }
  }

  async function waitForPaymentRecord(nextPaymentId) {
    let record = null;

    for (let attempt = 0; attempt < 10; attempt += 1) {
      if (attempt > 0) {
        await new Promise((resolve) => window.setTimeout(resolve, 1000));
      }

      record = await fetchPayment(nextPaymentId);
      if (record) {
        setPaymentRecord(record);
        const status = String(record.paymentStatus || record.status || "").toUpperCase();
        if (status === "SUCCESSFUL" || status === "FAILED" || status === "REFUNDED") {
          return record;
        }
      }
    }

    return record;
  }

  async function submitPayment(event) {
    event.preventDefault();
    if (!stripe || !elements) return;

    setBusy(true);
    setError("");
    setNotice("");

    try {
      const result = await stripe.confirmPayment({
        elements,
        confirmParams: {
          return_url: `${window.location.origin}/payments?paymentId=${encodeURIComponent(paymentId || "")}`
        },
        redirect: "if_required"
      });

      if (result.error) {
        setError(result.error.message || "Payment could not be confirmed");
        return;
      }

      const paymentIntent = result.paymentIntent;
      const nextPaymentId = paymentIntent?.id || paymentId;
      const status = paymentIntent?.status || "processing";
      const record =
        status === "succeeded"
          ? await waitForPaymentRecord(nextPaymentId)
          : await refreshPayment(nextPaymentId, { quiet: true });
      const backendStatus = String(record?.paymentStatus || record?.status || "").toUpperCase();

      if (backendStatus === "SUCCESSFUL") {
        recordSuccessfulPayment(record, nextPaymentId);
        setNotice(paymentRecordNotice(record));
      } else if (backendStatus === "FAILED") {
        setNotice("The backend recorded this payment as failed. Check your order status before trying again.");
      } else if (status === "succeeded") {
        setNotice("Stripe confirmed payment. Waiting for the backend webhook to record payment and update inventory.");
      } else {
        setNotice(paymentNotice(status, Boolean(record)));
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : "Payment could not be confirmed");
    } finally {
      setBusy(false);
    }
  }

  return (
    <form className="payment-form" onSubmit={submitPayment}>
      {notice && <div className="banner success">{notice}</div>}
      {error && <div className="banner error">{error}</div>}
      <PaymentElement />
      {paymentRecord && <PaymentRecord payment={paymentRecord} paymentId={paymentId} />}
      <div className="payment-actions">
        <button className="button" type="submit" disabled={!stripe || !elements || busy}>
          <CreditCard size={18} />
          {busy ? "Confirming..." : "Pay now"}
        </button>
        {paymentId && (
          <button className="button secondary" type="button" onClick={() => refreshPayment()} disabled={refreshing || busy}>
            <RefreshCw size={18} />
            {refreshing ? "Checking..." : "Check status"}
          </button>
        )}
        {onCancel && (
          <button className="icon-button" type="button" onClick={onCancel} title="Cancel payment" disabled={busy}>
            <X size={18} />
          </button>
        )}
      </div>
      {paymentId && (
        <Link className="inline-link" to={`/payments?paymentId=${encodeURIComponent(paymentId)}`}>
          <ExternalLink size={16} />
          Payment status
        </Link>
      )}
    </form>
  );
}

function PaymentRecord({ payment, paymentId }) {
  const status = payment.paymentStatus || payment.status || "RECORDED";
  const displayPaymentId = payment.paymentId || paymentId || "Payment recorded";
  const orders = Array.isArray(payment.orders) ? payment.orders : [];
  const failedOrders = orders.filter((order) => String(order.status || "").toUpperCase() === "FAILED");

  return (
    <div className="payment-record compact-record">
      <span className={`status status-${String(status).toLowerCase()}`}>{status}</span>
      <div>
        {failedOrders.length ? <AlertTriangle size={18} /> : <CheckCircle2 size={18} />}
        <strong>{displayPaymentId}</strong>
      </div>
      {!!orders.length && (
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
      )}
    </div>
  );
}

function paymentRecordNotice(record) {
  const failedCount = (Array.isArray(record?.orders) ? record.orders : []).filter(
    (order) => String(order.status || "").toUpperCase() === "FAILED"
  ).length;

  if (failedCount) {
    return `Payment succeeded, but ${failedCount} order${failedCount === 1 ? "" : "s"} could not be fulfilled. Review the order statuses below.`;
  }

  return "Payment confirmed and recorded.";
}

function paymentNotice(status, hasRecord) {
  if (status === "succeeded") {
    return hasRecord ? "Payment confirmed and recorded." : "Payment confirmed. Waiting for the webhook record.";
  }

  if (status === "processing") {
    return "Payment is processing. Check status again in a moment.";
  }

  return `Payment status: ${status}.`;
}
