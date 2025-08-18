// src/pages/InterviewLobbyPage.jsx

import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import apiClient from '../api/apiClient';

function InterviewLobbyPage() {
  const [interviewSets, setInterviewSets] = useState([]);

  useEffect(() => {
    const fetchInterviewSets = async () => {
      try {
        const response = await apiClient.get('/api/interview-sets');
        setInterviewSets(response.data);
      } catch (error) {
        console.error('면접 세트 목록을 불러오는 데 실패했습니다:', error);
      }
    };

    fetchInterviewSets();
  }, []);

  return (
    <div>
      <h2>면접 세트 선택</h2>
      <p>응시하고 싶은 면접을 선택해주세요.</p>
      <div>
        {interviewSets.map((set) => (
          <Link key={set.id} to={`/interview/${set.id}`}>
            <div style={{ border: '1px solid #ccc', margin: '10px', padding: '10px', cursor: 'pointer' }}>
              <h3>{set.name}</h3>
              <p>직무: {set.jobType}</p>
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
}

export default InterviewLobbyPage;