import { Link, NavLink, Outlet, useNavigate } from "react-router-dom";
import { LogOut, Package, Search, ShieldPlus, ShoppingBag, ShoppingCart, UserRound, UsersRound } from "lucide-react";
import { clearSession } from "../lib/api";

export default function AppShell({ session, onLogout }) {
  const navigate = useNavigate();

  function logout() {
    clearSession();
    onLogout();
    navigate("/login");
  }

  return (
    <div className="app-shell">
      <header className="topbar">
        <Link className="brand" to="/">
          <span className="brand-mark">T</span>
          <span>
            <strong>Thaimei</strong>
            <small>AI shopping</small>
          </span>
        </Link>

        <label className="search">
          <Search size={18} />
          <input placeholder="Search shirts, essentials, new drops" />
        </label>

        <nav className="nav">
          <NavLink to="/products">
            <ShoppingBag size={18} />
            Products
          </NavLink>
          <NavLink to="/cart">
            <ShoppingCart size={18} />
            Cart
          </NavLink>
          <NavLink to="/orders">
            <Package size={18} />
            Orders
          </NavLink>
          <NavLink to="/profile">
            <UserRound size={18} />
            Profile
          </NavLink>
          {session?.isAdmin && (
            <>
              <NavLink to="/admin">
                <UsersRound size={18} />
                Admin
              </NavLink>
              <NavLink to="/admin/register">
                <ShieldPlus size={18} />
                Add admin
              </NavLink>
            </>
          )}
        </nav>

        {session ? (
          <button className="icon-button" type="button" onClick={logout} title="Sign out">
            <LogOut size={19} />
          </button>
        ) : (
          <Link className="button compact" to="/login">
            Sign in
          </Link>
        )}
      </header>
      <Outlet />
    </div>
  );
}
