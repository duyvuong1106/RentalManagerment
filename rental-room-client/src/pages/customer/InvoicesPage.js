import { useEffect, useState } from "react";
import invoiceService from "../../services/invoiceService";
import { getErrorMessage } from "../../services/api";
import { formatCurrency, formatDate } from "../../utils/format";
function InvoicesPage() {
  const [items, setItems] = useState([]),
    [error, setError] = useState(""),
    [loading, setLoading] = useState(true);
  useEffect(() => {
    invoiceService
      .customerList()
      .then((x) => setItems(x || []))
      .catch((e) => setError(getErrorMessage(e)))
      .finally(() => setLoading(false));
  }, []);
  return (
    <div>
      <h2 className="fw-bold">Hóa đơn</h2>
      <p className="text-secondary">Theo dõi các hóa đơn phát sinh.</p>
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
                <th>Phòng</th>
                <th>Kỳ</th>
                <th>Tổng tiền</th>
                <th>Hạn thanh toán</th>
              </tr>
            </thead>
            <tbody>
              {items.map((x) => (
                <tr key={x.id}>
                  <td>{x.id}</td>
                  <td>{x.roomNumber || x.roomId}</td>
                  <td>{formatDate(x.billingDate)}</td>
                  <td className="fw-semibold">
                    {formatCurrency(x.totalAmount)}
                  </td>
                  <td>{formatDate(x.dueDate)}</td>
                </tr>
              ))}
              {items.length === 0 && (
                <tr>
                  <td colSpan="5" className="text-center py-5">
                    Chưa có hóa đơn.
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
export default InvoicesPage;
