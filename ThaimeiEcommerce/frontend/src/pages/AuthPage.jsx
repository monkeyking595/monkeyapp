import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { LockKeyhole, ShieldCheck, UserPlus } from "lucide-react";
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

  async function submit(event) {
    event.preventDefault();
    setError("");
    setBusy(true);
    try {
      const session = isSignup
        ? await api.signup(form.username, form.email, form.password, form.confirm)
        : isAdmin
          ? await api.adminLogin(form.username, form.password)
          : await api.login(form.username, form.password);
      onSession(session);
      navigate(isAdmin ? "/admin" : "/products");
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
          <span className="pill">{isAdmin ? "Admin access" : "Thaimei storefront"}</span>
          <h1>{isSignup ? "Create your shopping account" : isAdmin ? "Control the store" : "Welcome back"}</h1>
          <p>
            Browse the latest products, keep your cart ready, manage profile details, and track orders from one clean
            workspace.
          </p>
        </div>
      </section>

      <section className="auth-panel">
        <div className="auth-card">
          {isSignup ? <UserPlus size={26} /> : isAdmin ? <ShieldCheck size={26} /> : <LockKeyhole size={26} />}
          <h2>{isSignup ? "Sign up" : isAdmin ? "Admin sign in" : "Sign in"}</h2>
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
            {isSignup && (
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
            {isSignup && (
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
              {busy ? "Working..." : isSignup ? "Create account" : "Sign in"}
            </button>
          </form>
          <div className="auth-links">
            {!isSignup && !isAdmin && <Link to="/signup">Create account</Link>}
            {isSignup && <Link to="/login">Use existing account</Link>}
            {!isAdmin && <Link to="/admin-login">Admin login</Link>}
            {isAdmin && <Link to="/login">Customer login</Link>}
          </div>
        </div>
      </section>
    </main>
  );
}
