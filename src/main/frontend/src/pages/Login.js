import { useState } from "react";
import "./Login.css";
import { useNavigate } from "react-router-dom";
import api from "../api/axiosconfig";

const Login = ({ onLogin }) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      await api.post(
        "/api/member/login",
        new URLSearchParams({ email, password })
      );
      const res = await api.get("/api/member/me");
      onLogin(res.data.email);
      alert("로그인 성공");
      navigate("/", { replace: true });
    } catch (error) {
      alert("아이디 또는 비밀번호를 다시 확인해 주세요.");
      setPassword("");
    }
  };
  return (
    <div className="container">
      <h2>로그인</h2>
      <input
        className="input"
        type="email"
        placeholder="이메일"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <input
        className="input"
        type="password"
        placeholder="비밀번호"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <button className="button" onClick={handleLogin}>
        로그인
      </button>
    </div>
  );
};
export default Login;
