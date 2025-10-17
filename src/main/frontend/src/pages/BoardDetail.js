import "./BoardDetail.css";

import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../api/axiosConfig";
import PostView from "../component/PostView";
import PostEdit from "../component/PostEdit";
import CommentForm from "../component/CommentForm";
import CommentList from "../component/CommentList";

function BoardDetail({ user }) {
  const [post, setPost] = useState(null); // 해당 글 id 요청한 글 객체
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const { id } = useParams();

  const [editing, setEditing] = useState(false); // 수정화면 출력 여부

  //댓글 리스트 불러오기 함수
  const loadComments = async () => {
    try {
      const res = await api.get(`/api/comments/${id}`);
      //res->댓글 리스트 저장(ex:7번글에 달린 댓글 4개 리스트)
      setComments(res.data);
    } catch (err) {
      console.error(err);
      alert("댓글 리스트 불러오기 실패!");
    }
  };

  //특정 글 보기
  const loadPost = async () => {
    // 특정 글 요청
    try {
      setLoading(true);
      const res = await api.get(`/api/board/${id}`);
      setPost(res.data);
    } catch (err) {
      console.error(err);
      setError("존재하지 않는 게시글입니다.");
      // alert("존재하지 않는 게시글입니다.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadPost(); // 게시글 다시 불러오기
    loadComments(); // 게시글에 달린 댓글 다시 불러오기
  }, [id]);

  // 댓글 영역 시작

  const [comments, setComments] = useState([]); //백엔드에서 가져온 기존 댓글 배열

  // 댓글 영역 끝

  if (loading) return <p>게시글 불러오는 중</p>;
  if (error) return <p style={{ color: "red" }}>{error}</p>;
  if (!post) return <p style={{ color: "red" }}>존재하지 않는 글입니다.</p>;

  return (
    <div className="detail-container">
      {editing ? (
        <PostEdit post={post} setPost={setPost} setEditing={setEditing} />
      ) : (
        <PostView post={post} user={user} setEditing={setEditing} />
      )}

      {/* 댓글 영역 시작 */}
      <div className="comment-section">
        <CommentForm loadComments={loadComments} boardId={id} user={user} />

        <CommentList
          comments={comments}
          user={user}
          loadComments={loadComments}
        />
      </div>

      {/* 댓글 영역 끝 */}
    </div>
  );
}

export default BoardDetail;
