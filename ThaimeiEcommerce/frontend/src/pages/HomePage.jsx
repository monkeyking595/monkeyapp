import { Link } from "react-router-dom";
import { ArrowRight, Boxes, PackageCheck, ShieldCheck, ShoppingBag, ShoppingCart, Store, UserRound, UsersRound } from "lucide-react";
import { hasRole, ROLES } from "../lib/api";

const actions = {
  guest: [
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

  return (
    <main className="home">
      <section className="hero app-hero">
        <div className="hero-copy">
          <span className="pill">{session?.role || "Thaimei"}</span>
          <h1>Thaimei</h1>
          <p>{session ? `Signed in as ${session.username}.` : "Choose the right workspace for your account."}</p>
        </div>
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
    </main>
  );
}
