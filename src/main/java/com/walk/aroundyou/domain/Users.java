package com.walk.aroundyou.domain;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Entity
public class Users {

// unique 설정 - email ... 
// @Column(unique = true) -> 한 개
// @Table() -> 여러 개
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", nullable=false)
    private Long useId;
    // 중복되는 값을 가질 수 없음 (이메일, 주민번호 ... )

    // user_role (회원인지 관리자인지)
    @Column(name = "user_role", nullable=false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'USER'")
    private UserRole role;
    
    // StateId (일반, 탈퇴, ... 회원 상태)
    @Column(name = "user_role", nullable=false)
    @Enumerated(EnumType.STRING)
    private StateId stateId;

    // socal 소셜로그인 가입 여부
    @Column(name = "social", nullable=false)
    private boolean Social;
}