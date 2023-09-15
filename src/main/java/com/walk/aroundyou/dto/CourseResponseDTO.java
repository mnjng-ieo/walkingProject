package com.walk.aroundyou.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.walk.aroundyou.domain.Course;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
// 산책로 목록 조회에 대한 응답을 위한 DTO 클래스 
// (좋아요 수, 게시물 언급 수, 댓글 수 포함 전송)
public class CourseResponseDTO {

	private Long courseId;
	private String esntlId; 
	private String wlkCoursFlagNm;
	private String wlkCoursNm;
	private String coursDc;
	private String signguCn;
	private String coursLevelNm;
	private String coursLtCn;
	private Float coursDetailLtCn;
	private String aditDc;
	
	// 이 어노테이션 없으면 Json으로 조회했을 때 ISO 8601 형식으로 보이는 문제 발생
	// "HH:mm:ss" 형식으로 바꾸고, timezone 설정으로 원래 데이터와 15시간 차이나던 것 바로 잡았다.
	@JsonFormat(pattern = "HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp coursTimeCn;
	
	private String toiletDc;
	private String cvntlNm;
	private String lnmAddr;
	private Double coursSpotLa;
	private Double coursSpotLo;	
	private Integer coursViewCount; 
	
	// [뷰 추가 정보]
	private Integer likeCnt;     // 좋아요 수
	private Integer mentionCnt;  // 게시물 언급 수
	private Integer commentCnt;  // 댓글 수
	
	// 산책로데이터 생성 시 사용
	public CourseResponseDTO(Course entity) {
		this.courseId = entity.getCourseId();
		this.esntlId = entity.getEsntlId();
		this.wlkCoursFlagNm = entity.getWlkCoursFlagNm();
		this.wlkCoursNm = entity.getWlkCoursNm();
		this.coursDc = entity.getCoursDc();
		this.signguCn = entity.getSignguCn();
		this.coursLevelNm = entity.getCoursLevelNm();
		this.coursLtCn = entity.getCoursLtCn();
		this.coursDetailLtCn = entity.getCoursDetailLtCn();
		this.aditDc = entity.getAditDc();	
		this.coursTimeCn = entity.getCoursTimeCn();
		this.toiletDc = entity.getToiletDc();
		this.cvntlNm = entity.getCvntlNm();
		this.lnmAddr = entity.getLnmAddr();
		this.coursSpotLa = entity.getCoursSpotLa();
		this.coursSpotLo = entity.getCoursSpotLo();
		this.coursViewCount = entity.getCoursViewCount();
	}
	
	public CourseResponseDTO(Course entity, int likeCnt, int mentionCnt, int commentCnt) {
		this.courseId = entity.getCourseId();
		this.esntlId = entity.getEsntlId();
		this.wlkCoursFlagNm = entity.getWlkCoursFlagNm();
		this.wlkCoursNm = entity.getWlkCoursNm();
		this.coursDc = entity.getCoursDc();
		this.signguCn = entity.getSignguCn();
		this.coursLevelNm = entity.getCoursLevelNm();
		this.coursLtCn = entity.getCoursLtCn();
		this.coursDetailLtCn = entity.getCoursDetailLtCn();
		this.aditDc = entity.getAditDc();	
		this.coursTimeCn = entity.getCoursTimeCn();
		this.toiletDc = entity.getToiletDc();
		this.cvntlNm = entity.getCvntlNm();
		this.lnmAddr = entity.getLnmAddr();
		this.coursSpotLa = entity.getCoursSpotLa();
		this.coursSpotLo = entity.getCoursSpotLo();
		this.coursViewCount = entity.getCoursViewCount();
		this.likeCnt = likeCnt;
		this.mentionCnt = mentionCnt;
		this.commentCnt = commentCnt;
	}

	public CourseResponseDTO() {
	}
}
