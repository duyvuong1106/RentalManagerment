import { NavLink, Outlet } from "react-router-dom";
import Header from "../common/Header";

function LandLord() {
    return (
        <div className="d-flex flex-column min-vh-100 bg-light">
            <Header />
            <div className="container py-4 flex-grow-1">
                <div className="row g-4">
                    <aside className="col-lg-3">
                        <div className="card border-0 shadow-sm sticky-lg-top" style={{ top: 90 }}>
                            <div className="card-body p-3">
                                <h6 className="fw-bold px-2 mb-3">Quản lý chủ trọ</h6>
                                <div className="nav flex-column gap-1">
                                    <NavLink to="/landlord" end className={({ isActive }) => `customer-side-link ${isActive ? "active" : ""}`}><i className="bi bi-speedometer2 me-2"></i>Tổng quan</NavLink>
                                    <NavLink to="/landlord/rooms" className={({ isActive }) => `customer-side-link ${isActive ? "active" : ""}`}><i className="bi bi-door-open me-2"></i>Phòng trọ</NavLink>
                                    <NavLink to="/landlord/rental-requests" className={({ isActive }) => `customer-side-link ${isActive ? "active" : ""}`}><i className="bi bi-file-earmark-text me-2"></i>Yêu cầu thuê</NavLink>
                                    <NavLink to="/landlord/viewings" className={({ isActive }) => `customer-side-link ${isActive ? "active" : ""}`}><i className="bi bi-calendar-check me-2"></i>Lịch xem phòng</NavLink>
                                    <NavLink to="/landlord/contracts" className={({ isActive }) => `customer-side-link ${isActive ? "active" : ""}`}><i className="bi bi-file-earmark-check me-2"></i>Hợp đồng</NavLink>
                                    <NavLink to="/landlord/contracts/new" className="customer-side-link"><i className="bi bi-plus-circle me-2"></i>Tạo hợp đồng</NavLink>
                                    <NavLink to="/landlord/invoices" className={({ isActive }) => `customer-side-link ${isActive ? "active" : ""}`}><i className="bi bi-receipt me-2"></i>Hóa đơn</NavLink>
                                    <NavLink to="/landlord/invoices/new" className="customer-side-link"><i className="bi bi-plus-circle me-2"></i>Tạo hóa đơn</NavLink>
                                    <NavLink to="/landlord/payments" className={({ isActive }) => `customer-side-link ${isActive ? "active" : ""}`}><i className="bi bi-credit-card me-2"></i>Thanh toán</NavLink>
                                    <NavLink to="/landlord/statistics" className={({ isActive }) => `customer-side-link ${isActive ? "active" : ""}`}><i className="bi bi-bar-chart me-2"></i>Thống kê</NavLink>
                                    <NavLink to="/landlord/notifications" className={({ isActive }) => `customer-side-link ${isActive ? "active" : ""}`}><i className="bi bi-bell me-2"></i>Thông báo</NavLink>
                                    <NavLink to="/landlord/profile" className={({ isActive }) => `customer-side-link ${isActive ? "active" : ""}`}><i className="bi bi-person me-2"></i>Hồ sơ</NavLink>
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
export default LandLord;
