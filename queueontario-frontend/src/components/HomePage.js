import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import logo from '../assets/logo.JPG';
import '../styles/HomePage.css';
import Header from './Header';
import Footer from './Footer';

const HomePage = ({ userId }) => {
  const [selectedLocation, setSelectedLocation] = useState('');
  const navigate = useNavigate();

  const handleLocationChange = (event) => {
    setSelectedLocation(event.target.value);
  };

  const handleJoinWaitlist = () => {
    // Navigate to the ServiceCentersList component and pass the selected city
    navigate('/servicecenters', { state: { selectedLocation } });
  };

  return (
    <div>
      <Header />
      <div className="main-content">
        <img src={logo} alt="Logo" />
        <div className="location-select">
          <label htmlFor="locations">Available Locations:</label>
          <select id="locations" value={selectedLocation} onChange={handleLocationChange}>
            <option value="">-- Select Location --</option>
            <option value="pickering">Pickering</option>
            <option value="markham">Markham</option>
            <option value="scarborough">Scarborough</option>
            <option value="northyork">North York</option>
            <option value="mississauga">Mississauga</option>
            <option value="brampton">Brampton</option>
            <option value="milton">Milton</option>
            <option value="richmondhill">Richmond Hill</option>
            <option value="ajax">Ajax</option>
          </select>
          <button 
            onClick={handleJoinWaitlist} 
            className="circular-button" 
            disabled={!selectedLocation}
          >
            Join Waitlist
          </button>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default HomePage;
