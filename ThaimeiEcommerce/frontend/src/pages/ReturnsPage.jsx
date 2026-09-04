import { useEffect, useMemo, useState } from "react";
import { ClipboardList, Plus, RefreshCw, RotateCcw, Send, Trash2 } from "lucide-react";
import { api } from "../lib/api";
import { EmptyState, ErrorBanner, LoadingBlock } from "../components/StateBlocks";

const initialRefundItem = { itemId: "", quantity: 1 };

function label(value = "") {
  return String(value || "").replaceAll("_", " ");
}

function money(value) {
  if (value === null || value === undefined || value === "") return "-";
  const amount = Number(value);
  if (Number.isNaN(amount)) return value;
  return `Rs. ${amount.toFixed(2)}`;
}

function dateTime(value) {
  if (!value) return "-";
  const parsed = new Date(value);
  return Number.isNaN(parsed.getTime()) ? value : parsed.toLocaleString();
}

function refundId(refund) {
  return refund?.id ?? refund?.refundId;
}

function refundItemName(refund) {
  return refund?.itemName || refund?.ItemName || refund?.productName || "Item";
}

function parseItemIds(value) {
  return [...new Set(
    String(value)
      .split(/[\s,]+/)
      .map((item) => item.trim())
      .filter(Boolean)
      .map(Number)
  )];
}

function normalizeRefunds(data) {
  if (Array.isArray(data)) return data;
  return Array.isArray(data?.refunds) ? data.refunds : [];
}

