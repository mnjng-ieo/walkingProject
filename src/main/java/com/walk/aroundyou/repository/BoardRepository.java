package com.walk.aroundyou.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.Tag;
import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.domainenum.BoardType;
import com.walk.aroundyou.dto.IBoardDetailResponse;
import com.walk.aroundyou.dto.IBoardListResponse;

import jakarta.transaction.Transactional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
	
	
	// 게시판 타입별 게시판 리스트 출력
	@Query(value = "SELECT b FROM Board b WHERE boardType = :typeName")
	List<Board> findByBoardType(@Param("typeName") BoardType typeName);
	
//	// 산책로별 게시판 리스트 출력
	@Query(value = "SELECT b.* "
			+ "FROM board b "
			+ "WHERE board_id IN ("
				+ "SELECT board_id "
				+ "FROM board_course "
				+ "WHERE course_id = :#{#course.courseId})"
			, nativeQuery = true)
	List<Board> findBoardByCourse(@Param("course") Course course);
	
	// 산책로별 게시판 리스트 출력
//	@Query(value = ""
//				+ "SELECT board_id "
//				+ "FROM board_course "
//				+ "WHERE course_id = :#{#course.courseId}"
//			, nativeQuery = true)
//	List<Long> findBoardByCourse(@Param("course") Course course);
	
	////// 좋아요 수와 댓글 수를 같이 출력
	/// 전체출력
	@Query(value ="""
			SELECT 
				b.board_id as boardId
				, b.board_type as boardType
				, b.board_title as boardTitle
				, user_nickname as userNickname
				, user_id as userId
				, board_view_count as boardViewCount
				, board_created_date as boardCreatedDate
				, board_updated_date as boardUpdatedDate
				, ifnull(comment_cnt, 0) as commentCnt
				, ifnull(like_cnt, 0) as likeCnt
			FROM board as b
				LEFT JOIN 
					(select board_id, count(board_id) as comment_cnt
						from comment as c
						group by c.board_id) as cc
				on b.board_id = cc.board_id	
				LEFT JOIN 
					(select board_id, count(board_id) as like_cnt
						from board_like as bl
						group by bl.board_id) as bll
			on b.board_id = bll.board_id
			GROUP BY b.board_id
					""" // ORDER BY는 나중에 수정하기
			, nativeQuery = true)
	Page<IBoardListResponse> findBoardAndCnt(Pageable pageable);
	
//////좋아요 수와 댓글 수를 같이 출력
	/// 타입별 출력
	@Query(value ="""
			SELECT 
				b.board_id as boardId
				, b.board_type as boardType
				, b.board_title as boardTitle
				, user_nickname as userNickname
				, user_id as userId
				, board_view_count as boardViewCount
				, board_created_date as boardCreatedDate
				, board_updated_date as boardUpdatedDate
				, ifnull(comment_cnt, 0) as commentCnt
				, ifnull(like_cnt, 0) as likeCnt
				, 
			FROM board as b
				LEFT JOIN 
					(select board_id, count(board_id) as comment_cnt
						from comment as c
						group by c.board_id) as cc
				on b.board_id = cc.board_id	
				LEFT JOIN 
					(select board_id, count(board_id) as like_cnt
						from board_like as bl
						group by bl.board_id) as bll
				on b.board_id = bll.board_id
			WHERE board_type = :#{#type}
			GROUP BY b.board_id
					""" // ORDER BY는 나중에 수정하기
			, nativeQuery = true)
	Page<IBoardListResponse> findBoardAndCntByType(@Param("type") String boardType, Pageable pageable);
//	List<BoardListResponse> findBoardAndCnt();
//	List<BoardListResponse> findBoardAndCntByCourse(@Param("course") Course course);
	
	// 게시글 상세와 좋아요 수를 출력
	@Query(value = """
			SELECT 
			b.board_id as boardId
			, b.board_type as boardType
			, b.board_title as boardTitle
			, user_nickname as userNickname
			, user_id as userId
			, board_view_count as boardViewCount
			, board_created_date as boardCreatedDate
			, board_updated_date as boardUpdatedDate
			, ifnull(comment_cnt, 0) as commentCnt
			, ifnull(like_cnt, 0) as likeCnt

		FROM board as b
			LEFT JOIN 
				(select board_id, count(board_id) as comment_cnt
					from comment as c
					group by c.board_id) as cc
			on b.board_id = cc.board_id	
			LEFT JOIN 
				(select board_id, count(board_id) as like_cnt
					from board_like as bl
					group by bl.board_id) as bll
			on b.board_id = bll.board_id
		WHERE b.board_id = :#{#id}
				"""
			, nativeQuery = true)
	Optional<IBoardDetailResponse> findBoardDetailById(@Param("id") Long id);
	
	
   // 1. (추가)findByTag : 태그 하나 검색하여 관련 게시물 가져오기
   @Query(value = "SELECT b.* FROM board b " +
         " INNER JOIN board_tag bt " + 
         "    ON b.board_id = bt.board_id " + 
         " INNER JOIN tag t " +
         "    ON bt.tag_id = t.tag_id  " +
         " WHERE t.tag_content = :tagContent"
         , nativeQuery = true)
   List<Board> findBoardByTag(@Param("tagContent") String tagContent);
	
