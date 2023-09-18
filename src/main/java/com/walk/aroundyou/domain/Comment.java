package com.walk.aroundyou.domain;

import java.sql.Timestamp;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.walk.aroundyou.domainenum.CommentType;
import com.walk.aroundyou.domainenum.StateId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity	
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="comment")
public class Comment {

	// 코멘트 식별 번호 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="comment_id", nullable = false)
	private Long commentId;
	
	// 게시판 식별 번호 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="board_id", nullable = true)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Board boardId;
	
	// 코스 식별 번호 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="course_id", nullable = true)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Course courseId;
	
	// 코멘트 내용 
	@Column(name="comment_content", nullable=false, columnDefinition="varchar(255)")
	private String commentContent;

	// 코멘트 작성일시 
	@Column(name="comment_created_date", nullable=false)
	@ColumnDefault("now()")
	private Timestamp commentCreatedDate;

	// 코멘트 수정일시 
	@Column(name="comment_updated_date", nullable=false)
	@ColumnDefault("now()")
	private Timestamp commentUpdatedDate;

	// 상태정보 ID 
	@Column(name="state_id")
	@Enumerated(EnumType.STRING)
	@ColumnDefault("'NORMAL'")
	private StateId stateId;

	// 회원 ID
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id", referencedColumnName="user_id" )
	@OnDelete(action=OnDeleteAction.CASCADE)
	// 오류창에 JoinColumn과 같이 사용하지 않을수있다고해서 주석처리
	// @Column(nullable=false, columnDefinition="varchar(100)")
	private Member userId;
	
	// 회원 닉네임
	@Column(name = "user_nickname", nullable = false)
	private String userNickname;
	
	// 회원 이미지 
	@Column(name="user_img", nullable=true)
	private String userImg;
	
	// commentType = BOARD / COURSE 두 가지로 분류
	@Column(name = "comment_type", nullable = true)
	@Enumerated(EnumType.STRING)
	private CommentType commentType;
}