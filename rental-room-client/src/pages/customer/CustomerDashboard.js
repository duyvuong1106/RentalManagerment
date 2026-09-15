import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import { useAuth } from "../../context/AuthContext";
import rentalRequestService from "../../services/rentalRequestService";
import contractService from "../../services/contractService";
import invoiceService from "../../services/invoiceService";
import notificationService from "../../services/notificationService";

function CustomerDashboard() {
  const { user } = useAuth();
  const [data, setData] = useState({
    requests: [],
    contracts: [],
    invoices: [],
    unread: 0,
  });
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    (async () => {
      try {
        const [requests, contracts, invoices, unread] = await Promise.all([
          rentalRequestService.customerList(),
          contractService.customerList(),
          invoiceService.customerList(),
          notificationService.unreadCount(),
        ]);
        setData({
          requests: requests || [],
          contracts: contracts || [],
          invoices: invoices || [],
          unread: typeof unread === "number" ? unread : unread?.count || 0,
        });
      } finally {
        setLoading(false);
      }
    })();
  }, []);
  const cards = [
    {
      label: "Yêu cầu thuê",
      value: data.requests.length,
      icon: "bi-file-earmark-text",
      color: "primary",
      to: "/customer/rental-requests",
    },
    {
      label: "Hợp đồng",
      value: data.contracts.length,
      icon: "bi-file-earmark-check",
      color: "success",
      to: "/customer/contracts",
    },
    {
      label: "Hóa đơn",
      value: data.invoices.length,
      icon: "bi-receipt",
      color: "warning",
      to: "/customer/invoices",
    },
    {
      label: "Chưa đọc",
      value: data.unread,
      icon: "bi-bell",
      color: "danger",
      to: "/customer/notifications",
    },
  ];
  return (
    <div>
      <div className="mb-4">
        <h2 className="fw-bold mb-1">
          Xin chào, {user?.firstName || user?.username}
        </h2>
        <p className="text-secondary mb-0">
          Quản lý toàn bộ hoạt động thuê phòng của bạn.
        </p>
      </div>
      <div className="row g-4 mb-4">
        {cards.map((c) => (
          <div className="col-md-6 col-xl-3" key={c.label}>
            <Link to={c.to} className="text-decoration-none">
              <div className="card border-0 shadow-sm h-100">
                <div className="card-body">
                  <div className="d-flex justify-content-between">
                    <div>
                      <div className="text-secondary small">{c.label}</div>
                      <div className="display-6 fw-bold text-dark mt-2">
                        {loading ? "—" : c.value}
                      </div>
                    </div>
                    <div
                      className={`bg-${c.color}-subtle text-${c.color} rounded-3 p-3`}
                    >
                      <i className={`bi ${c.icon} fs-4`}></i>
                    </div>
                  </div>
                </div>
              </div>
            </Link>
          </div>
        ))}
      </div>
      <div className="card border-0 shadow-sm">
        <div className="card-body p-4">
          <div className="d-flex justify-content-between align-items-center mb-3">
            <h5 className="fw-bold mb-0">Bắt đầu</h5>
            <Link to="/rooms" className="btn btn-primary">
              Tìm phòng
            </Link>
          </div>
          <p className="text-secondary mb-0">
            Tìm phòng phù hợp, gửi yêu cầu thuê, đặt lịch xem và theo dõi hợp
            đồng, hóa đơn, thanh toán.
          </p>
        </div>
      </div>
    </div>
  );
}
export default CustomerDashboard;
