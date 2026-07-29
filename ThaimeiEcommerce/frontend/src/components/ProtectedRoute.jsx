import { Navigate } from "react-router-dom";
import { hasRole, landingPath, ROLES } from "../lib/api";

export default function ProtectedRoute({ session, adminOnly = false, sellerOnly = false, children }) {
  if (!session) {
    const loginPath = adminOnly ? "/admin-login" : sellerOnly ? "/seller-login" : "/login";
    return <Navigate to={loginPath} replace />;
  }

  if (adminOnly && !hasRole(session, ROLES.ADMIN)) return <Navigate to={landingPath(session)} replace />;
  if (sellerOnly && !hasRole(session, ROLES.SELLER)) return <Navigate to={landingPath(session)} replace />;
  return children;
}
