import { useNavigate } from "react-router-dom";
import api from "../api/axiosconfig";

const PostView = ({ post, member, setEditing }) => {
  const navigate = useNavigate();
  const handleDelete = async () => {
    if (!window.confirm("정말 삭제 하시겠습니까?")) {
      return;
    }
    try {
      await api.delete(`/api/post/${post.id}`);
      alert("게시글 삭제 성공");
      navigate("/post");
    } catch (error) {
      if (error.response.status === 403) {
        alert("삭제 권한이 없습니다.");
        navigate("/");
        return;
      } else {
        alert("삭제 실패");
      }
    }
  };
  const formatDate = (dateString) => {
    return dateString.substring(0, 10);
  };
  const isAuthor = member && member === post.author.email;
  return (
    <div className="post-detail-card">
      <div className="post-header">
        <h2>{post.title}</h2>
        <span className={`post-badge ${post.category}`}>{post.category}</span>
      </div>
      <div className="post-meta">
        <span>작성자: {post.author.name}</span>
        <span>이메일: {post.author.email}</span>
        <span>작성일: {formatDate(post.date)}</span>
        <span>조회수: {post.hit}</span>
      </div>
      <div className="post-content">{post.content}</div>

      {isAuthor && (
        <div className="post-actions">
          <button className="action-btn edit" onClick={() => setEditing(true)}>
            수정
          </button>
          <button className="action-btn delete" onClick={handleDelete}>
            삭제
          </button>
        </div>
      )}
    </div>
  );
};
export default PostView;
