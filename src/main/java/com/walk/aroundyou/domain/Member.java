package com.walk.aroundyou.domain;

import java.sql.Timestamp;
//import java.util.Collection;
//import java.util.List;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
// 인증 객체로 사용할 시 생기는 import
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.walk.aroundyou.domain.role.StateId;
import com.walk.aroundyou.domain.role.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
// 스프링 시큐리티에서 사용자 인증 및 권한 부여를 다루는 클래스는 보안 관련 작업을 수행하므로, 이러한 클래스의 인스턴스를 외부에서 생성 및 조작하는 것을 방지하기 위해 생성자 접근 제한을 두는 것이 좋음
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "user")
@Entity
public class Member{
	
    @Id
    @Column(name = "user_id", nullable=false)
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
	
	@Column(name="user_description", nullable=true)
	private String userDescription;
	
	@Column(name="user_join_date", nullable=false)
	@ColumnDefault("now()")
	private Timestamp userJoinDate;
	
	@Column(name="user_update_date", nullable=false)
	@ColumnDefault("now()")
	private Timestamp userUpdateDate;
	
    @Column(name = "user_role", nullable=false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'USER'")
    private UserRole role;
    
    @Column(name = "state_id", nullable=false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'NORMAL'")
    private StateId stateId;
    
    // socal 소셜로그인 가입 여부
    @Column(name = "social_yn", nullable=false)
    @ColumnDefault("false")
    private boolean socialYn;
    

}