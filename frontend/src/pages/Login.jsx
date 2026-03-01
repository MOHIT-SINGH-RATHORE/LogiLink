import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import api from '../services/api';
import { LogIn } from 'lucide-react';

export default function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const res = await api.post('/auth/login', { username, password });
            localStorage.setItem('token', res.data.token);
            localStorage.setItem('user', JSON.stringify(res.data));

            if (res.data.role === 'ROLE_USER') navigate('/user/dashboard');
            else if (res.data.role === 'ROLE_TRANSPORTER') navigate('/transporter/dashboard');
            else if (res.data.role === 'ROLE_ADMIN') navigate('/admin/dashboard');

        } catch (err) {
            setError('Invalid username or password');
        }
    };

    return (
        <div className="container flex items-center justify-center" style={{ minHeight: '100vh' }}>
            <div className="card" style={{ maxWidth: '400px', width: '100%' }}>
                <h2 className="text-center mb-4 flex items-center justify-center gap-2">
                    <LogIn className="text-primary" /> LogiLink Login
                </h2>

                {error && <div style={{ color: 'red', marginBottom: '1rem', textAlign: 'center' }}>{error}</div>}

                <form onSubmit={handleLogin}>
                    <div className="form-group">
                        <label className="form-label">Username</label>
                        <input type="text" className="form-input"
                            value={username} onChange={e => setUsername(e.target.value)} required />
                    </div>
                    <div className="form-group">
                        <label className="form-label">Password</label>
                        <input type="password" className="form-input"
                            value={password} onChange={e => setPassword(e.target.value)} required />
                    </div>
                    <button type="submit" className="btn btn-primary w-full">Sign In</button>
                </form>

                <div className="mt-4 text-center text-muted" style={{ fontSize: '0.875rem' }}>
                    Don't have an account? <br />
                    <Link to="/register/user" className="text-primary">Register as User</Link> |{' '}
                    <Link to="/register/transporter" className="text-primary">Register as Transporter</Link>
                </div>
            </div>
        </div>
    );
}
