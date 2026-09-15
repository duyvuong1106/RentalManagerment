import React, { lazy, Suspense } from 'react';
import { Routes, Route } from 'react-router-dom';
import ProtectedRoute from '../components/guard/ProtectedRoute';

// Lazy load trang Login
const LoginPage = lazy(() => import('../pages/public/LoginPage'));

const PageLoading = () => (
  <div style={{ padding: '20px', textAlign: 'center' }}>Đang tải trang...</div>
);

const AppRoutes = () => {
  return (
    <Suspense fallback={<PageLoading />}>
      <Routes>
        {/* Public Route */}
        <Route path="/login" element={<LoginPage />} />

        {/* Customer Route */}
        <Route element={<ProtectedRoute allowedRoles={['ROLE_CUSTOMER']} />}>
          <Route path="/customer/dashboard" element={<h1 style={{ padding: '20px' }}>Trang Khách Thuê</h1>} />
        </Route>

        {/* Landlord Route */}
        <Route element={<ProtectedRoute allowedRoles={['ROLE_LANDLORD']} />}>
          <Route path="/landlord/dashboard" element={<h1 style={{ padding: '20px' }}>Trang Chủ Trọ</h1>} />
        </Route>

        {/* Admin Route */}
        <Route element={<ProtectedRoute allowedRoles={['ROLE_ADMIN']} />}>
          <Route path="/admin/dashboard" element={<h1 style={{ padding: '20px' }}>Trang Admin</h1>} />
        </Route>

        {/* Route mặc định */}
        <Route path="*" element={<LoginPage />} />
      </Routes>
    </Suspense>
  );
};

export default AppRoutes;