import { useNavigate } from "react-router-dom";
import "./PostCard.css";

const PostCard = ({ post }) => {
  const navigate = useNavigate();
  const now = new Date();
  const postDate = new Date(post.date);
  const newPost = (now - postDate) / (1000 * 60 * 60 * 24) <= 1;

  const formatDate = (dateString) => {
    return dateString.substring(0, 10);
  };
  return (
    <div
      className="post-card"
      key={post.id}
      onClick={() => navigate(`/post/${post.id}`)}
    >
      <div className={`post-card badge ${post.category}`}>{post.category}</div>{" "}
      {/* 카테고리 배지 */}
      <h2>
        {post.hit > 50 && <span className="hot-badge">💥인기💥</span>}
        {newPost && <span className="new-badge">✨NEW✨</span>}
        {post.title}[{post.comments.length}]
      </h2>
      <p className="author">
        <img
          alt="avatar"
          src="https://img.icons8.com/?size=100&id=U41k6qzQ4m8W&format=png&color=000000"
        />
        {post.author.name}
        <span className="date">{formatDate(post.date)}</span>
        <span className="date">조회수: {post.hit}</span>
      </p>
      <p>{post.content}</p>
    </div>
  );
};
export default PostCard;
