import { useNavigate } from "react-router-dom";
import "./Home.css";
import PostCard from "../component/PostCard";
import { useEffect, useState } from "react";
import api from "../api/axiosconfig";

const Home = () => {
  const navigate = useNavigate();
  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(true);
  const popPost = async () => {
    try {
      setLoading(true);
      const res = await api.get("/api/post/popular");
      console.log(res);
      setPost(res.data);
    } catch (error) {
      console.error(error);

      setPost(null);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => {
    popPost();
  }, []);
  return (
    <div className="home-container">
      <h1>미니 관리샵</h1>
      <p>편리하게 예약하고, 후기를 확인하세요!</p>

      <div className="home-buttons">
        <button
          className="action-btn home-btn"
          onClick={() => navigate("/post")}
        >
          게시판 보기
        </button>
        <button
          className="action-btn home-btn"
          onClick={() =>
            navigate("/reservation", { state: { tab: "reservations" } })
          }
        >
          예약하기
        </button>
      </div>
      <div className="popular-section">
        <h2>🔥 인기 게시글 🔥</h2>
        {loading ? (
          <p>인기 게시글 로딩 중...</p>
        ) : post && post.author ? (
          <div className="popular-card-wrapper">
            <PostCard key={post.id} post={post} />
          </div>
        ) : (
          <div className="no-popular">
            <p>이번 주 인기 게시글이 없습니다 😢</p>
            <img
              src="https://placehold.co/300x150?text=No+Post"
              alt="no popular post"
              className="no-popular-img"
            />
          </div>
        )}
      </div>
    </div>
  );
};

export default Home;
