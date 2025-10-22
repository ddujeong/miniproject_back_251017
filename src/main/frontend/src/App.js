import "./App.css";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  useNavigate,
} from "react-router-dom";
import Footer from "./component/Footer";
import Header from "./component/Header";
import Login from "./pages/Login";
import PostDetail from "./pages/PostDetail";
import PostList from "./pages/PostList";
import PostWrite from "./pages/PostWrite";
import Profile from "./pages/Profile";
import Register from "./pages/Register";
import Reservation from "./pages/Reservation";
import Home from "./pages/Home";
import { useEffect, useState } from "react";
import api from "./api/axiosconfig";

function App() {
  const [member, setMember] = useState(null);
  const navigate = useNavigate();
  const checkMember = async () => {
    setMember(null);
    try {
      const res = await api.get("/api/member/me");
      if (res.data) {
        setMember(res.data.email);
      } else {
        setMember(null);
      }
    } catch (error) {
      setMember(null);
    }
  };
  const handleDelete = async (id) => {
    if (!window.confirm("정말 탈퇴 하시겠습니까?")) {
      return;
    }
    try {
      await api.delete(`/api/member/${member.id}`);
      await api.post("/api/member/logout");
      setMember(null);
      alert("회원탈퇴가 정상적으로 처리 되었습니다.");
      navigate("/");
      return;
    } catch (error) {
      alert("탈퇴 실패");
      console.error(error);
    }
  };

  useEffect(() => {
    checkMember();
  }, []);
  const handleLogout = async () => {
    if (window.confirm("정말 로그아웃 하시겠습니까?")) {
      await api.post("/api/member/logout");
      setMember(null);
    }
  };
  return (
    <>
      <Header onLogout={handleLogout} member={member} />
      <Routes>
        <Route path="/" element={<Home />}></Route>
        <Route path="/post" element={<PostList />}></Route>
        <Route
          path="/post/:id"
          element={<PostDetail member={member} />}
        ></Route>
        <Route path="/write" element={<PostWrite member={member} />}></Route>
        <Route path="/login" element={<Login onLogin={setMember} />}></Route>
        <Route path="/signup" element={<Register />}></Route>
        <Route
          path="/profile"
          element={
            <Profile
              member={member}
              setMember={setMember}
              handleDelete={handleDelete}
            />
          }
        ></Route>
        <Route
          path="/reservation"
          element={<Reservation member={member} />}
        ></Route>
      </Routes>
      <Footer />
    </>

    // <div className="App">
    //   <Header />
    //   <Login />
    //   <Register />
    //   <PostList />
    //   <PostDetail />
    //   <Profile />
    //   <PostWrite />
    //   <Footer />
    // </div>
  );
}

export default App;
