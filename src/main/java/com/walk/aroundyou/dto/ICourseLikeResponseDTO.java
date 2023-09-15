package com.walk.aroundyou.dto;

import java.sql.Time;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.walk.aroundyou.domain.Course;
import com.walk.aroundyou.domain.Member;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

// 내가 좋아요 한 산책로 목록 조회에 대한 응답을 위한 DTO
// (좋아요 수, 게시물 언급 수, 댓글 수 포함 전송)
public interface ICourseLikeResponseDTO {

	Long getCourseLikeId();
	String getUserId();
	Long getCourseId();
	String getEsntlId(); 
	String getWlkCoursFlagNm();
	String getWlkCoursNm();
	String getCoursDc();
	String getSignguCn();
	String getCoursLevelNm();
	String getCoursLtCn();
	Float getCoursDetailLtCn();
	String getAditDc();
	
	@JsonFormat(pattern = "HH:mm:ss", timezone = "Asia/Seoul")
	Time getCoursTimeCn();
	
	String getToiletDc();
	String getCvntlNm();
	String getLnmAddr();
	Double getCoursSpotLa();
	Double getCoursSpotLo();
	
	// [뷰 추가 정보]
	Long getCoursViewCount();
	Integer getLikeCnt();     // 좋아요 수
	Integer getMentionCnt();  // 게시물 언급 수
	Integer getCommentCnt();  // 댓글 수
	
	// 쿼리문에 작성 할 별칭
	// CourseLikeId
	// UserId
	//	CourseId
	//	EsntlId
	//	WlkCoursFlagNm
	//	WlkCoursNm
	//	CoursDc
	//	SignguCn
	//	CoursLevelNm
	//	CoursLtCn
	//	CoursDetailLtCn
	//	AditDc
	//	CoursTimeCn
	//	ToiletDc
	//	CvntlNm
	//	LnmAddr
	//	CoursSpotLa
	//	CoursSpotLo
	//	LikeCnt
	//	CommentCnt
	
}