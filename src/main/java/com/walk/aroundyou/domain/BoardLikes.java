package com.walk.aroundyou.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="board_like")
public class BoardLikes {

	@Id
	@Column(name="board_like_id",columnDefinition="bigint", nullable=false)
	private long BoardlikeId;
	
	@ManyToOne
	@JoinColumn(name = "board_id", nullable = false)
	private Boards BoardId;
}
