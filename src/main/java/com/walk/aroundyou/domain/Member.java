package com.walk.aroundyou.domain;

import java.sql.Timestamp;

import org.hibernate.annotations.ColumnDefault;

import com.walk.aroundyou.domainenum.StateId;
import com.walk.aroundyou.domainenum.UserRole;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude="userPwd")
@NoArgsConstructor 
@AllArgsConstructor
@Table(name = "user")
@Entity
public class Member {

	@Id
	@Column(name="user_id", nullable=false)
	private String userId;
	
	@Column(name="user_pwd", nullable=false)
	private String userPwd;
	
	@Column(name="user_name", nullable=false)
	private String userName;
	
	@Column(name="user_nickname", nullable=false)
	private String userNickname;
	
	@Column(name="user_tel_number", nullable=false)
	private String userTelNumber;
	
	@Column(name="user_email", nullable=false, unique=true)
	private String userEmail;
	
	// 기존의 String userImg는 삭제했습니다. 테이블에서 user_img 컬럼을 삭제해주세요.
	//private String userImg;
	
	// 0910 추가 - 파일 업로드 기능
	// mappedBy : user와 user_image 사이 연관관계의 주인은 image! 그것의 user 필드에 의해 연관관계가 맺어짐. 
	// cascade - All : 상위 엔티티의 모든 상태 변경이 하위 엔티티에 적용
	// fetch - LAZY : 지연 로딩. 참조 중이 아닐 때는 user를 읽지 않아서 성능에 좋음.
	// orphanRemoval = true : 하위 엔티티의 참조가 더 이상 없는 상태가 되었을 때 실제 삭제가 이뤄지도록 함.
//	@OneToOne(mappedBy = "user", 
//			cascade = {CascadeType.ALL},
//			fetch = FetchType.LAZY,
//			orphanRemoval = true)
//	private UploadImage userImageId;
	
	@Column(name="user_join_date", nullable=false)
	@ColumnDefault("now()")
	private Timestamp userJoinDate;
	
	@Column(name="user_update_date", nullable=false)
	@ColumnDefault("now()")
	private Timestamp userUpdateDate;
	
	@Column(name="user_role", nullable=false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'USER'")
	private UserRole userRole;
	
	@Column(name = "state_id", nullable = false)
	@Enumerated(EnumType.STRING)
	@ColumnDefault("'NORMAL'")
	private StateId stateId;

	// social 소셜로그인 가입 여부
    @Column(name = "social_yn", nullable=false)
    @ColumnDefault("false")
    private boolean SocialYn;
}

