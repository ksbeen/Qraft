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
      <div className="page-header">
        <h1 className="page-title">면접 연습</h1>
      </div>
      <p style={{marginBottom: '30px', color: 'var(--text-secondary-color)'}}>
        응시하고 싶은 면접을 선택하여 연습을 시작해보세요.
      </p>
      <div className="card-grid">
        {interviewSets.map((set) => (
          <Link key={set.id} to={`/interview/${set.id}`}>
            <div className="interview-card">
              <h3 className="interview-card-name">{set.name}</h3>
              <p className="interview-card-meta">직무: {set.jobType}</p>
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
}
export default InterviewLobbyPage;