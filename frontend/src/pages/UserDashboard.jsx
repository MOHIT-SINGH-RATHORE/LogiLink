import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import { Package, Plus, LogOut, Check, CreditCard, Star } from 'lucide-react';

export default function UserDashboard() {
    const [requests, setRequests] = useState([]);
    const [showForm, setShowForm] = useState(false);
    const [formData, setFormData] = useState({
        fromLocation: '', toLocation: '', distance: 0, isFragile: false,
        pricingRangeStart: 0, pricingRangeEnd: 0, loadUnloadOption: '', requiredDays: 1
    });
    const [bidsMap, setBidsMap] = useState({});

    const navigate = useNavigate();

    useEffect(() => {
        fetchRequests();
    }, []);

    const fetchRequests = async () => {
        try {
            const res = await api.get('/requests/my-requests');
            setRequests(res.data);
            res.data.forEach(req => {
                fetchBids(req.id);
            });
        } catch (err) {
            if (err.response?.status === 401) navigate('/login');
        }
    };

    const fetchBids = async (requestId) => {
        try {
            const res = await api.get(`/bids/request/${requestId}`);
            setBidsMap(prev => ({ ...prev, [requestId]: res.data }));
        } catch (err) { console.error(err); }
    };

    const handleCreateRequest = async (e) => {
        e.preventDefault();
        try {
            await api.post('/requests', formData);
            setShowForm(false);
            fetchRequests();
        } catch (err) { console.error(err); }
    };

    const acceptBid = async (bidId) => {
        try {
            await api.post(`/bids/${bidId}/accept`);
            fetchRequests();
        } catch (err) { alert('Failed to accept bid'); }
    };

    const processPayment = async (bidId, totalAmount) => {
        try {
            await api.post(`/payments/checkout/${bidId}?paymentMethod=Card`);
            alert(`Payment successful! 5% platform fee added.`);
        } catch (err) { alert('Payment failed'); }
    };

    const handleLogout = () => {
        localStorage.clear();
        navigate('/login');
    };

    return (
        <div className="container" style={{ padding: '2rem 0' }}>
            <div className="flex justify-between items-center mb-4">
                <h2><Package className="text-primary" /> My Transport Requests</h2>
                <div>
                    <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
                        <Plus /> New Request
                    </button>
                    <button className="btn btn-secondary" onClick={handleLogout} style={{ marginLeft: '1rem' }}>
                        <LogOut /> Logout
                    </button>
                </div>
            </div>

            {showForm && (
                <div className="card mb-4" style={{ borderLeft: '4px solid var(--primary)' }}>
                    <h3>Create New Request</h3>
                    <form className="mt-4" onSubmit={handleCreateRequest}>
                        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                            <div className="form-group">
                                <label className="form-label">From</label>
                                <input type="text" className="form-input" required onChange={e => setFormData({ ...formData, fromLocation: e.target.value })} />
                            </div>
                            <div className="form-group">
                                <label className="form-label">To</label>
                                <input type="text" className="form-input" required onChange={e => setFormData({ ...formData, toLocation: e.target.value })} />
                            </div>
                            <div className="form-group">
                                <label className="form-label">Distance (km)</label>
                                <input type="number" className="form-input" required onChange={e => setFormData({ ...formData, distance: e.target.value })} />
                            </div>
                            <div className="form-group flex items-center gap-2">
                                <input type="checkbox" onChange={e => setFormData({ ...formData, isFragile: e.target.checked })} />
                                <label className="form-label" style={{ marginBottom: 0 }}>Is Fragile?</label>
                            </div>
                            <div className="form-group">
                                <label className="form-label">Min Price</label>
                                <input type="number" className="form-input" required onChange={e => setFormData({ ...formData, pricingRangeStart: e.target.value })} />
                            </div>
                            <div className="form-group">
                                <label className="form-label">Max Price</label>
                                <input type="number" className="form-input" required onChange={e => setFormData({ ...formData, pricingRangeEnd: e.target.value })} />
                            </div>
                            <div className="form-group">
                                <label className="form-label">Load/Unload Details (Floor etc)</label>
                                <input type="text" className="form-input" required onChange={e => setFormData({ ...formData, loadUnloadOption: e.target.value })} />
                            </div>
                            <div className="form-group">
                                <label className="form-label">Required Days</label>
                                <input type="number" className="form-input" required onChange={e => setFormData({ ...formData, requiredDays: e.target.value })} />
                            </div>
                        </div>
                        <button type="submit" className="btn btn-primary">Submit Request</button>
                    </form>
                </div>
            )}

            {requests.length === 0 && <p className="text-muted">No requests found. Create one above.</p>}

            {requests.map(req => (
                <div key={req.id} className="card mb-4">
                    <div className="flex justify-between">
                        <div>
                            <h3 className="text-primary">{req.fromLocation} → {req.toLocation}</h3>
                            <p className="text-muted text-sm" style={{ fontSize: '0.875rem' }}>
                                Distance: {req.distance}km | Fragile: {req.isFragile ? 'Yes' : 'No'} |
                                Target Price: ${req.pricingRangeStart} - ${req.pricingRangeEnd} |
                                Details: {req.loadUnloadOption}
                            </p>
                        </div>
                        <div>
                            <span style={{ padding: '0.25rem 0.5rem', background: '#f1f5f9', borderRadius: '4px', fontWeight: 600 }}>
                                {req.status}
                            </span>
                        </div>
                    </div>

                    <div className="mt-4">
                        <h4>Bids</h4>
                        {(!bidsMap[req.id] || bidsMap[req.id].length === 0) && <p className="text-muted" style={{ fontSize: '0.875rem' }}>No bids yet.</p>}
                        {bidsMap[req.id]?.map(bid => (
                            <div key={bid.id} style={{ background: '#f8fafc', padding: '1rem', borderRadius: '8px', marginTop: '0.5rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                <div>
                                    <strong>Total: ${bid.totalAmount}</strong> <br />
                                    <span style={{ fontSize: '0.8rem' }} className="text-muted">
                                        Transporter ID: {bid.transporterId} | Service: ${bid.serviceCharge} | Vehicle: ${bid.vehicleCost} | Fuel: ${bid.fuelCost} | Toll: ${bid.tollTaxFee}
                                    </span>
                                </div>
                                <div className="flex gap-2">
                                    {req.status === 'PENDING' && bid.status === 'PENDING' && (
                                        <button className="btn btn-primary" style={{ padding: '0.25rem 0.5rem' }} onClick={() => acceptBid(bid.id)}>
                                            <Check size={16} /> Accept
                                        </button>
                                    )}
                                    {req.status === 'ACCEPTED' && bid.status === 'ACCEPTED' && (
                                        <button className="btn btn-primary" style={{ padding: '0.25rem 0.5rem', background: 'var(--secondary)' }} onClick={() => processPayment(bid.id, bid.totalAmount)}>
                                            <CreditCard size={16} /> Pay (incl. 5% fee)
                                        </button>
                                    )}
                                    {bid.status === 'ACCEPTED' && (
                                        <span style={{ color: 'var(--secondary)', fontWeight: 600 }}>Accepted</span>
                                    )}
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            ))}
        </div>
    );
}
