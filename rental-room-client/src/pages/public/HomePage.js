import { Link } from "react-router-dom";
import "../../styles/home.css";

function HomePage() {
  return (
    <>
      <section className="home-hero">
        <div className="container">
          <div className="row align-items-center min-vh-75">
            <div className="col-lg-7">
              <span className="badge bg-primary-subtle text-primary px-3 py-2 mb-3">
                <i className="bi bi-house-door me-1"></i>Tìm phòng trọ dễ dàng
              </span>
              <h1 className="display-4 fw-bold text-dark mb-4">
                Tìm căn phòng phù hợp{" "}
                <span className="text-primary">với bạn</span>
              </h1>
              <p className="lead text-secondary mb-4">
                Khám phá phòng trọ và lọc theo khu vực, giá thuê, diện tích và
                tiện ích.
              </p>
              <div className="d-flex flex-wrap gap-3">
                <Link to="/rooms" className="btn btn-primary btn-lg px-4">
                  <i className="bi bi-search me-2"></i>Tìm phòng ngay
                </Link>
                <a
                  href="#features"
                  className="btn btn-outline-secondary btn-lg px-4"
                >
                  Tìm hiểu thêm
                </a>
              </div>
            </div>
            <div className="col-lg-5 mt-5 mt-lg-0">
              <div className="hero-card shadow-lg">
                <div className="hero-card-icon">
                  <i className="bi bi-house-heart-fill"></i>
                </div>
                <h3 className="fw-bold mt-4">Ngôi nhà phù hợp đang chờ bạn</h3>
                <p className="text-secondary">
                  Tìm kiếm nhanh chóng và xem thông tin phòng trực quan.
                </p>
                <div className="row g-3 mt-3">
                  <div className="col-6">
                    <div className="stat-card">
                      <i className="bi bi-door-open text-primary"></i>
                      <strong>Nhiều phòng</strong>
                      <span>Đang chờ bạn</span>
                    </div>
                  </div>
                  <div className="col-6">
                    <div className="stat-card">
                      <i className="bi bi-shield-check text-success"></i>
                      <strong>An toàn</strong>
                      <span>Thông tin rõ ràng</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
      <section className="py-5 bg-white">
        <div className="container">
          <div className="row justify-content-center">
            <div className="col-lg-10">
              <div className="search-box shadow-sm d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3">
                <div>
                  <h5 className="fw-bold mb-1">Bạn đang tìm phòng?</h5>
                  <p className="text-secondary mb-0">
                    Mở trang tìm kiếm để dùng đầy đủ bộ lọc.
                  </p>
                </div>
                <Link to="/rooms" className="btn btn-primary">
                  <i className="bi bi-search me-2"></i>Tìm phòng
                </Link>
              </div>
            </div>
          </div>
        </div>
      </section>
      <section id="features" className="py-5 bg-light">
        <div className="container">
          <div className="text-center mb-5">
            <span className="text-primary fw-semibold">
              VÌ SAO CHỌN RENTAL ROOM?
            </span>
            <h2 className="fw-bold mt-2">Mọi thứ bạn cần để tìm phòng</h2>
            <p className="text-secondary">Đơn giản, trực quan và thuận tiện.</p>
          </div>
          <div className="row g-4">
            <div className="col-md-4">
              <div className="feature-card h-100">
                <div className="feature-icon bg-primary-subtle text-primary">
                  <i className="bi bi-search"></i>
                </div>
                <h5 className="fw-bold mt-4">Tìm kiếm dễ dàng</h5>
                <p className="text-secondary mb-0">
                  Lọc phòng theo khu vực, giá, diện tích và tiện ích.
                </p>
              </div>
            </div>
            <div className="col-md-4">
              <div className="feature-card h-100">
                <div className="feature-icon bg-success-subtle text-success">
                  <i className="bi bi-card-checklist"></i>
                </div>
                <h5 className="fw-bold mt-4">Quản lý thuê phòng</h5>
                <p className="text-secondary mb-0">
                  Theo dõi yêu cầu thuê, hợp đồng và thanh toán.
                </p>
              </div>
            </div>
            <div className="col-md-4">
              <div className="feature-card h-100">
                <div className="feature-icon bg-warning-subtle text-warning">
                  <i className="bi bi-bell"></i>
                </div>
                <h5 className="fw-bold mt-4">Nhận thông báo</h5>
                <p className="text-secondary mb-0">
                  Theo dõi thông báo về hợp đồng và thanh toán.
                </p>
              </div>
            </div>
          </div>
        </div>
      </section>
      <section className="py-5 bg-primary text-white">
        <div className="container">
          <div className="row align-items-center">
            <div className="col-lg-8">
              <h2 className="fw-bold">Sẵn sàng tìm phòng mới?</h2>
              <p className="mb-0 opacity-75">
                Khám phá những phòng phù hợp với nhu cầu của bạn.
              </p>
            </div>
            <div className="col-lg-4 text-lg-end mt-3 mt-lg-0">
              <Link to="/rooms" className="btn btn-light btn-lg">
                Xem phòng<i className="bi bi-arrow-right ms-2"></i>
              </Link>
            </div>
          </div>
        </div>
      </section>
    </>
  );
}
export default HomePage;
