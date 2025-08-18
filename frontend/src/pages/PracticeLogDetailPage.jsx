// src/pages/PracticeLogDetailPage.jsx

import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import apiClient from '../api/apiClient';

function PracticeLogDetailPage() {
  const { id } = useParams();
  const [log, setLog] = useState(null);
  const [loading, setLoading] = useState(true);

  // --- 피드백 관련 State 추가 ---
  // 이전에 작성된 피드백 목록을 저장할 state
  const [feedbacks, setFeedbacks] = useState([]);
  // 새로 작성하는 메모 내용을 저장할 state
  const [newMemo, setNewMemo] = useState('');

  // --- 데이터 불러오기 ---
  useEffect(() => {
    // 면접 기록 상세 정보와 피드백 목록을 모두 불러오는 함수
    const fetchLogDetails = async () => {
      try {
        // 1. 면접 기록 상세 정보 불러오기 (기존 로직)
        const logResponse = await apiClient.get(`/api/practice-logs/${id}`);
        setLog(logResponse.data);

        // 2. 이 기록에 달린 피드백 목록 불러오기 (새로운 로직)
        const feedbackResponse = await apiClient.get(`/api/feedback/${id}`);
        setFeedbacks(feedbackResponse.data);

      } catch (error) {
        console.error('데이터를 불러오는 데 실패했습니다:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchLogDetails();
  }, [id]); // id가 바뀔 때마다 다시 실행

  // --- 이벤트 핸들러 ---
  // 피드백 제출 폼(form)을 처리하는 함수
  const handleFeedbackSubmit = async (event) => {
    event.preventDefault(); // 폼 제출 시 기본 동작(새로고침) 방지

    // 요청에 보낼 데이터 DTO
    const requestData = {
      practiceLogId: id,
      videoTimestamp: 0, // 지금은 영상 시간과 연동하지 않고 0으로 고정
      memo: newMemo,
    };

    try {
      // 피드백 생성 API 호출
      const response = await apiClient.post('/api/feedback', requestData);
      // 성공 시, 반환된 새 피드백을 기존 feedbacks 목록에 추가하여 화면을 업데이트
      setFeedbacks([...feedbacks, response.data]);
      // 입력창 초기화
      setNewMemo('');
    } catch (error) {
      console.error('피드백 저장에 실패했습니다:', error);
      alert('피드백 저장에 실패했습니다.');
    }
  };


  // --- 렌더링 로직 ---
  if (loading) {
    return <div>기록을 불러오는 중입니다...</div>;
  }

  if (!log) {
    return <div>해당 면접 기록을 찾을 수 없습니다.</div>;
  }

  const videoFullUrl = `${apiClient.defaults.baseURL}/uploads/${log.videoUrl}`;

  return (
    <div>
      <h2>{log.interviewSetName}</h2>
      <p>연습 일시: {new Date(log.createdAt).toLocaleString()}</p>
      <hr />
      
      <video src={videoFullUrl} controls width="600" style={{ backgroundColor: 'black' }}>
        브라우저가 비디오 태그를 지원하지 않습니다.
      </video>
      
      <hr />

      {/* ▼▼▼▼▼ 피드백 기능 UI 추가 ▼▼▼▼▼ */}
      <div>
        <h3>셀프 피드백</h3>
        
        {/* 새로운 피드백을 작성하는 폼 */}
        <form onSubmit={handleFeedbackSubmit}>
          <textarea
            value={newMemo}
            onChange={(e) => setNewMemo(e.target.value)}
            placeholder="여기에 피드백을 작성하세요..."
            rows="4"
            style={{ width: '100%', boxSizing: 'border-box' }}
          />
          <button type="submit">메모 추가</button>
        </form>

        {/* 이전에 작성된 피드백 목록을 보여주는 영역 */}
        <div style={{ marginTop: '20px' }}>
          <h4>작성된 메모</h4>
          {feedbacks.length > 0 ? (
            <ul>
              {feedbacks.map(fb => (
                <li key={fb.id}>{fb.memo}</li>
              ))}
            </ul>
          ) : (
            <p>아직 작성된 메모가 없습니다.</p>
          )}
        </div>
      </div>
      {/* ▲▲▲▲▲ 피드백 기능 UI 추가 ▲▲▲▲▲ */}
    </div>
  );
}

export default PracticeLogDetailPage;