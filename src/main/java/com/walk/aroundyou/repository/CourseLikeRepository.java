package com.walk.aroundyou.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.CourseLike;
import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.dto.ICourseLikeResponseDTO;

public interface CourseLikeRepository 
					extends JpaRepository<CourseLike, Long>{
	
	// user와 course 조합으로 CourseLike 테이블 찾기
	Optional<CourseLike> findByUserIdAndCourseId(Member user, Course course);
	

	//// 마이페이지에서 게시글 리스트 출력(연서 추가)
	@Query(value ="""
			SELECT 
				c.course_id as courseId
				, c.adit_dc as aditDc
				, c.cours_dc as coursDc
				, c.cours_detail_lt_cn as coursDetailLtCn
				, c.cours_level_nm as coursLevelNm
				, c.cours_lt_cn as coursLtCn
				, c.cours_spot_la as coursSpotLa
				, c.cours_time_cn as coursTimeCn
				, c.cvntl_nm as cvntlNm
				, c.esntl_id as esntlId
				, c.signgu_cn as signguCn
				, c.toilet_dc as toiletDc
				, c.wlk_cours_flag_nm as wlkCoursFlagNm
				, c.wlk_cours_nm as wlkCoursNm
				, c.cours_view_count as coursViewCount
				, ifnull(like_cnt, 0) as likeCnt
				, ifnull(mention_cnt, 0) as mentionCnt
				, ifnull(comment_cnt, 0) as commentCnt
			FROM Course as c
				LEFT JOIN
					(select course_id, count(course_id) as like_cnt
						from course_like as cl
						group by cl.course_id) as cll
				on c.course_id = cll.course_id
				LEFT JOIN
					(select course_id, count(course_id) as mention_cnt
						from board_course as bc
						group by bc.course_id) as bcc
				on c.course_id = bcc.course_id
				LEFT JOIN
					(select course_id, count(course_id) as comment_cnt
						from comment as c
						group by c.course_id) as cc
				on c.course_id = cc.course_id
			WHERE c.course_id IN 
                (select cl2.course_id 
                    from course_like cl2 
                    where cl2.user_id  = :#{#userId})
			GROUP BY c.course_id
					""" // ORDER BY 최신순
			, nativeQuery = true)
	Page<ICourseLikeResponseDTO> findMyCourseAndCnt(
			@Param("userId") String userId, Pageable pageable);

}
