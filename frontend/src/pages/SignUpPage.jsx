// src/pages/SignUpPage.jsx
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import apiClient from '../api/apiClient';

function SignUpPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [nickname, setNickname] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    const formData = { email, password, nickname };
    try {
      await apiClient.post('/api/users/signup', formData);
      alert('회원가입이 성공적으로 완료되었습니다! 로그인 페이지로 이동합니다.');
      navigate('/login');
    } catch (error) {
      console.error('회원가입 실패:', error);
      if (error.response && error.response.data) {
        alert(error.response.data);
      } else {
        alert('회원가입에 실패했습니다.');
      }
    }
  };

  return (
    <div className="form-container">
      <h2 className="form-title">회원가입</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="email">이메일 주소</label>
          <input type="email" id="email" required value={email} onChange={(e) => setEmail(e.target.value)} />
        </div>
        <div className="form-group">
          <label htmlFor="password">비밀번호</label>
          <input type="password" id="password" required value={password} onChange={(e) => setPassword(e.target.value)} />
        </div>
        <div className="form-group">
          <label htmlFor="nickname">닉네임</label>
          <input type="text" id="nickname" required value={nickname} onChange={(e) => setNickname(e.target.value)} />
        </div>
        <button type="submit" className="form-button">회원가입</button>
      </form>
    </div>
  );
}
export default SignUpPage;