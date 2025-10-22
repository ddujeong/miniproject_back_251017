import Comment from "./Comment";
import CommentForm from "./CommentForm";

const CommentSection = ({ comment, member, loadComment, id }) => {
  return (
    <div>
      <h3>댓글 [{comment.length}]</h3>
      {comment && comment.length > 0 ? (
        comment.map((comment) => (
          <Comment
            key={comment.id}
            comment={comment}
            member={member}
            loadComment={loadComment}
          />
        ))
      ) : (
        <p className="err">댓글이 없습니다.</p>
      )}

      <CommentForm member={member} loadComment={loadComment} id={id} />
    </div>
  );
};
export default CommentSection;
