package com.walk.aroundyou.domain;

import java.sql.Timestamp;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UpdateTimestamp;

import com.walk.aroundyou.domainenum.BoardType;
import com.walk.aroundyou.domainenum.StateId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "boards")
public class Boards {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "board_id")
	private Long boardId;
	
	@Column(name = "user_nickname", nullable = false)
	private String userNickname;
	
	@Column(name = "board_type", nullable = true)
	@Enumerated(EnumType.STRING)
	private BoardType boardType;
	
	// 카테고리 나눌것이 없으면 지우고,
	// 나울것이 있으면 enum 데이터 형으로 변경
	@Column(name = "board_category", nullable = true)
	private String boardCategory;
	
	@Column(name = "board_title", nullable = false)
	private String boardTitle;
	
	@Column(name = "board_content", nullable = false)
	private String boardContent;

	@Column(name = "board_view_count", nullable = false)
	@ColumnDefault("0")
	private int board_view_count;
	
	// 생성일시
	@Column(name = "board_created_date", nullable = false)
	@UpdateTimestamp
	private Timestamp board_created_date;
	
	// 수정일시
	@Column(name = "board_updated_date", nullable = false)
	@UpdateTimestamp
	private Timestamp boardUpdatedDate;
	
	@Column(name = "board_private", nullable = false)
	@ColumnDefault("false")
	private boolean board_private;
	
	@Column(name = "state_id", nullable = false)
	@ColumnDefault("0")
	private StateId stateId;
}
