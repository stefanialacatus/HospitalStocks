import React from 'react';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Dashboard from "./Pages/Dashboard";
import Inventory from "./Pages/Inventory";
import Patients from "./Pages/Patients";
import Statistic from "./Pages/Statistic";
import Login from "./Pages/Login";
import Signup from "./Pages/Register";
import { AuthProvider } from './context/AuthContext';

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route index element={<Login />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/inventory" element={<Inventory />} />
          <Route path="/patients" element={<Patients/>} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/statistics" element={<Statistic />} />
          <Route path="*" element={<h1>Not Found</h1>} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
