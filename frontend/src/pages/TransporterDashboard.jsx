import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import { Truck, LogOut, CheckCircle, Bell } from 'lucide-react';

export default function TransporterDashboard() {
    const [marketRequests, setMarketRequests] = useState([]);
    const [notifications, setNotifications] = useState([]);
    const [biddingOn, setBiddingOn] = useState(null);
    const [bidForm, setBidForm] = useState({
        serviceCharge: 0, vehicleCost: 0, fuelCost: 0, tollTaxFee: 0
    });
    const navigate = useNavigate();

    useEffect(() => {
        fetchMarket();
        fetchNotifications();
    }, []);

    const fetchMarket = async () => {
        try {
            const res = await api.get('/requests/market');
            setMarketRequests(res.data);
        } catch (err) {
            if (err.response?.status === 401) navigate('/login');
        }
    };

    const fetchNotifications = async () => {
        try {
            const res = await api.get('/notifications/unread');
            setNotifications(res.data);
        } catch (err) {
            console.error("Could not fetch notifications", err);
        }
    };

    const markNotificationAsRead = async (id) => {
        try {
            await api.put(`/notifications/${id}/read`);
            setNotifications(notifications.filter(n => n.id !== id));
        } catch (err) {
            console.error("Could not mark as read", err);
        }
    };

    const handleLogout = () => {
        localStorage.clear();
        navigate('/login');
    };

    const handleBidSubmit = async (e, reqId) => {
        e.preventDefault();
        try {
            await api.post(`/bids/${reqId}`, bidForm);
            alert('Bid placed successfully!');
            setBiddingOn(null);
        } catch (err) {
            alert(err.response?.data || 'Failed to place bid');
        }
    };

    return (
        <div className="container" style={{ padding: '2rem 0' }}>
            <div className="flex justify-between items-center mb-4">
                <h2><Truck className="text-primary" /> Open Market</h2>
                <div className="flex items-center" style={{ gap: '1rem' }}>
                    <div style={{ position: 'relative' }}>
                        <Bell />
                        {notifications.length > 0 && (
                            <span style={{ position: 'absolute', top: -8, right: -8, background: 'red', color: 'white', borderRadius: '50%', padding: '0.1rem 0.4rem', fontSize: '0.75rem' }}>
                                {notifications.length}
                            </span>
                        )}
                    </div>
                    <button className="btn btn-secondary" onClick={handleLogout}>
                        <LogOut /> Logout
                    </button>
                </div>
            </div>

            {notifications.length > 0 && (
                <div className="card mb-4" style={{ backgroundColor: '#fff3cd', borderLeft: '4px solid #ffc107' }}>
                    <h3 className="mb-2" style={{ color: '#856404' }}>New Alerts</h3>
                    {notifications.map(n => (
                        <div key={n.id} className="flex justify-between items-center mb-2 p-2" style={{ background: '#fff' }}>
                            <p style={{ margin: 0 }}>{n.message}</p>
                            <button className="btn btn-sm btn-secondary" onClick={() => markNotificationAsRead(n.id)}>Dismiss</button>
                        </div>
                    ))}
                </div>
            )}

            {marketRequests.length === 0 && <p className="text-muted">No open requests in the market right now.</p>}

            {marketRequests.map(req => (
                <div key={req.id} className="card mb-4" style={{ borderLeft: '4px solid var(--secondary)' }}>
                    <div className="flex justify-between items-center">
                        <div>
                            <h3 className="text-primary">{req.fromLocation} → {req.toLocation}</h3>
                            <p className="text-muted text-sm" style={{ fontSize: '0.875rem' }}>
                                Distance: {req.distance}km | Fragile: {req.isFragile ? 'Yes' : 'No'} |
                                Client Price Target: ${req.pricingRangeStart} - ${req.pricingRangeEnd} |
                                Details: {req.loadUnloadOption}
                            </p>
                        </div>
                        <div>
                            <button className="btn btn-primary" onClick={() => setBiddingOn(req.id)}>Place Bid</button>
                        </div>
                    </div>

                    {biddingOn === req.id && (
                        <form className="mt-4 p-4" style={{ background: '#f8fafc', borderRadius: '8px' }} onSubmit={(e) => handleBidSubmit(e, req.id)}>
                            <h4 className="mb-2">Enter Bid Details</h4>
                            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                                <div className="form-group">
                                    <label className="form-label">Service Charge $</label>
                                    <input type="number" className="form-input" required onChange={e => setBidForm({ ...bidForm, serviceCharge: parseFloat(e.target.value) })} />
                                </div>
                                <div className="form-group">
                                    <label className="form-label">Vehicle Cost $</label>
                                    <input type="number" className="form-input" required onChange={e => setBidForm({ ...bidForm, vehicleCost: parseFloat(e.target.value) })} />
                                </div>
                                <div className="form-group">
                                    <label className="form-label">Fuel Cost $</label>
                                    <input type="number" className="form-input" required onChange={e => setBidForm({ ...bidForm, fuelCost: parseFloat(e.target.value) })} />
                                </div>
                                <div className="form-group">
                                    <label className="form-label">Toll Tax Fee $</label>
                                    <input type="number" className="form-input" required onChange={e => setBidForm({ ...bidForm, tollTaxFee: parseFloat(e.target.value) })} />
                                </div>
                            </div>
                            <div className="flex justify-between items-center mt-2">
                                <strong>Expected Total: ${(bidForm.serviceCharge || 0) + (bidForm.vehicleCost || 0) + (bidForm.fuelCost || 0) + (bidForm.tollTaxFee || 0)}</strong>
                                <div>
                                    <button type="button" className="btn btn-secondary mr-2" onClick={() => setBiddingOn(null)}>Cancel</button>
                                    <button type="submit" className="btn btn-primary" style={{ marginLeft: '0.5rem' }}><CheckCircle size={16} /> Submit Bid</button>
                                </div>
                            </div>
                        </form>
                    )}
                </div>
            ))}
        </div>
    );
}
