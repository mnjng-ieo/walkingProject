package com.walk.aroundyou.domain;

import java.sql.Timestamp;

import org.hibernate.annotations.ColumnDefault;

import com.walk.aroundyou.domain.role.StateId;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Entity
@Getter	
@NoArgsConstructor
@AllArgsConstructor
@Table(name="comment")
public class Comment {

	// 코멘트 식별 번호 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="comment_id", nullable = false)
	private Long commentId;
	
	// 회원 닉네임
	@Column(name = "user_nickname", nullable = false)
	private String userNickname;
	
	// 게시판 식별 번호 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="board_id", nullable = false)
	private Board boardId;

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

	// Enum 클래스때문에 오류 발생 
	// 상태정보 ID 
	@Column(name="state_id")
	@Enumerated(EnumType.STRING)
	@ColumnDefault("'NORMAL'")
	private StateId stateId;

	// 회원 ID
	@ManyToOne(fetch = FetchType.LAZY)
	//@Column(nullable=false, columnDefinition="varchar(100)")
	@JoinColumn(name="user_id", referencedColumnName="user_id", nullable=false )
	private User userId;
}