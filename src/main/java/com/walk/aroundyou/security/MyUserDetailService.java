package com.walk.aroundyou.security;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.walk.aroundyou.domain.Member;
import com.walk.aroundyou.domainenum.UserRole;
import com.walk.aroundyou.service.UserService;

@Component
public class MyUserDetailService implements UserDetailsService {
	
    private final UserService memberService;

    @Autowired
    public MyUserDetailService(UserService memberService) {
        this.memberService = memberService;
    }

    // securityconfig에서 사용할 사용자 정보 가져오기
    // 회원가입 기능
    @Override
    public UserDetails loadUserByUsername(String insertedId) throws UsernameNotFoundException {
        Optional<Member> findOne = memberService.findByUserId(insertedId);
        Member member = findOne.orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다"));

        // enum 클래스에서 사용자의 권한 정보를 가져옴
        UserRole userRole = member.getRole();
        
        // userRole 변수에서 가져온 권한 정보를 Spring Security의 GrantedAuthority 객체로 변환
        // SimpleGrantedAuthority : 권한을 나타내는 Spring Security의 기본 구현체 중 하나
        //  권한을 설정할 때 권한 이름을 문자열로 지정
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(userRole.getRoleName()));
        
        return User.builder()
        		.username(member.getUserId())
        		.password(member.getUserPwd())
        		// authorities : 사용자의 권한을 설정
        		.authorities(authorities)
                .build();
    }
}
