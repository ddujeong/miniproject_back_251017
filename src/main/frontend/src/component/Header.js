import { Link } from "react-router-dom";
import "./Header.css";

const Header = ({ onLogout, member }) => {
  return (
    <header className="header">
      <div className="header-container">
        <h1>미니 관리샵</h1>
        <nav className="nav">
          <Link to="/">홈</Link>
          <Link to="/post">후기 게시판</Link>
          <Link to="/write">후기 작성</Link>
          {!member && <Link to="/login">로그인</Link>}
          {!member && <Link to="/signup">회원가입</Link>}
          {member && <Link to="/profile">마이페이지</Link>}
          {member && (
            <button className="nav-link logout" onClick={onLogout}>
              로그아웃
            </button>
          )}
        </nav>
      </div>
    </header>
  );
};
export default Header;
