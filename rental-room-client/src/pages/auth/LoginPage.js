import { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { getErrorMessage } from "../../services/api";
function LoginPage() {
  const nav = useNavigate();
  const loc = useLocation();
  const { login } = useAuth();
  const [f, setF] = useState({ username: "", password: "" }),
    [error, setError] = useState(""),
    [loading, setLoading] = useState(false);
  function c(e) {
    setF({ ...f, [e.target.name]: e.target.value });
  }
  async function submit(e) {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      const { user } = await login(f.username, f.password);
      const from = loc.state?.from?.pathname;
      if (from) {
        nav(from, { replace: true });
        return;
      }
      if (user?.role === "CUSTOMER") nav("/customer");
      else if (user?.role === "LANDLORD") nav("/landlord");
      else if (user?.role === "ADMINISTRATOR")
        window.location.href = "http://localhost:8080/admin/rooms";
      else nav("/");
    } catch (e) {
      setError(getErrorMessage(e, "Tên đăng nhập hoặc mật khẩu không đúng."));
    } finally {
      setLoading(false);
    }
  }
  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-md-5">
          <div className="card border-0 shadow-sm">
            <div className="card-body p-4">
              <div className="text-center mb-4">
                <div className="display-6 text-primary">
                  <i className="bi bi-house-heart-fill" />
                </div>
                <h2 className="fw-bold mt-2">Đăng nhập</h2>
              </div>
              {error && <div className="alert alert-danger">{error}</div>}
              <form onSubmit={submit}>
                <div className="mb-3">
                  <label className="form-label">Tên đăng nhập</label>
                  <input
                    name="username"
                    className="form-control"
                    value={f.username}
                    onChange={c}
                    required
                  />
                </div>
                <div className="mb-3">
                  <label className="form-label">Mật khẩu</label>
                  <input
                    type="password"
                    name="password"
                    className="form-control"
                    value={f.password}
                    onChange={c}
                    required
                  />
                </div>
                <button className="btn btn-primary w-100" disabled={loading}>
                  {loading ? "Đang đăng nhập..." : "Đăng nhập"}
                </button>
              </form>
              <div className="text-center mt-3 text-secondary">
                Chưa có tài khoản? <Link to="/register">Đăng ký</Link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
export default LoginPage;
