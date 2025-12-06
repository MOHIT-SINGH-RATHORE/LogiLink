import { useState } from 'react';
import UserForm from './components/UserForm';
import UserList from './components/UserList';
import './App.css'; // You can keep the default styling or delete this import

function App() {
  // This state is used to trigger a refresh of the list when a new user is added
  const [refreshCount, setRefreshCount] = useState(0);

  const handleUserAdded = () => {
    setRefreshCount(prev => prev + 1);
  };

  return (
    <div style={{ maxWidth: '600px', margin: '0 auto', padding: '20px', fontFamily: 'Arial, sans-serif' }}>
      <h1 style={{ textAlign: 'center', color: '#333' }}>LogiLink Admin Panel</h1>
      
      {/* The Form */}
      <UserForm onUserAdded={handleUserAdded} />
      
      {/* The List - It watches 'refreshCount' to know when to update */}
      <UserList refreshTrigger={refreshCount} />
    </div>
  );
}

export default App;
