import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Login.css';
import { useAuth } from '../context/AuthContext';
import { Link } from 'react-router-dom';


const Login = () => {
    const { login } = useAuth();
    const navigate = useNavigate();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    function storeToken(token) {
        localStorage.setItem('token', token);
      }

    const onSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch('http://localhost:8080/api/v1/auth/authenticate', {
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
                const { token } = await response.json();
                storeToken(token);

                // Set token in authorization header for future requests
                const headers = new Headers();
                headers.append('Authorization', `Bearer ${token}`);

                // Update auth context
                login();

                navigate('/dashboard');
            } else {
                console.error('Login failed');
            }
        } catch (error) {
            console.error('Error:', error);
        }
    };


    return (
        <div className="login-container">
            <form className="login-form" onSubmit={onSubmit}>
                <div className="login-header">
                    <h2>Log In</h2>
                    <p>Don't have an account? <Link to="/signup">Sign Up</Link>!</p>
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
                <button type="submit" className="login">Log In</button>
            </form>
        </div>
    );
};

export default Login;
