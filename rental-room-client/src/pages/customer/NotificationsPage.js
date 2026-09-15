import { useEffect, useState } from "react";
import notificationService from "../../services/notificationService";
import { getErrorMessage } from "../../services/api";
import { formatDate } from "../../utils/format";
function NotificationsPage() {
  const [items, setItems] = useState([]),
    [error, setError] = useState(""),
    [loading, setLoading] = useState(true);
  async function load() {
    try {
      setLoading(true);
      setItems((await notificationService.list()) || []);
    } catch (e) {
      setError(getErrorMessage(e));
    } finally {
      setLoading(false);
    }
  }
  useEffect(() => {
    load();
  }, []);
  async function read(id) {
    try {
      await notificationService.markRead(id);
      load();
    } catch (e) {
      setError(getErrorMessage(e));
    }
  }
  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <div>
          <h2 className="fw-bold">Thông báo</h2>
          <p className="text-secondary mb-0">Các thông báo mới nhất của bạn.</p>
        </div>
        <button
          className="btn btn-outline-primary"
          onClick={async () => {
            await notificationService.markAllRead();
            load();
          }}
        >
          Đọc tất cả
        </button>
      </div>
      {error && <div className="alert alert-danger">{error}</div>}
      {loading ? (
        <div className="text-center py-5">
          <div className="spinner-border text-primary" />
        </div>
      ) : items.length === 0 ? (
        <div className="alert alert-light border">Không có thông báo.</div>
      ) : (
        <div className="d-flex flex-column gap-3">
          {items.map((x) => (
            <div
              className={`card border-0 shadow-sm ${x.isRead ? "" : "border-start border-4 border-primary"}`}
              key={x.id}
            >
              <div className="card-body">
                <div className="d-flex justify-content-between gap-3">
                  <div>
                    <h6 className="fw-bold mb-1">{x.title}</h6>
                    <p className="text-secondary mb-2">{x.content}</p>
                    <small className="text-muted">
                      {formatDate(x.createdDate)}
                    </small>
                  </div>
                  {!x.isRead && (
                    <button
                      className="btn btn-sm btn-outline-primary align-self-start"
                      onClick={() => read(x.id)}
                    >
                      Đã đọc
                    </button>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
export default NotificationsPage;
