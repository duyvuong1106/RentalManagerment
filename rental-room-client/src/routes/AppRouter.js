import { BrowserRouter, Routes, Route } from "react-router-dom";

import PublicLayout from "../components/layouts/PublicLayout";
import CustomerLayout from "../components/layouts/CustomerLayout";
import LandLord from "../components/layouts/LandLord";
import ProtectedRoute from "../components/common/ProtectedRoute";

import HomePage from "../pages/public/HomePage";
import RoomListPage from "../pages/public/RoomListPage";
import RoomDetailPage from "../pages/public/RoomDetailPage";

import LoginPage from "../pages/auth/LoginPage";
import RegisterPage from "../pages/auth/RegisterPage";

import CustomerDashboard from "../pages/customer/CustomerDashboard";
import RentalRequestsPage from "../pages/customer/RentalRequestsPage";
import ViewingsPage from "../pages/customer/ViewingsPage";
import ContractsPage from "../pages/customer/ContractsPage";
import InvoicesPage from "../pages/customer/InvoicesPage";
import PaymentsPage from "../pages/customer/PaymentsPage";
import NotificationsPage from "../pages/customer/NotificationsPage";
import ReviewsPage from "../pages/customer/ReviewsPage";
import ProfilePage from "../pages/customer/ProfilePage";
import PaymentCreatePage from "../pages/customer/PaymentCreatePage";
import ReviewCreatePage from "../pages/customer/ReviewCreatePage";

import LandlordDashboard from "../pages/landlord/LandlordDashboard";
import RoomsPage from "../pages/landlord/RoomsPage";
import RoomFormPage from "../pages/landlord/RoomFormPage";
import RoomImagesPage from "../pages/landlord/RoomImagesPage";
import LandlordRentalRequestsPage from "../pages/landlord/RentalRequestsPage";
import LandlordViewingsPage from "../pages/landlord/ViewingsPage";
import LandlordContractsPage from "../pages/landlord/ContractsPage";
import ContractCreatePage from "../pages/landlord/ContractCreatePage";
import InvoiceCreatePage from "../pages/landlord/InvoiceCreatePage";
import LandlordInvoicesPage from "../pages/landlord/InvoicesPage";
import LandlordPaymentsPage from "../pages/landlord/PaymentsPage";
import StatisticsPage from "../pages/landlord/StatisticsPage";
import LandlordNotificationsPage from "../pages/landlord/NotificationsPage";
import LandlordProfilePage from "../pages/landlord/ProfilePage";

function NotFoundPage() {
  return (
    <div className="container py-5 text-center">
      <h1 className="display-5 fw-bold">404</h1>
      <p className="text-secondary">Trang bạn đang tìm không tồn tại.</p>
    </div>
  );
}

function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Public */}
        <Route element={<PublicLayout />}>
          <Route path="/" element={<HomePage />} />
          <Route path="/rooms" element={<RoomListPage />} />
          <Route path="/rooms/:id" element={<RoomDetailPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
        </Route>

        {/* Customer */}
        <Route element={<ProtectedRoute roles={["CUSTOMER"]} />}>
          <Route element={<CustomerLayout />}>
            <Route path="/customer" element={<CustomerDashboard />} />
            <Route
              path="/customer/rental-requests"
              element={<RentalRequestsPage />}
            />
            <Route path="/customer/viewings" element={<ViewingsPage />} />
            <Route path="/customer/contracts" element={<ContractsPage />} />
            <Route path="/customer/invoices" element={<InvoicesPage />} />
            <Route path="/customer/payments" element={<PaymentsPage />} />
            <Route
              path="/customer/payments/new"
              element={<PaymentCreatePage />}
            />
            <Route path="/customer/reviews" element={<ReviewsPage />} />
            <Route
              path="/customer/reviews/new"
              element={<ReviewCreatePage />}
            />
            <Route
              path="/customer/notifications"
              element={<NotificationsPage />}
            />
            <Route path="/customer/profile" element={<ProfilePage />} />
          </Route>
        </Route>

        {/* Landlord */}
        <Route element={<ProtectedRoute roles={["LANDLORD"]} />}>
          <Route element={<LandLord />}>
            <Route path="/landlord" element={<LandlordDashboard />} />
            <Route path="/landlord/rooms" element={<RoomsPage />} />
            <Route path="/landlord/rooms/new" element={<RoomFormPage />} />
            <Route path="/landlord/rooms/:id/edit" element={<RoomFormPage />} />
            <Route
              path="/landlord/rooms/:id/images"
              element={<RoomImagesPage />}
            />
            <Route
              path="/landlord/rental-requests"
              element={<LandlordRentalRequestsPage />}
            />
            <Route
              path="/landlord/viewings"
              element={<LandlordViewingsPage />}
            />
            <Route
              path="/landlord/contracts"
              element={<LandlordContractsPage />}
            />
            <Route
              path="/landlord/contracts/new"
              element={<ContractCreatePage />}
            />
            <Route
              path="/landlord/invoices"
              element={<LandlordInvoicesPage />}
            />
            <Route
              path="/landlord/invoices/new"
              element={<InvoiceCreatePage />}
            />
            <Route
              path="/landlord/payments"
              element={<LandlordPaymentsPage />}
            />
            <Route path="/landlord/statistics" element={<StatisticsPage />} />
            <Route
              path="/landlord/notifications"
              element={<LandlordNotificationsPage />}
            />
            <Route path="/landlord/profile" element={<LandlordProfilePage />} />
          </Route>
        </Route>

        {/* Fallback */}
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default AppRouter;
