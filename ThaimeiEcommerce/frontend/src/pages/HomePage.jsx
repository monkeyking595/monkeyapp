import { Link } from "react-router-dom";
import { ArrowRight, Boxes, PackageCheck, ShieldCheck, ShoppingBag, ShoppingCart, Sparkles, Store, UserRound, UsersRound } from "lucide-react";
import { hasRole, ROLES } from "../lib/api";

const actions = {
  guest: [
    { to: "/products", icon: ShoppingBag, title: "Products", text: "Browse active products from the marketplace." },
    { to: "/login", icon: UserRound, title: "Customer Login", text: "Shop products, save your cart, and place orders." },
    { to: "/seller-login", icon: Store, title: "Seller Login", text: "Manage stores and publish inventory." },
    { to: "/admin-login", icon: ShieldCheck, title: "Admin Login", text: "Review users, sellers, stores, and orders." }
  ],
  customer: [
    { to: "/products", icon: ShoppingBag, title: "Products", text: "Browse active products from the backend." },
    { to: "/cart", icon: ShoppingCart, title: "Cart", text: "Review items and checkout through the API." },
    { to: "/orders", icon: PackageCheck, title: "Orders", text: "Track your order history." }
  ],
  seller: [
    { to: "/seller", icon: Store, title: "Store Manager", text: "Register stores and set open or closed state." },
    { to: "/seller", icon: Boxes, title: "Products", text: "Add inventory against your stores." },
    { to: "/profile", icon: UserRound, title: "Profile", text: "Keep seller account details current." }
  ],
  admin: [
    { to: "/admin", icon: UsersRound, title: "Users", text: "Manage customer and seller status." },
    { to: "/admin", icon: PackageCheck, title: "Orders", text: "Review orders across the marketplace." },
    { to: "/admin/register", icon: ShieldCheck, title: "Add Admin", text: "Create another admin account." }
  ]
};

function actionSet(session) {
  if (!session) return actions.guest;
  if (hasRole(session, ROLES.ADMIN)) return actions.admin;
  if (hasRole(session, ROLES.SELLER)) return actions.seller;
  return actions.customer;
}

export default function HomePage({ session }) {
  const cards = actionSet(session);
  const isGuest = !session;

  return (
    <main className="home">
      <section className="hero app-hero">
        <div className="hero-copy">
          <span className="eyebrow"><Sparkles size={14} /> {session?.role || "A smarter marketplace"}</span>
          <h1>Style that feels<br />like <em>you.</em></h1>
          <p>
            {session
              ? `Welcome back, ${session.username}. Your ${String(session.role || "customer").toLowerCase()} workspace is ready.`
              : "Discover everyday essentials, independent stores, and a checkout built to keep up with you."}
          </p>
          <div className="hero-actions">
            <Link className="button hero-button" to={cards[0].to}>
              {isGuest ? "Browse products" : `Open ${cards[0].title}`}
              <ArrowRight size={18} />
            </Link>
            {isGuest && <Link className="text-button" to="/seller-signup">Sell with Thaimei</Link>}
          </div>
        </div>
        <div className="hero-orb" aria-hidden="true" />
      </section>

      <section className="trust-bar" aria-label="Store benefits">
        <span>Curated stores</span>
        <span>Secure payments</span>
        <span>Simple returns</span>
        <span>Real-time orders</span>
      </section>

      <section className="home-intro">
        <div>
          <span className="section-kicker">Your next move</span>
          <h2>{isGuest ? "Everything in one considered place." : "Pick up where you left off."}</h2>
        </div>
        <p>{isGuest ? "Whether you are shopping, selling, or managing the marketplace, begin from the space made for you." : "Use these shortcuts to stay on top of the work that matters today."}</p>
      </section>
      <section className="feature-strip">
        {cards.map((item) => {
          const Icon = item.icon;
          return (
            <Link className="action-card" to={item.to} key={`${item.to}-${item.title}`}>
              <Icon size={22} />
              <h3>{item.title}</h3>
              <p>{item.text}</p>
              <span>
                Open
                <ArrowRight size={16} />
              </span>
            </Link>
          );
        })}
      </section>

      {isGuest && (
        <section className="home-editorial">
          <div className="editorial-image" />
          <div className="editorial-copy">
            <span className="section-kicker">Built for discovery</span>
            <h2>A marketplace with a human pulse.</h2>
            <p>Search active inventory, keep a cart across your session, and follow each order from checkout to delivery.</p>
            <Link className="inline-link" to="/login">Explore the storefront <ArrowRight size={16} /></Link>
          </div>
        </section>
      )}
    </main>
  );
}
