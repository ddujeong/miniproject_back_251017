import { useState } from "react";
import api from "../api/axiosconfig";
import { useNavigate } from "react-router-dom";
const categories = ["헤어", "피부", "패키지", "이벤트"];
const EditForm = ({ post, setPost, setEditing }) => {
  const [category, setCategory] = useState(post.category);
  const [title, setTitle] = useState(post.title);
  const [content, setContent] = useState(post.content);
  const [errors, setErrors] = useState({});
  const navigate = useNavigate();
  const handleUpdate = async () => {
    if (!window.confirm("정말 수정 하시겠습니까?")) {
      return;
    }
    try {
      const res = await api.put(`/api/post/${post.id}`, {
        title,
        content,
        category,
      });
      alert("게시글 수정 성공");
      setPost(res.data);
      setEditing(false);
      navigate(`/post/${post.id}`);
    } catch (err) {
      if (err.response.status === 403) {
        alert("수정 권한이 없습니다.");
        navigate("/");
        return;
      }
      if (err.response && err.response.status === 400) {
        setErrors(err.response.data);
        console.log(err.response.data);
      } else {
        alert("수정 실패");
      }
    }
  };
  return (
    <div>
      <div className="edit_form">
        <h2>글 수정하기</h2>
        <select value={category} onChange={(e) => setCategory(e.target.value)}>
          <option value="" disabled>
            -- 선택 --
          </option>
          {categories.map((cat) => (
            <option key={cat} value={cat}>
              {cat}
            </option>
          ))}
        </select>
        <input
          type="text"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />
        {errors.errors && errors.errors.length > 0 && (
          <p className="err">{errors.errors[0].defaultMessage}</p>
        )}
        <textarea
          value={content}
          onChange={(e) => setContent(e.target.value)}
        />
        {errors.errors && errors.errors[1] && (
          <p className="err">{errors.errors[1].defaultMessage}</p>
        )}
        <div className="button_group">
          <button className="action-btn edit" onClick={handleUpdate}>
            저장
          </button>
          <button
            className="action-btn delete"
            onClick={() => setEditing(false)}
          >
            취소
          </button>
        </div>
      </div>
    </div>
  );
};
export default EditForm;
