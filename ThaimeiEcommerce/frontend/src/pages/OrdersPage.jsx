import { useEffect, useState } from "react";
import { ArrowRight, PackageCheck, ShoppingBag } from "lucide-react";
import { Link } from "react-router-dom";
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
          <h1>Your orders</h1>
          <p className="page-subtitle">Every purchase, in one clear place.</p>
        </div>
        <PackageCheck size={30} />
      </div>

      {error && <ErrorBanner message={error} />}
      {loading && <LoadingBlock label="Loading orders" />}
      {!loading && !orders.length && (
        <div className="empty-state empty-state-action">
          <ShoppingBag size={26} />
          <h2>Your order history starts here</h2>
          <p>When you check out, order updates and totals will appear here automatically.</p>
          <Link className="button compact" to="/products">Discover products <ArrowRight size={16} /></Link>
        </div>
      )}

      <section className="line-items">
        {orders.map((order, index) => {
          const title = order.productName || "Order";

          return (
            <article className="line-item" key={`${title}-${index}`}>
              <div className="item-copy">
                {order.imageURL && <img className="item-thumb" src={order.imageURL} alt={title} />}
                <div>
                  <h2>{title}</h2>
                  <p>
                    {order.status || "PENDING"} / Qty {order.quantity || 1}
                  </p>
                </div>
              </div>
              <strong>Rs. {Number(order.totalPrice || 0).toFixed(2)}</strong>
            </article>
          );
        })}
      </section>
    </main>
  );
}
