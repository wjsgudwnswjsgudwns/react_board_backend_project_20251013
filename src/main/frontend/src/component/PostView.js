import { useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";

function PostView({ post, user, setEditing }) {
  const navigate = useNavigate();

  //삭제
  const handleDelete = async () => {
    try {
      if (window.confirm("정말 삭제하시겠습니까?")) {
        await api.delete(`/api/board/${post.id}`);
        alert("삭제 되었습니다.");
        navigate("/board", { replace: true });
      }
    } catch (err) {
      console.error(err);
      if (err.response.state === 403) {
        alert("삭제 권한이 없습니다.");
      } else {
        alert("삭제 실패 했습니다.");
      }
    }
  };

  if (!post) {
    return <p style={{ color: "red" }}>존재하지 않는 글입니다.</p>;
  }

  const isAuthor = user && user === post?.author?.username;

  return (
    <div className="post-view">
      <h2>{post.title}</h2>
      <p className="author">작성자 : {post.author.username}</p>
      <div className="content">{post.content}</div>

      <div className="button-group">
        <button onClick={() => navigate("/board")} className="list-button">
          목록
        </button>
        {isAuthor && (
          <>
            <button className="edit-button" onClick={() => setEditing(true)}>
              수정
            </button>
            <button className="delete-button" onClick={handleDelete}>
              삭제
            </button>
          </>
        )}
      </div>
    </div>
  );
}

export default PostView;
