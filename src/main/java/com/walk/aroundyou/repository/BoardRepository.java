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
import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.domain.Tag;
import com.walk.aroundyou.domainenum.BoardType;
import com.walk.aroundyou.dto.IBoardDetailResponse;
import com.walk.aroundyou.dto.IBoardListResponse;

import jakarta.transaction.Transactional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
	
	
   // 1. 특정 해시태그를 사용한 게시물을 출력(좋아요 수, 게시물 수 출력)
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
         WHERE b.board_id IN
            (SELECT bt.board_id
               FROM board_tag bt
               WHERE bt.tag_id = :#{#tagId.tagId})
            and b.board_secret = false
         GROUP BY b.board_id
               """ // ORDER BY는 자바스크립트 정렬필터로 사용
         , nativeQuery = true)
   Page<IBoardListResponse> findBoardAndCntByTagId(
		   @Param("tagId") Tag tagId, Pageable pageable);

	
	// 게시판 타입별 게시판 리스트 출력
	@Query(value = "SELECT b FROM Board b WHERE boardType = :typeName")
	List<Board> findByBoardType(@Param("typeName") BoardType typeName);
	
//// 산책로별 게시판 리스트 출력
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
      WHERE b.board_id in
         (SELECT board_id
           FROM board_course
           WHERE course_id = :courseId)
         and b.board_secret = false
  GROUP BY b.board_id
  		""", nativeQuery = true)
	Page<IBoardListResponse> findBoardAndCntByCourseId(
			@Param("courseId") Long courseId, Pageable pageable);
	
	
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
			WHERE b.board_secret = false
			GROUP BY b.board_id
					""" // ORDER BY는 나중에 수정하기
			, nativeQuery = true)
	Page<IBoardListResponse> findBoardAndCnt(Pageable pageable);
	
	// 다희 언니
	// [메인페이지] - 핫한 해시태그가 포함된 게시물 목록에 출력되는 디폴트 게시물 리스트 
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
			WHERE b.board_secret = false
				and b.board_id in (
					SELECT bt.board_id
						FROM board_tag bt
						JOIN tag t
							ON bt.tag_id = t.tag_id
						WHERE t.tag_content = :#{#tagContent} )
	        GROUP BY b.board_id
	        ORDER BY likeCnt desc
	        LIMIT 5
			""", nativeQuery = true)
	List<IBoardListResponse> findBoardAndCntByMainTagDefault(@Param("tagContent") String tagContent);
	
	
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
				and b.board_secret = false
			GROUP BY b.board_id
					""" // ORDER BY는 나중에 수정하기
			, nativeQuery = true)
	Page<IBoardListResponse> findBoardAndCntByType(@Param("type") String boardType, Pageable pageable);

	// 게시글 상세와 좋아요 수를 출력
	@Query(value = """
			SELECT 
			b.board_id as boardId
			, b.board_type as boardType
			, b.board_title as boardTitle
			, b.board_content as boardContent
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
				and b.board_secret = false
				"""
			, nativeQuery = true)
	Optional<IBoardDetailResponse> findBoardDetailById(@Param("id") Long id);
	
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

	/**
	 * [메인페이지] 검색창에 게시판 정보 검색하기 - 서비스에서 매개변수 앞뒤로 '%' 붙이기!
	 */
	// 게시물 출력 시 좋아요, 댓글도 같이 출력할 것이므로 join하기
	// 메인 페이지 검색 결과에서 5개만 출력 후 더보기 버튼 존재(컨트롤러에서 개수 제한)
	@Query(value = """
			SELECT 
				b.board_id as boardId
	            , b.board_type as boardType
	            , b.board_title as boardTitle
	            , b.board_content as boardContent
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
	          WHERE REPLACE(board_title, ' ', '') like :#{#keyword}
			      or REPLACE(board_content, ' ', '') like :#{#keyword}
			  ORDER BY b.board_id DESC
			""", nativeQuery = true)
	List<IBoardListResponse> findMainBoardByKeyword(
			@Param("keyword") String keyword);
	
	// 검색어를 기반으로 게시물 결과의 전체 개수를 반환하는 메서드
 @Query(value ="""
 		SELECT COUNT(*) 
 		FROM board b
         WHERE REPLACE(board_title, ' ', '') like :#{#keyword}
			      or REPLACE(board_content, ' ', '') like :#{#keyword}
	         """, nativeQuery = true)
	int countBoardResults(@Param("keyword") String keyword);
	
	// 메인 검색 결과에서 게시물 더보기에 출력될 페이지
	// 게시판 검색용 (제목, 내용)(타입을 선택하지 않은 경우)
	@Query(value = """
			SELECT 
				b.board_id as boardId
	            , b.board_type as boardType
	            , b.board_title as boardTitle
	            , b.board_content as boardContent
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
	            WHERE REPLACE(board_title, ' ', '') like :#{#keyword}
			      or REPLACE(board_content, ' ', '') like :#{#keyword}
			""", nativeQuery = true)
	Page<IBoardListResponse> findBoardAndCntByKeyword(
			@Param("keyword") String keyword, Pageable pageable);

	/**
	 * [게시판 검색페이지] 게시판 타입이 없을 경우
	 */
	// 제목 + 내용은 Page<IBoardListResponse> findBoardAndCntByKeyword 사용
	// 게시판 검색용 (제목)
	@Query(value = """
			SELECT 
				b.board_id as boardId
	            , b.board_type as boardType
	            , b.board_title as boardTitle
	            , b.board_content as boardContent
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
	            WHERE REPLACE(board_title, ' ', '') like :#{#keyword} 
			""", nativeQuery = true)
	Page<IBoardListResponse> findBoardAndCntByTitle(@Param("keyword") String keyword, Pageable pageable);	

	// 게시판 검색용 (내용)
	@Query(value = """
			SELECT 
				b.board_id as boardId
	            , b.board_type as boardType
	            , b.board_title as boardTitle
	            , b.board_content as boardContent
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
	            WHERE REPLACE(board_content, ' ', '') like :#{#keyword}
			""", nativeQuery = true)
	Page<IBoardListResponse> findBoardAndCntByContent(@Param("keyword") String keyword, Pageable pageable);	
		
	// 게시판 검색용 (작성자)
	@Query(value = """
			SELECT 
				b.board_id as boardId
	            , b.board_type as boardType
	            , b.board_title as boardTitle
	            , b.board_content as boardContent
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
	            WHERE REPLACE(user_nickname, ' ', '') like :#{#keyword} 
			""", nativeQuery = true)
	Page<IBoardListResponse> findBoardAndCntByNickname(@Param("keyword") String keyword, Pageable pageable);	
			
	/**
	 * [게시판 검색페이지] 게시판 타입이 존재할 경우
	 */
	// 게시판 검색용 (제목 + 내용), 타입을 선택한 경우
	@Query(value = """
			SELECT 
				b.board_id as boardId
	            , b.board_type as boardType
	            , b.board_title as boardTitle
	            , b.board_content as boardContent
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
	            WHERE (REPLACE(board_title, ' ', '') like :#{#keyword}
			      or REPLACE(board_content, ' ', '') like :#{#keyword}) 
			      and board_type = :#{#type}
			""", nativeQuery = true)
	Page<IBoardListResponse> findBoardAndCntByKeywordAndType(@Param("type") String type, @Param("keyword") String keyword, Pageable pageable);	
	
	// 게시판 검색용 (제목)
	@Query(value = """
			SELECT 
				b.board_id as boardId
	            , b.board_type as boardType
	            , b.board_title as boardTitle
	            , b.board_content as boardContent
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
	            WHERE REPLACE(board_title, ' ', '') like :#{#keyword} 
			      and board_type = :#{#type}
			""", nativeQuery = true)
	Page<IBoardListResponse> findBoardAndCntByTitleAndType(@Param("type") String type, @Param("keyword") String keyword, Pageable pageable);	

	// 게시판 검색용 (내용)
	@Query(value = """
			SELECT 
				b.board_id as boardId
	            , b.board_type as boardType
	            , b.board_title as boardTitle
	            , b.board_content as boardContent
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
	            WHERE REPLACE(board_content, ' ', '') like :#{#keyword}
			      and board_type = :#{#type}
			""", nativeQuery = true)
	Page<IBoardListResponse> findBoardAndCntByContentAndType(@Param("type") String type, @Param("keyword") String keyword, Pageable pageable);	
		
	// 게시판 검색용 (작성자)
	@Query(value = """
			SELECT 
				b.board_id as boardId
	            , b.board_type as boardType
	            , b.board_title as boardTitle
	            , b.board_content as boardContent
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
	            WHERE REPLACE(user_nickname, ' ', '') like :#{#keyword} 
			      and board_type = :#{#type}
			""", nativeQuery = true)
	Page<IBoardListResponse> findBoardAndCntByNicknameAndType(@Param("type") String type, @Param("keyword") String keyword, Pageable pageable);
	
	
	//// 마이페이지에서 게시글 리스트 출력(연서 추가)
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
			WHERE user_id = :#{#userId}
			GROUP BY b.board_id
			ORDER BY b.board_id desc
					""" // ORDER BY 최신순
			, nativeQuery = true)
	Page<IBoardListResponse> findMyBoardAndCnt(@Param("userId") String userId, Pageable pageable);
	
	
	
