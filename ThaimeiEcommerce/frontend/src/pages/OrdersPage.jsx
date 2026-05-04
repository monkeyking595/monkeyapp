import { useEffect, useState } from "react";
import { PackageCheck } from "lucide-react";
import { api } from "../lib/api";
import { EmptyState, ErrorBanner, LoadingBlock } from "../components/StateBlocks";

export default function OrdersPage() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    api
      .orders()
      .then(setOrders)
      .catch((err) => setError(err instanceof Error ? err.message : "Orders could not load"))
      .finally(() => setLoading(false));
  }, []);

  return (
    <main className="page">
      <div className="page-heading">
        <div>
          <span className="pill">History</span>
          <h1>Orders</h1>
        </div>
        <PackageCheck size={30} />
      </div>
      {error && <ErrorBanner message={error} />}
      {loading && <LoadingBlock label="Loading orders" />}
      {!loading && !orders.length && <EmptyState title="No orders found" text="Completed orders from your backend will appear here." />}
      <section className="line-items">
        {orders.map((order, index) => (
          <article className="line-item" key={`${order.id}-${index}`}>
            <div>
              <h2>{order.productName}</h2>
              <p>{order.status} · Qty {order.quantity}</p>
            </div>
            <strong>Rs. {Number(order.totalPrice).toFixed(2)}</strong>
          </article>
        ))}
      </section>
    </main>
  );
}
