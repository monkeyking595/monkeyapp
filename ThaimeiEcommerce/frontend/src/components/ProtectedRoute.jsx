import { Navigate } from "react-router-dom";
import { hasRole, ROLES } from "../lib/api";

export default function ProtectedRoute({ session, adminOnly = false, sellerOnly = false, children }) {
  if (!session) return <Navigate to="/login" replace />;
  if (adminOnly && !hasRole(session, ROLES.ADMIN)) return <Navigate to="/" replace />;
  if (sellerOnly && !hasRole(session, ROLES.SELLER)) return <Navigate to="/" replace />;
  return children;
}
