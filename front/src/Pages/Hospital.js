import * as React from "react";
import { useEffect, useState } from "react";
import styles from "./Hospital.css";

export default function Hospital() {
    const [hospital, setHospital] = useState(null);
    
    useEffect(() => {
        fetch("/api/hospital")
        .then((res) => res.json())
        .then((data) => setHospital(data));
    }, []);
    
    return (
        <>
            <header className="header">
                <div className="header-content">
                <div className="header-left">
                    <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/a0a9d789f882e264c6147f22c9098045cccac58ca47b5236bf41bb05030906c9?apiKey=166a782ca6344aad902f23b81529b6b9&" alt="Stockspital" className="header-logo" />
                    <div className="header-name">Stockspital</div>
                </div>
                <div className="header-center">Spital Sf. Maria</div>
                <div className="header-right">
                    <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/7e038ee69d5e007292339b72556c5ec5e57b20ff249d1a7e300aae51ba5b3bf2?apiKey=166a782ca6344aad902f23b81529b6b9&" alt="Notifications" className="header-notification-icon" />
                    <div className="header-notification-text">Notifications</div>
                    <div className="header-notification-count">01</div>
                </div>
                </div>
            </header>
            {hospital? (
                <div className={styles.container}>
                    <h1>{hospital.name}</h1>
                    <p>{hospital.address}</p>
                    <p>{hospital.phone}</p>
                </div>
            ) : (
                <div>Loading...</div>
            )}
        </>
        
    );
}