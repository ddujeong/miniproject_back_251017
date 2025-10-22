import { useEffect, useState } from "react";
import PostCard from "../component/PostCard";
import "./Profile.css";
import api from "../api/axiosconfig";
import MyReservation from "../component/MyReservation";
import { useLocation, useNavigate } from "react-router-dom";
import Info from "../component/Info";

const Profile = ({ member, setMember, handleDelete }) => {
  const location = useLocation();
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);

  const [reservations, setReservations] = useState([]);
  const [activeTab, setActiveTab] = useState(
    location.state?.activeTab || "profile"
  );
  const navigate = useNavigate();

  const [name, setName] = useState("");

  const myPage = async () => {
    try {
      const res = await api.get("/api/member/profile");
      setMember(res.data.member);
      setName(res.data.member.name);
      setPosts(res.data.posts);
      setReservations(res.data.myReservations);
    } catch (error) {
      console.error("프로필 불러오기 실패:", error);
      setMember(null);
      setPosts([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    myPage();
  }, []);

  const handleCancel = async (id, action) => {
    if (!window.confirm(`정말 ${action} 하시겠습니까?`)) {
      return;
    }
    try {
      await api.delete(`/api/reservation/${id}`);
      alert("예약 취소 성공");
      myPage();
    } catch (error) {
      alert("삭제 실패");
    }
  };

  if (loading) return <p>프로필 로딩 중...</p>;
  if (!member) {
    return <p>로그인이 필요합니다.</p>;
  }

  return (
    <div className="profile-container">
      <div className="tabs">
        <button
          className={activeTab === "profile" ? "active" : ""}
          onClick={() => setActiveTab("profile")}
        >
          프로필
        </button>
        <button
          className={activeTab === "posts" ? "active" : ""}
          onClick={() => setActiveTab("posts")}
        >
          내 게시글
        </button>
        <button
          className={activeTab === "reservations" ? "active" : ""}
          onClick={() => setActiveTab("reservations")}
        >
          내 예약
        </button>
      </div>
      {activeTab === "profile" && (
        <div className="profile-card">
          <h3>내 정보</h3>
          <Info
            member={member}
            setMember={setMember}
            setName={setName}
            name={name}
            handleDelete={handleDelete}
          />
        </div>
      )}
      {activeTab === "posts" && (
        <div className="profile-posts">
          <h3>내 게시글</h3>
          <div className="post-list">
            {posts.map((post) => (
              <PostCard key={post.id} post={post} />
            ))}
          </div>
        </div>
      )}
      {activeTab === "reservations" && (
        <div className="profile-reservations">
          <h3>내 예약</h3>
          <MyReservation
            reservations={reservations || []}
            onCancel={handleCancel}
          />
        </div>
      )}
    </div>
  );
};

export default Profile;
