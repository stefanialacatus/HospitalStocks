import { BrowserRouter, Routes, Route } from "react-router-dom";
import Dashboard from "./Pages/Dashboard";
import Inventory from "./Pages/Inventory";
import Patients from "./Pages/Patients";
import Hospital from "./Pages/Hospital";
import Statistic from "./Pages/Statistic";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route index element={<Dashboard />} />
        <Route path="/inventory" element={<Inventory />} />
        <Route path="/patients" element={<Patients/>} />
        <Route path="/patients" element={<Patients />} />
        <Route path="/myhospital" element={<Hospital />} />
        <Route path="/statistics" element={<Statistic />} />
        <Route path="*" element={<h1>Not Found</h1>} />

      </Routes>
    </BrowserRouter>
  );
}