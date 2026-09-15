import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

function ProtectedRoute({ roles = [] }) {
    const { user, loading } = useAuth();
    const location = useLocation();
    if (loading) return <div className="container py-5 text-center"><div className="spinner-border text-primary"></div></div>;
    if (!user) return <Navigate to="/login" replace state={{ from: location }} />;
    if (roles.length && !roles.includes(user.role)) return <Navigate to="/" replace />;
    return <Outlet />;
}
export default ProtectedRoute;
