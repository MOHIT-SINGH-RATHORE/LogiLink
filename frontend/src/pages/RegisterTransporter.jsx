import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import api from '../services/api';
import { Truck } from 'lucide-react';

export default function RegisterTransporter() {
    const [formData, setFormData] = useState({
        name: '', username: '', password: '', address: '', mobileNumber: '', serviceArea: ''
    });
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            await api.post('/auth/register/transporter', formData);
            navigate('/login');
        } catch (err) {
            setError(err.response?.data || 'Registration failed');
        }
    };

    return (
        <div className="container flex items-center justify-center" style={{ minHeight: '100vh', padding: '2rem 0' }}>
            <div className="card" style={{ maxWidth: '500px', width: '100%' }}>
                <h2 className="text-center mb-4 flex items-center justify-center gap-2">
                    <Truck className="text-primary" /> Register as Transporter
                </h2>
                {error && <div style={{ color: 'red', marginBottom: '1rem', textAlign: 'center' }}>{error}</div>}

                <form onSubmit={handleRegister}>
                    <div className="form-group">
                        <label className="form-label">Company / Full Name</label>
                        <input type="text" name="name" className="form-input" onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label className="form-label">Username</label>
                        <input type="text" name="username" className="form-input" onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label className="form-label">Password</label>
                        <input type="password" name="password" className="form-input" onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label className="form-label">Business Address</label>
                        <input type="text" name="address" className="form-input" onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label className="form-label">Mobile Number</label>
                        <input type="text" name="mobileNumber" className="form-input" onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label className="form-label">Service Area</label>
                        <input type="text" name="serviceArea" className="form-input" placeholder="e.g., California, NY to NJ" onChange={handleChange} required />
                    </div>
                    <button type="submit" className="btn btn-primary w-full mt-4">Register</button>
                </form>

                <div className="mt-4 text-center text-muted" style={{ fontSize: '0.875rem' }}>
                    Already have an account? <Link to="/login" className="text-primary">Login</Link>
                </div>
            </div>
        </div>
    );
}
