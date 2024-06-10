import * as React from 'react';
import './Patients.css';
import { useState, useEffect } from 'react';
import axios from 'axios';

const itemsPerPage = 8;


function SearchBar() {
    return (
        <div className="search-bar-container">
            <div className="search-input-container">
                <input type="text" placeholder="Search Patient .." className="search-input" />
                <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/3bf32ee1d2c19add8b4a5c84df63fc01d3fddfae65e52490b0e402e5b6b519e5?apiKey=92503cb420154d6c95f36ba59a7a554b&" alt="Search Icon" className="search-icon" />
            </div>
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
        axios.delete(`http://localhost:8080/patients/deletePatient/${firstName}/${lastName}`)
            .then((response) => {
                alert('Patient removed successfully');
                closeRemoveModal();
                /*window.location.reload();*/
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
    const startIndex = (currentPage - 1) * itemsPerPage + 1;
    const endIndex = Math.min(currentPage * itemsPerPage, totalPatients);

    const displayedPatients = patients.slice(startIndex, endIndex);

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

        axios.post('http://localhost:8080/patients/addPatient', requestPayload)
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
            const response = await axios.get(`http://localhost:8080/patients/getPatientsInPage?page=${page}`);
            setPatients(response.data);
            setTotalPatients(response.data.length);
            setTotalPages(Math.ceil(totalPatients / itemsPerPage));
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
    }, [currentPage]);

    useEffect(() => {
        fetchData(currentPage);
    }, [currentPage]);

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
                    <PatientsTable 
                        patients={patients} 
                        currentPage={currentPage}
                        itemsPerPage={itemsPerPage}
                        totalPatients={totalPatients}
                    
                    />
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
