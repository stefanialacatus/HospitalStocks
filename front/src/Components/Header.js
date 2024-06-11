import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Header.css';
import { useAuth } from '../context/AuthContext';

const Header = () => {
    const navigate = useNavigate();
    const { isLoggedIn, logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

    
    return (
        <header className="header">
            <div className="header-content">
                <div className="header-left">
                    <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/a0a9d789f882e264c6147f22c9098045cccac58ca47b5236bf41bb05030906c9?apiKey=166a782ca6344aad902f23b81529b6b9&" alt="Stockspital" className="header-logo" />
                    <div className="header-name">Stockspital</div>
                </div>
                <div className="header-right">
                        <button className="logout-button" onClick={handleLogout}>Logout</button>
                </div>
            </div>
        </header>
    );
};

export default Header;
