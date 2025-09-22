// src/pages/PracticeLogDetailPage.jsx
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import apiClient from '../api/apiClient';
import Header from '../components/Header';
import Navigation from '../components/Navigation';
import '../pages/MainPage.css';

function PracticeLogDetailPage() {
  const { id } = useParams();
  const [log, setLog] = useState(null);
  const [loading, setLoading] = useState(true);
  const [feedbacks, setFeedbacks] = useState([]);
  const [newMemo, setNewMemo] = useState('');

  useEffect(() => {
    const fetchLogDetails = async () => {
      try {
        const [logResponse, feedbackResponse] = await Promise.all([
          apiClient.get(`/api/practice-logs/${id}`),
          apiClient.get(`/api/feedback/${id}`),
        ]);
        setLog(logResponse.data);
        setFeedbacks(feedbackResponse.data);
      } catch (error) {
        console.error('데이터를 불러오는 데 실패했습니다:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchLogDetails();
  }, [id]);

  const handleFeedbackSubmit = async (event) => {
    event.preventDefault();
    const requestData = {
      practiceLogId: id,
      videoTimestamp: 0,
      memo: newMemo,
    };
    try {
      const response = await apiClient.post('/api/feedback', requestData);
      setFeedbacks([...feedbacks, response.data]);
      setNewMemo('');
    } catch (error) {
      console.error('피드백 저장에 실패했습니다:', error);
      alert('피드백 저장에 실패했습니다.');
    }
  };

  if (loading) return <div>기록을 불러오는 중입니다...</div>;
  if (!log) return <div>해당 면접 기록을 찾을 수 없습니다.</div>;
  
  const videoFullUrl = `${apiClient.defaults.baseURL}/uploads/${log.videoUrl}`;

  return (
    <div className="main-page">
      <Header />
      <Navigation />
      
      <div className="main-container">
        <div className="content-area">
          <div className="log-detail-container">
            <h1 className="post-detail-title">{log.interviewSetName}</h1>
            <p className="post-detail-meta" style={{justifyContent: 'flex-start'}}>
              연습 일시: {new Date(log.createdAt).toLocaleString()}
            </p>
            <video src={videoFullUrl} controls width="100%" className="session-video" />
            <div className="feedback-section">
              <h2 style={{marginBottom: '20px'}}>셀프 피드백</h2>
              <form onSubmit={handleFeedbackSubmit} className="form-group">
                <textarea
                  value={newMemo}
                  onChange={(e) => setNewMemo(e.target.value)}
                  placeholder="여기에 피드백을 작성하세요..."
                  rows="4"
                  required
                />
                <button type="submit" className="form-button" style={{width: 'auto', padding: '10px 20px', marginTop: '10px'}}>
                  메모 추가
                </button>
              </form>
              <div className="feedback-list" style={{marginTop: '30px'}}>
                <h3 style={{marginBottom: '10px'}}>작성된 메모</h3>
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
          </div>
        </div>
      </div>
    </div>
  );
}
export default PracticeLogDetailPage;