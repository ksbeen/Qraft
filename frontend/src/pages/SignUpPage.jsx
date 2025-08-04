// SignUpPage.jsx

import { useState } from 'react';
// axios를 사용하기 위해 불러옵니다.
import apiClient from '../api/apiClient';

function SignUpPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [nickname, setNickname] = useState('');

  // 폼 제출 함수를 async/await를 사용하는 비동기 함수로 변경합니다.
  const handleSubmit = async (event) => {
    event.preventDefault();

    const formData = { email, password, nickname };
    
    // try...catch 블록으로 API 호출 시 발생할 수 있는 에러를 처리합니다.
    try {
      // axios.post를 사용하여 백엔드 API에 POST 요청을 보냅니다.
      // 첫 번째 인자는 API 주소, 두 번째 인자는 전송할 데이터(JSON)입니다.
      const response = await apiClient.post('/api/users/signup', formData);

      // 요청이 성공하면, 서버로부터 받은 응답을 콘솔에 출력하고 알림창을 띄웁니다.
      console.log('성공:', response.data);
      alert('회원가입이 성공적으로 완료되었습니다!');

    } catch (error) {
      // 요청이 실패하면, 에러 정보를 콘솔에 출력하고 알림창을 띄웁니다.
      console.error('실패:', error);
      alert('회원가입에 실패했습니다.');
    }
  };

  // ... return 부분의 JSX 코드는 이전과 동일 ...
  return (
    <div>
      <h2>회원가입</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>이메일</label>
          <input type="email" value={email} onChange={(event) => setEmail(event.target.value)} />
        </div>
        <div>
          <label>비밀번호</label>
          <input type="password" value={password} onChange={(event) => setPassword(event.target.value)} />
        </div>
        <div>
          <label>닉네임</label>
          <input type="text" value={nickname} onChange={(event) => setNickname(event.target.value)} />
        </div>
        <button type="submit">회원가입</button>
      </form>
    </div>
  );
}

export default SignUpPage;