package com.walk.aroundyou.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.walk.aroundyou.domain.Comment;
import com.walk.aroundyou.domain.CommentLike;
import com.walk.aroundyou.domain.Member;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
	

	// 특정 댓글(commentId)에 대한 좋아요(userId)누른 회원들의 목록 
	// : 목록의 요소는 userId 대신 userId와 매칭되는 프로필 사진 이미지로 조회된다. (user 권한에서)
	// : 목록의 요소는 userId 그리고 userId와 매칭되는 프로필 사진 이미지로 조회된다. (admin 권한에서)
	@Query(value = "SELECT bl.user_id FROM Comment_like bl WHERE bl.comment_id = :#{#commentId}"
			, nativeQuery = true)
	List<String> findUserIdByCommentId(@Param(value = "commentId") Long commentId);
	
	// 특정 댓글(commentId)에 대한 좋아요(userId)의 갯수
	// @Query(value = "SELECT count_big(commentId) FROM CommentLike bl WHERE bl.commentId = :commentId") 아래와 동일
	@Query(value = "SELECT count(bl.user_id) FROM Comment_like bl WHERE bl.comment_id = :#{#commentId}"
			, nativeQuery = true)
	Long countUserIdByCommentId(@Param(value = "commentId") Long commentId);
	
	
	// 마이페이지
	// 마이페이지에서 좋아요한 댓글 목록 확인(로그인한 회원 본인과 관리자만 목록을 확인할 수 있음)
	@Query(value = "SELECT bl.comment_id FROM Comment_like bl WHERE bl.user_id = :#{#userId}"
			, nativeQuery = true)
	List<Long> findLikedCommentIdByUserId(@Param(value = "userId")String userId);
	
	// 마이페이지에서 좋아요한 댓글의 갯수 확인(로그인한 회원 본인과 관리자만 목록을 확인할 수 있음)
	@Query(value = "SELECT count(comment_id) FROM Comment_like bl WHERE bl.user_id = :#{#userId}"
			, nativeQuery = true)
	Long countLikedCommentIdByUserId(@Param(value = "userId")String userId);
	
	
	
	// 사용자가 댓글 좋아요 표시 클릭 시 선택 + 해제
	// 값의 유무 모르니까 에러 발생하지 않기 위해 Optional<>
	Optional<CommentLike> findByUserIdAndCommentId(Member userId, Comment commentId);
	
	
	
	// 특정 게시물에 특정 유저가 좋아요한 commentId 목록
	@Query(value = """
			SELECT cl.comment_id
				FROM comment_like cl
				WHERE cl.user_id = :#{#userId}
					AND cl.comment_id IN
						(SELECT c.comment_id
							FROM comment c 
							WHERE c.board_id = :#{#boardId});
			"""
			, nativeQuery = true)
	List<Long> findCommentIdByUserIdAndBoardId(@Param("userId") String userId, @Param("boardId") Long boardId);
	
	
	
	
	
	
}
