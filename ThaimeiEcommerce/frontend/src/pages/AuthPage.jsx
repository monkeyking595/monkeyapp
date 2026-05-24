import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { LockKeyhole, ShieldCheck, Store, UserPlus } from "lucide-react";
import { api } from "../lib/api";
import { ErrorBanner } from "../components/StateBlocks";

export default function AuthPage({ mode, onSession }) {
  const [form, setForm] = useState({
    username: "",
    email: "",
    password: "",
    confirm: ""
  });
  const [error, setError] = useState("");
  const [busy, setBusy] = useState(false);
  const navigate = useNavigate();
  const isSignup = mode === "signup";
  const isAdmin = mode === "admin";
  const isSeller = mode === "seller";
  const isSellerSignup = mode === "seller-signup";
  const isRegistration = isSignup || isSellerSignup;

  async function submit(event) {
    event.preventDefault();
    setError("");
    setBusy(true);
    try {
      const session = isSellerSignup
        ? await api.sellerSignup(form.username, form.email, form.password, form.confirm)
        : isSignup
        ? await api.signup(form.username, form.email, form.password, form.confirm)
        : isSeller
          ? await api.sellerLogin(form.username, form.password)
        : isAdmin
          ? await api.adminLogin(form.username, form.password)
          : await api.login(form.username, form.password);
      onSession(session);
      navigate(isAdmin ? "/admin" : session.isSeller ? "/seller" : "/products");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Could not sign in");
    } finally {
      setBusy(false);
    }
  }

  return (
    <main className="auth-page">
      <section className="auth-visual">
        <div>
          <span className="pill">{isAdmin ? "Admin access" : isSeller || isSellerSignup ? "Seller workspace" : "Thaimei storefront"}</span>
          <h1>
            {isRegistration
              ? isSellerSignup
                ? "Create your seller account"
                : "Create your shopping account"
              : isAdmin
                ? "Control the store"
                : isSeller
                  ? "Manage your stores"
                  : "Welcome back"}
          </h1>
          <p>
            Browse the latest products, keep your cart ready, manage profile details, and track orders from one clean
            workspace.
          </p>
        </div>
      </section>

      <section className="auth-panel">
        <div className="auth-card">
          {isRegistration ? <UserPlus size={26} /> : isAdmin ? <ShieldCheck size={26} /> : isSeller ? <Store size={26} /> : <LockKeyhole size={26} />}
          <h2>{isRegistration ? "Sign up" : isAdmin ? "Admin sign in" : isSeller ? "Seller sign in" : "Sign in"}</h2>
          {error && <ErrorBanner message={error} />}
          <form onSubmit={submit}>
            <label>
              Username
              <input
                value={form.username}
                onChange={(event) => setForm({ ...form, username: event.target.value })}
                minLength={3}
                required
              />
            </label>
            {isRegistration && (
              <label>
                Email
                <input
                  type="email"
                  value={form.email}
                  onChange={(event) => setForm({ ...form, email: event.target.value })}
                  required
                />
              </label>
            )}
            <label>
              Password
              <input
                type="password"
                value={form.password}
                onChange={(event) => setForm({ ...form, password: event.target.value })}
                minLength={6}
                required
              />
            </label>
            {isRegistration && (
              <label>
                Confirm password
                <input
                  type="password"
                  value={form.confirm}
                  onChange={(event) => setForm({ ...form, confirm: event.target.value })}
                  minLength={6}
                  required
                />
              </label>
            )}
            <button className="button" disabled={busy} type="submit">
              {busy ? "Working..." : isRegistration ? "Create account" : "Sign in"}
            </button>
          </form>
          <div className="auth-links">
            {!isRegistration && !isAdmin && !isSeller && <Link to="/signup">Create account</Link>}
            {isRegistration && <Link to={isSellerSignup ? "/seller-login" : "/login"}>Use existing account</Link>}
            {!isAdmin && !isSeller && !isSellerSignup && <Link to="/admin-login">Admin login</Link>}
            {!isSeller && !isSellerSignup && <Link to="/seller-login">Seller login</Link>}
            {isSeller && <Link to="/seller-signup">Create seller account</Link>}
            {(isSeller || isSellerSignup) && <Link to="/login">Customer login</Link>}
            {isAdmin && <Link to="/login">Customer login</Link>}
          </div>
        </div>
      </section>
    </main>
  );
}
