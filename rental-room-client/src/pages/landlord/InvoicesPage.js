import { useEffect, useState } from "react";
import invoiceService from "../../services/invoiceService";
import { getErrorMessage } from "../../services/api";
import { formatCurrency, formatDate } from "../../utils/format";
function InvoicesPage() {
  const [items, setItems] = useState([]),
    [error, setError] = useState("");
  useEffect(() => {
    invoiceService
      .landlordList()
      .then((x) => setItems(x || []))
      .catch((e) => setError(getErrorMessage(e)));
  }, []);
  return (
    <div>
      <h2 className="fw-bold">Hóa đơn</h2>
      {error && <div className="alert alert-danger">{error}</div>}
      <div className="table-responsive card border-0 shadow-sm">
        <table className="table align-middle mb-0">
          <thead>
            <tr>
              <th>#</th>
              <th>Phòng</th>
              <th>Khách</th>
              <th>Kỳ</th>
              <th>Tổng</th>
              <th>Hạn</th>
            </tr>
          </thead>
          <tbody>
            {items.map((x) => (
              <tr key={x.id}>
                <td>{x.id}</td>
                <td>{x.roomNumber || x.roomId}</td>
                <td>{x.customer || x.customerUsername || x.userId}</td>
                <td>{formatDate(x.billingDate)}</td>
                <td>{formatCurrency(x.totalAmount)}</td>
                <td>{formatDate(x.dueDate)}</td>
              </tr>
            ))}
            {items.length === 0 && (
              <tr>
                <td colSpan="6" className="text-center py-5">
                  Chưa có hóa đơn.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
export default InvoicesPage;
