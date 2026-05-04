import { Navigate } from "react-router-dom";

export default function ProtectedRoute({ session, adminOnly = false, children }) {
  if (!session) return <Navigate to="/login" replace />;
  if (adminOnly && !session.isAdmin) return <Navigate to="/products" replace />;
  return children;
}
