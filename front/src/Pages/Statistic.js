import * as React from 'react';
import './Statistic.css';
import { useState, useEffect } from 'react';
import axios from 'axios';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

const itemsPerPage = 8;
const totalMedicines = 50; 
const totalPages = Math.ceil(totalMedicines / itemsPerPage); 

const BarChartExample = ({ data }) => {
    return (
      <ResponsiveContainer width="100%" height={300}>
        <BarChart
          data={data}
          margin={{ top: 20, right: 30, left: 20, bottom: 5 }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Bar dataKey="pv" fill="#8884d8" />
          <Bar dataKey="uv" fill="#82ca9d" />
        </BarChart>
      </ResponsiveContainer>
    );
  };
function MedicineListItem({ name, dosageForm, stock, average_stock, id }) {
    return (
        <tr className="medicine-list-item">
            <td>{name}</td>
            <td>{dosageForm}</td>
            <td>{stock}</td>
            <td>{average_stock}</td>
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
                    <th>Stock in Qty</th>
                    <th>Ideal Stock</th>
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


export default function Inventory() {
    const [currentPage, setCurrentPage] = useState(1);

    const [listType, setListType] = React.useState('Medicines');
    const [medicines, setMedicines] = useState([]);
    const [barChartData, setBarChartData] = useState([]);

    const fetchData = async (page) => {
        try {
            const response = await axios.get(`http://localhost:8080/drugs/getBadDrugsInPage?page=${page}`);
            setMedicines(response.data);
            console.log('Data fetched:', response.data);
            console.log('Medicines:', medicines);
            const chartData = response.data.map(medicine => ({
                name: medicine.name,
                pv: medicine.stock, 
                uv: medicine.average_stock, 
            }));
            setBarChartData(chartData);
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
                    <div className="tabel">
                    {listType === 'Medicines' ? <MedicinesTable medicines={medicines} /> : null}
                    </div>
                    <div className="chart-container">
                        <h2>Medicines Bar Chart</h2>
                        <BarChartExample data={barChartData} />
                    </div>
                </section>
            </main>
        </>
    );
}
