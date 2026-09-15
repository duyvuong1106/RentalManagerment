import { NavLink, Outlet } from "react-router-dom";
import Header from "../common/Header";

function CustomerLayout() {
    return (
        <div className="d-flex flex-column min-vh-100 bg-light">
            <Header />
            <div className="container py-4 flex-grow-1">
                <div className="row g-4">
                    <aside className="col-lg-3">
                        <div className="card border-0 shadow-sm sticky-lg-top" style={{ top: 90 }}>
                            <div className="card-body p-3">
                                <h6 className="fw-bold px-2 mb-3">Tài khoản khách hàng</h6>
                                <div className="nav flex-column gap-1">
                                    <NavLink to="/customer" end className={({ isActive }) => `customer-side-link ${isActive ? "active" : ""}`}><i className="bi bi-speedometer2 me-2"></i>Tổng quan</NavLink>
                                    <NavLink to="/customer/rental-requests" className={({ isActive }) => `customer-side-link ${isActive ? "active" : ""}`}><i className="bi bi-file-earmark-text me-2"></i>Yêu cầu thuê</NavLink>
                                    <NavLink to="/customer/viewings" className={({ isActive }) => `customer-side-link ${isActive ? "active" : ""}`}><i className="bi bi-calendar-check me-2"></i>Lịch xem phòng</NavLink>
                                    <NavLink to="/customer/contracts" className={({ isActive }) => `customer-side-link ${isActive ? "active" : ""}`}><i className="bi bi-file-earmark-check me-2"></i>Hợp đồng</NavLink>
                                    <NavLink to="/customer/invoices" className={({ isActive }) => `customer-side-link ${isActive ? "active" : ""}`}><i className="bi bi-receipt me-2"></i>Hóa đơn</NavLink>
                                    <NavLink to="/customer/payments" className={({ isActive }) => `customer-side-link ${isActive ? "active" : ""}`}><i className="bi bi-credit-card me-2"></i>Thanh toán</NavLink>
                                    <NavLink to="/customer/reviews" className={({ isActive }) => `customer-side-link ${isActive ? "active" : ""}`}><i className="bi bi-star me-2"></i>Đánh giá</NavLink>
                                    <NavLink to="/customer/notifications" className={({ isActive }) => `customer-side-link ${isActive ? "active" : ""}`}><i className="bi bi-bell me-2"></i>Thông báo</NavLink>
                                    <NavLink to="/customer/profile" className={({ isActive }) => `customer-side-link ${isActive ? "active" : ""}`}><i className="bi bi-person me-2"></i>Hồ sơ</NavLink>
                                </div>
                            </div>
                        </div>
                    </aside>
                    <section className="col-lg-9"><Outlet /></section>
                </div>
            </div>
        </div>
    );
}
export default CustomerLayout;
