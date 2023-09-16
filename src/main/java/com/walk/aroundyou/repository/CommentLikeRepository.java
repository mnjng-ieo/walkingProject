package com.walk.aroundyou.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.walk.aroundyou.domain.Comment;
import com.walk.aroundyou.domain.CommentLike;
import com.walk.aroundyou.domain.Member;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long>{
/* 관리자 권한에 해당하는 메서드 없음 */	
	
	
/* comment 좋아요 조회 구현 */
	// user와 commentId로 CommentLike 조회하여 
	// 해당 회원이 좋아요를 등록한 적이 있는지 체크하는 용도로 사용 
	Optional<CommentLike> findByUserIdAndCommentId(Member user, Comment comment);

	
}
