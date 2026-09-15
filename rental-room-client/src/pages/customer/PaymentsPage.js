import { useEffect, useState } from "react";
import paymentService from "../../services/paymentService";
import { getErrorMessage } from "../../services/api";
import {
  formatCurrency,
  formatDate,
  statusClass,
  statusLabel,
} from "../../utils/format";
function PaymentsPage() {
  const [items, setItems] = useState([]),
    [error, setError] = useState(""),
    [loading, setLoading] = useState(true);
  useEffect(() => {
    paymentService
      .customerList()
      .then((x) => setItems(x || []))
      .catch((e) => setError(getErrorMessage(e)))
      .finally(() => setLoading(false));
  }, []);
  return (
    <div>
      <h2 className="fw-bold">Thanh toán</h2>
      <p className="text-secondary">Lịch sử thanh toán của bạn.</p>
      {error && <div className="alert alert-danger">{error}</div>}
      {loading ? (
        <div className="text-center py-5">
          <div className="spinner-border text-primary" />
        </div>
      ) : (
        <div className="table-responsive card border-0 shadow-sm">
          <table className="table mb-0 align-middle">
            <thead>
              <tr>
                <th>#</th>
                <th>Số tiền</th>
                <th>Phương thức</th>
                <th>Trạng thái</th>
                <th>Ngày</th>
              </tr>
            </thead>
            <tbody>
              {items.map((x) => (
                <tr key={x.id}>
                  <td>{x.id}</td>
                  <td>{formatCurrency(x.amount)}</td>
                  <td>{x.paymentMethod || x.method || "—"}</td>
                  <td>
                    <span className={`badge text-bg-${statusClass(x.status)}`}>
                      {statusLabel(x.status)}
                    </span>
                  </td>
                  <td>{formatDate(x.paidDate || x.createdDate)}</td>
                </tr>
              ))}
              {items.length === 0 && (
                <tr>
                  <td colSpan="5" className="text-center py-5">
                    Chưa có thanh toán.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
export default PaymentsPage;
