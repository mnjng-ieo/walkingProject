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
public interface BoardLikeRepository extends JpaRepository<BoardLike, Long>{

	// 특정 게시물 좋아요 개수(JPQL)
	//@Query(value = "SELECT count(bl) FROM BoardLike bl WHERE bl.boardId = :{boardId}")
	//Long countLike(@Param(value = "boardId")Board boardId);
	// JPA 
	Long countByBoardId(Board boardId);
	
	
	// controller 필요 O -> 사용자 응답으로 확인하는 거니까
	// 마이페이지에서 좋아요한 게시물 확인 (유저 - 게시글) (JPQL)
	// BoardLike 엔티티 내의 boardId 필드. boardId 필드가 실제로는 Board 엔티티의 인스턴스를 가리키므로 bl.boardId.boardId는 실제로는 Board 엔티티의 식별자를 가져옴
	@Query(value = "SELECT bl.boardId.boardId FROM BoardLike bl WHERE bl.userId = :userId")
	List<Long> findLikedBoardByUserId(@Param(value = "userId")Member userId);
	
	// 사용자가 게시물 좋아요 표시 클릭 시 선택 + 해제 (JPA)
	// 값의 유무 모르니까 에러 발생하지 않기 위해 Optional<>
	Optional<BoardLike> findByUserIdAndBoardId(Member userId, Board boardId);


	
	
	
	
}
