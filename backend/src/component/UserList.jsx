import { useEffect, useState } from 'react';
import api from '../api';

function UserList({ refreshTrigger }) {
    const [users, setUsers] = useState([]);

    // This function fetches data from the Java backend
    const fetchUsers = async () => {
        try {
            // Sends a GET request to http://localhost:8080/api/users
            const response = await api.get('/'); 
            setUsers(response.data);
        } catch (error) {
            console.error("Error fetching users:", error);
        }
    };

    // useEffect runs when the component mounts or when 'refreshTrigger' changes
    useEffect(() => {
        fetchUsers();
    }, [refreshTrigger]);

    return (
        <div style={{ border: '1px solid #ccc', padding: '20px', borderRadius: '8px' }}>
            <h2>Registered Users</h2>
            {users.length === 0 ? (
                <p>No users found.</p>
            ) : (
                <ul style={{ listStyleType: 'none', padding: 0 }}>
                    {users.map((user) => (
                        <li key={user.id} style={{ background: '#f9f9f9', margin: '5px 0', padding: '10px', borderBottom: '1px solid #eee' }}>
                            <strong>{user.username}</strong> ({user.email})
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default UserList;
