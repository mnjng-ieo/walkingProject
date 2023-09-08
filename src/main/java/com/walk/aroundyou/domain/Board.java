package com.walk.aroundyou.domain;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.walk.aroundyou.domainenum.BoardType;
import com.walk.aroundyou.domainenum.StateId;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
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
@Table(name = "board")
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "board_id")
	private Long boardId;
	
	// users 테이블과 연결된 외래키
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private Member userId;
	
	@Column(name = "user_nickname", nullable = false)
	private String userNickname;
	// ↳ 이렇게 두 개를 쓰지 않고 아래처럼 nickname에만 연결하면 어떨까?↳
		
	//@ManyToOne(fetch = FetchType.LAZY)
	//@JoinColumn(name = "user_nickname", referencedColumnName = "user_nickname")
	//private User userNickname;
	
	@Column(name = "board_type", nullable = true)
	@Enumerated(EnumType.STRING)
	private BoardType boardType;
	
	// 카테고리 나눌것이 없으면 지우고,
	// 나울것이 있으면 enum 데이터 형으로 변경
	@Column(name = "board_category", nullable = true)
	private String boardCategory;
	
	@Column(name = "board_title", nullable = false)
	private String boardTitle;
	
	@Column(name = "board_content", nullable = false, columnDefinition = "LONGTEXT")
	private String boardContent;

	@Column(name = "board_view_count", nullable = false)
	@ColumnDefault("0")
	private int boardViewCount;
	
	// 생성일시
	@Column(name = "board_created_date", nullable = false)
	@ColumnDefault("now()")
	private Timestamp boardCreatedDate;
	
	// 수정일시
	@Column(name = "board_updated_date", nullable = false)
	@ColumnDefault("now()")
	private Timestamp boardUpdatedDate;
	
	@Column(name = "board_secret", nullable = false)
	@ColumnDefault("false")
	private boolean boardSecret;
	
	@Column(name = "state_id", nullable = false)
	@Enumerated(EnumType.STRING)
	@ColumnDefault("'NORMAL'")
	private StateId stateId;
}

