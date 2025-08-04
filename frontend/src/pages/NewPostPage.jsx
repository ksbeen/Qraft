// NewPostPage.jsx

import { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // 페이지 이동을 위한 useNavigate
import apiClient from '../api/apiClient';

function NewPostPage() {
  // 폼 입력을 위한 state
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');

  // 페이지 이동을 위한 navigate 함수
  const navigate = useNavigate();

  // 폼 제출 시 실행될 함수
  const handleSubmit = async (event) => {
    event.preventDefault();

    // TODO: 로그인 기능 구현 후, 실제 로그인된 사용자의 ID를 사용해야 합니다.
    const requestData = {
      userId: 1, // 지금은 임시로 1번 유저가 작성한다고 가정
      title,
      content,
      boardType: 'FREE', // '자유'게시판으로 고정
    };

    try {
      // 게시글 생성 API 호출
      const response = await apiClient.post('/api/posts', requestData);
      console.log('게시글 생성 성공:', response.data);
      alert('게시글이 성공적으로 등록되었습니다.');

      // 게시글 생성 성공 후, 게시판 목록 페이지로 이동
      navigate('/posts'); 
    } catch (error) {
      console.error('게시글 생성 실패:', error);
      alert('게시글 생성에 실패했습니다.');
    }
  };

  return (
    <div>
      <h2>새 게시글 작성</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>제목</label>
          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />
        </div>
        <div>
          <label>내용</label>
          <textarea
            value={content}
            onChange={(e) => setContent(e.target.value)}
            rows="10"
          />
        </div>
        <button type="submit">등록</button>
      </form>
    </div>
  );
}

export default NewPostPage;