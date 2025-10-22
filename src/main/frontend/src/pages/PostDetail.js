import { useEffect, useState } from "react";
import "./PostDetail.css";
import { useNavigate, useParams } from "react-router-dom";
import api from "../api/axiosconfig";
import PostDetailContent from "../component/PostDetailContent";
import CommentSection from "../component/CommentSection";

const PostDetail = ({ member }) => {
  const navigate = useNavigate();
  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(true);
  const { id } = useParams(); // board/:id id 파라미터 받아오기
  const [comment, setComment] = useState([]);

  const loadPost = async () => {
    try {
      setLoading(true);
      const res = await api.get(`/api/post/${id}`);

      setPost(res.data);
    } catch (error) {
      alert("해당 게시글은 존재하지 않습니다.");
      navigate("/post");
      return;
    } finally {
      setLoading(false);
    }
  };

  const loadComment = async () => {
    try {
      setLoading(true);
      const res = await api.get(`/api/comment/${id}`);
      setComment(res.data);
    } catch (error) {
      alert("해당 댓글은 존재하지 않습니다.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadPost();
    loadComment();
  }, [id]);

  if (loading) return <p>게시글 로딩 중...</p>;
  if (!post) {
    return <div>Loading...</div>;
  }

  return (
    <div className="post-detail-container">
      <PostDetailContent post={post} member={member} setPost={setPost} />
      <div className="comments-section">
        <CommentSection
          comment={comment}
          member={member}
          loadComment={loadComment}
          id={id}
        />
      </div>
    </div>
  );
};
export default PostDetail;
