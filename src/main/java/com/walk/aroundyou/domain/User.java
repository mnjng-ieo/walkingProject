package com.walk.aroundyou.domain;

import java.security.Timestamp;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
// 인증 객체로 사용할 시 생기는 import
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.walk.aroundyou.domain.role.StateId;
import com.walk.aroundyou.domain.role.UserRole;

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
public class User implements UserDetails {
	// UserDetails를 상속받아 인증 객체로 사용
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    
    
    ////////// UserDetails를 상속받을 시 자동으로 생성되는 필수 오버라이드
    ///// UserDetails 클래스는 스프링 시큐리티에서 사용자의 인증 정보를 담아두는 인터페이스
    ///// 스프링 시큐리티에서 해당 객체를 통해 인증 정보를 가져오므로 필수 오버라이드 메서드가 있음(아래 오버라이드된 메서드들)
    // getAuthorities() 메서드는 UserDetails 인터페이스를 구현하는 클래스에서 사용자의 권한을 반환하는 메서드
    // 이 메서드에서 사용자에게 부여된 권한을 정의해야 한다.
    // 주의 : 사용자에게 부여된 권한을 정확하게 반환해야 함!
	@Override 
	public Collection<? extends GrantedAuthority> getAuthorities() { // 어떤 타입의 GrantedAuthority 객체들을 반환할 수 있도록 설계되었음을 나타냄
		// <? extends GrantedAuthority> : GrantedAuthority 또는 그 하위 타입의 컬렉션을 반환한다
		// 다시말해 이 메서드는 GrantedAuthority 인터페이스를 구현한 클래스 또는 하위 클래스의 객체들을 담은 컬렉션을 반환하는 것
		return List.of(
				// SimpleGrantedAuthority : 사용자에게 주는 권한
				//여러 권한은 부여하기 위해서는 SimpleGrantedAuthority객체를 리스트로 반환해 여러 권한을 추가
				// -> 후에 Spring Security에서 권한으로 사용됨.
				new SimpleGrantedAuthority("USER"),
				new SimpleGrantedAuthority("ADMIN"),
				new SimpleGrantedAuthority("GUEST")
				);
	}

	// 사용자의 id를 반환(고유한 값)
	@Override
	public String getUsername() {
		return userId;
	}
	
	// 사용자의 pwd를 반환
	@Override
	public String getPassword() {
		return userPwd;
	}

	///// 사용자 계정의 여러 상태를 확인
	// 계정 만료 여부 반환
	@Override
	public boolean isAccountNonExpired() {
		// 만료되었는지 확인하는 로직
		return true; // true : 만료되지 않음
	}

	// 계정 잠금 여부 반환
	@Override
	public boolean isAccountNonLocked() {
		// 계정 잠금되었는지 확인하는 로직
		return true; // true : 잠금되지 않았음
	}

	// 패스워드의 만료 여부 반환
	@Override
	public boolean isCredentialsNonExpired() {
		// 패스워드가 만료되었는지 확인하는 로직
		return true; // true : 만료되지 않음
	}

	// 계정 사용 가능 여부 반환
	@Override
	public boolean isEnabled() {
		// 계정이 사용 가능한지 확인하는 로직
		return true; // true : 사용 가능
	}

}