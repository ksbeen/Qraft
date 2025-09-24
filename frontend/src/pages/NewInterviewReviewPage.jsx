// src/pages/NewInterviewReviewPage.jsx
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import apiClient from '../api/apiClient';
import Header from '../components/Header';
import Navigation from '../components/Navigation';
import '../pages/MainPage.css';

function NewInterviewReviewPage() {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [company, setCompany] = useState('');
  const [position, setPosition] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    const requestData = { title, content, company, position };
    try {
      await apiClient.post('/api/interview-reviews', requestData);
      alert('면접 후기가 성공적으로 등록되었습니다.');
      navigate('/interview-reviews');
    } catch (error) {
      console.error('면접 후기 생성 실패:', error);
      alert('면접 후기 생성에 실패했습니다.');
    }
  };

  return (
    <div className="main-page">
      <Header />
      <Navigation />
      
      <div className="main-container">
        <div className="content-area">
          <div className="form-container" style={{maxWidth: '800px'}}>
            <h2 className="form-title">면접 후기 작성</h2>
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
                  placeholder="면접 후기를 상세히 작성해주세요.&#10;&#10;예시:&#10;- 면접 절차 및 분위기&#10;- 질문 내용&#10;- 면접관들의 태도&#10;- 준비하면 좋을 것들&#10;- 기타 팁 등"
                  rows="15"
                  required
                />
              </div>
              
              <button type="submit" className="form-button">등록</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default NewInterviewReviewPage;