//			, CASE :#{#userId} IN (select user_id
//	from board_like as bbl
//	where b.board_id = :#{#id}

	// 게시물 작성
	// 제목, 내용, 유저정보(id, 닉네임)만으로 작성하는 쿼리
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO board "
			+ "(board_title, board_content, user_nickname, user_id) "
			+ "VALUES "
			+ "( "
			+ ", :#{#post.boardTitle}, :#{#post.boardContent}"
			+ ", :#{#post.userNickname}, :#{#post.userId.userId} ) "
			, nativeQuery = true)
	int saveByTitleAndContentAndUserInfo(@Param("post") Board post);
	
	
	// 게시물 수정
	@Modifying
	@Transactional
	@Query(value = "UPDATE board "
			+ "SET "
				+ "  board_title = :#{#post.boardTitle}"
				+ ", board_content = :#{#post.boardContent}"
				+ ", user_nickname = :#{#post.userNickname}"
				+ ", board_updated_date = now() "
			+ "WHERE board_id = :#{#post.boardId} "
			, nativeQuery = true)
	void updateByTitleAndContentAndUserInfo(@Param("post") Board post);
	
	List<Board> findByUserId(Member userId);
	
	void deleteByBoardTitle(String boardTitle);

	// 조회수 증가
	@Modifying
	@Transactional
	@Query(value = """
			UPDATE board b
			SET board_view_count = board_view_count + 1
			WHERE board_id = :#{#id}
			"""
			, nativeQuery = true)
	void updateViewCount(@Param("id")Long id);

   // 산책로별 게시판 리스트 출력
    @Query(value = """
	      SELECT 
	        b.board_id as boardId
	        , b.board_type as boardType
	        , b.board_title as boardTitle
	        , user_nickname as userNickname
	        , user_id as userId
	        , board_view_count as boardViewCount
	        , board_created_date as boardCreatedDate
	        , board_updated_date as boardUpdatedDate
	        , ifnull(comment_cnt, 0) as commentCnt
	        , ifnull(like_cnt, 0) as likeCnt
	     FROM board as b
	            LEFT JOIN 
	               (select board_id, count(board_id) as comment_cnt
	                  from comment as c
	                  group by c.board_id) as cc
	            on b.board_id = cc.board_id   
	            LEFT JOIN 
	               (select board_id, count(board_id) as like_cnt
	                  from board_like as bl
	                  group by bl.board_id) as bll
	         on b.board_id = bll.board_id
	            and b.board_secret = false
	         WHERE b.board_id in
	            (SELECT board_id
	              FROM board_course
	              WHERE course_id = :courseId)
	     GROUP BY b.board_id
	           """, nativeQuery = true)
    Page<IBoardListResponse> findBoardAndCntByCourseId(
    		@Param("courseId") Long courseId, Pageable pageable);
    
    // 특정 해시태그를 사용한 게시물을 출력(좋아요 수, 게시물 수 출력)
    @Query(value ="""
          SELECT 
             b.board_id as boardId
             , b.board_type as boardType
             , b.board_title as boardTitle
             , user_nickname as userNickname
             , user_id as userId
             , board_view_count as boardViewCount
             , board_created_date as boardCreatedDate
             , board_updated_date as boardUpdatedDate
             , ifnull(comment_cnt, 0) as commentCnt
             , ifnull(like_cnt, 0) as likeCnt
          FROM board as b
             LEFT JOIN 
                (select board_id, count(board_id) as comment_cnt
                   from comment as c
                   group by c.board_id) as cc
             on b.board_id = cc.board_id   
             LEFT JOIN 
                (select board_id, count(board_id) as like_cnt
                   from board_like as bl
                   group by bl.board_id) as bll
          on b.board_id = bll.board_id
             and b.board_secret = false
          WHERE b.board_id IN
             (SELECT bt.board_id
                FROM board_tag bt
                WHERE bt.tag_id = :#{#tagId.tagId})
          GROUP BY b.board_id
                """ // ORDER BY는 자바스크립트 정렬필터로 사용
          , nativeQuery = true)
    Page<IBoardListResponse> findBoardAndCntByTagId(
    		@Param("tagId") Tag tagId, Pageable pageable);

	
	/**
	 * [메인페이지] 검색창에 게시판 정보 검색하기 - 서비스에서 매개변수 앞뒤로 '%' 붙이기!
	 */
	@Query(value = """
			SELECT * FROM Board b
	          WHERE REPLACE(board_title, ' ', '') like :#{#keyword}
			         or REPLACE(board_content, ' ', '') like :#{#keyword}
			         or REPLACE(cours_dc, ' ', '') like :#{#keyword}
			         or REPLACE(adit_dc, ' ', '') like :#{#keyword}
			""", nativeQuery = true)
	Page<IBoardDetailResponse> findMainCourseByKeyword(
			@Param("keyword") String keyword, Pageable pageable);
	
}
