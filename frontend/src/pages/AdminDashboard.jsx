import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import { Shield, Users, Truck, DollarSign, MessageCircle, LogOut } from 'lucide-react';

export default function AdminDashboard() {
    const [stats, setStats] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        fetchStats();
    }, []);

    const fetchStats = async () => {
        try {
            const res = await api.get('/admin/dashboard');
            setStats(res.data);
        } catch (err) {
            if (err.response?.status === 401 || err.response?.status === 403) navigate('/login');
        }
    };

    const handleLogout = () => {
        localStorage.clear();
        navigate('/login');
    };

    return (
        <div className="container" style={{ padding: '2rem 0' }}>
            <div className="flex justify-between items-center mb-6">
                <h2><Shield className="text-primary" /> Admin Dashboard</h2>
                <button className="btn btn-secondary" onClick={handleLogout}>
                    <LogOut /> Logout
                </button>
            </div>

            {stats ? (
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1.5rem', marginBottom: '2rem' }}>
                    <div className="card text-center">
                        <Users size={32} className="text-primary mb-2 mx-auto" style={{ margin: '0 auto' }} />
                        <h3 className="text-2xl">{stats.totalUsers}</h3>
                        <p className="text-muted">Total Users</p>
                    </div>
                    <div className="card text-center">
                        <Truck size={32} className="text-primary mb-2 mx-auto" style={{ margin: '0 auto' }} />
                        <h3 className="text-2xl">{stats.totalTransporters}</h3>
                        <p className="text-muted">Total Transporters</p>
                    </div>
                    <div className="card text-center">
                        <DollarSign size={32} className="text-primary mb-2 mx-auto" style={{ margin: '0 auto' }} />
                        <h3 className="text-2xl">${stats.totalRevenue?.toFixed(2)}</h3>
                        <p className="text-muted">Platform Revenue (5%)</p>
                    </div>
                    <div className="card text-center">
                        <MessageCircle size={32} className="text-primary mb-2 mx-auto" style={{ margin: '0 auto' }} />
                        <h3 className="text-2xl">{stats.totalComplaints}</h3>
                        <p className="text-muted">Total Complaints</p>
                    </div>
                </div>
            ) : (
                <p>Loading stats...</p>
            )}

            <div className="card">
                <h3>System Overview</h3>
                <p className="text-muted mt-2">
                    From this dashboard, admins can view total usage, revenue, and active complaints.
                    Use the backend API endpoints to extend management capabilities for specific users, transporters, and support tickets.
                </p>
            </div>
        </div>
    );
}
