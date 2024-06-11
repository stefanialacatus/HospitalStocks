import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Register.css';
import { useAuth } from '../context/AuthContext';
import { Link } from 'react-router-dom';


const Register = () => {
    const { login } = useAuth();
    const navigate = useNavigate();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const onSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch('http://localhost:8080/api/v1/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username,
                    password,
                }),
            });
            if (response.ok) {
                navigate('/');
            } else {
                console.error('Register failed');
            }
        } catch (error) {
            console.error('Error:', error);
        }
    };

    return (
        <div className="reg-container">
            <form className="reg-form" onSubmit={onSubmit}>
                <div className="reg-header">
                    <h2>Sign Up</h2>
                    <p>Already have an account? <Link to="/">Log In</Link>!</p>
                </div>
                <div className="form-group">
                    <label>Username</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        placeholder="Enter your username"
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Password</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="Enter your password"
                        required
                    />
                </div>
                <button type="submit" className="reg">Sign Up</button>
            </form>
        </div>
    );
};

export default Register;
