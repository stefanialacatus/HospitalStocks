import * as React from 'react';
import './Statistic.css';
import { useState, useEffect } from 'react';
import axios from 'axios';

const itemsPerPage = 8;
const totalMedicines = 50; 
const totalPages = Math.ceil(totalMedicines / itemsPerPage); 

function MedicineListItem({ name, dosageForm, illness, stock, id }) {
    return (
        <tr className="medicine-list-item">
            <td>{name}</td>
            <td>{dosageForm}</td>
            <td>{illness}</td>
            <td>{stock}</td>
            <td className="view-details-status">
                <span>Bad</span>
            </td>
        </tr>
    );
}

function MedicinesTable({ medicines }) {
    return (
        <table className="medicines-table">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Dosage Form</th>
                    <th>Illness</th>
                    <th>Stock in Qty</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                {medicines.map((medicine) => (
                    <MedicineListItem key={medicine.id} {...medicine} />
                ))}
            </tbody>
        </table>
    );
}

function Pagination({ currentPage, totalPages, onNextPage, onPreviousPage }) {
    const startItem = (currentPage - 1) * itemsPerPage + 1;
    const endItem = Math.min(currentPage * itemsPerPage, totalMedicines);
    return (
        <div className="pagination-container">
            <span>Showing {startItem} - {endItem}</span>
            <div className="pagination-controls">
                <button onClick={onPreviousPage} disabled={currentPage === 1}>
                    <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/9c5603b1ade53a12461fcc579f39c2e84d89bb535241d15a38ce46cac6ef1981?apiKey=92503cb420154d6c95f36ba59a7a554b&" alt="Previous Page" className="pagination-icon" />
                </button>
                <span>Page {currentPage < 10 ? `${currentPage}` : currentPage}</span>
                <button onClick={onNextPage} disabled={currentPage === totalPages}>
                    <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/73a4e29bb1c974471e9660a44354779d6ed7bceb50229ce1628cc2ba4ef68621?apiKey=92503cb420154d6c95f36ba59a7a554b&" alt="Next Page" className="pagination-icon" />
                </button>
                
            </div>
        </div>
    );
}

export default function Inventory() {
    const [currentPage, setCurrentPage] = useState(1);

    const [listType, setListType] = React.useState('Medicines');
    const [medicines, setMedicines] = useState([]);

    const fetchData = async (page) => {
        try {
            const response = await axios.get(`http://localhost:8080/drugs/getBadDrugsInPage?page=${page}`);
            setMedicines(response.data);
            console.log('Data fetched:', response.data);
            console.log('Medicines:', medicines);
    
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    useEffect(() => {
        fetchData(currentPage);
    }, [currentPage]);

    const handleNextPage = () => {
        setCurrentPage((prevPage) => Math.min(prevPage + 1, totalPages));
    };

    const handlePreviousPage = () => {
        setCurrentPage((prevPage) => Math.max(prevPage - 1, 1));
    };
    

    return (
        <>
            <header className="header">
                <div className="header-content">
                    <div className="header-left">
                        <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/a0a9d789f882e264c6147f22c9098045cccac58ca47b5236bf41bb05030906c9?apiKey=166a782ca6344aad902f23b81529b6b9&" alt="Stockspital" className="header-logo" />
                        <div className="header-name">Stockspital</div>
                    </div>
                    <div className="header-center">Spital Sf. Maria</div>
                </div>
            </header>
            <main className="main-cont">
                <section className="introducere">
                    <div className="title-section">
                        <h1 className="title">
                            Statistics & Predictions
                        </h1>
                    </div>
                    {listType === 'Medicines' ? <MedicinesTable medicines={medicines} /> : null}
                    <Pagination
                        currentPage={currentPage}
                        totalPages={totalPages}
                        onNextPage={handleNextPage}
                        onPreviousPage={handlePreviousPage}
                    />
                </section>
            </main>
        </>
    );
}
