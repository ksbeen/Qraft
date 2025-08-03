// EditPostPage.jsx

import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';

function EditPostPage() {
  // URL에서 수정할 게시글의 id를 가져옵니다.
  const { id } = useParams();
  // 수정 후 페이지 이동을 위해 navigate 함수를 사용합니다.
  const navigate = useNavigate();

  // 폼 입력을 위한 state
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');

  // 1. 페이지가 처음 렌더링될 때, 기존 게시글 데이터를 불러옵니다.
  useEffect(() => {
    const fetchPost = async () => {
      try {
        // id를 사용하여 특정 게시글을 조회하는 API를 호출합니다.
        const response = await axios.get(`http://localhost:8080/api/posts/${id}`);
        // 응답으로 받은 기존 데이터를 state에 저장하여 폼에 보여줍니다.
        setTitle(response.data.title);
        setContent(response.data.content);
      } catch (error) {
        console.error('게시글 정보를 불러오는 데 실패했습니다:', error);
      }
    };
    fetchPost();
  }, [id]); // id가 바뀔 때마다 다시 데이터를 불러옵니다.

  // 2. 폼 제출 시 실행될 함수
  const handleSubmit = async (event) => {
    event.preventDefault();
    
    const updatedData = { title, content };

    try {
      // 수정 API를 호출합니다. (PUT 요청)
      await axios.put(`http://localhost:8080/api/posts/${id}`, updatedData);
      alert('게시글이 성공적으로 수정되었습니다.');
      // 수정 성공 후, 해당 게시글의 상세 페이지로 이동합니다.
      navigate(`/posts/${id}`);
    } catch (error) {
      console.error('게시글 수정에 실패했습니다:', error);
      alert('게시글 수정에 실패했습니다.');
    }
  };

  return (
    <div>
      <h2>게시글 수정</h2>
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
        <button type="submit">수정 완료</button>
      </form>
    </div>
  );
}

export default EditPostPage;