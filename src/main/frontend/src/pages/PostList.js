import "./PostList.css";
import PostCard from "../component/PostCard.js";
import { useEffect, useState } from "react";
import api from "../api/axiosconfig.js";
const categories = ["전체", "헤어", "피부", "패키지", "이벤트"];

const PostList = () => {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filter, setFilter] = useState("전체");
  const [currentPage, setCurrentPage] = useState(0);
  const [totalItems, setTotalItems] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const loadPost = async (page = 0, category = "전체") => {
    try {
      setLoading(true);
      const res = await api.get(
        `/api/post?page=${page}&size=5&category=${category}`
      );
      setPosts(res.data.posts);
      setCurrentPage(res.data.currentPage); // 현재 페이지
      setTotalItems(res.data.totalItems); // 전체 게시글 수
      setTotalPages(res.data.totalPages); // 전체 페이지 수
      console.log(category);
    } catch (error) {
      console.error(error);
      setError("게시글을 불러오는데 실패했습니다.");
      setPosts([]);
    } finally {
      setLoading(false);
    }
  };
  const handleFilterChange = (cat) => {
    setFilter(cat);
    loadPost(0, cat); // 카테고리 반영해서 새로 로딩
  };
  // 페이지 버튼 클릭 시
  const handlePageChange = (page) => {
    loadPost(page, filter);
  };

  useEffect(() => {
    loadPost(0, filter);
  }, []);
  const getPageNumbers = () => {
    const startPage = Math.floor(currentPage / 10) * 10;
    const endPage = Math.min(startPage + 10, totalPages);
    const pages = [];
    for (let i = startPage; i < endPage; i++) {
      pages.push(i);
    }
    return pages;
  };
  return (
    <div className="post-list-container">
      <h1>후기 게시판</h1>
      {loading && <p className="err">게시판 리스트 로딩 중...</p>}

      <div className="category-tabs">
        {categories.map((cat) => (
          <button
            key={cat}
            className={filter === cat ? "active" : ""}
            onClick={() => handleFilterChange(cat)}
          >
            {cat}
          </button>
        ))}
      </div>
      {posts.length === 0 && (
        <p className="err">아직 작성 된 후기가 없습니다</p>
      )}
      <div className="post-cards">
        {posts.map((post) => (
          <PostCard key={post.id} post={post} />
        ))}
      </div>
      <div className="pagination">
        <button
          onClick={() => handlePageChange(0)}
          disabled={currentPage === 0}
        >
          &lt;&lt;
        </button>
        <button
          onClick={() => handlePageChange(currentPage - 1)}
          disabled={currentPage === 0}
        >
          &lt;
        </button>
        {getPageNumbers().map((num) => (
          <button
            className={num === currentPage ? "active" : ""}
            key={num}
            onClick={() => handlePageChange(num)}
          >
            {num + 1}
          </button>
        ))}
        <button
          onClick={() => handlePageChange(currentPage + 1)}
          disabled={currentPage === totalPages - 1 || totalPages === 0}
        >
          &gt;
        </button>
        <button
          onClick={() => handlePageChange(totalPages - 1)}
          disabled={currentPage === totalPages - 1 || totalPages === 0}
        >
          &gt;&gt;
        </button>
      </div>
    </div>
  );
};
export default PostList;
