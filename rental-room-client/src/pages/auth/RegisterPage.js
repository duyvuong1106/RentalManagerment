import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import authService from "../../services/authService";
import { getErrorMessage } from "../../services/api";

function RegisterPage() {
    const navigate = useNavigate();
    const [form, setForm] = useState({ username: "", password: "", firstName: "", lastName: "", email: "", phone: "", address: "" });
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const [loading, setLoading] = useState(false);
    function change(e) { setForm({ ...form, [e.target.name]: e.target.value }); }
    async function submit(e) {
        e.preventDefault(); setError(""); setSuccess(""); setLoading(true);
        try { await authService.register(form); setSuccess("Đăng ký thành công. Bạn có thể đăng nhập."); setTimeout(() => navigate("/login"), 900); }
        catch (err) { setError(getErrorMessage(err, "Đăng ký không thành công.")); }
        finally { setLoading(false); }
    }
    return <div className="container py-5"><div className="row justify-content-center"><div className="col-lg-7"><div className="card border-0 shadow-sm"><div className="card-body p-4 p-md-5"><div className="text-center mb-4"><h2 className="fw-bold">Tạo tài khoản</h2><p className="text-secondary">Đăng ký tài khoản khách hàng</p></div>{error && <div className="alert alert-danger">{error}</div>}{success && <div className="alert alert-success">{success}</div>}<form onSubmit={submit}><div className="row g-3"><div className="col-md-6"><label className="form-label">Tên đăng nhập</label><input className="form-control" name="username" value={form.username} onChange={change} required /></div><div className="col-md-6"><label className="form-label">Email</label><input type="email" className="form-control" name="email" value={form.email} onChange={change} required /></div><div className="col-md-6"><label className="form-label">Họ</label><input className="form-control" name="firstName" value={form.firstName} onChange={change} /></div><div className="col-md-6"><label className="form-label">Tên</label><input className="form-control" name="lastName" value={form.lastName} onChange={change} /></div><div className="col-md-6"><label className="form-label">Số điện thoại</label><input className="form-control" name="phone" value={form.phone} onChange={change} /></div><div className="col-md-6"><label className="form-label">Mật khẩu</label><input type="password" className="form-control" name="password" value={form.password} onChange={change} required minLength={6} /></div><div className="col-12"><label className="form-label">Địa chỉ</label><input className="form-control" name="address" value={form.address} onChange={change} /></div></div><button className="btn btn-primary w-100 mt-4" disabled={loading}>{loading ? "Đang đăng ký..." : "Đăng ký"}</button></form><p className="text-center text-secondary mt-3 mb-0">Đã có tài khoản? <Link to="/login">Đăng nhập</Link></p></div></div></div></div></div>;
}
export default RegisterPage;
