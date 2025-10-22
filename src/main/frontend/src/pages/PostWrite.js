import React, { useState } from "react";
import "./PostWrite.css";
import { useNavigate } from "react-router-dom";
import api from "../api/axiosconfig";

const categories = ["헤어", "피부", "패키지", "이벤트"];
const PostWrite = ({ member }) => {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [category, setCategory] = useState("");
  const [errors, setErrors] = useState({});
  const navigate = useNavigate();

  const handlePost = async (e) => {
    e.preventDefault();
    setErrors({});
    if (!member) {
      alert("로그인 후 글을 작성해주세요");
      return navigate("/login");
    }
    try {
      await api.post("/api/post", { title, content, category });
      alert("글 작성 완료");
      navigate("/post");
    } catch (err) {
      if (err.response && err.response.status === 400) {
        setErrors(err.response.data);
      } else {
        alert("글 작성 중 오류가 발생했습니다.");
      }
    }
  };

  return (
    <div className="post-write-container">
      <h2>게시글 작성</h2>
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
      {errors.category && <p className="err">{errors.category}</p>}
      {}
      <form onSubmit={handlePost}>
        <input
          className="input"
          type="text"
          placeholder="제목을 입력하세요"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />
        {errors.title && <p className="err">{errors.title}</p>}
        <textarea
          className="textarea"
          placeholder="내용을 입력하세요"
          value={content}
          onChange={(e) => setContent(e.target.value)}
        />
        {errors.content && <p className="err">{errors.content}</p>}
        <button type="submit" className="button">
          작성
        </button>
        <button
          type="button"
          onClick={() => navigate("/post")}
          className="button"
        >
          취소
        </button>
      </form>
    </div>
  );
};

export default PostWrite;
