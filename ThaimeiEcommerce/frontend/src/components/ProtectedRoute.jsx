import { Navigate } from "react-router-dom";

export default function ProtectedRoute({ session, adminOnly = false, sellerOnly = false, children }) {
  if (!session) return <Navigate to="/login" replace />;
  if (adminOnly && !session.isAdmin) return <Navigate to="/products" replace />;
  if (sellerOnly && !session.isSeller) return <Navigate to="/products" replace />;
  return children;
}
