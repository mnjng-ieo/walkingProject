package com.walk.aroundyou.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name="UploadImage")
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
	
	// 게시판 식별 번호 
	// - 하나의 게시판에는 여러 개의 이미지 존재 가능 (9개까지로 제한하면 좋겠다)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="board_id", nullable = true)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Board boardId;
	
	// 코스 식별 번호
	// - 하나의 산책로에는 하나의 메인 사진만 존재 가능, 
	//   따라서 upload_image 테이블에서 unique key에 해당
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="course_id", nullable = true, unique = true)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Course courseId;
}
