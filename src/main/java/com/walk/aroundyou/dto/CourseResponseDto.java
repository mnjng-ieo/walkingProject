package com.walk.aroundyou.dto;

import java.sql.Timestamp;

import com.walk.aroundyou.domain.Course;

import lombok.Getter;

@Getter
// 산책로 목록 조회에 대한 응답을 위한 DTO 클래스
public class CourseResponseDto {

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
	private Timestamp coursTimeCn;
	private String toiletDc;
	private String cvntlNm;
	private String lnmAddr;
	private double coursSpotLa;
	private double coursSpotLo;	
	
	public CourseResponseDto(Course entity) {
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
	}
}
