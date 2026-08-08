import { useState } from "react";
import { Elements, PaymentElement, useElements, useStripe } from "@stripe/react-stripe-js";
import { loadStripe } from "@stripe/stripe-js";
import { CheckCircle2, CreditCard, ExternalLink, RefreshCw, ShieldCheck, X } from "lucide-react";
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
  const [busy, setBusy] = useState(false);
  const [refreshing, setRefreshing] = useState(false);
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");
  const [paymentRecord, setPaymentRecord] = useState(null);

  async function refreshPayment(nextPaymentId = paymentId, { quiet = false } = {}) {
    if (!nextPaymentId) return null;
    setRefreshing(true);
    if (!quiet) {
      setError("");
      setNotice("");
    }

    try {
      const record = await api.paymentDetails(nextPaymentId);
      setPaymentRecord(record);
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
      const record = await refreshPayment(nextPaymentId, { quiet: true });

      setNotice(paymentNotice(status, Boolean(record)));
      onPaid?.({ paymentIntent, payment: record, paymentId: nextPaymentId });
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
  const status = payment.status || payment.paymentStatus || "RECORDED";
  const displayPaymentId = payment.paymentId || paymentId || "Payment recorded";

  return (
    <div className="payment-record compact-record">
      <span className={`status status-${String(status).toLowerCase()}`}>{status}</span>
      <div>
        <CheckCircle2 size={18} />
        <strong>{displayPaymentId}</strong>
      </div>
    </div>
  );
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
