// App.jsx

import { Route, Routes } from 'react-router-dom';
import SignUpPage from './pages/SignUpPage';
import PostListPage from './pages/PostListPage';
import PostDetailPage from './pages/PostDetailPage';
import NewPostPage from './pages/NewPostPage';
import EditPostPage from './pages/EditPostPage'; // EditPostPage를 불러옵니다.

function App() {
  return (
    <Routes>
      <Route path="/" element={<div>홈 페이지</div>} />
      <Route path="/signup" element={<SignUpPage />} />
      <Route path="/posts" element={<PostListPage />} />
      <Route path="/posts/new" element={<NewPostPage />} />
      <Route path="/posts/:id" element={<PostDetailPage />} />
      {/* ▼▼▼▼▼ 이 라우트를 추가해주세요 ▼▼▼▼▼ */}
      <Route path="/posts/:id/edit" element={<EditPostPage />} />
    </Routes>
  );
}

export default App;