package com.walk.aroundyou.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="course")
public class Course {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="course_id", nullable = false)
	private Long courseId;
	
	@Column(name="esntl_id", columnDefinition="varchar(50)")
	private String esntlId;
	
	@Column(name="wlk_cours_flag_nm", nullable=false, columnDefinition="varchar(200)")
	private String wlkCoursFlagNm;
	
	@Column(name="wlk_cours_nm", nullable=false, columnDefinition="varchar(200)")
	private String wlkCoursNm;
	
	@Column(name="cours_dc", nullable=false, columnDefinition="text")
	private String coursDc;
	
	@Column(name="signgu_cn", nullable=false, columnDefinition="varchar(200)")
	private String signguCn;
	
	@Column(name="cours_level_nm", nullable=false, columnDefinition="varchar(255)")
	private String coursLevelNm;

	@Column(name="cours_lt_cn", columnDefinition="varchar(50)")
	private String coursLtCn;
	
	@Column(name="cours_detail_lt_cn", nullable=false, columnDefinition="float")
	private Float coursDetailLtCn;
	
	@Column(name="adit_dc", columnDefinition="text")
	private String aditDc;
	
	// JsonFormat 
	@JsonFormat(pattern = "HH:mm:ss", timezone = "Asia/Seoul")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="cours_time_cn", columnDefinition="time")
	private Timestamp coursTimeCn;

	@Column(name="toilet_dc", columnDefinition="varchar(200)")
	private String toiletDc;
	
	@Column(name="cvntl_nm", columnDefinition="varchar(200)")
	private String cvntlNm;

	@Column(name="lnm_addr", columnDefinition="varchar(200)")
	private String lnmAddr;

	@Column(name="cours_spot_la", nullable=false, columnDefinition="double")
	private double coursSpotLa;
	
	@Column(name="cours_spot_lo", nullable=false, columnDefinition="double")
	private double coursSpotLo;	
	
	// 값 수정 메서드
	public void update(
			String wlkCoursFlagNm,
			String wlkCoursNm,
			String coursDc,
			String signguCn,
			String coursLevelNm,
			String coursLtCn,
			Float coursDetailLtCn,
			String aditDc,
			Timestamp coursTimeCn,
			String toiletDc,
			String cvntlNm,
			String lnmAddr,
			double coursSpotLa,
			double coursSpotLo) {
		this.wlkCoursFlagNm = wlkCoursFlagNm;
		this.wlkCoursNm = wlkCoursNm;
		this.coursDc = coursDc;
		this.signguCn = signguCn;
		this.coursLevelNm = coursLevelNm;
		this.coursLtCn = coursLtCn;
		this.coursDetailLtCn = coursDetailLtCn;
		this.aditDc = aditDc;
		this.coursTimeCn = coursTimeCn;
		this.toiletDc = toiletDc;
		this.cvntlNm = cvntlNm;
		this.lnmAddr = lnmAddr;
		this.coursSpotLa = coursSpotLa;
		this.coursSpotLo = coursSpotLo;
	}
}