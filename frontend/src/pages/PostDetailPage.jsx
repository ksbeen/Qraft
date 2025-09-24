import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import apiClient from '../api/apiClient';
import Header from '../components/Header';
import Navigation from '../components/Navigation';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';
import '../pages/MainPage.css';

function PostDetailPage() {
  const { id } = useParams();
  const [post, setPost] = useState(null);
  const [currentUser, setCurrentUser] = useState(null);
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState('');
  const [editingCommentId, setEditingCommentId] = useState(null);
  const [editContent, setEditContent] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  // 현재 로그인한 사용자 정보 가져오기
  const fetchCurrentUser = async () => {
    try {
      const token = localStorage.getItem('authToken');
      if (token) {
        // JWT 토큰을 디코딩해서 사용자 정보 추출
        const payload = JSON.parse(atob(token.split('.')[1]));
        
        // API 호출로 전체 사용자 정보 가져오기
        try {
          const response = await apiClient.get('/api/users/me');
          setCurrentUser(response.data);
        } catch (apiError) {
          console.error('API 호출 실패, JWT에서 정보 사용:', apiError);
          // API 호출이 실패하면 JWT에서 추출한 정보 사용
          setCurrentUser({
            id: payload.userId || payload.id,
            email: payload.sub || payload.email,
            nickname: payload.nickname
          });
        }
      }
    } catch (error) {
      console.error('사용자 정보를 불러오는 데 실패했습니다:', error);
    }
  };

  const fetchPost = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await apiClient.get(`/api/posts/${id}`);
      setPost(response.data);
    } catch (error) {
      console.error('게시글을 불러오는 데 실패했습니다:', error);
      setError('게시글을 불러오는 데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  // 조회수 증가 (별도 함수)
  const increaseViews = async () => {
    try {
      await apiClient.post(`/api/posts/${id}/views`);
    } catch (error) {
      console.error('조회수 증가에 실패했습니다:', error);
    }
  };

  // 댓글 목록 가져오기
  const fetchComments = async () => {
    try {
      const response = await apiClient.get(`/api/posts/${id}/comments`);
      setComments(response.data);
    } catch (error) {
      console.error('댓글을 불러오는 데 실패했습니다:', error);
    }
  };

  // 댓글 작성
  const handleCommentSubmit = async (e) => {
    e.preventDefault();
    if (!newComment.trim()) {
      alert('댓글 내용을 입력해주세요.');
      return;
    }

    try {
      await apiClient.post(`/api/posts/${id}/comments`, {
        content: newComment
      });
      setNewComment('');
      await fetchComments(); // 댓글 목록 새로고침
      alert('댓글이 성공적으로 작성되었습니다.');
    } catch (error) {
      console.error('댓글 작성에 실패했습니다:', error);
      alert('댓글 작성에 실패했습니다.');
    }
  };

  // 댓글 삭제
  const handleCommentDelete = async (commentId) => {
    if (window.confirm('댓글을 삭제하시겠습니까?')) {
      try {
        await apiClient.delete(`/api/comments/${commentId}`);
        await fetchComments(); // 댓글 목록 새로고침
        alert('댓글이 성공적으로 삭제되었습니다.');
      } catch (error) {
        console.error('댓글 삭제에 실패했습니다:', error);
        alert('댓글 삭제에 실패했습니다.');
      }
    }
  };

  // 댓글 수정 시작
  const handleCommentEdit = (comment) => {
    setEditingCommentId(comment.id);
    setEditContent(comment.content);
  };

  // 댓글 수정 취소
  const handleCommentEditCancel = () => {
    setEditingCommentId(null);
    setEditContent('');
  };

  // 댓글 수정 저장
  const handleCommentUpdate = async (commentId) => {
    if (!editContent.trim()) {
      alert('댓글 내용을 입력해주세요.');
      return;
    }

    try {
      await apiClient.put(`/api/comments/${commentId}`, {
        content: editContent
      });
      setEditingCommentId(null);
      setEditContent('');
      await fetchComments(); // 댓글 목록 새로고침
      alert('댓글이 성공적으로 수정되었습니다.');
    } catch (error) {
      console.error('댓글 수정에 실패했습니다:', error);
      alert('댓글 수정에 실패했습니다.');
    }
  };

  // 추천 처리
  const handleRecommend = async () => {
    if (!currentUser) {
      alert('로그인이 필요합니다.');
      return;
    }

    try {
      const response = await apiClient.post(`/api/posts/${id}/recommend`);
      setPost(response.data);
    } catch (error) {
      console.error('추천 처리에 실패했습니다:', error);
      alert('추천 처리에 실패했습니다.');
    }
  };

  // 비추천 처리
  const handleOppose = async () => {
    if (!currentUser) {
      alert('로그인이 필요합니다.');
      return;
    }

    try {
      const response = await apiClient.post(`/api/posts/${id}/oppose`);
      setPost(response.data);
    } catch (error) {
      console.error('비추천 처리에 실패했습니다:', error);
      alert('비추천 처리에 실패했습니다.');
    }
  };

  useEffect(() => {
    const loadData = async () => {
      setLoading(true);
      await Promise.all([
        fetchPost(),
        fetchCurrentUser(),
        fetchComments()
      ]);
      setLoading(false);
    };

    loadData();
    
    // 조회수 증가는 페이지 로드 시 한 번만 실행
    increaseViews();
  }, [id]);

  const handleDelete = async () => {
    if (window.confirm('정말 이 게시글을 삭제하시겠습니까?')) {
      try {
        await apiClient.delete(`/api/posts/${id}`);
        alert('게시글이 성공적으로 삭제되었습니다.');
        navigate('/posts');
      } catch (error) {
        console.error('게시글 삭제에 실패했습니다:', error);
        alert('게시글 삭제에 실패했습니다.');
      }
    }
  };

  const handleReport = async () => {
    if (window.confirm('이 게시글을 신고하시겠습니까?')) {
      try {
        // 신고 API 호출 (현재는 알림만 표시)
        alert('신고가 접수되었습니다. 검토 후 조치하겠습니다.');
      } catch (error) {
        console.error('신고 처리에 실패했습니다:', error);
        alert('신고 처리에 실패했습니다.');
      }
    }
  };

  // 현재 사용자가 게시글 작성자인지 확인
  const isAuthor = currentUser && post && currentUser.id === post.authorId;

  if (loading) return (
    <div className="main-page">
      <Header />
      <Navigation />
      <div className="main-container">
        <div className="content-area">
          <Loading message="게시글을 불러오는 중..." />
        </div>
      </div>
    </div>
  );
  
  if (error || !post) return (
    <div className="main-page">
      <Header />
      <Navigation />
      <div className="main-container">
        <div className="content-area">
          <ErrorMessage 
            message={error || "해당 게시글을 찾을 수 없습니다."} 
            onRetry={fetchPost}
            retryText="다시 불러오기"
          />
        </div>
      </div>
    </div>
  );
  
  return (
    <div className="main-page">
      <Header />
      <Navigation />
      
      <div className="main-container">
        <div className="content-area">
          <div className="post-detail-container">
            {/* 게시글 헤더 */}
            <div className="post-header">
              <h1 className="post-title">{post.title}</h1>
              <div className="post-meta">
                <div className="post-date">{new Date(post.createdAt).toLocaleString('ko-KR')}</div>
                <div className="post-stats">
                  <span>조회 {post.views || 0}</span>
                  <span>댓글 {comments.length}</span>
                </div>
              </div>
            </div>

            {/* 작성자 정보 */}
            <div className="author-section">
              <div className="author-avatar">
                <span className="avatar-icon">👤</span>
              </div>
              <span className="author-name">{post.authorNickname}</span>
            </div>

            {/* 게시글 내용 */}
            <div className="post-content">
              {post.content}
            </div>

            {/* 추천/비추천 버튼 */}
            <div className="post-reactions">
              <button 
                className={`reaction-button recommend ${post.currentUserReaction === 'RECOMMEND' ? 'active' : ''}`}
                onClick={handleRecommend}
              >
                <span className="reaction-icon">👍</span>
                <span className="reaction-text">추천</span>
                <span className="reaction-count">{post.recommendCount || 0}</span>
              </button>
              <button 
                className={`reaction-button oppose ${post.currentUserReaction === 'OPPOSE' ? 'active' : ''}`}
                onClick={handleOppose}
              >
                <span className="reaction-icon">👎</span>
                <span className="reaction-text">비추</span>
                <span className="reaction-count">{post.opposeCount || 0}</span>
              </button>
            </div>

            {/* 액션 버튼들 */}
            <div className="post-detail-actions">
              {isAuthor ? (
                // 작성자 본인인 경우: 수정/삭제 버튼
                <>
                  <Link to={`/posts/${id}/edit`}>
                    <button className="action-button edit">수정</button>
                  </Link>
                  <button className="action-button delete" onClick={handleDelete}>삭제</button>
                </>
              ) : (
                // 다른 사람의 글인 경우: 신고 버튼
                <button className="action-button report" onClick={handleReport}>신고</button>
              )}
            </div>

            {/* 댓글 섹션 */}
            <div className="comments-section">
              <h3 className="comments-title">댓글 {comments.length} 개</h3>
              
              {/* 댓글 작성 폼 */}
              {currentUser && (
                <form className="comment-form" onSubmit={handleCommentSubmit}>
                  <div className="comment-input-container">
                    <div className="comment-author-avatar">
                      <span>{currentUser.nickname?.[0]?.toUpperCase() || 'U'}</span>
                    </div>
                    <textarea
                      className="comment-input"
                      placeholder="댓글을 작성하세요..."
                      value={newComment}
                      onChange={(e) => setNewComment(e.target.value)}
                      rows="3"
                    />
                  </div>
                  <div className="comment-form-actions">
                    <button type="submit" className="comment-submit-btn">등록</button>
                  </div>
                </form>
              )}

              {/* 댓글 목록 */}
              <div className="comments-list">
                {comments.map(comment => (
                  <div key={comment.id} className="comment-item">
                    <div className="comment-header">
                      <div className="comment-author-info">
                        <div className="comment-author-avatar">
                          <span>{comment.authorNickname?.[0]?.toUpperCase() || 'U'}</span>
                        </div>
                        <div className="comment-author-details">
                          <span className="comment-author-name">{comment.authorNickname}</span>
                          <span className="comment-date">
                            {new Date(comment.createdAt).toLocaleDateString('ko-KR', {
                              month: 'numeric',
                              day: 'numeric',
                              hour: '2-digit',
                              minute: '2-digit'
                            })}
                          </span>
                        </div>
                      </div>
                      {currentUser && currentUser.id === comment.authorId && (
                        <div className="comment-actions">
                          {editingCommentId === comment.id ? (
                            // 수정 모드일 때
                            <>
                              <button 
                                className="comment-action-btn save"
                                onClick={() => handleCommentUpdate(comment.id)}
                              >
                                저장
                              </button>
                              <button 
                                className="comment-action-btn cancel"
                                onClick={handleCommentEditCancel}
                              >
                                취소
                              </button>
                            </>
                          ) : (
                            // 일반 모드일 때
                            <>
                              <button 
                                className="comment-action-btn edit"
                                onClick={() => handleCommentEdit(comment)}
                              >
                                수정
                              </button>
                              <button 
                                className="comment-action-btn delete"
                                onClick={() => handleCommentDelete(comment.id)}
                              >
                                삭제
                              </button>
                            </>
                          )}
                        </div>
                      )}
                    </div>
                    <div className="comment-content">
                      {editingCommentId === comment.id ? (
                        // 수정 모드일 때 텍스트 영역 표시
                        <div className="comment-edit-form">
                          <textarea
                            className="comment-edit-input"
                            value={editContent}
                            onChange={(e) => setEditContent(e.target.value)}
                            rows="3"
                          />
                        </div>
                      ) : (
                        // 일반 모드일 때 댓글 내용 표시
                        <p>{comment.content}</p>
                      )}
                    </div>
                  </div>
                ))}
                
                {comments.length === 0 && (
                  <div className="no-comments">
                    <p>첫 번째 댓글을 작성해보세요!</p>
                  </div>
                )}
              </div>
            </div>

            {/* 이전/다음 글 네비게이션 */}
            <div className="post-navigation">
              <button className="nav-button prev">← 이전 글</button>
              <Link to="/posts" className="nav-button list">목록으로</Link>
              <button className="nav-button next">다음 글 →</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
export default PostDetailPage;