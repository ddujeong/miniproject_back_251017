import { useState } from "react";
import api from "../api/axiosconfig";
import { useNavigate } from "react-router-dom";
import EditForm from "./EditForm";
import PostView from "./PostView";

const PostDetailContent = ({ post, member, setPost }) => {
  const [editing, setEditing] = useState(false);

  return (
    <div>
      {editing ? (
        <EditForm post={post} setPost={setPost} setEditing={setEditing} />
      ) : (
        <PostView post={post} member={member} setEditing={setEditing} />
      )}
    </div>
  );
};
export default PostDetailContent;
