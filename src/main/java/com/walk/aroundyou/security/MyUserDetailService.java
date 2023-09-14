package com.walk.aroundyou.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

        return User.builder()
        		.username(member.getUserId())
        		.password(member.getUserPwd())
        		.roles(UserRole.USER.getRoleName(), UserRole.ADMIN.getRoleName(), UserRole.GUEST.getRoleName())
                .build();
    }
}
