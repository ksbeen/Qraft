// src/pages/MyPracticeLogsPage.jsx
import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import apiClient from '../api/apiClient';

function MyPracticeLogsPage() {
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    // 로그인 상태 확인
    const token = localStorage.getItem('authToken');
    if (!token) {
      alert('로그인이 필요합니다.');
      navigate('/login');
      return;
    }

    const fetchMyLogs = async () => {
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
        }
      } finally {
        setLoading(false);
      }
    };
    fetchMyLogs();
  }, [navigate]);

  if (loading) return <div>기록을 불러오는 중입니다...</div>;

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">마이페이지 - 내 면접 기록</h1>
      </div>
      {logs.length === 0 ? (
        <p>아직 완료한 면접이 없습니다.</p>
      ) : (
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
  );
}
export default MyPracticeLogsPage;