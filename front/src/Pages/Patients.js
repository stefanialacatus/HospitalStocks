import * as React from 'react';
import './Patients.css';
import { useState, useEffect } from 'react';
import axios from 'axios';

const itemsPerPage = 8;
const totalPatients = 163;
const totalPages = Math.ceil(totalPatients / itemsPerPage);

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

function PatientList({ firstName, lastName, illness_name, id }) {
    const [showRemoveModal, setShowRemoveModal] = React.useState(false);

    const openRemoveModal = () => {
        setShowRemoveModal(true);
    };

    const closeRemoveModal = () => {
        setShowRemoveModal(false);
    };

    const handleRemovePatient = () => {
        // Functionality to remove the patient
        console.log(`Removing patient ${id}`);
        setShowRemoveModal(false);
    };

    return (
        <tr className="medicine-list-item">
            <td>{firstName}</td>
            <td>{lastName}</td>
            <td>{illness_name}</td>
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

function PatientsTable({ patients }) {
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
                {patients.map((patient) => (
                    <PatientList key={patient.id} {...patient} />
                ))}
            </tbody>
        </table>
    );
}

function Pagination({ currentPage, totalPages, onNextPage, onPreviousPage }) {
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
    const [patients, setPatients] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [showAddModal, setShowAddModal] = useState(false);
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
                fetchData(currentPage); // Refresh the list after adding a patient
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
            console.log('Data fetched:', response.data);
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
                    <div className="header-right">
                        <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/7e038ee69d5e007292339b72556c5ec5e57b20ff249d1a7e300aae51ba5b3bf2?apiKey=166a782ca6344aad902f23b81529b6b9&" alt="Notifications" className="header-notification-icon" />
                        <div className="header-notification-text">Notifications</div>
                    </div>
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
                    <PatientsTable patients={patients} />
                    <Pagination
                        currentPage={currentPage}
                        totalPages={totalPages}
                        onNextPage={handleNextPage}
                        onPreviousPage={handlePreviousPage}
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
