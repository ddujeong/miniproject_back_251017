import { useState } from "react";
import "./Register.css";
import { useNavigate } from "react-router-dom";
import api from "../api/axiosconfig";

const Register = () => {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState({});
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();
    setErrors({});
    try {
      await api.post("/api/member/signup", { name, email, password });
      alert("회원가입 성공!");
      navigate("/login");
    } catch (err) {
      if (err.response && err.response.status === 400) {
        setErrors(err.response.data);
      } else {
        alert("회원가입 중 오류가 발생했습니다.");
      }
    }
  };
  return (
    <div className="container">
      <h2>회원가입</h2>

      <input
        className="input"
        type="text"
        placeholder="이름"
        value={name}
        onChange={(e) => setName(e.target.value)}
      />
      {errors.name && <p className="err">{errors.name}</p>}
      <input
        className="input"
        type="email"
        placeholder="이메일"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      {errors.email && <p className="err">{errors.email}</p>}
      <input
        className="input"
        type="password"
        placeholder="비밀번호"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      {errors.password && <p className="err">{errors.password}</p>}
      <button className="button" type="submit" onClick={handleRegister}>
        회원가입
      </button>
    </div>
  );
};
export default Register;
