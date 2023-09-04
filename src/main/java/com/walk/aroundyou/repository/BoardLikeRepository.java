package com.walk.aroundyou.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.BoardLike;
import com.walk.aroundyou.domain.User;

@Repository
public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

	
	// 특정 게시물(boardId)에 대한 좋아요(userId)누른 회원들 목록 
	// : 목록의 요소는 userId 대신 userId와 매칭되는 (프로필 사진 이미지/닉네임)으로 조회된다. (user 권한에서)
	// : 목록의 요소는 userId 그리고 userId와 매칭되는 (프로필 사진 이미지/닉네임)으로 조회된다. (admin 권한에서)
	@Query(value = "SELECT bl.userId FROM BoardLike bl WHERE bl.boardId = :boardId")
	List<Long> findUserIdByBoardId(@Param(value = "boardId")Board boardId);
	
	// 특정 게시물(boardId)에 대한 좋아요(userId)의 갯수
	// @Query(value = "SELECT count_big(boardId) FROM BoardLike bl WHERE bl.boardId = :boardId") 아래와 동일
	@Query(value = "SELECT count_big(bl.userId) FROM BoardLike bl WHERE bl.boardId = :boardId")
	Long countUserIdByBoardId(@Param(value = "boardId")Board boardId);
	
	
	// 마이페이지
	// 마이페이지에서 좋아요한 게시물 목록 확인(로그인한 회원 본인과 관리자만 목록을 확인할 수 있음)
	@Query(value = "SELECT bl.boardId FROM BoardLike bl WHERE bl.userId = :userId")
	List<Long> findLikedBoardIdByUserId(@Param(value = "userId")User userId);
	
	// 마이페이지에서 좋아요한 게시물의 갯수 확인(로그인한 회원 본인과 관리자만 목록을 확인할 수 있음)
	@Query(value = "SELECT count_big(boardId) FROM BoardLike bl WHERE bl.userId = :userId")
	Long countLikedBoardIdByUserId(@Param(value = "userId")User userId);
	
	
	
	// 사용자가 게시물 좋아요 표시 클릭 시 선택 + 해제
	// 값의 유무 모르니까 에러 발생하지 않기 위해 Optional<>
	Optional<BoardLike> findByUserIdAndBoardId(User userId, Board boardId);
	
	
	
	
	
}
