package com.walk.aroundyou.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.walk.aroundyou.domain.Comment;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.dto.AddCommentRequest;
import com.walk.aroundyou.dto.ICommentResponseDto;
import com.walk.aroundyou.dto.UpdateCommentRequest;

import jakarta.transaction.Transactional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
/* 관리자 권한에 해당하는 메서드 없음 */
	
	///////////////////////////////////////////////////////////////
	////// 당장 안쓰는것같아 모아놓은것
	////////////////////////////////////////////////////////////////
	// 외래키 제한으로 인해 comment_like 먼저 삭제 후, comment 삭제해야 함 
	
	// 1) comment_id로 comment_like 엔티티 먼저 삭제 
	@Transactional
	@Modifying
	@Query(value="DELETE FROM comment_like WHERE comment_id = :#{#commentId}", nativeQuery=true)
	void deleteCommentByCommentLikeId(@Param("commentId") Long commentId);
	/* comment 내용 수정 메서드 */
	@Transactional
	@Modifying
	@Query(value="UPDATE "
			+ "comment c "
			+ "	SET c.comment_content =:#{#update.commentContent}"
			+ "	WHERE c.comment_id = :#{#commentId}", nativeQuery = true)
	void updateCommentContent(@Param("commentId") Long commentId, @Param("update") UpdateCommentRequest update);
	/* 해당 산책로(commentType = COURSE)에 관한 comment 개수 조회 메서드 */	
	@Query(value="select "
			+ "COUNT(c.comment_content) "
			+ "from comment c "
			+ "where comment_type = 'COURSE' AND c.course_id = :#{#course.courseId}"
			, nativeQuery = true)
	long countCourseCommentByCourseId(@Param("course") Course courseId);


	
	//////////////////////////////////////////////////////////////////
	
	
	
	
	
	
	
	
	
	
/* comment 조회 메서드  */
	// 1. Board에서 board_id를 이용한 Comment 목록 조회 ( 닉네임 / 유저이미지 / 내용 / 수정날짜 / 좋아요 수 )
	@Query(value= "SELECT"
				+ " c.board_id AS boardId, "
				+ " c.comment_id AS commentId, "
				+ " c.user_nickname AS userNickname, "
				+ " c.user_img AS userImg, "
				+ " c.user_id AS userId, "
				+ " c.comment_content AS commentContent, "
				+ " c.comment_updated_date AS commentUpdatedDate, "
				+ " IFNULL(COUNT(cl.comment_like_id), 0) AS commentLikeCnt"
			+ "	FROM comment c"
			+ "		LEFT JOIN comment_like cl ON c.comment_id  = cl.comment_id "
			+ "		INNER JOIN user u ON c.user_id = u.user_id "
			+ "	WHERE c.board_id = :#{#boardId}"
			+ "	GROUP BY c.comment_id ", nativeQuery = true)
	List<ICommentResponseDto> findByBoardId(@Param("boardId") Long boardId);

	
	
	// 2. Course에서 course_id를 이용한 Comment 목록 조회 ( 닉네임 / / 내용 / 수정날짜 / 좋아요 수 )
	@Query(value="SELECT"
			+ " c.course_Id AS courseId, "
			+ " c.comment_id AS commentId, "
			+ " c.user_nickname AS userNickname, "
			+ " c.user_img AS userImg, "
			+ " c.user_id AS userId, "
			+ " c.comment_content AS commentContent, "
			+ " c.comment_updated_date AS commentUpdatedDate, "
			+ " IFNULL(COUNT(cl.comment_like_id), 0) AS commentLikeCnt"
			+ "		FROM comment c"
			+ "		LEFT JOIN comment_like cl ON c.comment_id  = cl.comment_id "
			+ "		INNER JOIN user u ON c.user_id = u.user_id "
			+ "		WHERE c.course_id = :#{#courseId}"
			+ " 	GROUP BY c.comment_id ", nativeQuery = true)
	List<ICommentResponseDto> findByCourseId(@Param("courseId") Long courseId);	

	
	
	
/* comment 추가 메서드 */
	// 1. Board에서 Comment 추가(작성) API 구현
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO comment "
			+ "	(user_id, user_nickname, comment_content, board_id, comment_type)"
			//+ "	(user_nickname, comment_content, comment_type)"
			+ "	VALUES "
			+ "	(:#{#create.userId.userId}, :#{#create.userNickname}, :#{#create.commentContent}, "
			+ " :#{#create.boardId.boardId} , 'BOARD')"
			//+ "	(:#{#create.userNickname}, :#{#create.commentContent}, 'BOARD')"
			, nativeQuery = true)
	int saveCommentAsBoard(@Param("create") AddCommentRequest commentReqDto);
	
	
	// 2. Course에서 Comment 추가(작성) API 구현
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO comment "
			+ "	(user_id, user_nickname, comment_content, course_id, comment_type)"
			+ "	VALUES "
			+ "	(:#{#create.userId.userId}, :#{#create.userNickname}, :#{#create.commentContent}, "
			+ " :#{#create.courseId.courseId}, 'COURSE')"
			, nativeQuery = true)
	int saveCommentAsCourse(@Param("create") AddCommentRequest create);

	
	
/* comment 내용 삭제 메서드 */

	
	// 2) comment_id로 comment 엔티티 삭제 
	@Transactional
	@Modifying
	@Query(value="DELETE FROM comment WHERE comment_id = :#{#commentId}", nativeQuery = true)
	int deleteCommentByCommentId(@Param("commentId") Long commentId);


	
/* comment 내용 수정 메서드 */

	// 게시판 댓글 수정 메서드
	@Transactional
	@Modifying
	@Query(value="UPDATE "
			+ "comment c "
			+ "	SET c.comment_content =:#{#update.commentContent}"
			+ " 	, c.comment_updated_date = :#{#update.commentUpdatedDate}"
			+ "	WHERE c.comment_id = :#{#update.commentId}", nativeQuery = true)
	int updateCommentByCommentId(@Param("update") Comment update);
	// 산책로 댓글 수정 메서드
	// 내용이 같아서 병합
//	@Transactional
//	@Modifying
//	@Query(value="UPDATE "
//			+ "comment c "
//			+ "	SET c.comment_content =:#{#update.commentContent}"
//			+ " 	, c.comment_updated_date = :#{#update.commentUpdatedDate}"
//			+ "	WHERE c.comment_id = :#{#update.commentId}", nativeQuery = true)
//	void updateCourseCommentByCommentId(@Param("update") Comment update);
	
	
	@Query(value = """ 
			SELECT COUNT(*) FROM comment_like WHERE comment_id = :commentId
			""", nativeQuery = true)
	public int countCommentLikesByCommentId(@Param("commentId") Long commentId);
		
	
	

}
