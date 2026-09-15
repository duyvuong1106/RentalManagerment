import { Link } from "react-router-dom";

function Footer() {
    return (
        <footer className="bg-dark text-white mt-auto">
            <div className="container py-5">
                <div className="row g-4">
                    <div className="col-lg-5">
                        <h5 className="fw-bold mb-3"><i className="bi bi-house-heart-fill me-2"></i>Rental Room</h5>
                        <p className="text-white-50 mb-0">Website quản lý và tìm kiếm phòng trọ, hỗ trợ khách hàng tìm phòng và chủ trọ quản lý hoạt động cho thuê.</p>
                    </div>
                    <div className="col-md-3 col-lg-2">
                        <h6 className="fw-bold">Khám phá</h6>
                        <ul className="list-unstyled">
                            <li className="mb-2"><Link to="/" className="text-white-50 text-decoration-none">Trang chủ</Link></li>
                            <li><Link to="/rooms" className="text-white-50 text-decoration-none">Tìm phòng</Link></li>
                        </ul>
                    </div>
                    <div className="col-md-4 col-lg-3">
                        <h6 className="fw-bold">Hỗ trợ</h6>
                        <ul className="list-unstyled text-white-50">
                            <li className="mb-2"><i className="bi bi-envelope me-2"></i>support@rentalroom.com</li>
                            <li><i className="bi bi-telephone me-2"></i>0900 000 000</li>
                        </ul>
                    </div>
                </div>
                <hr className="border-secondary my-4" />
                <div className="d-flex flex-column flex-md-row justify-content-between align-items-center">
                    <p className="text-white-50 mb-0">© 2026 Rental Room. All rights reserved.</p>
                    <div className="mt-3 mt-md-0"><a href="#" className="text-white me-3"><i className="bi bi-facebook"></i></a><a href="#" className="text-white me-3"><i className="bi bi-instagram"></i></a><a href="#" className="text-white"><i className="bi bi-github"></i></a></div>
                </div>
            </div>
        </footer>
    );
}
export default Footer;
