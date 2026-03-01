import { useState } from 'react';
import api from './api';

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
            
            // Extract error message from response
            let errorMessage = 'Registration Failed';
            if (error.response && error.response.data) {
                if (error.response.data.error) {
                    errorMessage = error.response.data.error;
                } else if (typeof error.response.data === 'string') {
                    errorMessage = error.response.data;
                }
            } else if (error.message) {
                errorMessage = `Registration Failed: ${error.message}`;
            }
            
            alert(errorMessage);
        }
    };

    return (
        <div style={{ border: '1px solid #ccc', padding: '20px', marginBottom: '20px', borderRadius: '8px', backgroundColor: '#fff' }}>
            <h2 style={{ color: '#333', marginTop: 0 }}>Register New User</h2>
            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: '10px' }}>
                    <input
                        type="text"
                        name="username"
                        placeholder="Username"
                        value={formData.username}
                        onChange={handleChange}
                        required
                        style={{ padding: '8px', width: '100%', color: '#333', backgroundColor: '#fff', border: '1px solid #ccc', borderRadius: '4px' }}
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
                        style={{ padding: '8px', width: '100%', color: '#333', backgroundColor: '#fff', border: '1px solid #ccc', borderRadius: '4px' }}
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
                        style={{ padding: '8px', width: '100%', color: '#333', backgroundColor: '#fff', border: '1px solid #ccc', borderRadius: '4px' }}
                    />
                </div>
                <button type="submit" style={{ padding: '10px 20px', cursor: 'pointer', backgroundColor: '#646cff', color: '#fff', border: 'none', borderRadius: '4px', fontWeight: '500' }}>
                    Register
                </button>
            </form>
        </div>
    );
}

export default UserForm;
