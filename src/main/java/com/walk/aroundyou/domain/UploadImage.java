package com.walk.aroundyou.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Table(name="UploadImage")
// 게시판 | 산책로 | 유저와 관련된 사진이 업로드되면 저장하는 테이블
public class UploadImage {

	// 이미지 파일 식별 번호
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="file_id", nullable = false)
	private Long fileId;
	
	// 파일 원본 이름 - 중복 가능
	@Column(name="original_file_name", nullable=false, columnDefinition="varchar(255)")
	private String originalFileName;
	
	// 서버에 저장된 이름 - 고유 uuid로 식별 가능
	@Column(name="saved_file_name", nullable=false, columnDefinition="varchar(255)")
	private String savedFileName;
	
	// 이미지 순번 ; 게시물의 경우 여러 개의 이미지가 있을 수 있으므로 순번 정해서 뷰에 노출 가능
	@Column(name="image_ord", nullable=true, columnDefinition="int")
	private Integer imageOrd;
	
	// 게시판 식별 번호 
	// - 하나의 게시판에는 여러 개의 이미지 존재 가능 (9개까지로 제한하면 좋겠다)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="board_id", referencedColumnName = "board_id", nullable = true)
	@OnDelete(action=OnDeleteAction.CASCADE)
	@JsonIgnore
	private Board board;
	
	// 코스 식별 번호
	// - 하나의 산책로에는 하나의 메인 사진만 존재 가능, 
	//   따라서 upload_image 테이블에서 unique key에 해당
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="course_id", referencedColumnName = "course_id", 
	            nullable = true, unique = true)
	@OnDelete(action=OnDeleteAction.CASCADE)
	@JsonIgnore
	private Course course;
	
	// 유저 식별 번호
	// - 하나의 유저에게는 하나의 프로필 사진만 존재 가능, 
	//   따라서 upload_image 테이블에서 unique key에 해당
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id", referencedColumnName = "user_id", nullable = true, unique = true)
	@OnDelete(action=OnDeleteAction.CASCADE)
	@JsonIgnore
	private Member user;
}
