import React, { useState } from 'react';
import '../styles/About.css'; 
import Footer from './Footer';
import Header from './Header';

const Report = () => {
  const [name, setName] = useState('');
  const [issue, setIssue] = useState('');
  const [comments, setComments] = useState('');
  const [email, setEmail] = useState(''); 

  const handleSubmit = (event) => {
    event.preventDefault();
    //handle submission
    const reportData = {
      name,
      issue,
      comments,
      email, 
    };

    console.log('Report submitted:', reportData);

    setName('');
    setIssue('');
    setComments('');
    setEmail(''); 
  };

  return (
    <div className="report-page">
        <Header />
      <h1>Report an Issue</h1>
      <form onSubmit={handleSubmit} className="report-form">
        <div className="form-group">
          <label htmlFor="name">Name:</label>
          <input
            type="text"
            id="name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="issue">Select Issue:</label>
          <select
            id="issue"
            value={issue}
            onChange={(e) => setIssue(e.target.value)}
            required
          >
            <option value="">-- Select Issue --</option>
            <option value="long_wait_time">Long Wait Time</option>
            <option value="technical_issue">Technical Issue</option>
            <option value="service_quality">Service Quality</option>
            <option value="location_issue">Location Issue</option>
            <option value="other">Other</option>
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="comments">Comments:</label>
          <textarea
            id="comments"
            value={comments}
            onChange={(e) => setComments(e.target.value)}
            required
            rows="4"
          />
        </div>

        <div className="form-group">
          <label htmlFor="email">Email (optional):</label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </div>

        <button type="submit" className="submit-button">Submit Report</button>
      </form>
      <Footer />
    </div>
  );
};

export default Report;