export default function ReturnsPage() {
  const [orders, setOrders] = useState([]);
  const [refunds, setRefunds] = useState([]);
  const [returnItemIds, setReturnItemIds] = useState("");
  const [refundItems, setRefundItems] = useState([initialRefundItem]);
  const [loading, setLoading] = useState(true);
  const [returnBusy, setReturnBusy] = useState(false);
  const [refundBusy, setRefundBusy] = useState(false);
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");

  useEffect(() => {
    loadPage();
  }, []);

  const refundTotal = useMemo(
    () => refunds.reduce((sum, refund) => sum + (Number(refund.amount) || 0), 0),
    [refunds]
  );

  async function loadPage() {
    setError("");
    setLoading(true);

    const [ordersResult, refundsResult] = await Promise.allSettled([api.orders(), api.customerRefunds()]);

    if (ordersResult.status === "fulfilled") {
      setOrders(Array.isArray(ordersResult.value) ? ordersResult.value : []);
    } else {
      setOrders([]);
      setError(ordersResult.reason instanceof Error ? ordersResult.reason.message : "Orders could not load");
    }

    if (refundsResult.status === "fulfilled") {
      setRefunds(normalizeRefunds(refundsResult.value));
    } else {
      setRefunds([]);
      setError((current) =>
        current || (refundsResult.reason instanceof Error ? refundsResult.reason.message : "Refunds could not load")
      );
    }

    setLoading(false);
  }

  function updateRefundItem(index, key, value) {
    setRefundItems((current) =>
      current.map((item, itemIndex) => (itemIndex === index ? { ...item, [key]: value } : item))
    );
  }

  function addRefundItem() {
    setRefundItems((current) => [...current, initialRefundItem]);
  }

  function removeRefundItem(index) {
    setRefundItems((current) => (current.length === 1 ? current : current.filter((_, itemIndex) => itemIndex !== index)));
  }

  async function submitReturn(event) {
    event.preventDefault();
    const itemIds = parseItemIds(returnItemIds);

    if (!itemIds.length || itemIds.some((id) => !Number.isInteger(id) || id < 1)) {
      setError("Return item IDs must be positive numbers.");
      return;
    }

    setError("");
    setNotice("");
    setReturnBusy(true);

    try {
      const response = await api.requestReturn(itemIds);
      setReturnItemIds("");
      setNotice(response?.message || "Return request submitted.");
      await loadPage();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Return request could not be submitted");
    } finally {
      setReturnBusy(false);
    }
  }

  async function submitRefund(event) {
    event.preventDefault();
    const items = refundItems
      .map((item) => ({ itemId: Number(item.itemId), quantity: Number(item.quantity) }))
      .filter((item) => item.itemId || item.quantity);

    if (!items.length) {
      setError("Add at least one item for refund.");
      return;
    }

    if (items.some((item) => !Number.isInteger(item.itemId) || item.itemId < 1)) {
      setError("Refund item IDs must be positive numbers.");
      return;
    }

    if (items.some((item) => !Number.isInteger(item.quantity) || item.quantity < 1)) {
      setError("Refund quantities must be at least 1.");
      return;
    }

    setError("");
    setNotice("");
    setRefundBusy(true);

    try {
      const response = await api.requestRefund(items);
      setRefundItems([initialRefundItem]);
      setNotice(response?.message || "Refund request submitted.");
      await loadPage();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Refund request could not be submitted");
    } finally {
      setRefundBusy(false);
    }
  }

  return (
    <main className="page returns-page">
      <div className="page-heading">
        <div>
          <span className="pill">Customer</span>
          <h1>Returns & Refunds</h1>
        </div>
        <button className="icon-button" type="button" onClick={loadPage} title="Refresh returns and refunds">
          <RefreshCw size={18} />
        </button>
      </div>

      {error && <ErrorBanner message={error} />}
      {notice && <div className="banner success">{notice}</div>}
      {loading && <LoadingBlock label="Loading returns and refunds" />}

      {!loading && (
        <>
          <section className="returns-summary-grid">
            <article>
              <strong>{orders.length}</strong>
              <span>Orders</span>
            </article>
            <article>
              <strong>{refunds.length}</strong>
              <span>Refunds</span>
            </article>
            <article>
              <strong>{money(refundTotal)}</strong>
              <span>Requested</span>
            </article>
          </section>

          <section className="returns-layout">
            <div className="returns-forms">
              <form className="profile-form returns-form" onSubmit={submitReturn}>
                <div className="form-heading span-two">
                  <RotateCcw size={22} />
                  <h2>Return Items</h2>
                </div>
                <label className="span-two">
                  Order item IDs
                  <input
                    value={returnItemIds}
                    onChange={(event) => setReturnItemIds(event.target.value)}
                    placeholder="12, 13"
                    disabled={returnBusy}
                  />
                </label>
                <button className="button span-two" type="submit" disabled={returnBusy}>
                  <Send size={18} />
                  {returnBusy ? "Submitting..." : "Request return"}
                </button>
              </form>

              <form className="profile-form returns-form" onSubmit={submitRefund}>
                <div className="form-heading span-two">
                  <ClipboardList size={22} />
                  <h2>Refund Request</h2>
                </div>
                <div className="refund-item-list span-two">
                  {refundItems.map((item, index) => (
                    <div className="refund-item-row" key={index}>
                      <label>
                        Item ID
                        <input
                          type="number"
                          min="1"
                          value={item.itemId}
                          onChange={(event) => updateRefundItem(index, "itemId", event.target.value)}
                          disabled={refundBusy}
                          required
                        />
                      </label>
                      <label>
                        Quantity
                        <input
                          type="number"
                          min="1"
                          value={item.quantity}
                          onChange={(event) => updateRefundItem(index, "quantity", event.target.value)}
                          disabled={refundBusy}
                          required
                        />
                      </label>
                      <button
                        className="icon-button"
                        type="button"
                        onClick={() => removeRefundItem(index)}
                        disabled={refundBusy || refundItems.length === 1}
                        title="Remove item"
                      >
                        <Trash2 size={17} />
                      </button>
                    </div>
                  ))}
                </div>
                <div className="refund-form-actions span-two">
                  <button className="button secondary compact" type="button" onClick={addRefundItem} disabled={refundBusy}>
                    <Plus size={17} />
                    Add item
                  </button>
                  <button className="button compact" type="submit" disabled={refundBusy}>
                    <Send size={17} />
                    {refundBusy ? "Submitting..." : "Request refund"}
                  </button>
                </div>
              </form>
            </div>

            <aside className="summary-panel recent-orders-panel">
              <div className="form-heading">
                <ClipboardList size={22} />
                <h2>Recent Orders</h2>
              </div>
              {!orders.length ? (
                <EmptyState title="No orders found" text="Orders will appear here after checkout." />
              ) : (
                <div className="return-order-list">
                  {orders.slice(0, 6).map((order, index) => (
                    <article className="return-order-row" key={`${order.productName || "order"}-${index}`}>
                      <div>
                        <strong>{order.productName || "Order"}</strong>
                        <small>
                          {label(order.status || "PENDING")} / Qty {order.quantity || 1}
                        </small>
                      </div>
                      <span>{money(order.totalPrice)}</span>
                    </article>
                  ))}
                </div>
              )}
            </aside>
          </section>

          <section className="table-wrap refund-history-table">
            <div className="table-title">
              <div className="form-heading">
                <ClipboardList size={22} />
                <h2>Refund History</h2>
              </div>
            </div>
            {!refunds.length ? (
              <EmptyState title="No refund requests" text="Submitted refund requests will appear here." />
            ) : (
              <table>
                <thead>
                  <tr>
                    <th>Refund</th>
                    <th>Item</th>
                    <th>Quantity</th>
                    <th>Amount</th>
                    <th>Status</th>
                    <th>Requested</th>
                  </tr>
                </thead>
                <tbody>
                  {refunds.map((refund, index) => {
                    const id = refundId(refund);
                    const status = refund.status || "PENDING";

                    return (
                      <tr key={id || index}>
                        <td>{id ? `#${id}` : "-"}</td>
                        <td>{refundItemName(refund)}</td>
                        <td>{refund.quantity ?? "-"}</td>
                        <td>{money(refund.amount)}</td>
                        <td>
                          <span className={`status status-${String(status).toLowerCase()}`}>{label(status)}</span>
                        </td>
                        <td>{dateTime(refund.createdAt || refund.requestedAt)}</td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            )}
          </section>
        </>
      )}
    </main>
  );
}