//	// 산책로별 게시판 리스트 출력
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
		WHERE b.board_secret = false
			AND WHERE board_id IN (
					SELECT board_id
					FROM board_course
					WHERE course_id = :#{#course.courseId})
		GROUP BY b.board_id
				"""
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
	/// 특정 해시태그를 사용한 게시물을 출력
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
					WHERE bt.tag_id = :#{#tagId})
			GROUP BY b.board_id
					""" // ORDER BY는 나중에 수정하기
			, nativeQuery = true)
	List<IBoardListResponse> findBoardAndCntByTagId(@Param("tagId") Long tagId);
		
	// 1. (추가)findByTag : 태그 하나 검색하여 관련 게시물 가져오기
	@Query(value = "SELECT b.* FROM board b " +
		 " INNER JOIN board_tag bt " + 
		 "    ON b.board_id = bt.board_id " + 
		 " INNER JOIN tag t " +
		 "    ON bt.tag_id = t.tag_id  " +
		 " WHERE t.tag_content = :tagContent"
		     , nativeQuery = true)
	List<Board> findBoardByTag(@Param("tagContent") String tagContent);


	////// 좋아요 기능 추가
    @Query(value = """ 
		SELECT COUNT(*) FROM board_like WHERE board_id = :boardId
		""", nativeQuery = true)
	public int countBoardLikesByBoardId(@Param("boardId")Long boardId);

	
	//// 마이페이지에서 작성한 댓글들의 게시글 목록 출력
	@Query(value ="""
	        SELECT 
			    b.board_id as boardId,
			    b.board_type as boardType,
			    b.board_title as boardTitle,
			    b.user_nickname as userNickname,
			    b.user_id as userId,
			    b.board_view_count as boardViewCount,
			    b.board_created_date as boardCreatedDate,
			    b.board_updated_date as boardUpdatedDate,
			    COUNT(c.comment_id) as commentCnt,
			    COUNT(bl.board_like_id) as likeCnt
			FROM board as b
			LEFT JOIN comment as c ON b.board_id = c.board_id
			LEFT JOIN board_like as bl ON b.board_id = bl.board_id
			WHERE b.board_id  IN 
			    (select c2.board_id
			        from comment c2 
			        where c2.user_id = :#{#userId} 
			            and c2.comment_type = 'BOARD') 
			GROUP BY b.board_id
			ORDER BY b.board_id DESC
					""" // ORDER BY 최신순
			, nativeQuery = true)
	Page<IBoardListResponse> findMyBoardCommentAndCnt(@Param("userId") String userId, Pageable pageable);
	
	
}
