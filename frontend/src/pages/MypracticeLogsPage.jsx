// src/pages/MyPracticeLogsPage.jsx
import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import apiClient from '../api/apiClient';
import Header from '../components/Header';
import Navigation from '../components/Navigation';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';
import EmptyState from '../components/EmptyState';
import '../pages/MainPage.css';

function MyPracticeLogsPage() {
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const fetchMyLogs = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await apiClient.get('/api/practice-logs/my');
      setLogs(response.data);
    } catch (error) {
      console.error('면접 기록을 불러오는 데 실패했습니다:', error);
      // 401 Unauthorized 에러인 경우 로그인 페이지로 리다이렉트
      if (error.response && error.response.status === 401) {
        localStorage.removeItem('authToken');
        alert('로그인이 만료되었습니다. 다시 로그인해주세요.');
        navigate('/login');
        return;
      }
      setError('면접 기록을 불러오는 데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    // 로그인 상태 확인
    const token = localStorage.getItem('authToken');
    if (!token) {
      alert('로그인이 필요합니다.');
      navigate('/login');
      return;
    }

    fetchMyLogs();
  }, [navigate]);

  if (loading) return (
    <div className="main-page">
      <Header />
      <Navigation />
      <div className="main-container">
        <div className="content-area">
          <Loading message="면접 기록을 불러오는 중..." />
        </div>
      </div>
    </div>
  );

  return (
    <div className="main-page">
      <Header />
      <Navigation />
      
      <div className="main-container">
        <div className="content-area">
          <div className="page-header">
            <h1 className="page-title">마이페이지 - 내 면접 기록</h1>
          </div>
          
          {error && (
            <ErrorMessage 
              message={error} 
              onRetry={fetchMyLogs}
              retryText="다시 불러오기"
            />
          )}
          
          {!error && logs.length === 0 && (
            <EmptyState
              icon="🎯"
              title="아직 완료한 면접이 없습니다"
              description="면접 연습을 시작해보세요!"
              actionText="면접 연습하기"
              actionLink="/interview"
            />
          )}
          
          {!error && logs.length > 0 && (
            <div className="card-grid">
              {logs.map((log) => (
                <Link key={log.id} to={`/practice-logs/${log.id}`}>
                  <div className="interview-card">
                    <h3 className="interview-card-name">{log.interviewSetName}</h3>
                    <p className="interview-card-meta">
                      연습 일시: {new Date(log.createdAt).toLocaleString()}
                    </p>
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
export default MyPracticeLogsPage;