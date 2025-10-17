import { useState } from "react";
import api from "../api/axiosConfig";

function PostEdit({ post, setPost, setEditing }) {
  const [title, setTitle] = useState(post.title);
  const [content, setContent] = useState(post.content);

  // 수정
  const handleUpdate = async () => {
    if (!window.confirm("정말 수정하시겠습니까?")) {
      return;
    }

    try {
      const res = await api.put(`/api/board/${post.id}`, { title, content });
      alert("수정 되었습니다.");
      setPost(res.data); // 새로 수정된 글로 post 변경
      setEditing(false);
    } catch (err) {
      console.error(err);
      if (err.response.state === 403) {
        alert("수정 권한이 없습니다.");
      } else {
        alert("수정 실패 했습니다.");
      }
    }
  };

  return (
    <div className="edit-form">
      <h2>글 수정</h2>
      <input
        type="text"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
      ></input>
      <textarea
        value={content}
        onChange={(e) => setContent(e.target.value)}
      ></textarea>
      <div className="button-group">
        <button className="edit-button" onClick={handleUpdate}>
          저장
        </button>
        <button className="delete-button" onClick={() => setEditing(false)}>
          취소
        </button>
      </div>
    </div>
  );
}

export default PostEdit;
