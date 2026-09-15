import { useEffect, useState } from "react";
import viewingService from "../../services/viewingService";
import { getErrorMessage } from "../../services/api";
import { formatDate, statusClass, statusLabel } from "../../utils/format";

function ViewingsPage() {
  const [items, setItems] = useState([]),
    [error, setError] = useState(""),
    [loading, setLoading] = useState(true);
  async function load() {
    try {
      setLoading(true);
      setItems((await viewingService.customerList()) || []);
    } catch (e) {
      setError(getErrorMessage(e));
    } finally {
      setLoading(false);
    }
  }
  useEffect(() => {
    load();
  }, []);
  async function cancel(id) {
    try {
      await viewingService.cancel(id);
      load();
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }
  return (
    <div>
      <h2 className="fw-bold">Lịch xem phòng</h2>
      <p className="text-secondary">Theo dõi và hủy lịch xem phòng.</p>
      {error && <div className="alert alert-danger">{error}</div>}
      {loading ? (
        <div className="text-center py-5">
          <div className="spinner-border text-primary" />
        </div>
      ) : (
        <div className="row g-3">
          {items.length === 0 ? (
            <div className="col-12">
              <div className="card border-0 shadow-sm">
                <div className="card-body text-center py-5">
                  Chưa có lịch xem phòng.
                </div>
              </div>
            </div>
          ) : (
            items.map((x) => (
              <div className="col-md-6" key={x.id}>
                <div className="card border-0 shadow-sm h-100">
                  <div className="card-body">
                    <h5 className="fw-bold">
                      {x.roomNumber ||
                        x.room?.roomNumber ||
                        `Phòng #${x.roomId}`}
                    </h5>
                    <div className="text-secondary mb-2">
                      <i className="bi bi-calendar me-2" />
                      {formatDate(x.appointmentDate)} {x.appointmentTime || ""}
                    </div>
                    <span className={`badge text-bg-${statusClass(x.status)}`}>
                      {statusLabel(x.status)}
                    </span>
                    {x.message && (
                      <p className="mt-3 text-secondary mb-2">{x.message}</p>
                    )}
                    {x.status === "PENDING" && (
                      <button
                        className="btn btn-sm btn-outline-danger mt-2"
                        onClick={() => cancel(x.id)}
                      >
                        Hủy lịch
                      </button>
                    )}
                  </div>
                </div>
              </div>
            ))
          )}
        </div>
      )}
    </div>
  );
}
export default ViewingsPage;
