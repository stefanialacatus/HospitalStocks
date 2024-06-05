import * as React from 'react';
import { useState } from 'react';
import './Inventory.css';

function SearchBar() {
    return (
      <div className="search-bar-container">
        <div className="search-input-container">
          <input type="text" placeholder="Search Medicine Inventory.." className="search-input" />
          <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/3bf32ee1d2c19add8b4a5c84df63fc01d3fddfae65e52490b0e402e5b6b519e5?apiKey=92503cb420154d6c95f36ba59a7a554b&" alt="Search Icon" className="search-icon" />
        </div>
      </div>
    );
  }
  
  

function MedicineListItem({ name, dosageForm, illness, stock, id }) {
    return (
      <tr className="medicine-list-item">
        <td>{name}</td>
        <td>{dosageForm}</td>
        <td>{illness}</td>
        <td>{stock}</td>
        <td className="view-details">
          <span>View Full Detail</span>
          <img 
            src="https://cdn.builder.io/api/v1/image/assets/TEMP/756c414269a215514297154b085d87a50de4ddb17649baf6c9a306c311aeb3fd?apiKey=92503cb420154d6c95f36ba59a7a554b&" 
            alt="Detail Icon" 
            className="detail-icon" 
          />
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
            <th>Action</th>
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
  
  
  

function Pagination() {
  return (
    <div className="pagination-container">
      <span>Showing 1 - 8 results of 298</span>
      <div className="pagination-controls">
        <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/9c5603b1ade53a12461fcc579f39c2e84d89bb535241d15a38ce46cac6ef1981?apiKey=92503cb420154d6c95f36ba59a7a554b&" alt="Previous Page" className="pagination-icon" />
        <span>Page 01</span>
        <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/57c9dc11e65aa7ca10e40536198477735aa6783a5c6b26a8719815fc6b83eab2?apiKey=92503cb420154d6c95f36ba59a7a554b&" alt="Next Page" className="pagination-icon" />
        <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/73a4e29bb1c974471e9660a44354779d6ed7bceb50229ce1628cc2ba4ef68621?apiKey=92503cb420154d6c95f36ba59a7a554b&" alt="Last Page" className="pagination-icon" />
      </div>
    </div>
  );
}

function AddNewEntryPopup({ onClose }) {
  const [formData, setFormData] = React.useState({ drugName: '', supplierName: '', quantity: '', date: '' });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = () => {
    // Add functionality to handle form submission
    console.log('Form submitted:', formData);
    fetch('/drugentries/add', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(formData),
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to add entry');
        }
        // Add logic here if needed
        console.log('Entry added successfully');
        onClose(); // Close the popup after successful submission
      })
      .catch(error => {
        console.error('Error adding entry:', error);
        // Handle error scenario if needed
      });
  
    onClose();
  };

  return (
    <div className="popup-overlay">
      <div className="add-new-entry-popup">
        <button className="close-button" onClick={onClose}>X</button>
        <h2>Add New Entry</h2>
        <input type="text" name="drugName" placeholder="Drug Name" value={formData.drugName} onChange={handleChange} />
        <input type="text" name="supplierName" placeholder="Supplier Name" value={formData.supplierName} onChange={handleChange} />
        <input type="text" name="quantity" placeholder="Quantity" value={formData.quantity} onChange={handleChange} />
        <input type="date" name="date" placeholder="Date" value={formData.date} onChange={handleChange} />
        <button onClick={handleSubmit}>Add Entry</button>
      </div>
    </div>
  );
}


export default function Inventory() {
  const [showAddNewEntryPopup, setShowAddNewEntryPopup] = useState(false);
  const [listType, setListType] = React.useState('Medicines');
  const toggleListType = () => {
    setListType(prevType => prevType === 'Medicines' ? 'Illness Groups' : 'Medicines');
  };

  const medicines = [
    { name: "Augmentin 625 Duo Tablet", dosageForm: "tablet", illness: "Generic Medicine", stock: 350, id: "D06ID232435450" },
    { name: "Azithral 500 Tablet", dosageForm: "injection", illness: "Generic Medicine", stock: 20, id: "D06ID232435455" },
    { name: "Ascoril LS Syrup", dosageForm: "inhaler", illness: "Diabetes", stock: 85, id: "D06ID232435456" },
    { name: "Azee 500 Tablet", dosageForm: "D06ID232435450", illness: "Generic Medicine", stock: 75, id: "D06ID232435457" },
    { name: "Allegra 120mg Tablet", dosageForm: "D06ID232435455", illness: "Diabetes", stock: 44, id: "D06ID232435458" },
    { name: "Alex Syrup", dosageForm: "D06ID232435455", illness: "Generic Medicine", stock: 65, id: "D06ID232435455" },
    { name: "Amoxyclav 625 Tablet", dosageForm: "D06ID232435455", illness: "Generic Medicine", stock: 150, id: "D06ID232435457" },
    { name: "Avil 25 Tablet", dosageForm: "D06ID232435455", illness: "Generic Medicine", stock: 270, id: "D06ID232435458" },
  ];

  const handleAddNewEntryClick = () => {
    setShowAddNewEntryPopup(true);
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
          <div className="header-right">
            <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/7e038ee69d5e007292339b72556c5ec5e57b20ff249d1a7e300aae51ba5b3bf2?apiKey=166a782ca6344aad902f23b81529b6b9&" alt="Notifications" className="header-notification-icon" />
            <div className="header-notification-text">Notifications</div>
            <div className="header-notification-count">01</div>
          </div>
        </div>
      </header>
      <main className="main-cont">
        <section className="introducere">
          <div className="title-section">
            <h1 className="title">
              Inventory
              <span className="subtitle">{` > List of ${listType}`}</span>
            </h1>
            <div className="buttons">
              <button className="toggle-button" onClick={toggleListType}>
                {listType === 'Medicines' ? 'View Illness Groups' : 'View Medicines'}
              </button>
              <button className="add-button" onClick={handleAddNewEntryClick}>
                {listType === 'Medicines' ? 'New Entry' : 'New Illness Group'}
              </button>
            </div>
          </div>
          {listType === 'Medicines' ? <SearchBar /> : null}
          {listType === 'Medicines' ? <MedicinesTable medicines={medicines} /> : null}
          <Pagination />
        </section>
      </main>
      {showAddNewEntryPopup && <AddNewEntryPopup onClose={() => setShowAddNewEntryPopup(false)} />}
    </>
  );
}
