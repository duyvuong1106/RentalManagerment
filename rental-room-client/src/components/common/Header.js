import { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

function Header() {
    const navigate = useNavigate();
    const location = useLocation();
    const { user, isAuthenticated, logout } = useAuth();
    const [loggingOut, setLoggingOut] = useState(false);

    async function handleLogout() {
        try { setLoggingOut(true); await logout(); navigate("/"); }
        finally { setLoggingOut(false); }
    }

    const isActive = path => location.pathname === path;

    return (
        <header>
            <nav className="navbar navbar-expand-lg bg-white border-bottom sticky-top">
                <div className="container">
                    <Link to="/" className="navbar-brand fw-bold d-flex align-items-center">
                        <i className="bi bi-house-heart-fill text-primary me-2"></i>Rental Room
                    </Link>
                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNavbar" aria-label="Mở menu">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="mainNavbar">
                        <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                            <li className="nav-item"><Link className={`nav-link ${isActive("/") ? "active fw-semibold" : ""}`} to="/">Trang chủ</Link></li>
                            <li className="nav-item"><Link className={`nav-link ${location.pathname.startsWith("/rooms") ? "active fw-semibold" : ""}`} to="/rooms">Tìm phòng</Link></li>
                            {isAuthenticated && user?.role === "CUSTOMER" && <li className="nav-item"><Link className={`nav-link ${location.pathname.startsWith("/customer") ? "active fw-semibold" : ""}`} to="/customer">Quản lý thuê</Link></li>}
                            {isAuthenticated && user?.role === "LANDLORD" && <li className="nav-item"><Link className={`nav-link ${location.pathname.startsWith("/landlord") ? "active fw-semibold" : ""}`} to="/landlord">Quản lý</Link></li>}
                        </ul>
                        <div className="d-flex align-items-center gap-2">
                            {!isAuthenticated ? <Link to="/login" className="btn btn-primary"><i className="bi bi-box-arrow-in-right me-1"></i>Đăng nhập</Link> : <>
                                <Link to={user?.role === "LANDLORD" ? "/landlord/profile" : "/customer/profile"} className="text-secondary text-decoration-none d-none d-md-inline"><i className="bi bi-person-circle me-1"></i>{user?.firstName || user?.username}</Link>
                                <button className="btn btn-outline-danger" onClick={handleLogout} disabled={loggingOut}><i className="bi bi-box-arrow-right me-1"></i>{loggingOut ? "Đang đăng xuất..." : "Đăng xuất"}</button>
                            </>}
                        </div>
                    </div>
                </div>
            </nav>
        </header>
    );
}
export default Header;
