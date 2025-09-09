// src/App.jsx
import { Route, Routes } from 'react-router-dom';
import NavBar from './components/NavBar';
import MainPage from './pages/MainPage';
import SignUpPage from './pages/SignUpPage';
import LoginPage from './pages/LoginPage';
import PostListPage from './pages/PostListPage';
import PostDetailPage from './pages/PostDetailPage';
import NewPostPage from './pages/NewPostPage';
import EditPostPage from './pages/EditPostPage';
import InterviewLobbyPage from './pages/InterviewLobbyPage';
import InterviewSessionPage from './pages/InterviewSessionPage';
import MyPracticeLogsPage from './pages/MyPracticeLogsPage';
import PracticeLogDetailPage from './pages/PracticeLogDetailPage';

function App() {
  return (
    <div>
      <NavBar />
      <main style={{ maxWidth: '1200px', margin: '20px auto', padding: '0 20px' }}>
        <Routes>
          <Route path="/" element={<MainPage />} />
          <Route path="/signup" element={<SignUpPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/posts" element={<PostListPage />} />
          <Route path="/posts/new" element={<NewPostPage />} />
          <Route path="/posts/:id" element={<PostDetailPage />} />
          <Route path="/posts/:id/edit" element={<EditPostPage />} />
          <Route path="/interview" element={<InterviewLobbyPage />} />
          <Route path="/interview/:id" element={<InterviewSessionPage />} />
          <Route path="/my-logs" element={<MyPracticeLogsPage />} />
          <Route path="/practice-logs/:id" element={<PracticeLogDetailPage />} />
        </Routes>
      </main>
    </div>
  );
}
export default App;