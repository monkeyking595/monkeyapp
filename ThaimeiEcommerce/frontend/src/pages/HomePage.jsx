import { Link } from "react-router-dom";
import { ArrowRight, Sparkles, Truck, WalletCards } from "lucide-react";

export default function HomePage({ session }) {
  return (
    <main className="home">
      <section className="hero">
        <div className="hero-copy">
          <span className="pill">Spring collection</span>
          <h1>Thaimei</h1>
          <p>Fashion essentials with a faster, cleaner ecommerce experience built around your Spring backend.</p>
          <Link className="button" to={session ? "/products" : "/login"}>
            {session ? "Shop products" : "Sign in to shop"}
            <ArrowRight size={18} />
          </Link>
        </div>
      </section>

      <section className="feature-strip">
        <article>
          <Sparkles size={22} />
          <h3>Curated finds</h3>
          <p>Browse product data straight from your API.</p>
        </article>
        <article>
          <Truck size={22} />
          <h3>Cart to order</h3>
          <p>Add items, review totals, and place orders.</p>
        </article>
        <article>
          <WalletCards size={22} />
          <h3>Account ready</h3>
          <p>JWT login, profile editing, and admin access are wired in.</p>
        </article>
      </section>
    </main>
  );
}
