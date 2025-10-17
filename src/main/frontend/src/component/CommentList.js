import { useState } from "react";
import api from "../api/axiosConfig";

function CommentList({ comments, user, loadComments }) {
  const [editingCommentContent, setEditingCommentContent] = useState("");
  const [editingCommentId, setEditingCommentId] = useState(null);

  // 날짜 포맷 함수
  const commentFormatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString();
  };

  //댓글 삭제 이벤트 함수
  const handleCommentDelete = async (commentId) => {
    if (!window.confirm("정말 삭제하시겠습니까?")) {
      //확인->true, 취소->false
      return;
    }
    try {
      await api.delete(`/api/comments/${commentId}`);
      alert("댓글 삭제 성공!");
      //navigate("/board");
      loadComments(); //갱신된 댓글 리스트를 다시 로딩
    } catch (err) {
      console.error(err);
      alert("댓글 삭제 권한이 없거나 삭제할 수 없는 댓글입니다.");
    }
  };

  //댓글 수정 이벤트 함수->백엔드 수정 요청
  const handleCommentUpdate = async (commentId) => {
    try {
      await api.put(`/api/comments/${commentId}`, {
        content: editingCommentContent,
      });
      setEditingCommentId(null);
      setEditingCommentContent("");
      loadComments();
    } catch (err) {
      alert("댓글 수정 실패!");
    }
  };

  //댓글 수정 여부 확인
  const handleCommentEdit = (comment) => {
    setEditingCommentId(comment.id);
    setEditingCommentContent(comment.content);
    //EditingCommentContent->수정할 내용으로 저장
  };

  return (
    <div className="comment-list-section">
      <h3>댓글 목록</h3>

      {comments.length === 0 ? (
        <p style={{ color: "blue" }}>아직 등록된 댓글이 없습니다.</p>
      ) : (
        <ul className="comment-list">
          {comments.map((c) => (
            <li key={c.id} className="comment-item">
              <div className="comment-header">
                <span className="comment-author">{c.author.username}</span>
                <span className="comment-date">
                  {commentFormatDate(c.createDate)}
                </span>
              </div>

              {/* 댓글 수정 중일 때 */}
              {editingCommentId === c.id ? (
                <>
                  <textarea
                    value={editingCommentContent}
                    onChange={(e) => setEditingCommentContent(e.target.value)}
                  />
                  <button
                    className="comment-save"
                    onClick={() => handleCommentUpdate(c.id)}
                  >
                    저장
                  </button>
                  <button
                    className="comment-cancel"
                    onClick={() => setEditingCommentId(null)}
                  >
                    취소
                  </button>
                </>
              ) : (
                <>
                  {/* 일반 보기 모드 */}
                  <div className="comment-content">{c.content}</div>

                  {/* 작성자 본인만 수정/삭제 가능 */}
                  {user === c.author.username && (
                    <div className="button-group">
                      <button
                        className="edit-button"
                        onClick={() => handleCommentEdit(c)}
                      >
                        수정
                      </button>
                      <button
                        className="delete-button"
                        onClick={() => handleCommentDelete(c.id)}
                      >
                        삭제
                      </button>
                    </div>
                  )}
                </>
              )}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default CommentList;
