package com.walk.aroundyou.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.walk.aroundyou.domain.Board;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.domain.UploadImage;

@Repository
public interface UploadImageRepository 
			extends JpaRepository<UploadImage, Long>{

	// 게시판, 산책로, 유저로 이미지 찾기
	List<UploadImage> findByBoard(Board board);
	UploadImage findByCourse(Course course);
	UploadImage findByUser(Member user);
	
	@Query(value="""
         SELECT u.*
            FROM upload_image as u
            WHERE u.board_id IN 
               (SELECT b.board_id
                  FROM board as b
                  WHERE b.user_id = :#{#userId})
         """, nativeQuery = true)
	List<UploadImage> findMyImageByUserId(@Param("userId") String userId);
	
}
