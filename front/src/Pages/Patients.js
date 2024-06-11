import * as React from 'react';
import './Patients.css';
import { useState, useEffect } from 'react';
import axios from 'axios';

const itemsPerPage = 8;

function SearchBar({ setPatients, setTotalPatients, setTotalPages, setCurrentPage }) {
    const [searchQuery, setSearchQuery] = useState("");
    const [selectedOption, setSelectedOption] = useState('filter');
    const [searchResults, setSearchResults] = useState([]);

    const handleSearch = async () => {
        try {
            const token = localStorage.getItem('token');
            const response = await fetch(`http://localhost:8080/patients/searchByName?name=${searchQuery}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            
            });
            if (!response.ok) {
                throw new Error("Failed to fetch search results");
            }
            const data = await response.json();
            setSearchResults(data); 
            setPatients(data); 
            setTotalPatients(data.length); 
            setTotalPages(Math.ceil(data.length / itemsPerPage)); 
            setCurrentPage(1); 
        } catch (error) {
            console.error("Error searching patients:", error);
        }
    };

    const handleChange = (event) => {
        setSearchQuery(event.target.value);
    };

    const handleOptionChange = (event) => {
        const selectedValue = event.target.value;
        setSelectedOption(selectedValue);
        sendDataToBackend(selectedValue);
        console.log('Selected value:', selectedValue);
    };

    const sendDataToBackend = async (selectedValue) => {
        try {
            const token = localStorage.getItem('token');
            const response = await fetch('http://localhost:8080/patients/filter', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify({ selectedValue })
            });
            if (!response.ok) {
                throw new Error('Failed to fetch filtered patients');
            }
            const data = await response.json();
            setPatients(data);
            console.log('Filtered patients:', data);
        } catch (error) {
            console.error('Error filtering patients:', error);
        }
    };
    return (
        <div className="search-bar-container">
            <div className="search-input-container">
                <input
                    type="text"
                    placeholder="Search Medicine Inventory.."
                    className="search-input"
                    value={searchQuery}
                    onChange={handleChange}
                />
                <button onClick={handleSearch}>
                    <img
                        src="https://cdn.builder.io/api/v1/image/assets/TEMP/3bf32ee1d2c19add8b4a5c84df63fc01d3fddfae65e52490b0e402e5b6b519e5?apiKey=92503cb420154d6c95f36ba59a7a554b&"
                        alt="Search Icon"
                        className="search-icon"
                    />
                </button>
            </div>
            <div>
                {searchResults.map((result) => (
                    <div key={result.id}>{result.name}</div>
                ))}
            </div>
            <select className="filter-drop" value={selectedOption} onChange={handleOptionChange}>
                <option value="filter">Filter</option>
                <option value="az">A-Z</option>
                <option value="za">Z-A</option>
            </select>
        </div>
    );

}

function PatientList({ firstName, lastName, illnessName, id }) {
    const [showRemoveModal, setShowRemoveModal] = React.useState(false);

    const openRemoveModal = () => {
        setShowRemoveModal(true);
    };

    const closeRemoveModal = () => {
        setShowRemoveModal(false);
    };

    const handleRemovePatient = () => {
            const token = localStorage.getItem('token');
    
            axios.delete(`http://localhost:8080/patients/deletePatient/${firstName}/${lastName}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })
            .then((response) => {
                alert('Patient removed successfully');
                closeRemoveModal();
                window.location.reload();
            })
            .catch((error) => {
                console.error('Error removing patient:', error);
                alert('Failed to remove patient');
            });
    };


    return (
        <tr className="medicine-list-item">
            <td>{firstName}</td>
            <td>{lastName}</td>
            <td>{illnessName}</td>
            <td className="view-details">
                <span onClick={openRemoveModal}>Remove Patient</span>
                <img
                    src="https://cdn.builder.io/api/v1/image/assets/TEMP/756c414269a215514297154b085d87a50de4ddb17649baf6c9a306c311aeb3fd?apiKey=92503cb420154d6c95f36ba59a7a554b&"
                    alt="Detail Icon"
                    className="detail-icon"
                />
                {showRemoveModal && (
                    <div className="modal">
                        <div className="modal-content">
                            <span className="close" onClick={closeRemoveModal}>&times;</span>
                            <h2>Remove Patient</h2>
                            <p>Please confirm removing patient:</p>
                            <p>{`${firstName} ${lastName}`}</p>
                            <button onClick={handleRemovePatient}>Remove Patient</button>
                        </div>
                    </div>
                )}
            </td>
        </tr>
    );
}

function PatientsTable({ patients, currentPage, itemsPerPage, totalPatients }) {
    let displayedPatients;
    if (patients.length === 1) {
        displayedPatients = patients;
    } else {
        const startIndex = (currentPage - 1) * itemsPerPage;
        const endIndex = Math.min(currentPage * itemsPerPage, totalPatients);
        displayedPatients = patients.slice(startIndex, endIndex);
    }

    return (
        <table className="medicines-table">
            <thead>
                <tr>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Illness</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                {displayedPatients.map((patient) => (
                    <PatientList key={patient.id} {...patient} />
                ))}
            </tbody>
        </table>
    );
}


function Pagination({ currentPage, totalPages, onNextPage, onPreviousPage, totalPatients }) {
    const startItem = (currentPage - 1) * itemsPerPage + 1;
    const endItem = Math.min(currentPage * itemsPerPage, totalPatients);
    return (
        <div className="pagination-container">
            <span>Showing {startItem} - {endItem} results of {totalPatients}</span>
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


export default function Patients() {
    const [filteredPatients, setFilteredPatients] = useState([]);
    const [currentFilter, setCurrentFilter] = useState(null);
    const [TotalPages, setTotalPages] = useState(null);
    const [patients, setPatients] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [showAddModal, setShowAddModal] = useState(false);
    const [totalPatients, setTotalPatients] = useState(1);
    const [newPatient, setNewPatient] = useState({
        firstName: "",
        lastName: "",
        illness: ""
    });

    const closeAddModal = () => {
        setShowAddModal(false);
    };

    const openAddModal = () => {
        setShowAddModal(true);
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNewPatient({ ...newPatient, [name]: value });
    };

    const handleAddPatient = (e) => {
        e.preventDefault();
        const requestPayload = {
            firstName: newPatient.firstName,
            lastName: newPatient.lastName,
            illnessName: newPatient.illness,
        };
        console.log('Submitting payload:', requestPayload);
        const token = localStorage.getItem('token');
        axios.post('http://localhost:8080/patients/addPatient', requestPayload, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then((response) => {
                alert('Patient added successfully');
                closeAddModal();
                fetchData(currentPage);
            })
            .catch((error) => {
                console.error('Error adding patient:', error);
                alert('Failed to add patient');
            });
    };

    const fetchData = async (page) => {
        try {
            const token = localStorage.getItem('token');
            const response = await axios.get(`http://localhost:8080/patients/getPatientsInPage?page=${page}`,
                {
                    headers: {
                         'Authorization': `Bearer ${token}`

                    }
                }

            );
            setPatients(response.data);
            setTotalPatients(response.data.length);
            setTotalPages(Math.ceil(response.data.length / itemsPerPage));
            if (currentFilter) {
                applyFilter(currentFilter);
            }
            console.log('Total pages:', TotalPages);
            console.log('Total patients:', totalPatients);
            console.log('Data fetched:', response.data);

        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    useEffect(() => {
        const interval = setInterval(() => {
            fetchData(currentPage);
        }, 60000);

        return () => clearInterval(interval);
    }, [currentFilter]);

    useEffect(() => {
        fetchData(currentPage);
    }, [currentFilter]);

    const applyFilter = async (filter) => {
        try {
            const token = localStorage.getItem('token');
            const response = await axios.post('http://localhost:8080/patients/filter', { filter }, 
            {
                headers: {
                    'Authorization': `Bearer ${token}`

               }
            }

            );
            setFilteredPatients(response.data);
            setCurrentFilter(filter);
        } catch (error) {
            console.error('Error applying filter:', error);
        }
    };

    const handleNextPage = () => {
        setCurrentPage((prevPage) => Math.min(prevPage + 1, TotalPages));
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
                            Patients
                            <span className="subtitle">{` > List of Patients`}</span>
                        </h1>
                        <div className="buttons">
                            <button className="add-button" onClick={openAddModal}>
                                Add Patient
                            </button>
                        </div>
                    </div>
                    <SearchBar 
                        setPatients={setPatients} 
                        setTotalPatients={setTotalPatients}
                        setTotalPages={setTotalPages}
                        setCurrentPage={setCurrentPage}
                     />
                    <div className="table">
                    <PatientsTable 
                        patients={currentFilter? filteredPatients : patients} 
                        currentPage={currentPage}
                        itemsPerPage={itemsPerPage}
                        totalPatients={totalPatients}
                    
                    />
                    </div>
                    <Pagination
                        currentPage={currentPage}
                        totalPages={TotalPages}
                        onNextPage={handleNextPage}
                        onPreviousPage={handlePreviousPage}
                        totalPatients={totalPatients}
                    />
                </section>
                {showAddModal && (
                    <div className="modal">
                        <div className="modal-content">
                            <span className="close" onClick={closeAddModal}>×</span>
                            <h2>Add New Patient</h2>
                            <input type="text" name="firstName" placeholder="First Name" value={newPatient.firstName} onChange={handleInputChange} />
                            <input type="text" name="lastName" placeholder="Last Name" value={newPatient.lastName} onChange={handleInputChange} />
                            <input type="text" name="illness" placeholder="Illness" value={newPatient.illness} onChange={handleInputChange} />
                            <button onClick={handleAddPatient}>Add Patient</button>
                        </div>
                    </div>
                )}
            </main>
        </>
    );
}