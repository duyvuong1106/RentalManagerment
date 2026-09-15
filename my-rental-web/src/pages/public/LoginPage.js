import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useNavigate } from 'react-router-dom';

const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    // Giả lập dữ liệu nhận về từ backend khi test
    const mockUser = { email, role: 'ROLE_CUSTOMER' };
    const mockToken = 'mock-jwt-token-xyz';

    login(mockUser, mockToken);
    navigate('/customer/dashboard');
  };

  return (
    <div style={{ padding: '30px', maxWidth: '400px', margin: '50px auto' }}>
      <h2>Đăng nhập Hệ thống</h2>
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: '15px' }}>
          <label>Email:</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            style={{ width: '100%', padding: '8px', marginTop: '5px' }}
            required
          />
        </div>
        <div style={{ marginBottom: '15px' }}>
          <label>Mật khẩu:</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            style={{ width: '100%', padding: '8px', marginTop: '5px' }}
            required
          />
        </div>
        <button type="submit" style={{ padding: '10px 20px', cursor: 'pointer' }}>
          Đăng nhập
        </button>
      </form>
    </div>
  );
};

export default LoginPage;