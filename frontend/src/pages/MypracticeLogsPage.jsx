// src/pages/MyPracticeLogsPage.jsx
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import apiClient from '../api/apiClient';

function MyPracticeLogsPage() {
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMyLogs = async () => {
      try {
        const response = await apiClient.get('/api/practice-logs/my');
        setLogs(response.data);
      } catch (error) {
        console.error('면접 기록을 불러오는 데 실패했습니다:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchMyLogs();
  }, []);

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