// src/pages/EditInterviewReviewPage.jsx
import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import apiClient from '../api/apiClient';
import Header from '../components/Header';
import Navigation from '../components/Navigation';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';
import '../pages/MainPage.css';

function EditInterviewReviewPage() {
  const { id } = useParams();
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [company, setCompany] = useState('');
  const [position, setPosition] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const fetchReview = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await apiClient.get(`/api/interview-reviews/${id}`);
      const review = response.data;
      setTitle(review.title);
      setContent(review.content);
      setCompany(review.company);
      setPosition(review.position);
    } catch (error) {
      console.error('면접 후기를 불러오는 데 실패했습니다:', error);
      setError('면접 후기를 불러오는 데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const requestData = { title, content, company, position };
    try {
      await apiClient.put(`/api/interview-reviews/${id}`, requestData);
      alert('면접 후기가 성공적으로 수정되었습니다.');
      navigate(`/interview-reviews/${id}`);
    } catch (error) {
      console.error('면접 후기 수정 실패:', error);
      alert('면접 후기 수정에 실패했습니다.');
    }
  };

  useEffect(() => {
    fetchReview();
  }, [id]);

  if (loading) return (
    <div className="main-page">
      <Header />
      <Navigation />
      <div className="main-container">
        <Loading message="면접 후기를 불러오는 중..." />
      </div>
    </div>
  );

  if (error) return (
    <div className="main-page">
      <Header />
      <Navigation />
      <div className="main-container">
        <ErrorMessage 
          message={error} 
          onRetry={fetchReview}
          retryText="다시 불러오기"
        />
      </div>
    </div>
  );

  return (
    <div className="main-page">
      <Header />
      <Navigation />
      
      <div className="main-container">
        <div className="content-area">
          <div className="form-container" style={{maxWidth: '800px'}}>
            <h2 className="form-title">면접 후기 수정</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label htmlFor="company">회사명</label>
                <input
                  type="text"
                  id="company"
                  value={company}
                  onChange={(e) => setCompany(e.target.value)}
                  placeholder="회사명을 입력하세요"
                  required
                />
              </div>
              
              <div className="form-group">
                <label htmlFor="position">지원 포지션</label>
                <input
                  type="text"
                  id="position"
                  value={position}
                  onChange={(e) => setPosition(e.target.value)}
                  placeholder="지원한 포지션을 입력하세요 (예: 프론트엔드 개발자, 백엔드 개발자)"
                  required
                />
              </div>
              
              <div className="form-group">
                <label htmlFor="title">제목</label>
                <input
                  type="text"
                  id="title"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                  placeholder="면접 후기 제목을 입력하세요"
                  required
                />
              </div>
              
              <div className="form-group">
                <label htmlFor="content">내용</label>
                <textarea
                  id="content"
                  value={content}
                  onChange={(e) => setContent(e.target.value)}
                  placeholder="면접 후기를 상세히 작성해주세요."
                  rows="15"
                  required
                />
              </div>
              
              <div className="form-actions">
                <button type="submit" className="submit-button">수정</button>
                <button 
                  type="button" 
                  className="cancel-button"
                  onClick={() => navigate(`/interview-reviews/${id}`)}
                >
                  취소
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default EditInterviewReviewPage;