// src/pages/InterviewLobbyPage.jsx
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import apiClient from '../api/apiClient';
import Header from '../components/Header';
import Navigation from '../components/Navigation';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';
import EmptyState from '../components/EmptyState';
import '../pages/MainPage.css';

function InterviewLobbyPage() {
  const [interviewSets, setInterviewSets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchInterviewSets = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await apiClient.get('/api/interview-sets');
      setInterviewSets(response.data);
    } catch (error) {
      console.error('면접 세트 목록을 불러오는 데 실패했습니다:', error);
      setError('면접 세트 목록을 불러오는 데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchInterviewSets();
  }, []);

  return (
    <div className="main-page">
      <Header />
      <Navigation />
      
      <div className="main-container">
        <div className="content-area">
          <div className="page-header">
            <h1 className="page-title">면접 연습</h1>
          </div>
          <p style={{marginBottom: '30px', color: 'var(--text-secondary-color)'}}>
            응시하고 싶은 면접을 선택하여 연습을 시작해보세요.
          </p>
          
          {loading && <Loading message="면접 세트를 불러오는 중..." />}
          
          {error && (
            <ErrorMessage 
              message={error} 
              onRetry={fetchInterviewSets}
              retryText="다시 불러오기"
            />
          )}
          
          {!loading && !error && interviewSets.length === 0 && (
            <EmptyState
              icon="🎯"
              title="아직 면접 세트가 없습니다"
              description="면접 세트가 준비되면 여기에 표시됩니다."
            />
          )}
          
          {!loading && !error && interviewSets.length > 0 && (
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
          )}
        </div>
      </div>
    </div>
  );
}
export default InterviewLobbyPage;