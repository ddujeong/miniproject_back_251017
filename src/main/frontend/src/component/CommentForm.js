import { useNavigate } from "react-router-dom";
import api from "../api/axiosconfig";
import { useState } from "react";

const CommentForm = ({ member, loadComment, id }) => {
  const [commentText, setCommentText] = useState("");
  const [errors, setErrors] = useState({});
  const navigate = useNavigate();

  const handleComment = async (e) => {
    e.preventDefault();
    setErrors({});
    if (!commentText.trim()) {
      alert("댓글 내용을 입력해주세요.");
      return;
    }
    if (!member) {
      alert("로그인 후 댓글을 작성해주세요.");
      navigate("/login");
      return;
    }
    try {
      await api.post(`/api/comment/${id}`, { content: commentText });
      alert("댓글 작성 완료");
      loadComment();
    } catch (err) {
      if (err.response && err.response.status === 400) {
        console.log(err.response.data);
        setErrors(err.response.data);
      } else {
        alert("댓글 작성 중 오류가 발생했습니다.");
      }
    }
  };
  return (
    <div>
      {errors.errors && errors.errors.length > 0 && (
        <p className="err">{errors.errors[0].defaultMessage}</p>
      )}
      <div className="comment-form">
        <input
          className="comment-input"
          placeholder={member ? "댓글 작성..." : "로그인 후 댓글 작성 해주세요"}
          readOnly={!member}
          value={commentText}
          onChange={(e) => setCommentText(e.target.value)}
        />

        <button
          className="comment-button"
          onClick={handleComment}
          disabled={!member}
        >
          댓글 달기
        </button>
      </div>
    </div>
  );
};
export default CommentForm;
