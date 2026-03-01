import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import RegisterUser from './pages/RegisterUser';
import RegisterTransporter from './pages/RegisterTransporter';
import UserDashboard from './pages/UserDashboard';
import TransporterDashboard from './pages/TransporterDashboard';
import AdminDashboard from './pages/AdminDashboard';

function App() {
  return (
    <Router>
      <div className="app-layout">
        <main>
          <Routes>
            <Route path="/" element={<Navigate to="/login" replace />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register/user" element={<RegisterUser />} />
            <Route path="/register/transporter" element={<RegisterTransporter />} />

            <Route path="/user/dashboard" element={<UserDashboard />} />
            <Route path="/transporter/dashboard" element={<TransporterDashboard />} />
            <Route path="/admin/dashboard" element={<AdminDashboard />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
