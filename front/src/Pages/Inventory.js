import React, { useState, useEffect } from 'react';
import './Inventory.css';
import axios from 'axios';

const itemsPerPage = 8;
const totalMedicines = 50; 
const totalPages = Math.ceil(totalMedicines / itemsPerPage); 

function SearchBar({ setMedicines }) {
    const [searchQuery, setSearchQuery] = useState("");
    const [searchResults, setSearchResults] = useState([]);
    const [showDropdown, setShowDropdown] = useState(false);

    const handleSearch = async () => {
        try {
            const response = await fetch(`http://localhost:8080/drugs/findByName?name=${searchQuery}`);
            if (!response.ok) {
                throw new Error("Failed to fetch search results");
            }
            const data = await response.json();
            setMedicines(data);
        } catch (error) {
            console.error("Error searching drugs:", error);
        }
    };

    const handleChange = async (e) => {
        const searchTerm = e.target.value;
        setSearchQuery(searchTerm);

        if (searchTerm) {
            try {
                const response = await fetch(`http://localhost:8080/drugs/search?name=${searchTerm}`);
                if (!response.ok) {
                    throw new Error("Failed to fetch search results");
                }
                const data = await response.json();
                setSearchResults(data);
                setShowDropdown(true);
            } catch (error) {
                console.error("Error searching drugs:", error);
            }
        } else {
            setSearchResults([]);
            setShowDropdown(false);
        }
    };
    const [selectedOption, setSelectedOption] = useState('filter');

    const handleOptionChange = (event) => {
        const selectedValue = event.target.value;
        setSelectedOption(selectedValue);
        sendDataToBackend(selectedValue);
    };
    const sendDataToBackend = (selectedValue) => {
        fetch('http://localhost:8080/drugs/filter', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(selectedValue)
        })
        .then(() => {
            window.location.reload();
        })
        .catch(error => {
            console.error('Error sending data to backend:', error);
        });
    };
    const handleOptionClick = (option) => {
        setSearchQuery(option); 
        setShowDropdown(false);
    };

    return (
        <div className="search-bar-container">
            <div className="search-input-container">
                <input
                    type="text"
                    placeholder="Search Medicine Inventory.."
                    className="search-input"
                    value={searchQuery}
                    onChange={(e) => handleChange(e)}
                />
                <button onClick={handleSearch}>
                    <img
                        src="https://cdn.builder.io/api/v1/image/assets/TEMP/3bf32ee1d2c19add8b4a5c84df63fc01d3fddfae65e52490b0e402e5b6b519e5?apiKey=92503cb420154d6c95f36ba59a7a554b&"
                        alt="Search Icon"
                        className="search-icon"
                    />
                </button>
            </div>
            {showDropdown && (
                <div className="search-results-dropdown">
                    {searchResults.map((result) => (
                        <div
                            key={result.id}
                            onClick={() => handleOptionClick(result.name)}
                            className="search-result-item"
                        >
                            {result.name}
                        </div>
                    ))}
                </div>
            )}
            <select className="filter-drop" value={selectedOption} onChange={handleOptionChange}>
                <option value="0">Filter</option>
                <option value="1">A-Z</option>
                <option value="2">Z-A</option>
                <option value="3">Quantity</option>
            </select>
        </div>
    );
}
function MedicineListItem({ name, dosageForm, illness, stock, id, cost }) {
    return (
        <tr className="medicine-list-item">
            <td>{name}</td>
            <td>{dosageForm}</td>
            <td>{illness}</td>
            <td>{stock}</td>
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
            <span className='8-page'>Showing {startItem} - {endItem}</span>
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

function AddNewEntryPopup({ onClose }) {
    const [formData, setFormData] = React.useState({ drugName: '', supplierName: '', quantity: ''});
    const [error, setError] = useState('');
    const handleInput = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const requestPayload = {
            drugName: formData.drugName,
            supplierName: formData.supplierName,
            quantity: parseInt(formData.quantity, 10),
        };
        console.log('Submitting payload:', requestPayload);

        axios.post('http://localhost:8080/addEntry', requestPayload)
            .then((response) => {
                alert('Entry added successfully');
                onClose(); 
            })
            .catch((error) => {
                console.error('Error adding entry:', error);
                if (error.response && error.response.data) {
                    setError(error.response.data); 
                    alert('Insufficient budget. The entry failed.');
                } else {
                    setError('Failed to add entry'); 
                    alert(error.response.data);
                }
            });
    };

    return (
        <div className="popup-overlay">
            <div className="add-new-entry-popup">
                <button className="close-button" onClick={onClose}>X</button>
                <h2>Add New Entry</h2>
                <form onSubmit={handleSubmit}>
                    <input type="text" name="drugName" placeholder="Drug Name" value={formData.drugName} onChange={handleInput} required />
                    <input type="text" name="supplierName" placeholder="Supplier Name" value={formData.supplierName} onChange={handleInput} required />
                    <input type="number" name="quantity" placeholder="Quantity" value={formData.quantity} onChange={handleInput} required />
                    <button type="submit">Add Entry</button>
                </form>
            </div>
        </div>
    );
}

function ConsumptionPopup({ onClose }) {
    const [formData, setFormData] = React.useState({ drugName: '', supplierName: '', quantity: ''});
    const [error, setError] = useState('');
    const handleInput = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const requestPayload = {
            drugName: formData.drugName,
            quantity: parseInt(formData.quantity, 10),
        };
        console.log('Submitting payload:', requestPayload);

        axios.post('http://localhost:8080/consumeEntry', requestPayload)
            .then((response) => {
                alert('Consumption added successfully');
                onClose();
              })
              .catch((error) => {
                  console.error('Error adding consumption:', error);
                  if (error.response && error.response.data) {
                      setError(error.response.data); 
                      alert('Error??');
                  } else {
                      setError('Failed to add consumption'); 
                      alert(error.response.data);
                  }
              });
      };
  
      return (
          <div className="popup-overlay">
              <div className="add-new-entry-popup">
                  <button className="close-button" onClick={onClose}>X</button>
                  <h2>Add New Consumption</h2>
                  <form onSubmit={handleSubmit}>
                      <input type="text" name="drugName" placeholder="Drug Name" value={formData.drugName} onChange={handleInput} required />
                      <input type="number" name="quantity" placeholder="Quantity" value={formData.quantity} onChange={handleInput} required />
                      <button type="submit">Register Consumption</button>
                  </form>
              </div>
          </div>
      );
  }
  
  export default function Inventory() {
      const [totalMedicines, setTotalMedicines] = useState(0); 
      const [currentPage, setCurrentPage] = useState(1);
      const [searchQuery, setSearchQuery] = useState("");
      const [searchResults, setSearchResults] = useState([]);
      
      const [showAddNewEntryPopup, setShowAddNewEntryPopup] = useState(false);
      const [showConsumptionPopup, setShowConsumptionPopup] = useState(false);
      const [listType, setListType] = React.useState('Medicines');
      const [medicines, setMedicines] = useState([]);
      const fetchTotalMedicines = async () => {
            try {
                const response = await axios.get('http://localhost:8080/drugs/count');
                setTotalMedicines(response.data);
            } catch (error) {
                console.error('Error fetching total number of medicines:', error);
            }
        };
      const fetchData = async (page) => {
          try {
              const response = await axios.get(`http://localhost:8080/drugs/getDrugsInPage?page=${page}`);
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
  
      const handleSearch = async () => {
          try {
              const response = await fetch(`http://localhost:8080/drugs/findByName?name=${searchQuery}`);
              if (!response.ok) {
                  throw new Error("Failed to fetch search results");
              }
              const data = await response.json();
              setMedicines(data);
          } catch (error) {
              console.error("Error searching drugs:", error);
          }
      };
  
      const handleAddNewEntryClick = () => {
          setShowAddNewEntryPopup(true);
      };
  
      const handleConsumptionClick = () => {
          setShowConsumptionPopup(true);
      };

      const handleFileUpload = async (event) => {
        const file = event.target.files[0];
        const formData = new FormData();
        formData.append('file', file);

        try {
            const response = await axios.post('http://localhost:8080/importFile', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            alert('File uploaded successfully');
        } catch (error) {
            console.error('Error uploading file:', error);
            alert('Failed to upload file');
        }
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
                              Inventory
                              <span className="subtitle">{` > List of ${listType}`}</span>
                          </h1>
                          <div className="buttons">
                              <button className="add-button" onClick={handleAddNewEntryClick}>
                                  {listType === 'Medicines' ? 'New Entry' : 'New Illness Group'}
                              </button>
                              <button className="remove-button" onClick={handleConsumptionClick}>
                                  Consume Entry
                              </button>
                              <input 
                                  type="file" 
                                  id="fileUpload" 
                                  style={{ display: 'none' }} 
                                  onChange={handleFileUpload} 
                              />
                              <button className="file-button" onClick={() => document.getElementById('fileUpload').click()}>
                                  Upload Entry File
                              </button>
                          </div>
                      </div>
                      {listType === 'Medicines' ? <SearchBar setMedicines={setMedicines} /> : null}
                      <div className="table">
                      {listType === 'Medicines' ? <MedicinesTable medicines={medicines} /> : null}
                        </div>
                      <Pagination
                          currentPage={currentPage}
                          totalPages={totalPages}
                          onNextPage={handleNextPage}
                          onPreviousPage={handlePreviousPage}
                      />
                  </section>
              </main>
              {showAddNewEntryPopup && <AddNewEntryPopup onClose={() => setShowAddNewEntryPopup(false)} />}
              {showConsumptionPopup && <ConsumptionPopup onClose={() => setShowConsumptionPopup(false)} />}
          </>
      );
  }
  