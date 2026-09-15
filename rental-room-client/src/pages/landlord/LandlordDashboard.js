import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import * as statisticsService from "../../services/statisticsService";
import { formatCurrency } from "../../utils/format";
function LandlordDashboard() {
  const [s, setS] = useState({});
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    statisticsService
      .getLandlordStatistics()
      .then(setS)
      .finally(() => setLoading(false));
  }, []);
  const cards = [
    ["Tổng phòng", s.totalRooms, "bi-door-open", "primary"],
    ["Phòng trống", s.availableRooms, "bi-house-check", "success"],
    ["Đang thuê", s.rentedRooms, "bi-house-lock", "warning"],
    ["Doanh thu", formatCurrency(s.totalRevenue), "bi-cash-stack", "info"],
  ];
  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h2 className="fw-bold">Tổng quan chủ trọ</h2>
          <p className="text-secondary mb-0">
            Theo dõi hoạt động kinh doanh phòng trọ.
          </p>
        </div>
        <Link to="/landlord/rooms/new" className="btn btn-primary">
          <i className="bi bi-plus-lg me-2" />
          Thêm phòng
        </Link>
      </div>
      <div className="row g-4">
        {cards.map(([label, value, icon, color]) => (
          <div className="col-md-6 col-xl-3" key={label}>
            <div className="card border-0 shadow-sm">
              <div className="card-body">
                <div className="d-flex justify-content-between">
                  <div>
                    <small className="text-secondary">{label}</small>
                    <div className="fs-3 fw-bold mt-2">
                      {loading ? "—" : value}
                    </div>
                  </div>
                  <div
                    className={`bg-${color}-subtle text-${color} rounded-3 p-3`}
                  >
                    <i className={`bi ${icon} fs-4`} />
                  </div>
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
export default LandlordDashboard;
