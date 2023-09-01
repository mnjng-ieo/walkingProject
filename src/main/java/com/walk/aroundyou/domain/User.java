package com.walk.aroundyou.domain;



import java.sql.Timestamp;

import org.hibernate.annotations.ColumnDefault;

import com.walk.aroundyou.domain.role.UserRole;
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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@Entity
public class User {

	// 멤버변수
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	
	@Column(name="user_img", nullable=true)
	private String userImg;
	
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
    @Column(name = "social_yn", nullable = false)
    @ColumnDefault("false")
    private boolean socialYn;

}
