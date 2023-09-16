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
@Table(name="comment_like")
public class CommentLike {

	// 코멘트좋아요 식별번호 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="comment_like_id", nullable=false)
	private Long commentLikeId;

	// 코멘트 식별번호 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="comment_id", nullable=false)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Comment commentId;	

	// 회원 ID 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id", nullable=false)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Member userId;
}