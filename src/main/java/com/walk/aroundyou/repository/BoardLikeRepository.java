package com.walk.aroundyou.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.BoardLike;
import com.walk.aroundyou.domain.Member;

@Repository
public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
	

	// 특정 게시물(boardId)에 대한 좋아요(userId)누른 회원들의 목록 
	// : 목록의 요소는 userId 대신 userId와 매칭되는 프로필 사진 이미지로 조회된다. (user 권한에서)
	// : 목록의 요소는 userId 그리고 userId와 매칭되는 프로필 사진 이미지로 조회된다. (admin 권한에서)
	@Query(value = "SELECT bl.user_id FROM Board_like bl WHERE bl.board_id = :#{#boardId}"
			, nativeQuery = true)
	List<String> findUserIdByBoardId(@Param(value = "boardId") Long boardId);
	
	// 특정 게시물(boardId)에 대한 좋아요(userId)의 갯수
	// @Query(value = "SELECT count_big(boardId) FROM BoardLike bl WHERE bl.boardId = :boardId") 아래와 동일
	@Query(value = "SELECT count(bl.user_id) FROM Board_like bl WHERE bl.board_id = :#{#boardId}"
			, nativeQuery = true)
	Long countUserIdByBoardId(@Param(value = "boardId") Long boardId);
	
	
	// 마이페이지
	// 마이페이지에서 좋아요한 게시물 목록 확인(로그인한 회원 본인과 관리자만 목록을 확인할 수 있음)
	@Query(value = "SELECT bl.board_id FROM Board_like bl WHERE bl.user_id = :#{#userId}"
			, nativeQuery = true)
	List<Long> findLikedBoardIdByUserId(@Param(value = "userId")String userId);
	
	// 마이페이지에서 좋아요한 게시물의 갯수 확인(로그인한 회원 본인과 관리자만 목록을 확인할 수 있음)
	@Query(value = "SELECT count(board_id) FROM Board_like bl WHERE bl.user_id = :#{#userId}"
			, nativeQuery = true)
	Long countLikedBoardIdByUserId(@Param(value = "userId")String userId);
	
	
	
	// 사용자가 게시물 좋아요 표시 클릭 시 선택 + 해제
	// 값의 유무 모르니까 에러 발생하지 않기 위해 Optional<>
	Optional<BoardLike> findByUserIdAndBoardId(Member userId, Board boardId);


	
	
	
	
}
