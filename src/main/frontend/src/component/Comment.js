import "./Comment.css";
import { useState } from "react";
import api from "../api/axiosconfig";
import CommentEdit from "./CommentEdit";

const Comment = ({ comment, member, loadComment }) => {
  const [editing, setEditing] = useState(false);

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleString();
  };
  const handleDelete = async () => {
    if (!window.confirm("정말 삭제 하시겠습니까?")) {
      return;
    }
    try {
      await api.delete(`/api/comment/${comment.id}`);
      alert("댓글 삭제 성공");
      loadComment();
    } catch (error) {
      if (error.response.status === 403) {
        alert("삭제 권한이 없습니다.");
      } else {
        alert("삭제 실패");
      }
    }
  };

  const isAuthor = member && member === comment.author.email;
  return (
    <div className="comment">
      <img src={comment.avatar || "https://placehold.co/40x40"} alt="avatar" />
      <div className="comment-content">
        <span className="comment-author">{comment.author.name}</span>
        <span className="comment-date">{formatDate(comment.date)}</span>
        <p className="comment-text">
          {editing ? (
            <CommentEdit
              comment={comment}
              setEditing={setEditing}
              loadComment={loadComment}
              member={member}
              editing={editing}
              isAuthor={isAuthor}
            />
          ) : (
            comment.content
          )}
        </p>
        {!editing && isAuthor && (
          <div className="comment-actions">
            <>
              <button
                className="action-btn edit"
                onClick={() => setEditing(true)}
              >
                수정
              </button>
              <button
                className="action-btn delete"
                onClick={() => handleDelete()}
              >
                삭제
              </button>
            </>
          </div>
        )}
      </div>
    </div>
  );
};
export default Comment;
