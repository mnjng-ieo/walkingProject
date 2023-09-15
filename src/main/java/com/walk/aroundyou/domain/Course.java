package com.walk.aroundyou.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
	private Double coursSpotLa;
	
	@Column(name="cours_spot_lo", nullable=false, columnDefinition="double")
	private Double coursSpotLo;	
	
	@Column(name="cours_view_count", nullable=false, columnDefinition="int default 0")
	private Integer coursViewCount;
	
	// 0910 추가 - 파일 업로드 기능
	// mappedBy : course와 upload_image 사이 연관관계의 주인은 image! 그것의 course 필드에 의해 연관관계가 맺어짐. 
	// cascade - All : 상위 엔티티(image)의 모든 상태 변경이 하위 엔티티(course)에 적용
	// fetch - LAZY : 지연 로딩. 참조 중이 아닐 때는 course를 읽지 않아서 성능에 좋음.
	// orphanRemoval = true : 하위 엔티티의 참조가 더 이상 없는 상태가 되었을 때 실제 삭제가 이뤄지도록 함.
//	@OneToOne(mappedBy = "course", 
//			cascade = {CascadeType.ALL},
//			fetch = FetchType.LAZY,
//			orphanRemoval = true)
//	private UploadImage courseImageId;
	
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
			Double coursSpotLa,
			Double coursSpotLo
			//UploadImage courseImageId
			) {
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
		//this.courseImageId = courseImageId;
	}
}