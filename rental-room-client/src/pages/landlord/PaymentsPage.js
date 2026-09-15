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
    [error, setError] = useState("");
  async function load() {
    try {
      setItems((await paymentService.landlordList()) || []);
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }
  useEffect(() => {
    load();
  }, []);
  async function act(id, t) {
    try {
      await paymentService[t](id);
      load();
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }
  return (
    <div>
      <h2 className="fw-bold">Thanh toán</h2>
      {error && <div className="alert alert-danger">{error}</div>}
      <div className="table-responsive card border-0 shadow-sm">
        <table className="table align-middle mb-0">
          <thead>
            <tr>
              <th>#</th>
              <th>Khách</th>
              <th>Phòng</th>
              <th>Số tiền</th>
              <th>Trạng thái</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {items.map((x) => (
              <tr key={x.id}>
                <td>{x.id}</td>
                <td>{x.customerUsername || x.userId}</td>
                <td>{x.roomNumber || x.roomId}</td>
                <td>{formatCurrency(x.amount)}</td>
                <td>
                  <span className={`badge text-bg-${statusClass(x.status)}`}>
                    {statusLabel(x.status)}
                  </span>
                </td>
                <td>
                  {x.status === "PENDING" && (
                    <>
                      <button
                        className="btn btn-sm btn-success me-2"
                        onClick={() => act(x.id, "confirm")}
                      >
                        Xác nhận
                      </button>
                      <button
                        className="btn btn-sm btn-outline-danger"
                        onClick={() => act(x.id, "reject")}
                      >
                        Từ chối
                      </button>
                    </>
                  )}
                </td>
              </tr>
            ))}
            {items.length === 0 && (
              <tr>
                <td colSpan="6" className="text-center py-5">
                  Chưa có thanh toán.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
export default PaymentsPage;
