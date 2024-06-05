import { BrowserRouter, Routes, Route } from "react-router-dom";
import Dashboard from "./Pages/Dashboard";
import Inventory from "./Pages/Inventory";
import Patients from "./Pages/Patients";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route index element={<Dashboard />} />
        <Route path="/inventory" element={<Inventory />} />
        <Route path="/patients" element={<Patients/>} />
      </Routes>
    </BrowserRouter>
  );
}