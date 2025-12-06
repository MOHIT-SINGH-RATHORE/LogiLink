import { useState } from 'react';
import api from '../api';

function UserForm({ onUserAdded }) {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: ''
    });

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // Sends a POST request to http://localhost:8080/api/users/register
            const response = await api.post('/register', formData);
            alert('User Registered Successfully!');
            
            // Clear the form
            setFormData({ username: '', email: '', password: '' });
            
            // Notify the parent component to refresh the list
            if (onUserAdded) onUserAdded();
            
        } catch (error) {
            console.error("Error registering user:", error);
            alert('Registration Failed');
        }
    };

    return (
        <div style={{ border: '1px solid #ccc', padding: '20px', marginBottom: '20px', borderRadius: '8px' }}>
            <h2>Register New User</h2>
            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: '10px' }}>
                    <input
                        type="text"
                        name="username"
                        placeholder="Username"
                        value={formData.username}
                        onChange={handleChange}
                        required
                        style={{ padding: '8px', width: '100%' }}
                    />
                </div>
                <div style={{ marginBottom: '10px' }}>
                    <input
                        type="email"
                        name="email"
                        placeholder="Email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                        style={{ padding: '8px', width: '100%' }}
                    />
                </div>
                <div style={{ marginBottom: '10px' }}>
                    <input
                        type="password"
                        name="password"
                        placeholder="Password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                        style={{ padding: '8px', width: '100%' }}
                    />
                </div>
                <button type="submit" style={{ padding: '10px 20px', cursor: 'pointer' }}>
                    Register
                </button>
            </form>
        </div>
    );
}

export default UserForm;
