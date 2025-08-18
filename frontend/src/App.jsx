import { Route, Routes, Link } from 'react-router-dom';

import SignUpPage from './pages/SignUpPage';
import LoginPage from './pages/LoginPage';
import PostListPage from './pages/PostListPage';
import PostDetailPage from './pages/PostDetailPage';
import NewPostPage from './pages/NewPostPage';
import EditPostPage from './pages/EditPostPage';
import InterviewLobbyPage from './pages/InterviewLobbyPage';
import InterviewSessionPage from './pages/InterviewSessionPage';
import MyPracticeLogsPage from './pages/MyPracticeLogsPage';

function App() {
  return (
    <div>
      {/* --- 네비게이션 메뉴 --- */}
      <nav>
        <Link to="/">홈</Link> | 
        <Link to="/posts">게시판</Link> | 
        <Link to="/interview">면접 연습</Link> |
        <Link to="/my-logs">내 기록</Link> | 
        <Link to="/login">로그인</Link> |
        <Link to="/signup">회원가입</Link>
      </nav>
      <hr />

      {/* --- 페이지 경로 설정 --- */}
      <Routes>
        <Route path="/" element={
          <div>
            <h1>Qraft에 오신 것을 환영합니다!</h1>
            <p>상단 메뉴에서 면접 연습을 시작해보세요.</p>
          </div>
        } />
        <Route path="/signup" element={<SignUpPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/posts" element={<PostListPage />} />
        <Route path="/posts/new" element={<NewPostPage />} />
        <Route path="/posts/:id" element={<PostDetailPage />} />
        <Route path="/posts/:id/edit" element={<EditPostPage />} />
        <Route path="/interview" element={<InterviewLobbyPage />} />
        <Route path="/interview/:id" element={<InterviewSessionPage />} />
        <Route path="/my-logs" element={<MyPracticeLogsPage />} />
      </Routes>
    </div>
  );
}

export default App;