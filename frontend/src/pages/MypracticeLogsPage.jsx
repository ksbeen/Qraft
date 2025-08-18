// src/pages/MyPracticeLogsPage.jsx

import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import apiClient from '../api/apiClient';

function MyPracticeLogsPage() {
  // 면접 기록 목록을 저장할 state
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(true);

  // 컴포넌트가 처음 렌더링될 때, 자신의 면접 기록을 불러옵니다.
  useEffect(() => {
    const fetchMyLogs = async () => {
      try {
        // '/api/practice-logs/my' API를 호출합니다.
        const response = await apiClient.get('/api/practice-logs/my');
        setLogs(response.data);
      } catch (error) {
        console.error('면접 기록을 불러오는 데 실패했습니다:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchMyLogs();
  }, []); // 빈 배열을 전달하여 한 번만 실행되도록 합니다.

  // 로딩 중일 때 보여줄 UI
  if (loading) {
    return <div>기록을 불러오는 중입니다...</div>;
  }

  return (
    <div>
      <h2>내 면접 기록</h2>
      {/* logs 배열의 길이를 확인하여, 기록이 없을 때와 있을 때 다른 UI를 보여줍니다. */}
      {logs.length === 0 ? (
        <p>아직 완료한 면접이 없습니다.</p>
      ) : (
        <div>
          {/* logs 배열을 순회하며 각 기록을 화면에 표시합니다. */}
          {logs.map((log) => (
            // 각 기록을 클릭하면 나중에 만들 상세 보기 페이지로 이동합니다.
            <Link key={log.id} to={`/practice-logs/${log.id}`}>
               <div style={{ border: '1px solid #ccc', margin: '10px', padding: '10px', cursor: 'pointer' }}>
                <h3>{log.interviewSetName}</h3>
                <p>연습 일시: {new Date(log.createdAt).toLocaleString()}</p>
              </div>
            </Link>
          ))}
        </div>
      )}
    </div>
  );
}

export default MyPracticeLogsPage;