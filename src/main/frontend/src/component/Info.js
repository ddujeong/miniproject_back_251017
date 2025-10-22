import { useState } from "react";
import api from "../api/axiosconfig";
import { useNavigate } from "react-router-dom";

const Info = ({ member, setMember, setName, name, handleDelete }) => {
  const [editing, setEditing] = useState(false);
  const [password, setPassword] = useState("");

  const handleUpdate = async () => {
    try {
      const res = await api.put("/api/member/profile", { name, password });
      alert("프로필 수정 성공");
      setMember(res.data);
      setEditing(false);
      setPassword("");
    } catch (error) {
      alert("프로필 수정 실패");
      console.error(error);
    }
  };
  return (
    <>
      <img
        src={
          "https://img.icons8.com/?size=100&id=U41k6qzQ4m8W&format=png&color=000000"
        }
        alt="프로필"
        className="profile-avatar"
      />
      <div className="profile-info">
        {editing ? (
          <>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="이름"
            />
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="새 비밀번호"
              required
            />
            <div className="button-group">
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
          </>
        ) : (
          <>
            <h2>{member.name}</h2>
            <p>{member.email}</p>
            <button
              className="action-btn edit"
              onClick={() => setEditing(true)}
            >
              프로필 수정
            </button>
            <button
              className="action-btn delete"
              onClick={() => handleDelete(member.id)}
            >
              회원 탈퇴
            </button>
          </>
        )}
      </div>
    </>
  );
};
export default Info;
