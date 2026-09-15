import { useEffect, useState } from "react";
import contractService from "../../services/contractService";
import { getErrorMessage } from "../../services/api";
import {
  formatCurrency,
  formatDate,
  statusClass,
  statusLabel,
} from "../../utils/format";
function ContractsPage() {
  const [items, setItems] = useState([]),
    [error, setError] = useState(""),
    [loading, setLoading] = useState(true);
  useEffect(() => {
    contractService
      .customerList()
      .then((x) => setItems(x || []))
      .catch((e) => setError(getErrorMessage(e)))
      .finally(() => setLoading(false));
  }, []);
  return (
    <div>
      <h2 className="fw-bold">Hợp đồng</h2>
      <p className="text-secondary">Danh sách hợp đồng thuê của bạn.</p>
      {error && <div className="alert alert-danger">{error}</div>}
      {loading ? (
        <div className="text-center py-5">
          <div className="spinner-border text-primary" />
        </div>
      ) : items.length === 0 ? (
        <div className="alert alert-light border">Chưa có hợp đồng.</div>
      ) : (
        <div className="row g-4">
          {items.map((x) => (
            <div className="col-md-6" key={x.id}>
              <div className="card border-0 shadow-sm h-100">
                <div className="card-body">
                  <h5 className="fw-bold">Phòng {x.roomNumber || x.roomId}</h5>
                  <span className={`badge text-bg-${statusClass(x.status)}`}>
                    {statusLabel(x.status)}
                  </span>
                  <dl className="row mt-3 mb-0">
                    <dt className="col-5">Bắt đầu</dt>
                    <dd className="col-7">{formatDate(x.startDate)}</dd>
                    <dt className="col-5">Kết thúc</dt>
                    <dd className="col-7">{formatDate(x.endDate)}</dd>
                    <dt className="col-5">Tiền thuê</dt>
                    <dd className="col-7">{formatCurrency(x.monthlyRent)}</dd>
                    <dt className="col-5">Tiền cọc</dt>
                    <dd className="col-7">{formatCurrency(x.deposit)}</dd>
                  </dl>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
export default ContractsPage;
